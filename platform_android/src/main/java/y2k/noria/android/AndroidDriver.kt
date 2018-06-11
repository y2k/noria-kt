package y2k.noria.android

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import noria.Host
import noria.Update
import kotlin.reflect.KFunction
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.memberFunctions
import kotlin.reflect.full.memberProperties

class AndroidDriver(private val context: Context) : Host {

    private val roots = mutableMapOf<String?, Any>()
    private val nodes = mutableMapOf<Int, Any>()

    override fun applyUpdates(updates: List<Update>) {
        val dirtyComponents = mutableSetOf<View>()

        for (u in updates) {
            when (u) {
                is Update.MakeNode -> makeNode(u)
                is Update.SetAttr -> setAttr(u)
                is Update.SetNodeAttr -> setNodeAttr(u)
                is Update.SetCallback -> setCallback(u)
                is Update.RemoveCallback -> removeCallback(u)
                is Update.Add -> add(u, dirtyComponents)
                is Update.Remove -> remove(u, dirtyComponents)
                is Update.DestroyNode -> destroyNode(u)
            }
        }

        dirtyComponents.forEach(View::invalidate)
    }

    private fun makeNode(u: Update.MakeNode) {
        if (nodes[u.node] != null) error("Update $u. Node already exists")

        val newElement = when (u.type) {
            "root" -> {
                val id = u.parameters["id"] as? String
                roots[id] ?: error("Root $id has not been registered")
            }

            else -> Class.forName(u.type).getConstructor(Context::class.java).newInstance(context)
        }

        nodes[u.node] = newElement
    }

    private fun setAttr(u: Update.SetAttr) {
        val node = nodes[u.node] ?: error("Update $u. Cannot find node")

        if (u.attr != "id")
            findFunction(node, u.attr)?.call(node, u.value)
    }

    private fun setNodeAttr(u: Update.SetNodeAttr) {
        val node = nodes[u.node] ?: error("Update $u. Cannot find node")
        val v = nodes[u.value]
        findFunction(node, u.attr)?.call(node, v)
    }

    private fun setCallback(u: Update.SetCallback): Unit = TODO()
    private fun removeCallback(u: Update.RemoveCallback): Unit = TODO()

    private fun add(u: Update.Add, dirtyComponents: MutableSet<View>) {
        val node = nodes[u.node] ?: error("Update $u. Cannot find node")
        val child = nodes[u.value as Int] ?: error("Update $u. Cannot find child")

        (node as ViewGroup).apply {
            addView(child as View, u.index)
            dirtyComponents += this
        }
    }

    private fun remove(u: Update.Remove, dirtyComponents: MutableSet<View>) {
        val node = nodes[u.node] ?: error("Update $u. Cannot find node")
        val child = nodes[u.value as Int] ?: error("Update $u. Cannot find child")

        (node as ViewGroup).apply {
            removeView(child as View)
            dirtyComponents += this
        }
    }

    private fun destroyNode(u: Update.DestroyNode) {
        nodes[u.node] ?: error("Update $u. Cannot find node")
        nodes.remove(u.node)
    }

    @Suppress("UNCHECKED_CAST")
    private fun findFunction(node: Any, attr: String): KFunction<Any?>? {
        // TODO Reflection cache

        val setterName = "set${attr.capitalize()}"
        return node::class.memberFunctions.find { it.name == setterName }
            ?: (node::class.memberProperties.find { it.name == attr } as? KMutableProperty<Any?>)?.setter
    }

    fun registerRoot(id: String, activity: Activity) {
        val root = FrameLayout(activity)
        roots[id] = root

        activity.setContentView(root)
    }
}