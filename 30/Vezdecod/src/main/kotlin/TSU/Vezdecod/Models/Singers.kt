package TSU.Vezdecod.Models

import TSU.Vezdecod.Core.StatsResponse

class Singers() {
    private val singers: MutableList<Singer> = mutableListOf()
    fun addSinger(singer: Singer) {
        singers.add(singer)
    }

    fun getAllSignerResponse(): List<SingerResponse> {
        val response: MutableList<SingerResponse> = mutableListOf()
        singers.forEach {
            response.add(SingerResponse(it.getName(), it.getVotes()))
        }
        return response
    }

    fun findArtist(name: String): Singer? {
        var singer: Singer? = null
        singers.forEach {
            if (it.getName() == name) {
                singer = it
            }
        }
        return singer
    }

    fun getStat(from: Int?, to: Int?, intervals: Int, artists: String?): List<List<StatsResponse>> {
        val singerList = artists?.split(',')
        val allRecord = mutableListOf<VoteRecord>()
        var fromRes: Long? = null
        var toRes: Long? = null
        if (singerList != null) {
            singerList.forEach {
                singers.forEach {
                    allRecord.addAll(it.getStat(from, to, artists))
                    if(fromRes == null){
                        fromRes = it.firsVoteDate
                    }
                    if(toRes == null){
                        toRes = it.lastVoteDate
                    }
                    if (fromRes!! > it.firsVoteDate) fromRes = it.firsVoteDate
                    if (toRes!! < it.lastVoteDate) toRes = it.lastVoteDate
                }
            }
        } else singers.forEach {
            allRecord.addAll(it.getStat(from, to, artists))
        }

        singers.forEach {
            it.getStat(from, to, artists)
        }
        allRecord.sortBy { it.data }
        val uniqueDate = mutableListOf<Long>()
        allRecord.forEach {
            uniqueDate.add(it.data)
        }
        uniqueDate.distinct()
        val minElement = uniqueDate.minOrNull()
        val maxElement = uniqueDate.minOrNull()
        if (minElement != null && maxElement != null) {
            val resultList = mutableListOf<MutableList<StatsResponse>>()
            val recordResultList = mutableListOf<StatsResponse>()
            val stats = StatsResponse()
            allRecord.forEachIndexed { index, element ->
                stats.votes += 1
            }
            stats.start = fromRes!!
            stats.start = toRes!!
            recordResultList.add(stats)
            resultList.add(recordResultList)
            return resultList.toList()
        } else return listOf(listOf())
    }
}

data class SingerResponse(val name: String, val votes: Int)