package ru.kropotov.denet.test.compose.node

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

@Composable
fun AbstractNodeShape(
    modifier: Modifier = Modifier,
    seed: String,
    size: Dp = 200.dp
) {
    val seed = seed.hashCode()
    val random = remember(seed) { Random(seed) }

    val colorStops = listOf(
        Color(0xFF6A00F4),
        Color(0xFFFF3C38),
        Color(0xFFFFA600),
        Color(0xFF00F0FF),
        Color(0xFF00FFB3),
        Color(0xFF0077FF),
        Color(0xFFFF00F0),
        Color(0xFFA6FF00)
    )

    val gradient = Brush.radialGradient(
        colors = colorStops.shuffled(random).take(4),
        center = Offset.Zero,
        radius = 800f
    )

    val infiniteTransition = rememberInfiniteTransition(label = "animations")

    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 20000, easing = LinearEasing)
        ),
        label = "rotation"
    )

    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    // Glow
    val glowColor = Color(0x12969696)

    Canvas(
        modifier = modifier
            .size(size)
            .graphicsLayer {
                rotationZ = rotation
                scaleX = pulseScale
                scaleY = pulseScale
            }
    ) {
        val width = size.toPx()
        val height = size.toPx()
        val center = Offset(width / 2, height / 2)
        val radius = width / 2.2f

        val path = Path()
        val points = mutableListOf<Offset>()

        val pointCount = 8 + random.nextInt(4)
        for (i in 0 until pointCount) {
            val angle = i * (2 * Math.PI / pointCount)
            val r = radius * (0.6f + random.nextFloat() * 0.5f)
            val x = (center.x + r * cos(angle)).toFloat()
            val y = (center.y + r * sin(angle)).toFloat()
            points.add(Offset(x, y))
        }

        path.moveTo(points[0].x, points[0].y)
        for (i in 1 until points.size) {
            val p0 = points[i - 1]
            val p1 = points[i]
            val mid = Offset((p0.x + p1.x) / 2, (p0.y + p1.y) / 2)
            path.quadraticTo(p0.x, p0.y, mid.x, mid.y)
        }

        val pLast = points.last()
        val pFirst = points.first()
        val midLast = Offset((pLast.x + pFirst.x) / 2, (pLast.y + pFirst.y) / 2)
        path.quadraticTo(pLast.x, pLast.y, midLast.x, midLast.y)

        path.close()

        // Glowing path
        drawCircle(
            color = glowColor
        )

        // Main figure
        drawPath(path = path, brush = gradient)
    }
}