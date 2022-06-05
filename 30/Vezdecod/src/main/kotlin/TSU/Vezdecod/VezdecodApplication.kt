package TSU.Vezdecod

import TSU.Vezdecod.Helpers.initApplication
import TSU.Vezdecod.Models.Singers
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

var DbSingers = Singers()

@SpringBootApplication
class VezdecodApplication

fun main(args: Array<String>) {
    initApplication()
    runApplication<VezdecodApplication>(*args)
}








