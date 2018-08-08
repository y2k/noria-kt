package y2k.noria.robovm.components

import noria.*
import noria.components.BoxProps
import noria.components.Container
import y2k.noria.robovm.NPanel

class PanelProps : HostProps() {
    var flex by value<BoxProps>()
    var children by elementList<MutableList<NElement<*>>>()
}

val Panel = HostComponentType<PanelProps>(NPanel::class.qualifiedName!!)

class FlexBox(props: BoxProps) : Container<BoxProps>(props) {
    override fun RenderContext.render() {
        x(Panel) {
            flex = props
            children.addAll(props.children)
        }
    }
}