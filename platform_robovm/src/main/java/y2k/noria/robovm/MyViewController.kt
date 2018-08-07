package y2k.noria.robovm

import org.robovm.apple.coregraphics.CGRect
import org.robovm.apple.uikit.NSTextAlignment
import org.robovm.apple.uikit.UIButton
import org.robovm.apple.uikit.UIButtonType
import org.robovm.apple.uikit.UIColor
import org.robovm.apple.uikit.UIControl
import org.robovm.apple.uikit.UIControlState
import org.robovm.apple.uikit.UIEvent
import org.robovm.apple.uikit.UIFont
import org.robovm.apple.uikit.UILabel
import org.robovm.apple.uikit.UIView
import org.robovm.apple.uikit.UIViewController

class MyViewController : UIViewController() {

    fun foo(root: Any) {
        val driver = RobovmDriver()
        driver.registerRoot("app", root)
    }

    init {
        // Get the view of this view controller.
        val view = view

        // Setup background.
        view.backgroundColor = UIColor.white()

        // Setup label.
        val label = UILabel(CGRect(20.0, 250.0, 280.0, 44.0))
        label.font = UIFont.getSystemFont(24.0)
        label.textAlignment = NSTextAlignment.Center
        view.addSubview(label)

        // Setup button.
        val button = UIButton(UIButtonType.RoundedRect)
        button.frame = CGRect(110.0, 150.0, 100.0, 40.0)
        button.setTitle("Click me!", UIControlState.Normal)
        button.titleLabel.font = UIFont.getBoldSystemFont(22.0)

        var clickCount = 0
        button.addOnTouchUpInsideListener { _, _ ->
            label.text = "Click Nr. ${++clickCount}"
        }

        view.addSubview(button)
    }
}