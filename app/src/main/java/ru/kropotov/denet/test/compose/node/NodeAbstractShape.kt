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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

@Composable
fun AbstractNodeShape(
    modifier: Modifier = Modifier,
    seed: String,
    size: Dp = 180.dp,
    isMinimized: Boolean = false
) {
    val seedHash = seed.hashCode()
    val random = remember(seedHash) { Random(seedHash) }

    val widthPx = with(LocalDensity.current) { size.toPx() }
    val heightPx = widthPx
    val center = Offset(widthPx / 2, heightPx / 2)
    val radius = widthPx / 2.2f

    val randomPoint = random.nextInt(4)
    val randomRadius = random.nextFloat()

    val points = remember(seedHash, widthPx) {
        val deformationFactor = 0.15f
        val pointCount = 8 + randomPoint

        List(pointCount) { i ->
            val angle = i * (1.92 * Math.PI / pointCount)

            val baseRadius = radius * (0.6f + randomRadius * 0.3f)

            val deformation = (random.nextFloat() * 2f - 1f) * radius * deformationFactor

            val r = baseRadius + deformation
            Offset(
                x = (center.x + r * cos(angle)).toFloat(),
                y = (center.y + r * sin(angle)).toFloat()
            )
        }
    }

    val shuffledColors = remember(seedHash) {
        listOf(
            Color(0xFF00FFB3),
            Color(0xFF6A00F4),
            Color(0xFFFF3C38),
            Color(0xFFFFA600),
            Color(0xFF0077FF),
            Color(0xFFFF00F0),
            Color(0xFF00F0FF),
            Color(0xFFA6FF00)
        ).shuffled(random).take(4)
    }

    val gradient = Brush.linearGradient(
        colors = shuffledColors,
        start = Offset.Zero,
        end = Offset(widthPx, heightPx)
    )

    val glowColor = Color(0x12969696)

    var drawModifier = modifier.size(size)
    if (!isMinimized) {
        val infiniteTransition = rememberInfiniteTransition()
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

        drawModifier = drawModifier.graphicsLayer {
            rotationZ = rotation
            scaleX = pulseScale
            scaleY = pulseScale
        }
    }

    Canvas(drawModifier) {
        val path = Path()
        path.moveTo(points[0].x, points[0].y)

        for (i in 1 until points.size) {
            val p0 = points[i - 1]
            val p1 = points[i]
            val mid = Offset((p0.x + p1.x) / 2.02f, (p0.y + p1.y) / 2)
            path.quadraticTo(p0.x, p0.y, mid.x, mid.y)
        }

        val pLast = points.last()
        val pFirst = points.first()
        val midLast = Offset((pLast.x + pFirst.x) / 2, (pLast.y + pFirst.y) / 2)
        path.quadraticTo(pLast.x, pLast.y, midLast.x, midLast.y)

        path.close()

        if (!isMinimized) {
            drawCircle(color = glowColor)
        }

        drawPath(path = path, brush = gradient)
    }
}

@Preview
@Composable
fun NodeAbstrachShapePreview() {
    AbstractNodeShape(seed = "123")
}