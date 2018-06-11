package y2k.noria.android

import android.app.Activity
import android.os.Bundle
import noria.GraphState
import noria.demo.DemoAppComponent
import noria.demo.DemoAppProps
import noria.x

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initNoria()
    }

    private fun initNoria() {
        val driver = AndroidDriver(this)
        driver.registerRoot("app", this)

        GraphState(AndroidPlatform, driver).apply {
            mount("app") {
                x(::DemoAppComponent, DemoAppProps())
            }
        }
    }
}