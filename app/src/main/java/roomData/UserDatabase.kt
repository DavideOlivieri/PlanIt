package roomData

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [User::class],
    version = 3
)
abstract class UserDatabase: RoomDatabase() {
    abstract fun dao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: UserDatabase? = null
        fun getInstance(context: Context): UserDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    UserDatabase::class.java, "user_database"
                ).allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }


}