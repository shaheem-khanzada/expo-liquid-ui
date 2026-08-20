package expo.modules.nativeliquidtabs

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastCoerceAtMost
import androidx.compose.ui.util.lerp
import com.kyant.backdrop.drawBackdrop
import com.kyant.backdrop.backdrops.emptyBackdrop
import com.kyant.backdrop.effects.blur
import com.kyant.backdrop.effects.lens
import com.kyant.backdrop.effects.vibrancy
import com.kyant.shapes.Capsule
import expo.modules.kotlin.views.ComposeProps
import expo.modules.kotlin.views.FunctionalComposableScope
import expo.modules.kotlin.views.OptimizedComposeProps
import expo.modules.nativeliquidtabs.kyant.utils.InteractiveHighlight
import expo.modules.ui.ModifierList
import expo.modules.ui.ModifierRegistry
import expo.modules.ui.UIComposableScope
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.tanh

@OptimizedComposeProps
data class LiquidButtonProps(
  val backdropEnabled: Boolean = true,
  val interactive: Boolean = true,
  val enabled: Boolean = true,
  val tint: String? = null,
  val surfaceColor: String? = null,
  val contentPaddingHorizontal: Float = 16f,
  val contentSpacing: Float = 8f,
  val modifiers: ModifierList = emptyList(),
) : ComposeProps

@Composable
fun FunctionalComposableScope.LiquidButtonContent(
  props: LiquidButtonProps,
  onPress: () -> Unit,
) {
  val backdrop = if (props.backdropEnabled) {
    LocalNativeLiquidBackdrop.current ?: emptyBackdrop()
  } else {
    emptyBackdrop()
  }
  val animationScope = rememberCoroutineScope()
  val interactiveHighlight = remember(animationScope) {
    InteractiveHighlight(animationScope = animationScope)
  }
  val tint = parseNativeLiquidColor(props.tint) ?: Color.Unspecified
  val surfaceColor = parseNativeLiquidColor(props.surfaceColor) ?: Color.Unspecified
  val baseModifier = ModifierRegistry.applyModifiers(
    props.modifiers,
    appContext,
    composableScope,
    globalEventDispatcher,
  )
  val liquidModifier = baseModifier.drawBackdrop(
    backdrop = backdrop,
    shape = { Capsule() },
    effects = {
      if (props.backdropEnabled) {
        vibrancy()
        blur(2f.dp.toPx())
        lens(12f.dp.toPx(), 24f.dp.toPx())
      }
    },
    layerBlock = if (props.interactive && props.enabled) {
      {
        val width = size.width
        val height = size.height
        val progress = interactiveHighlight.pressProgress
        val scale = lerp(1f, 1f + 8f.dp.toPx() / size.height, progress)
        val maxOffset = size.minDimension
        val initialDerivative = 0.05f
        val offset = interactiveHighlight.offset

        translationX = maxOffset * tanh(initialDerivative * offset.x / maxOffset)
        translationY = maxOffset * tanh(initialDerivative * offset.y / maxOffset)

        val maxDragScale = 4f.dp.toPx() / size.height
        val offsetAngle = atan2(offset.y, offset.x)
        scaleX = scale +
          maxDragScale * abs(cos(offsetAngle) * offset.x / size.maxDimension) *
          (width / height).fastCoerceAtMost(1f)
        scaleY = scale +
          maxDragScale * abs(sin(offsetAngle) * offset.y / size.maxDimension) *
          (height / width).fastCoerceAtMost(1f)
      }
    } else {
      null
    },
    onDrawSurface = {
      if (tint.isSpecified) {
        drawRect(tint, blendMode = BlendMode.Hue)
        drawRect(tint.copy(alpha = 0.75f))
      }
      if (surfaceColor.isSpecified) {
        drawRect(surfaceColor)
      }
    },
  )

  Row(
    modifier = liquidModifier
      .clickable(
        interactionSource = null,
        indication = if (props.interactive) null else LocalIndication.current,
        enabled = props.enabled,
        role = Role.Button,
        onClick = onPress,
      )
      .then(
        if (props.interactive && props.enabled) {
          Modifier
            .then(interactiveHighlight.modifier)
            .then(interactiveHighlight.gestureModifier)
        } else {
          Modifier
        },
      )
      .height(48f.dp)
      .padding(horizontal = props.contentPaddingHorizontal.dp),
    horizontalArrangement = Arrangement.spacedBy(
      props.contentSpacing.dp,
      Alignment.CenterHorizontally,
    ),
    verticalAlignment = Alignment.CenterVertically,
  ) {
    Children(UIComposableScope(rowScope = this))
  }
}
