package roomData


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User_Calendar_id(

    var username: String?,
    var calendar_id: Long,
    val livello: String,

    @PrimaryKey
    var id: Long =System.currentTimeMillis()
){
    // Costruttore senza argomenti richiesto da Firebase
    constructor() : this("", 0, "")
}