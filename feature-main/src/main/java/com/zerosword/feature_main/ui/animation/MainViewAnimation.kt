package com.zerosword.feature_main.ui.animation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.InfiniteTransition
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp


object SpotSummaryListAnimation {

    private const val TOTAL_DURATION = 2000
    suspend fun Animatable<Float, AnimationVector1D>.animateMainToSpotSummaryList(
        progress: Float = 0f,
        onComplete: () -> Unit
    ) {

        val min = TOTAL_DURATION / 3
        val max = TOTAL_DURATION

        animateTo(
            targetValue = 0f,
            animationSpec = tween(
                durationMillis = (TOTAL_DURATION * progress).toInt()
                    .coerceIn(
                        min,
                        TOTAL_DURATION.coerceIn(min, max)
                    ),
                easing = CubicBezierEasing(0.21f, 0.0f, 0.35f, 1.0f)
            )
        ) {
            onComplete()
        }
    }

    suspend fun Animatable<Float, AnimationVector1D>.animateSpotSummaryListToMain(
        progress: Float = 0f,
        onComplete: () -> Unit
    ) {

        animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = ((TOTAL_DURATION - (TOTAL_DURATION * progress).toInt()) / 2).coerceIn(
                    0,
                    TOTAL_DURATION
                ),
                easing = CubicBezierEasing(0.21f, 0.0f, 0.35f, 1.0f)
            )
        ) {
            onComplete()
        }
    }
}

object MainSpotDragAnimation {

    private const val TOTAL_DURATION = 1000

    private fun MainSpotDragAnimation.duration(progress: Float) =
        ((progress * TOTAL_DURATION).coerceIn(
            0f,
            TOTAL_DURATION.toFloat()
        )).toInt()

    suspend fun Animatable<Float, AnimationVector1D>.dragFromMainSpotToSpotSummary(
        progress: Float,
        onComplete: () -> Unit
    ) {
        val min = 0f
        val duration = duration(progress)

        animateTo(
            targetValue = min,
            animationSpec = tween(
                durationMillis = duration,
                easing = LinearEasing
            )
        ) {
            onComplete()
        }

    }

    suspend fun Animatable<Float, AnimationVector1D>.dragFromSpotSummaryToMainSpot(
        max: Float = 1f,
        progress: Float,
        onComplete: () -> Unit
    ) {

        val duration = (TOTAL_DURATION - duration(progress) / 2)

        animateTo(
            targetValue = max,
            animationSpec = tween(
                durationMillis = duration,
                easing = LinearEasing
            )
        ) {
            onComplete()
        }

    }

}

object MainSpotCircleAnimation {

    const val CIRCLE_DIAMETER_WIDTH_RATE1 = 2.1f
    const val CIRCLE_DIAMETER_HEIGHT_RATE1 = 2.24f

    const val CIRCLE_DIAMETER_WIDTH_RATE2 = 2.17f
    const val CIRCLE_DIAMETER_HEIGHT_RATE2 = 2.26f

    private const val ROTATION_DURATION = 20000
    private const val ROTATION_START_ANGLE = 0f
    private const val ROTATION_END_ANGLE = 360f


    fun MainSpotCircleAnimation.circleTranslationX2(width: Dp): Float {
        val circleWidth = width * CIRCLE_DIAMETER_WIDTH_RATE2
        return circleWidth.value / 2f - circleWidth.value / 1.8f
    }

    fun MainSpotCircleAnimation.circleTranslationY2(width: Dp, height: Dp): Float {
        val circleHeight = width * CIRCLE_DIAMETER_HEIGHT_RATE2
        return (height.value * 0.7f) - circleHeight.value * 1.45f
    }

    fun MainSpotCircleAnimation.circleTranslationX1(width: Dp): Float {
        val circleWidth = width * CIRCLE_DIAMETER_WIDTH_RATE1
        return -circleWidth.value / 1.3f
    }

    fun MainSpotCircleAnimation.circleTranslationY1(width: Dp, height: Dp): Float {
        val circleHeight = width * CIRCLE_DIAMETER_HEIGHT_RATE1
        return height.value * 0.7f - circleHeight.value * 1.35f
    }

    @Composable
    fun InfiniteTransition.infiniteRotation() = animateFloat(
        initialValue = ROTATION_START_ANGLE,
        targetValue = ROTATION_END_ANGLE,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = ROTATION_DURATION, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = ""
    )

}
