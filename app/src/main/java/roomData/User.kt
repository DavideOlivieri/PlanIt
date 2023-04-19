package roomData

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity
data class User(

    val password: String,
    val email: String,

    @PrimaryKey
    val user: String

)
