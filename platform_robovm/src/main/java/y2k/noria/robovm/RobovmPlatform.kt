package y2k.noria.robovm

import noria.HostComponentType
import noria.Platform
import noria.PlatformComponentType
import noria.Root
import noria.components.*
import org.robovm.apple.uikit.*
import y2k.noria.robovm.components.BeanHostProps
import y2k.noria.robovm.components.FlexBox
import y2k.noria.robovm.components.ManagedBeanView
import y2k.noria.robovm.components.RobovmRoot
import kotlin.reflect.KClass

object RobovmPlatform : Platform() {

    init {
        register(Root, ::RobovmRoot)
        register(HBox, ::FlexBox)
        register(VBox, ::FlexBox)

        register(Label, NLabel::class) { props ->
            set(NLabel::setStringText, props.text)
        }

        register(Button, NButton::class) { props ->
            set(NButton::setStringText, props.title)
            set(NButton::setEnabled, !props.disabled)
        }

        register(TextField, NTextField::class) { props ->
            set(NTextField::setStringText, props.bind.getter.call())
            set(NTextField::setEnabled, !props.disabled)
            set(NTextField::onTextChanged, props.bind::set)
            set(NTextField::events, props.events)
        }

        register(CheckBox, NCheckBox::class) { props ->
            set(NCheckBox::setStringText, props.text)
            set(NCheckBox::setCheckOn, props.bind.getter.call())
            set(NCheckBox::setEnabled, !props.disabled)
            set(NCheckBox::onChange, props.bind::set)
        }
    }

    private fun <Props, Bean : Any> register(
        pct: PlatformComponentType<Props>,
        bean: KClass<Bean>,
        build: BeanHostProps<Bean>.(Props) -> Unit
    ) {
        register(pct) { props ->
            ManagedBeanView(props, HostComponentType(bean.qualifiedName!!), build)
        }
    }
}

class NLabel : UITextView() {

    fun setStringText(x: String) {
        text = x
    }
}

class NButton : UIButton() {

    fun setStringText(x: String) {
        setTitle(x, UIControlState.Normal)
    }
}

class NTextField : UITextField() {

    var onTextChanged: ((newText: String) -> Unit)? = null
    var events: Events? = null

    init {
        addOnEditingChangedListener {
            onTextChanged?.invoke(text)
        }

//        setOnEditorActionListener { _, actionId, _ ->
//            if (actionId == EditorInfo.IME_ACTION_DONE) {
//                events?.onEnter?.invoke()
//            }
//            false
//        }
    }

    fun setStringText(t: String) {
        if (t != text) {
            text = t
        }
    }
}

class NCheckBox : UISwitch() {

    var onChange: ((v: Boolean) -> Unit)? = null

    init {
        this.addOnValueChangedListener {
            onChange?.invoke(isOn)
        }
    }

    fun setCheckOn(x: Boolean) {
        isOn = x
    }

    fun setStringText(x: String) {
        // TODO:
    }
}

class NPanel : UIView() {

    private var flexDirection: FlexDirection = FlexDirection.column
    private var justifyContent: JustifyContent? = null

    fun setFlex(box: BoxProps) {
        this.flexDirection = box.flexDirection
        this.justifyContent = box.justifyContent
    }

    override fun layoutSubviews() {
        super.layoutSubviews()

        // FIXME:

    }

}