package roomData

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Event(

    val titolo: String,
    val data: String,
    val orario_inizio: String,
    val orario_fine: String,
    val descrizione: String,
    val calendar_id: Long,

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)