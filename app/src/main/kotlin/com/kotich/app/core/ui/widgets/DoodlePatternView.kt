package com.kotich.app.core.ui.widgets

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

/**
 * Draws scattered doodle patterns (stars, moons, planets, hearts, rockets)
 * over a transparent background — Telegram wallpaper style.
 *
 * Set as an overlay on top of a gradient background.
 */
class DoodlePatternView @JvmOverloads constructor(
	context: Context,
	attrs: AttributeSet? = null,
	defStyleAttr: Int = 0,
) : View(context, attrs, defStyleAttr) {

	private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
		style = Paint.Style.STROKE
		strokeWidth = 2f
		color = 0x1AFFFFFF // 10% white
	}

	private val doodles = mutableListOf<Doodle>()
	private var isInitialized = false

	override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
		super.onSizeChanged(w, h, oldw, oldh)
		if (w > 0 && h > 0 && !isInitialized) {
			generateDoodles(w, h)
			isInitialized = true
		}
	}

	override fun onDraw(canvas: Canvas) {
		super.onDraw(canvas)
		for (doodle in doodles) {
			when (doodle.type) {
				DoodleType.STAR -> drawStar(canvas, doodle.x, doodle.y, doodle.size)
				DoodleType.MOON -> drawMoon(canvas, doodle.x, doodle.y, doodle.size)
				DoodleType.PLANET -> drawPlanet(canvas, doodle.x, doodle.y, doodle.size)
				DoodleType.HEART -> drawHeart(canvas, doodle.x, doodle.y, doodle.size)
				DoodleType.ROCKET -> drawRocket(canvas, doodle.x, doodle.y, doodle.size)
				DoodleType.DOT -> drawDot(canvas, doodle.x, doodle.y, doodle.size)
			}
		}
	}

	private fun generateDoodles(w: Int, h: Int) {
		doodles.clear()
		val random = Random(42) // fixed seed for consistent pattern
		val count = (w * h) / 40000 // density based on screen size

		repeat(count) {
			doodles.add(
				Doodle(
					x = random.nextFloat() * w,
					y = random.nextFloat() * h,
					size = 8f + random.nextFloat() * 16f,
					type = DoodleType.entries[random.nextInt(DoodleType.entries.size)],
				),
			)
		}
	}

	private fun drawStar(canvas: Canvas, cx: Float, cy: Float, size: Float) {
		val path = android.graphics.Path()
		val points = 5
		val outerR = size
		val innerR = size * 0.4f

		for (i in 0 until points * 2) {
			val r = if (i % 2 == 0) outerR else innerR
			val angle = Math.toRadians((i * 180.0 / points) - 90)
			val x = cx + (r * cos(angle)).toFloat()
			val y = cy + (r * sin(angle)).toFloat()
			if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
		}
		path.close()
		canvas.drawPath(path, paint)
	}

	private fun drawMoon(canvas: Canvas, cx: Float, cy: Float, size: Float) {
		val path = android.graphics.Path()
		path.addCircle(cx, cy, size, android.graphics.Path.Direction.CW)
		path.addCircle(cx + size * 0.4f, cy - size * 0.3f, size * 0.7f, android.graphics.Path.Direction.CCW)
		canvas.drawPath(path, paint)
	}

	private fun drawPlanet(canvas: Canvas, cx: Float, cy: Float, size: Float) {
		canvas.drawCircle(cx, cy, size * 0.6f, paint)
		// Ring
		val oval = android.graphics.RectF(cx - size, cy - size * 0.3f, cx + size, cy + size * 0.3f)
		canvas.drawOval(oval, paint)
	}

	private fun drawHeart(canvas: Canvas, cx: Float, cy: Float, size: Float) {
		val path = android.graphics.Path()
		path.moveTo(cx, cy + size * 0.5f)
		path.cubicTo(cx - size, cy - size * 0.2f, cx - size * 0.5f, cy - size, cx, cy - size * 0.4f)
		path.cubicTo(cx + size * 0.5f, cy - size, cx + size, cy - size * 0.2f, cx, cy + size * 0.5f)
		canvas.drawPath(path, paint)
	}

	private fun drawRocket(canvas: Canvas, cx: Float, cy: Float, size: Float) {
		val path = android.graphics.Path()
		path.moveTo(cx, cy - size)
		path.lineTo(cx + size * 0.4f, cy + size * 0.3f)
		path.lineTo(cx, cy + size * 0.1f)
		path.lineTo(cx - size * 0.4f, cy + size * 0.3f)
		path.close()
		canvas.drawPath(path, paint)
	}

	private fun drawDot(canvas: Canvas, cx: Float, cy: Float, size: Float) {
		canvas.drawCircle(cx, cy, size * 0.15f, paint)
	}

	private data class Doodle(
		val x: Float,
		val y: Float,
		val size: Float,
		val type: DoodleType,
	)

	private enum class DoodleType {
		STAR, MOON, PLANET, HEART, ROCKET, DOT,
	}
}
