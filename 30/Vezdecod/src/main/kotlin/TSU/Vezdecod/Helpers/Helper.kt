package TSU.Vezdecod.Helpers

import TSU.Vezdecod.Core.RequestAddVote
import TSU.Vezdecod.DbSingers
import TSU.Vezdecod.Models.Singer
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.core.io.ClassPathResource
import java.io.BufferedReader
import java.io.File
import java.io.IOException

fun initApplication() {
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

fun isNotValidAddVoteRequest(request: RequestAddVote): Boolean {
    return request.artist.isEmpty() || request.phone.isEmpty()
            || request.phone.length != 10 || request.phone[0] != '9'
            || Boolean.equals(request.phone.toIntOrNull())
}

fun getDataTimeNowInSeconds(): Long {
    return System.currentTimeMillis()
}


