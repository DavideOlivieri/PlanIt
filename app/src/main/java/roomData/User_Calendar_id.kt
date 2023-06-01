package roomData


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User_Calendar_id(

    var username: String?,
    var calendar_id: Long,
    val livello: String,

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
){
    // Costruttore senza argomenti richiesto da Firebase
    constructor() : this("", 0, "")
}