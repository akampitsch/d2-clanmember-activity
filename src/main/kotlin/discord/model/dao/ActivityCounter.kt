package discord.model.dao

import discord.model.response.enum.DestinyActivityModeType
import org.hibernate.annotations.LazyCollection
import org.hibernate.annotations.LazyCollectionOption
import javax.persistence.*

@Entity
@Table(name = "activity_counters")
class ActivityCounter(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    val id: Long = -1,

    var activitySum: Long = 0,

    @OneToOne
    var clanMember: ClanMember? = null,

    @ElementCollection
    @LazyCollection(LazyCollectionOption.FALSE)
    val activitiesByMode: MutableMap<DestinyActivityModeType, Long> = mutableMapOf()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ActivityCounter

        if (id != other.id) return false
        if (activitySum != other.activitySum) return false
        if (activitiesByMode != other.activitiesByMode) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + activitySum.hashCode()
        result = 31 * result + activitiesByMode.hashCode()
        return result
    }

    override fun toString(): String {
        return "ActivityCounter(id=$id, activitySum=$activitySum, activityByMode=$activitiesByMode)"
    }


}