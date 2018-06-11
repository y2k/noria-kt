package y2k.noria.android

import android.app.Activity
import android.os.Handler
import android.os.Looper
import noria.Host
import noria.Update

class AndroidDriver : Host {

    private val handler = Handler(Looper.getMainLooper())

    override fun applyUpdates(updates: List<Update>) {
        handler.post {
            for (u in updates) {
                when (u) {
                    is Update.MakeNode -> TODO()
                    is Update.SetAttr -> TODO()
                    is Update.SetNodeAttr -> TODO()
                    is Update.SetCallback -> TODO()
                    is Update.RemoveCallback -> TODO()
                    is Update.Add -> TODO()
                    is Update.Remove -> TODO()
                    is Update.DestroyNode -> TODO()
                }
            }
        }
    }

    fun registerRoot(activity: Activity) {
//        TODO()
    }
}