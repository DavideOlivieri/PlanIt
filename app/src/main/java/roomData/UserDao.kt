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

    @Query("SELECT * FROM User WHERE User.user = :user")
    fun selectUser(user: String?): User

    @Query("UPDATE User SET password = :password, email = :email WHERE User.user = :checkuser")
    fun modUser(checkuser: String?, password: String, email: String)



    // Calendar
    @Insert
    fun insertCalendar(calendar: Calendar)

    @Delete
    fun deleteCalendar(calendar: Calendar)



    @Query("SELECT * " +
            "FROM Calendar " +
            " INNER JOIN User_Calendar_id ON Calendar.id = User_Calendar_id.calendar_id " +
            " INNER JOIN User ON User_Calendar_id.username = User.user " +
            "WHERE User.user = :username"
            )
    fun selectAllIdCalendarofUser(username: String?): Array<Calendar>



    //Event
    @Insert
    fun insertEvent(event: Event)

    @Delete
    fun deleteEvent(event: Event)

    @Query("SELECT * FROM Event WHERE Event.id = :checkid")
    fun selectEvent(checkid: String): Event


    @Query("SELECT id FROM Calendar WHERE Calendar.titolo = :nome")
    fun getIdFromCalendar(nome: String): Long


    @Insert
    fun insertUserCalendarId(user_calendar_id: User_Calendar_id)


    @Query("SELECT * FROM Calendar WHERE Calendar.titolo = :id")
    fun selectCalendarbyId(id: String?): Calendar
}