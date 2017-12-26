package noria.views

import org.jetbrains.noria.*

val inputCT = HostComponentType<InputProps>("input")
class Button : View<ButtonProps>() {
    override fun RenderContext.render() {
        inputCT with InputProps().apply {
            type = "button"
            value = props.title

            if (props.disabled) {
                disabled = "true"
            }

            click = CallbackInfo(true) {
                props.action()
            }
        }
    }
}
