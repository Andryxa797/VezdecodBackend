package TSU.Vezdecod

import TSU.Vezdecod.Core.RequestAddVote
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import java.net.URI

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class IntegrationTests(@Autowired val restTemplate: TestRestTemplate) {
	private fun applicationUrl() = "http://localhost:8090"

	@Test
	fun testAddVote() {
		for(i in 1..100) {
			val addVote = RequestAddVote()
			addVote.phone = "9888888888"
			addVote.artist = "Алла Пугачева"

			val result = restTemplate.exchange(
				URI(applicationUrl() + "/votes"),
				HttpMethod.POST,
				HttpEntity(addVote),
				String::class.java
			)
			Assertions.assertEquals(HttpStatus.CREATED, result.statusCode)
		}
		for(i in 1..100) {
			val addVote = RequestAddVote()
			addVote.phone = "9888888888"
			addVote.artist = "Филипп Киркоров"

			val result = restTemplate.exchange(
				URI(applicationUrl() + "/votes"),
				HttpMethod.POST,
				HttpEntity(addVote),
				String::class.java
			)
			Assertions.assertEquals(HttpStatus.CREATED, result.statusCode)
		}
		for(i in 1..100) {
			val addVote = RequestAddVote()
			addVote.phone = "9888888888"
			addVote.artist = "Жанна Фриске"

			val result = restTemplate.exchange(
				URI(applicationUrl() + "/votes"),
				HttpMethod.POST,
				HttpEntity(addVote),
				String::class.java
			)
			Assertions.assertEquals(HttpStatus.CREATED, result.statusCode)
		}
		for(i in 1..100) {
			val addVote = RequestAddVote()
			addVote.phone = "9888888888"
			addVote.artist = "Анна Седокова"

			val result = restTemplate.exchange(
				URI(applicationUrl() + "/votes"),
				HttpMethod.POST,
				HttpEntity(addVote),
				String::class.java
			)
			Assertions.assertEquals(HttpStatus.CREATED, result.statusCode)
		}
	}
	@Test
	fun testStats() {

	}
}




//@SpringBootTest()
//class VezdecodApplicationTests {
//	@Autowired
//	private mvc: MockMvc
//
//	private fun applicationUrl() = "http://localhost:8090"
//
//	@Test
//	fun simpleGetTest() {
//		val result = testRestTemplate.exchange(
//			URI(applicationUrl() + "/votes"),
//			HttpMethod.GET,
//			HttpEntity(""),
//			String::class.java)
//
//		Assertions.assertEquals(HttpStatus.OK, result.statusCode)
//	}
//
//}
//	lateinit var testRestTemplate: TestRestTemplate
//	private fun applicationUrl() = "http://localhost:8090"
//
//	@Test
//	fun simpleGetTest() {
//		val result = testRestTemplate.exchange(
//			URI(applicationUrl() + "/votes"),
//			HttpMethod.GET,
//			HttpEntity(""),
//			String::class.java)
//
//		Assertions.assertEquals(HttpStatus.OK, result.statusCode)
//	}

//	@Autowired
//	lateinit var testRestTemplate: TestRestTemplate
//	@Test
//	fun statsTest() {
//		val result = testRestTemplate.getForEntity("/hello/string", String::class.java)
//	}
//	@Autowired
//	lateinit var testRestTemplate: TestRestTemplate
//	@Test
//	fun statsTest() {
//		val result = testRestTemplate.getForEntity("/hello/string", String::class.java)
//	}