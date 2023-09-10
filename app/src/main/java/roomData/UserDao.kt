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

    @Query("SELECT * FROM Event WHERE Event.data = :data ORDER BY Event.orario_inizio ASC")
    fun getEventsByDate(data: String?): List<Event>

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
    fun selectEvent(checkid: Int): Event


    @Query("SELECT id FROM Calendar WHERE Calendar.titolo = :nome")
    fun getIdFromCalendar(nome: String): Long

    @Query("SELECT id FROM Calendar WHERE Calendar.titolo = :nome AND Calendar.codiceIngresso = :codiceIngresso")
    fun getIdFromCalendar(nome: String, codiceIngresso: String): Long

    @Query("SELECT id FROM Event WHERE Event.titolo = :titolo")
    fun getIdFromEvent(titolo: String): Int

    @Query("SELECT * FROM event WHERE Event.calendar_id = :calendarId AND Event.data = :data  ORDER BY Event.orario_inizio ASC")
    fun getEventsByCalendarId(calendarId: Long, data: String?): List<Event>

    @Insert
    fun insertUserCalendarId(user_calendar_id: User_Calendar_id)

    @Query("SELECT data FROM Event WHERE calendar_id = :idCalendario GROUP BY data HAVING COUNT(*) > 0")
    fun getDatesWithEvents(idCalendario: Long): List<String>

    @Query("SELECT * FROM Calendar WHERE Calendar.id = :id")
    fun selectCalendarbyId(id: Long): Calendar

    @Query("SELECT Calendar.id FROM Calendar WHERE Calendar.codiceIngresso = :codice")
    fun selectIdbyCodice(codice: String?): Long

    @Query("SELECT Calendar.id FROM Calendar INNER JOIN User_Calendar_id ON Calendar.id = User_Calendar_id.calendar_id INNER JOIN User ON User_Calendar_id.username = User.user WHERE Calendar.titolo = :titolo AND User.user = :username")
    fun getIdFromTitoloandUser(titolo: String?, username: String?): Long

    @Query("SELECT username FROM User_Calendar_id WHERE User_Calendar_id.calendar_id = :id")
    fun selectAllUserbyId(id: Long?): Array<String>

    @Query("SELECT livello FROM User_Calendar_id WHERE User_Calendar_id.calendar_id = :id AND User_Calendar_id.username = :username")
    fun selectLivello(username: String?, id: Long?): Int

    @Query("SELECT * FROM User_Calendar_id WHERE User_Calendar_id.calendar_id = :id AND User_Calendar_id.username = :username")
    fun selectUserCalendar(username: String?, id: Long?): User_Calendar_id

    @Query("SELECT * FROM User_Calendar_id WHERE User_Calendar_id.id = :id")
    fun selectUserCalendarbyID( id: Long?): User_Calendar_id

    @Query("SELECT * FROM User_Calendar_id WHERE User_Calendar_id.calendar_id = :id")
    fun selectUserCalendarbyCalendar( id: Long?): User_Calendar_id

    @Query("SELECT * FROM User_Calendar_id")
    fun selectAllUserCalendar(): List<User_Calendar_id>


    @Query("SELECT * FROM Calendar WHERE Calendar.codiceIngresso = :codiceIngresso")
    fun selectCalendarbyCodice( codiceIngresso: String?): Calendar

    @Delete
    fun deleteUserFromCalendar(user_calendar_id: User_Calendar_id)

    @Query("SELECT id FROM User_Calendar_id WHERE User_Calendar_id.username = :username")
    fun selectUserCalendarIdsByUsername(username: String?): List<Long>

    @Delete
    fun deleteUserCalendarIds(idsToDelete: List<User_Calendar_id>)

    @Query("SELECT * FROM User_Calendar_id WHERE username = :username AND id NOT IN (:existingIds)")
    fun selectUserCalendarIdsToDelete(username: String, existingIds: List<Long>): List<User_Calendar_id>

    @Query("SELECT id FROM Event WHERE Event.calendar_id = :calendarId")
    fun selectEventIdsByCalendarId(calendarId :String?): List<Int>

    @Query("SELECT * FROM Event WHERE calendar_id = :calendarId AND id NOT IN (:existingEventIds)")
    fun selectEventsToDelete(calendarId: String, existingEventIds: List<Int>): List<Event>

    @Delete
    fun deleteEvents(eventsToDelete: List<Event>)

    @Query("SELECT id FROM Calendar")
    fun selectCalendarIds(): List<Long>

    @Query("SELECT * FROM Calendar WHERE id NOT IN (:existingCalendarIds)")
    fun selectCalendarsToDelete(existingCalendarIds: List<Long>): List<Calendar>

    @Delete
    fun deleteCalendars(calendarsToDelete: List<Calendar>)

    @Query("SELECT * FROM User_Calendar_id WHERE username = :username AND id NOT IN (:existingIds)")
    fun selectAssociationsNotInList(username: String, existingIds: List<Long>): List<User_Calendar_id>

    @Query("SELECT * FROM Event WHERE calendar_id = :calendarId AND id NOT IN (:existingEventIds)")
    fun selectEventsNotInList(calendarId: String, existingEventIds: List<Int>): List<Event>

    @Query("SELECT * FROM Calendar WHERE id = :calendarId AND id NOT IN (:existingCalendarIds)")
    fun selectCalendarsNotInList(calendarId: String, existingCalendarIds: List<Long>): List<Calendar>
}