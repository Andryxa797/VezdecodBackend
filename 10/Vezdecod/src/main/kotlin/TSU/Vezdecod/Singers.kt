package TSU.Vezdecod

class Singers() {
    private val singers: MutableList<Singer> = mutableListOf()
    fun addSinger(singer: Singer) {
        singers.add(singer)
    }
    fun getAllSignerResponse(): List<SingerResponse>{
        val response: MutableList<SingerResponse> = mutableListOf()
        singers.forEach{
            response.add(SingerResponse(it.getName(), it.getVotes()))
        }
        return response
    }
    fun findArtist(name: String): Singer? {
        var singer: Singer? = null
        singers.forEach{
            if(it.getName() == name){
                singer = it
            }
        }
        return singer
    }
}
data class SingerResponse(val name: String, val votes: Int)