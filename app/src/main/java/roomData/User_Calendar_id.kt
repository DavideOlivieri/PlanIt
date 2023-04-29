package roomData


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User_Calendar_id (

    val username: String,
    val calendar_id: Int,

    @PrimaryKey
    val id: Int
)