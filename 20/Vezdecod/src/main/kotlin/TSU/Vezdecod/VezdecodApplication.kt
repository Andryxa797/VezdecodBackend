package TSU.Vezdecod

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import io.github.bucket4j.Bandwidth
import io.github.bucket4j.Bucket
import io.github.bucket4j.Bucket4j
import io.github.bucket4j.Refill
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.core.io.ClassPathResource
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.io.BufferedReader
import java.io.File
import java.io.IOException
import java.time.Duration

class ConfigRateLimit {
    public var refill: Long = 50
    public var limit: Long = 50
    public var time: Long = 1
}

@SpringBootApplication
class VezdecodApplication

var DbSingers = Singers()
var RateLimitConfig = ConfigRateLimit()

fun main(args: Array<String>) {
    run()
    runApplication<VezdecodApplication>(*args)
}

fun run() {
    val file: File = ClassPathResource("config/singers.json").file
    val bufferedReader: BufferedReader = file.bufferedReader()
    val inputString = bufferedReader.use { it.readText() }
    val mapper = ObjectMapper()
    try {
        val singersJSON = mapper.readValue(inputString, object : TypeReference<List<String>?>() {})
        if (singersJSON != null) {
            for (singer in singersJSON) {
                DbSingers.addSinger(Singer(singer))
            }
        }
    } catch (e: IOException) {
        e.printStackTrace()
    }
    val rateLimitFile: File = ClassPathResource("config/rate-limit.json").file
    val bufferedReaderRateLimit: BufferedReader = rateLimitFile.bufferedReader()
    val inputStringRateLimit = bufferedReaderRateLimit.use { it.readText() }
    val mapperRateLimit = ObjectMapper()
    try {
        val configJSON = mapperRateLimit.readValue(inputStringRateLimit, object : TypeReference<ConfigRateLimit>() {})
        if (configJSON != null) {
            RateLimitConfig.limit = configJSON.limit
            RateLimitConfig.refill = configJSON.refill
            RateLimitConfig.time = configJSON.time
        }
    } catch (e: IOException) {
        e.printStackTrace()
    }


}

@RestController
class MessageResource {
    private lateinit var bucket: Bucket

    init {
        val refill = Refill.intervally(RateLimitConfig.refill, Duration.ofMinutes(RateLimitConfig.time))
        val limit = Bandwidth.classic(RateLimitConfig.limit, refill)
        bucket = Bucket4j.builder().addLimit(limit).build()
    }

    @GetMapping("/votes")
    fun getVotes(): List<SingerResponse> {
        return DbSingers.getAllSignerResponse()
    }

    @PostMapping("/votes")
    fun setVotes(@RequestBody request: RequestAddVote): ResponseEntity<Any> {
        val probe = this.bucket.tryConsumeAndReturnRemaining(1)
        if (!probe.isConsumed()) {
            val responseHeaders = HttpHeaders()
            responseHeaders.set("x-ratelimit-remaining", probe.remainingTokens.toString())
            responseHeaders.set("x-ratelimit-limit", RateLimitConfig.limit.toString())
            responseHeaders.set("x-ratelimit-reset", probe.getNanosToWaitForRefill().toString())
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).headers(responseHeaders).build()
        }
        if (isNotValidRequest(request)) return ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
        val signer: Singer? = DbSingers.findArtist(request.artist)
        if (signer == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        else signer.incrementVote()
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @GetMapping("/votes/stats")
    fun getVotesStats(): List<SingerResponse> {
        return DbSingers.getAllSignerResponse()
    }
}


fun isNotValidRequest(request: RequestAddVote): Boolean {
    return request.artist.isEmpty() || request.phone.isEmpty()
            || request.phone.length != 10 || request.phone[0] != '9'
            || Boolean.equals(request.phone.toIntOrNull())
}


