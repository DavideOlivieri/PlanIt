package com.example.planit



import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import com.example.calendario.R
import org.junit.After
import org.junit.Before


@RunWith(AndroidJUnit4::class)
class LogInTest {

    @get:Rule
    val activityRule = ActivityTestRule(MainActivity::class.java)

    @Before
    fun setUp() {
        // Inizializza Espresso Intents prima di eseguire il test
        Intents.init()
    }

    @After
    fun tearDown() {
        // Rilascia Espresso Intents dopo il test
        Intents.release()
    }

    @Test
    fun testLoginSuccess() {
        // Inserisci lo username e la password validi
        onView(withId(R.id.username)).perform(typeText("jacopo"))
        onView(withId(R.id.password)).perform(typeText("a1"))
        onView(withId(R.id.btn_login)).perform(click())


        // Verifica che l'intent per avviare l'attività Home sia stato inviato
        Intents.intended(hasComponent(Home::class.java.name))
    }

}