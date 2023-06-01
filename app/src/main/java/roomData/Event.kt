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

    @PrimaryKey
    var id: Int = System.currentTimeMillis().toInt()
){
    // Costruttore senza argomenti richiesto da Firebase
    constructor() : this("", "", "","","",0)
}