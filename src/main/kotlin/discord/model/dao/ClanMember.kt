package discord.model.dao

import com.fasterxml.jackson.annotation.JsonIgnore
import org.hibernate.Hibernate
import org.hibernate.annotations.LazyCollection
import org.hibernate.annotations.LazyCollectionOption
import javax.persistence.*

@Entity
@Table(name = "clan_member")
class ClanMember(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    val id: Long = -1L,
    @Column(unique = true)
    val membershipId: Long = -1L,
    
    val displayName: String = "",
    
    val bungieDisplayName: String = "",
    
    val memberShipType: Int = -1,
    
    @OneToMany(cascade = [CascadeType.ALL])
    @LazyCollection(LazyCollectionOption.FALSE)
    var characters: MutableList<Character> = mutableListOf(),
    
    @JsonIgnore
    @ManyToMany(cascade = [CascadeType.ALL])
    @LazyCollection(LazyCollectionOption.FALSE)
    val activities: MutableList<Activity> = mutableListOf(),
    
    @OneToOne(mappedBy = "clanMember", cascade = [CascadeType.ALL])
    @LazyCollection(LazyCollectionOption.FALSE)
    var activityCounter: ActivityCounter
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as ClanMember

        return membershipId == other.membershipId
    }

    override fun hashCode(): Int = javaClass.hashCode()
    
    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id , membershipId = $membershipId , displayName = $displayName , bungieDisplayName = $bungieDisplayName , memberShipType = $memberShipType , characterIds = $characters , activities = $activities )"
    }
}
