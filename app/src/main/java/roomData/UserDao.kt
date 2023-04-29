package roomData

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {


    //User
    @Insert
    fun insertUser(user: User)

    @Delete
    fun deleteUser(user: User)

    @Query("SELECT * FROM User WHERE User.user = :checkpass")
    fun checkPass(checkpass: String): User

    @Query("SELECT user FROM User WHERE User.user = :checkuser")
    fun checkUser(checkuser: String): String

    @Query("UPDATE User SET password = :password, email = :email WHERE User.user = :checkuser")
    fun modUser(checkuser: String?, password: String, email: String)



    // Calendar
    @Insert
    fun insertCalendar(calendar: Calendar)

    @Delete
    fun deleteCalendar(calendar: Calendar)

    @Query("SELECT * FROM Calendar WHERE Calendar.id = :checkid")
    fun selectCalendar(checkid: String): Calendar



    //Event
    @Insert
    fun insertEvent(event: Event)

    @Delete
    fun deleteEvent(event: Event)

    @Query("SELECT * FROM Event WHERE Event.id = :checkid")
    fun selectEvent(checkid: String): Event
}