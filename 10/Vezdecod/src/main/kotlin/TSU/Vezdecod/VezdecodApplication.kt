package TSU.Vezdecod

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.core.io.ClassPathResource
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.io.BufferedReader
import java.io.File
import java.io.IOException

@SpringBootApplication
class VezdecodApplication

var DbSingers = Singers()

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
}

@RestController
class MessageResource {
    @GetMapping("/votes")
    fun getVotes(): List<SingerResponse> {
        return DbSingers.getAllSignerResponse()
    }

    @PostMapping("/votes")
    fun setVotes(@RequestBody request: RequestAddVote): ResponseEntity<Any> {
        if (isNotValidRequest(request)) return ResponseEntity<Any>(null, HttpStatus.BAD_REQUEST)
        val signer: Singer? = DbSingers.findArtist(request.artist)
        if (signer == null) return ResponseEntity<Any>(null, HttpStatus.NOT_FOUND)
        else signer.incrementVote()
        return ResponseEntity<Any>(null, HttpStatus.CREATED)
    }
}


fun isNotValidRequest(request: RequestAddVote): Boolean {
    return request.artist.isEmpty() || request.phone.isEmpty()
            || request.phone.length != 10 || request.phone[0] != '9'
            || Boolean.equals(request.phone.toIntOrNull())
}


