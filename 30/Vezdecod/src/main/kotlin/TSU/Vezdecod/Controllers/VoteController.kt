package TSU.Vezdecod.Controllers

import TSU.Vezdecod.DbSingers
import TSU.Vezdecod.Helpers.isNotValidAddVoteRequest
import TSU.Vezdecod.Core.RequestAddVote
import TSU.Vezdecod.Core.StatsResponse
import TSU.Vezdecod.Models.Singer
import TSU.Vezdecod.Models.SingerResponse
import io.github.bucket4j.Bandwidth
import io.github.bucket4j.Bucket
import io.github.bucket4j.Bucket4j
import io.github.bucket4j.Refill
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.Duration

@RestController
class VoteController {
    private lateinit var bucket: Bucket

    init {
        val refill = Refill.intervally(500, Duration.ofMinutes(1))
        val limit = Bandwidth.classic(500, refill)
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
            responseHeaders.set("x-ratelimit-limit", 50.toString())
            responseHeaders.set("x-ratelimit-reset", probe.getNanosToWaitForRefill().toString())
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).headers(responseHeaders).build()
        }
        if (isNotValidAddVoteRequest(request)) return ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
        val signer: Singer? = DbSingers.findArtist(request.artist)
        if (signer == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        else signer.addVote(request.phone)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @GetMapping("/votes/stats")
    fun getVotesStats(
        @RequestParam from: Int? = null,
        @RequestParam to: Int? = null,
        @RequestParam intervals: Int = 10,
        @RequestParam artists: String? = null
    ): List<List<StatsResponse>> {

        return DbSingers.getStat(from, to, intervals, artists)
    }
}