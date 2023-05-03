package roomData

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Calendar(

    val titolo: String,

    val colore: String,

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0

)