package y2k.noria.android

import noria.Platform
import noria.Root
import y2k.noria.android.components.AndroidRoot

object AndroidPlatform : Platform() {

    init {
        register(Root, ::AndroidRoot)
    }
}