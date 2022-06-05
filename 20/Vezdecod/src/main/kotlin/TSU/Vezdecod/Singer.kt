package TSU.Vezdecod

class Singer(private val name: String) {
    private var votes: Int = 0

    fun getName(): String {
        return name
    }
    fun getVotes(): Int {
        return votes
    }
    fun incrementVote(){
        votes += 1
    }
}