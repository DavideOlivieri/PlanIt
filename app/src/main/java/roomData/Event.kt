package roomData

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Event (

    val titolo: String,
    val giorno: Int,
    val mese: Int,
    val anno: Int,
    val descrizione: String,
    val calendar_id: Int,

    @PrimaryKey
    val id: Int
)