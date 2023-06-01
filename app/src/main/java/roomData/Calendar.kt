package roomData

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Calendar(

    val titolo: String,

    val colore: String?,

    val codiceIngresso: String,

    @PrimaryKey
    var id: Long = System.currentTimeMillis()

){
    // Costruttore senza argomenti richiesto da Firebase
    constructor() : this("", "", "")
}