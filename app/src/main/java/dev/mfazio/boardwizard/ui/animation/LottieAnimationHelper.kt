package dev.mfazio.boardwizard.ui.animation

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.toArgb
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.compose.LottieDynamicProperties
import com.airbnb.lottie.compose.rememberLottieDynamicProperties
import com.airbnb.lottie.compose.rememberLottieDynamicProperty

object LottieAnimationHelper {
    @Composable
    private fun getDynamicColorProperties(specs: List<LottiePropertySpec>) =
        specs.flatMap { spec ->
            (0 until spec.groupCount).flatMap { group ->
                (0 until spec.fillCount).map { fill ->
                    rememberLottieDynamicProperty(
                        property = LottieProperty.COLOR,
                        value = spec.colors[group + fill].toArgb(),
                        keyPath = arrayOf(
                            spec.layerName,
                            "Group ${group + 1}",
                            "Fill ${fill + 1}"
                        )
                    )
                }
            }
        }.toTypedArray()

    @Composable
    fun getBoardGameProperties(): LottieDynamicProperties {
        val rightBills = getDynamicColorProperties(
            listOf(
                LottiePropertySpec(
                    colors = listOf(
                        MaterialTheme.colors.secondary,
                        MaterialTheme.colors.secondary,
                        MaterialTheme.colors.secondary,
                        MaterialTheme.colors.secondary,
                        MaterialTheme.colors.secondaryVariant,
                    ),
                    layerName = "Layer 2 Outlines",
                    groupCount = 5,
                    fillCount = 1
                ),
                LottiePropertySpec(
                    colors = listOf(
                        MaterialTheme.colors.secondary,
                        MaterialTheme.colors.secondary,
                        MaterialTheme.colors.secondary,
                        MaterialTheme.colors.secondary,
                        MaterialTheme.colors.secondaryVariant,
                    ),
                    layerName = "Layer 3 Outlines",
                    groupCount = 5,
                    fillCount = 1
                ),
            )
        )
        val leftBills = getDynamicColorProperties(
            listOf(
                LottiePropertySpec(
                    colors = listOf(
                        MaterialTheme.colors.primary,
                        MaterialTheme.colors.primary,
                        MaterialTheme.colors.primary,
                        MaterialTheme.colors.primary,
                        MaterialTheme.colors.primaryVariant,
                    ),
                    layerName = "Layer 6 Outlines",
                    groupCount = 5,
                    fillCount = 1
                ),
                LottiePropertySpec(
                    colors = listOf(
                        MaterialTheme.colors.primary,
                        MaterialTheme.colors.primary,
                        MaterialTheme.colors.primary,
                        MaterialTheme.colors.primary,
                        MaterialTheme.colors.primaryVariant,
                    ),
                    layerName = "Layer 7 Outlines",
                    groupCount = 5,
                    fillCount = 1
                ),
            )
        )
        val dice = getDynamicColorProperties(
            listOf(
                LottiePropertySpec(
                    colors = listOf(
                        MaterialTheme.colors.onPrimary,
                    ),
                    layerName = "Layer 11 Outlines",
                    groupCount = 1,
                    fillCount = 1
                ),
                LottiePropertySpec(
                    colors = listOf(
                        MaterialTheme.colors.onPrimary,
                    ),
                    layerName = "Layer 12 Outlines",
                    groupCount = 1,
                    fillCount = 1
                ),
                LottiePropertySpec(
                    colors = listOf(
                        MaterialTheme.colors.onPrimary,
                    ),
                    layerName = "Layer 13 Outlines",
                    groupCount = 1,
                    fillCount = 1
                ),
                LottiePropertySpec(
                    colors = listOf(
                        MaterialTheme.colors.primary,
                    ),
                    layerName = "Layer 14 Outlines",
                    groupCount = 1,
                    fillCount = 1
                ),
                LottiePropertySpec(
                    colors = listOf(
                        MaterialTheme.colors.onPrimary,
                        MaterialTheme.colors.primary,
                    ),
                    layerName = "Layer 15 Outlines",
                    groupCount = 2,
                    fillCount = 1
                ),
            )
        )

        return rememberLottieDynamicProperties(properties = leftBills + rightBills + dice)
    }
}