package roomData

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(

    val password: String,
    val email: String,

    @PrimaryKey
    val user: String

)
