package y2k.noria.android

import android.content.Context
import android.widget.EditText
import android.widget.TextView
import com.google.android.flexbox.FlexDirection.*
import com.google.android.flexbox.FlexboxLayout
import com.google.android.flexbox.JustifyContent.*
import noria.HostComponentType
import noria.Platform
import noria.PlatformComponentType
import noria.Root
import noria.components.*
import y2k.noria.android.components.AndroidRoot
import y2k.noria.android.components.BeanHostProps
import y2k.noria.android.components.FlexBox
import y2k.noria.android.components.ManagedBeanView
import kotlin.reflect.KClass

object AndroidPlatform : Platform() {

    init {
        register(Root, ::AndroidRoot)

//        register(HBox, FlexboxLayout::class) { props ->
//            set(FlexboxLayout::setFlexDirection, props.flexDirection.toFlexboxLayout())
//        }
//
//        register(VBox, FlexboxLayout::class) { props ->
//            set(FlexboxLayout::setFlexDirection, props.flexDirection.toFlexboxLayout())
//        }

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
        }

        register(CheckBox, NCheckBox::class) { props ->
            set(NCheckBox::setStringText, props.text)
            set(NCheckBox::setChecked, props.bind.getter.call())
            set(NCheckBox::setEnabled, !props.disabled)
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

class NLabel(context: Context?) : TextView(context) {

    fun setStringText(x: String) {
        text = x
    }
}

class NButton(context: Context?) : android.widget.Button(context) {

    fun setStringText(x: String) {
        text = x
    }
}

class NTextField(context: Context?) : EditText(context) {

    fun setStringText(x: String) {
        setText(x, BufferType.NORMAL)
    }
}

class NCheckBox(context: Context?) : android.widget.CheckBox(context) {

    fun setStringText(x: String) {
        text = x
    }
}

class NPanel(context: Context?) : FlexboxLayout(context) {

    fun setFlex(box: BoxProps) {
        flexDirection = when (box.flexDirection) {
            FlexDirection.row -> ROW
            FlexDirection.column -> COLUMN
            FlexDirection.rowReverse -> ROW_REVERSE
            FlexDirection.columnReverse -> COLUMN_REVERSE
        }
        justifyContent = when (box.justifyContent) {
            null -> FLEX_START
            JustifyContent.center -> CENTER
            JustifyContent.flexStart -> FLEX_START
            JustifyContent.flexEnd -> FLEX_END
            JustifyContent.spaceBetween -> SPACE_BETWEEN
            JustifyContent.spaceAround -> SPACE_AROUND
            JustifyContent.spaceEvenly -> SPACE_EVENLY
            else -> TODO("Can't parse ${box.justifyContent}")
        }
    }
}