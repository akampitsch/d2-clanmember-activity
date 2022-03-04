package discord.model.dao

import com.fasterxml.jackson.annotation.JsonIgnore
import discord.model.response.enum.DestinyActivityModeType
import org.hibernate.Hibernate
import org.hibernate.annotations.LazyCollection
import org.hibernate.annotations.LazyCollectionOption
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "activities")
class Activity(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    val id: Long = -1,
    
    @Column(unique = true)
    val instanceId: Long = -1,
    
    val date: LocalDateTime = LocalDateTime.now(),
    
    @ElementCollection(targetClass = DestinyActivityModeType::class)
    @LazyCollection(LazyCollectionOption.FALSE)
    @Enumerated(EnumType.ORDINAL)
    @CollectionTable(name="activity_modes")
    val modes: MutableList<DestinyActivityModeType> = mutableListOf(),
    
    @JsonIgnore
    @ManyToMany(cascade = [CascadeType.ALL])
    @LazyCollection(LazyCollectionOption.FALSE)
    val players: MutableList<ClanMember> = mutableListOf()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Activity

        return id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id , instanceId = $instanceId , date = $date , modes = $modes )"
    }
}