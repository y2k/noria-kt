package y2k.noria.robovm

import noria.Host
import noria.Update
import org.robovm.apple.dispatch.DispatchQueue
import org.robovm.apple.uikit.UIView
import kotlin.reflect.KFunction
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.memberFunctions
import kotlin.reflect.full.memberProperties

class RobovmDriver : Host {

    private val roots = mutableMapOf<String?, Any>()
    private val nodes = mutableMapOf<Int, Any>()

    override fun applyUpdates(updates: List<Update>) {
        DispatchQueue.getMainQueue().async {
            val dirtyComponents = mutableSetOf<UIView>()

            for (u in updates) {
                logUpdate(u)

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

            dirtyComponents.forEach(UIView::setNeedsLayout)
        }
    }

    private fun makeNode(u: Update.MakeNode) {
        if (nodes[u.node] != null) error("Update $u. Node already exists")

        val newElement = when (u.type) {
            "root" -> {
                val id = u.parameters["id"] as? String
                roots[id] ?: error("Root $id has not been registered")
            }

            else -> Class.forName(u.type).getConstructor().newInstance()
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

    private fun add(u: Update.Add, dirtyComponents: MutableSet<UIView>) {
        val node = nodes[u.node] ?: error("Update $u. Cannot find node")
        val child = nodes[u.value as Int] ?: error("Update $u. Cannot find child")

        (node as UIView).apply {
            node.insertSubview(child as UIView, u.index.toLong())
            dirtyComponents += this
        }
    }

    private fun remove(u: Update.Remove, dirtyComponents: MutableSet<UIView>) {
        val node = nodes[u.node] ?: error("Update $u. Cannot find node")
        val child = nodes[u.value as Int] ?: error("Update $u. Cannot find child")

        (node as UIView).apply {
            (child as UIView).removeFromSuperview()
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

    fun registerRoot(id: String, root: UIView) {
        roots[id] = root
    }

    private fun logUpdate(u: Update) {
//        if (BuildConfig.DEBUG)
//            Log.d("AndroidDriver", "update = $u")
    }
}