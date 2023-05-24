package roomData


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User_Calendar_id(

    var username: String?,
    var calendar_id: Long,
    val livello: Int,

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0
)