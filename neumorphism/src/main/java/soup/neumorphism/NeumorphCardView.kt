package soup.neumorphism

import android.annotation.TargetApi
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.ColorInt
import androidx.core.view.ViewCompat
import soup.neumorphism.internal.util.NeumorphResources

class NeumorphCardView : FrameLayout {

    private var isInitialized: Boolean = false
    private val shapeDrawable: NeumorphShapeDrawable

    private var attrs: AttributeSet? = null
    private var defStyleAttr: Int = 0
    private var defStyleRes: Int = 0

    @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = R.attr.neumorphCardViewStyle
    ) : super(context, attrs, defStyleAttr) {
        this.attrs = attrs
        this.defStyleAttr = defStyleAttr
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = R.attr.neumorphCardViewStyle,
        defStyleRes: Int = R.style.Widget_Neumorph_CardView
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        this.attrs = attrs
        this.defStyleAttr = defStyleAttr
        this.defStyleRes = defStyleRes
    }

    init {
        val a = context.obtainStyledAttributes(
            attrs, R.styleable.NeumorphCardView, defStyleAttr, defStyleRes
        )
        val fillColor = a.getColorStateList(R.styleable.NeumorphCardView_neumorph_backgroundColor)
        val strokeColor = a.getColorStateList(R.styleable.NeumorphCardView_neumorph_strokeColor)
        val strokeWidth = a.getDimension(R.styleable.NeumorphCardView_neumorph_strokeWidth, 0f)
        val shapeType = a.getInt(R.styleable.NeumorphCardView_neumorph_shapeType, ShapeType.DEFAULT)
        val shadowElevation = a.getDimension(
            R.styleable.NeumorphCardView_neumorph_shadowElevation, 0f
        )
        val shadowColorLight = NeumorphResources.getColor(
            context, a,
            R.styleable.NeumorphCardView_neumorph_shadowColorLight,
            R.color.design_default_color_shadow_light
        )
        val shadowColorDark = NeumorphResources.getColor(
            context, a,
            R.styleable.NeumorphCardView_neumorph_shadowColorDark,
            R.color.design_default_color_shadow_dark
        )
        a.recycle()

        shapeDrawable = NeumorphShapeDrawable(context, attrs, defStyleAttr, defStyleRes).apply {
            setInEditMode(isInEditMode)
            setShapeType(shapeType)
            setShadowElevation(shadowElevation)
            setShadowColorLight(shadowColorLight)
            setShadowColorDark(shadowColorDark)
            setFillColor(fillColor)
            setStroke(strokeWidth, strokeColor)
            setTranslationZ(ViewCompat.getTranslationZ(this@NeumorphCardView))

            val left = paddingLeft
            val top = paddingTop
            val right = paddingRight
            val bottom = paddingBottom
            if (arrayOf(left, top, right, bottom).any { it > 0 }) {
                setPadding(left, top, right, bottom)
            }
        }
        setBackgroundInternal(shapeDrawable)
        isInitialized = true
    }

    override fun drawChild(canvas: Canvas, child: View, drawingTime: Long): Boolean {
        //TODO: clip using Outline smoothly
        val checkpoint = canvas.save()
        canvas.clipPath(shapeDrawable.getOutlinePath())
        try {
            return super.drawChild(canvas, child, drawingTime)
        } finally {
            canvas.restoreToCount(checkpoint)
        }
    }

    override fun setPadding(left: Int, top: Int, right: Int, bottom: Int) {
        super.setPadding(left, top, right, bottom)
        shapeDrawable.setPadding(left, top, right, bottom)
    }

    override fun setBackground(drawable: Drawable?) {
        setBackgroundDrawable(drawable)
    }

    override fun setBackgroundDrawable(drawable: Drawable?) {
        Log.i(LOG_TAG, "Setting a custom background is not supported.")
    }

    private fun setBackgroundInternal(drawable: Drawable?) {
        super.setBackgroundDrawable(drawable)
    }

    fun setShapeAppearanceModel(shapeAppearanceModel: NeumorphShapeAppearanceModel) {
        shapeDrawable.setShapeAppearanceModel(shapeAppearanceModel)
    }

    fun getShapeAppearanceModel(): NeumorphShapeAppearanceModel {
        return shapeDrawable.getShapeAppearanceModel()
    }

    override fun setBackgroundColor(color: Int) {
        shapeDrawable.setFillColor(ColorStateList.valueOf(color))
    }

    fun setBackgroundColor(backgroundColor: ColorStateList?) {
        shapeDrawable.setFillColor(backgroundColor)
    }

    fun getBackgroundColor(): ColorStateList? {
        return shapeDrawable.getFillColor()
    }

    fun setStrokeColor(strokeColor: ColorStateList?) {
        shapeDrawable.setStrokeColor(strokeColor)
    }

    fun getStrokeColor(): ColorStateList? {
        return shapeDrawable.getStrokeColor()
    }

    fun setStrokeWidth(strokeWidth: Float) {
        shapeDrawable.setStrokeWidth(strokeWidth)
    }

    fun getStrokeWidth(): Float {
        return shapeDrawable.getStrokeWidth()
    }

    fun setShapeType(@ShapeType shapeType: Int) {
        shapeDrawable.setShapeType(shapeType)
    }

    @ShapeType
    fun getShapeType(): Int {
        return shapeDrawable.getShapeType()
    }

    fun setShadowElevation(shadowElevation: Float) {
        shapeDrawable.setShadowElevation(shadowElevation)
    }

    fun getShadowElevation(): Float {
        return shapeDrawable.getShadowElevation()
    }

    fun setShadowColorLight(@ColorInt shadowColor: Int) {
        shapeDrawable.setShadowColorLight(shadowColor)
    }

    fun setShadowColorDark(@ColorInt shadowColor: Int) {
        shapeDrawable.setShadowColorDark(shadowColor)
    }

    override fun setTranslationZ(translationZ: Float) {
        super.setTranslationZ(translationZ)
        if (isInitialized) {
            shapeDrawable.setTranslationZ(translationZ)
        }
    }

    companion object {
        private const val LOG_TAG = "NeumorphCardView"
    }
}
