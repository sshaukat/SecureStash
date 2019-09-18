package ru.devipad.androidsecurestash

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.preference.PreferenceManager
import kotlinx.android.synthetic.main.activity_main.*
import ru.devipad.securestash.Crypto
import ru.devipad.securestash.SecureStash
import ru.devipad.securestash.SecureSharedPreferences

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val crypto = Crypto.getInstance("!qAzXsW2")

        button.setOnClickListener {
            val s = crypto.encrypt(textView.text.toString())
            textView.text = s
        }

        decodeButton.setOnClickListener {
            val s = crypto.decrypt(textView.text.toString())
            decodeTextView.text = s
        }





        val settings = Settings(PreferenceManager.getDefaultSharedPreferences(this))

        button2.setOnClickListener {
            settings.login = "user_login"
            settings.password = "user_password"
            textView2.text = settings.password

        }
    }


    private class Settings(sharedPreferences: SharedPreferences) {

        private val preferences = SecureSharedPreferences(
            sharedPreferences = sharedPreferences,
            key = "!qAzXsW2"
        )

        var login: String by SecureStash(preferences, LOGIN_KEY, "")
        var password: String by SecureStash(preferences, PASSWORD_KEY, "")

        fun clear() {
            preferences.remove(LOGIN_KEY)
            preferences.remove(PASSWORD_KEY)
        }


        companion object {
            private const val LOGIN_KEY = "login"
            private const val PASSWORD_KEY = "password"
        }
    }
}
