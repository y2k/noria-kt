package y2k.noriaandroid

import android.app.Activity
import android.os.Bundle
import android.widget.Button

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(Button(this).apply { text = "Hello World" })
    }
}