package y2k.noria.robovm.components

import noria.*

class RobovmRoot(p: RootProps) : View<RootProps>(p) {

    override fun RenderContext.render() {
        x(root) {
            id = props.id
            children.add(props.child)
        }
    }

    companion object {
        private val root = HostComponentType<AndroidRootProps>("root")
    }
}

class AndroidRootProps : HostProps() {
    var id: String by value(true)
    val children: MutableList<NElement<*>> by elementList()
}