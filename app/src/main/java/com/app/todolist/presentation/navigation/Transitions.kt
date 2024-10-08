package com.app.todolist.presentation.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut

/**
 * Created by P,Venkatesh on 06-Aug-24
 *
 */
fun scaleIntoContainer(
    direction: ScaleTransitionDirection = ScaleTransitionDirection.INWARDS,
    initialScale: Float = if (direction == ScaleTransitionDirection.OUTWARDS) 0.9f else 1.1f
): EnterTransition {
    return scaleIn(
        animationSpec = tween(durationMillis = 220, delayMillis = 90),
        initialScale = initialScale
    ) + fadeIn(animationSpec = tween(durationMillis = 220, delayMillis = 90))
}

fun scaleOutOfContainer(
    direction: ScaleTransitionDirection = ScaleTransitionDirection.OUTWARDS,
    targetScale: Float = if (direction == ScaleTransitionDirection.INWARDS) 0.9f else 1.1f
): ExitTransition {
    return scaleOut(
        animationSpec = tween(durationMillis = 220, delayMillis = 90),
        targetScale = targetScale
    ) + fadeOut(animationSpec = tween(delayMillis = 90))
}

enum class ScaleTransitionDirection {
    OUTWARDS,
    INWARDS
}