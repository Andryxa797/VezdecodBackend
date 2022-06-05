package TSU.Vezdecod.Models

import TSU.Vezdecod.Helpers.getDataTimeNowInSeconds

class VoteRecord(public val number: String) {
    public var data: Long = getDataTimeNowInSeconds()
}

class Singer(private val name: String) {
    private var votes: Int = 0
    public var firsVoteDate: Long = 0
    public var lastVoteDate: Long = 0
    private var voteRecords: MutableList<VoteRecord> = mutableListOf()

    fun getName(): String {
        return name
    }

    fun getVotes(): Int {
        return votes
    }

    fun addVote(number: String) {
        if (voteRecords.isEmpty()) firsVoteDate = getDataTimeNowInSeconds()
        votes += 1
        lastVoteDate = getDataTimeNowInSeconds()
        voteRecords.add(VoteRecord(number))
    }

    fun getStat(
        from: Int? = null,
        to: Int? = null,
        artist: String? = null
    ): MutableList<VoteRecord> {
        val records: MutableList<VoteRecord> = mutableListOf()
        if (artist == null || artist == name) {
            if (from != null && to != null) {
                voteRecords.forEach {
                    if (it.data > from && it.data < to) records.add(it)
                }
            }
            if (from != null && to == null)
                voteRecords.forEach {
                    if (it.data > from) records.add(it)
                }
            if (from == null && to != null)
                voteRecords.forEach {
                    if (it.data < to) records.add(it)
                }
            if (from == null && to == null)
                voteRecords.forEach { records.add(it) }
        }
        return records
    }
}