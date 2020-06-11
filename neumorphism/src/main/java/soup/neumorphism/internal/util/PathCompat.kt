package soup.neumorphism.internal.util

import android.graphics.Path
import android.graphics.RectF
import android.os.Build

internal object PathCompat {

    fun addOval(path: Path, left: Float, top: Float, right: Float, bottom: Float, dir: Path.Direction) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            path.addOval(left, top, right, bottom, dir)
        } else {
            val rect = RectF(left, top, right, bottom)
            path.addOval(rect, dir)
        }
    }

    fun addRoundRect(path: Path, left: Float, top: Float, right: Float, bottom: Float, rx: Float, ry: Float, dir: Path.Direction) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            path.addRoundRect(left, top, right, bottom, rx, ry, dir)
        } else {
            val rect = RectF(left, top, right, bottom)
            path.addRoundRect(rect, rx, ry, dir)
        }
    }

}