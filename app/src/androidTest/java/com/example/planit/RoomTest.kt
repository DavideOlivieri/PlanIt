package com.example.planit

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import roomData.User
import roomData.UserDao
import roomData.UserDatabase

@RunWith(AndroidJUnit4::class)
class RoomTest {

    private lateinit var userDao: UserDao
    private lateinit var db: UserDatabase

    @Before
    fun setup() {
        // Inizializza il database Room in memoria temporanea
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        db = Room.inMemoryDatabaseBuilder(context, UserDatabase::class.java).build()
        userDao = db.dao()
    }

    @After
    fun tearDown() {
        // Chiudi il database dopo il test
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun testInsertUser() {
        // Crea un utente
        val user = User("test1", "test@gmail.com","Test")

        // Inserisci l'utente nel database
        userDao.insertUser(user)

        // Recupera l'utente dal database utilizzando il suo ID (o un altro campo unico)
        val retrievedUser = userDao.selectUser(user.user)

        // Verifica che l'utente inserito corrisponda all'utente recuperato
        assert(retrievedUser == user)
    }
}