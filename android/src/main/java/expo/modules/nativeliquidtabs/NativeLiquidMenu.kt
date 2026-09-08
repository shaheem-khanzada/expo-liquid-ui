package expo.modules.nativeliquidtabs

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastCoerceAtMost
import androidx.compose.ui.util.lerp
import com.kyant.backdrop.backdrops.emptyBackdrop
import com.kyant.backdrop.drawBackdrop
import com.kyant.backdrop.effects.blur
import com.kyant.backdrop.effects.lens
import com.kyant.backdrop.effects.vibrancy
import com.kyant.backdrop.highlight.Highlight
import com.kyant.shapes.RoundedRectangle
import expo.modules.kotlin.records.Field
import expo.modules.kotlin.records.Record
import expo.modules.kotlin.views.ComposeProps
import expo.modules.kotlin.views.FunctionalComposableScope
import expo.modules.kotlin.views.OptimizedComposeProps
import expo.modules.ui.ModifierList
import expo.modules.ui.ModifierRegistry
import expo.modules.ui.UIComposableScope
import expo.modules.nativeliquidtabs.kyant.utils.InteractiveHighlight
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.tanh

data class LiquidMenuItemRecord(
  @Field val id: String = "",
  @Field val label: String = "",
  @Field val enabled: Boolean = true,
  @Field val destructive: Boolean = false,
) : Record

data class LiquidMenuExpandedEvent(
  @Field val expanded: Boolean,
) : Record

data class LiquidMenuItemPressEvent(
  @Field val id: String,
) : Record

@OptimizedComposeProps
data class LiquidMenuProps(
  val items: List<LiquidMenuItemRecord> = emptyList(),
  val selectedId: String? = null,
  val enabled: Boolean = true,
  val tint: String? = null,
  val surfaceColor: String? = null,
  val contentColor: String? = null,
  val topInset: Float = 16f,
  val endInset: Float = 16f,
  val triggerSize: Float = 48f,
  val menuWidth: Float = 240f,
  val itemHeight: Float = 52f,
  val blurRadius: Float = 8f,
  val highlightEnabled: Boolean = true,
  val highlightAlpha: Float = 0.7f,
  val highlightWidth: Float = 0.5f,
  val highlightBlurRadius: Float? = null,
  val pressScaleAmount: Float = 8f,
  val dragStretchAmount: Float = 4f,
  val modifiers: ModifierList = emptyList(),
) : ComposeProps

@Composable
fun FunctionalComposableScope.LiquidMenuContent(
  props: LiquidMenuProps,
  onExpandedChange: (LiquidMenuExpandedEvent) -> Unit,
  onItemPress: (LiquidMenuItemPressEvent) -> Unit,
) {
  var expanded by remember { mutableStateOf(false) }
  val backdrop = LocalNativeLiquidBackdrop.current ?: emptyBackdrop()
  val triggerSize = props.triggerSize.coerceAtLeast(40f)
  val itemHeight = props.itemHeight.coerceAtLeast(40f)
  val menuWidth = props.menuWidth.coerceAtLeast(triggerSize)
  val menuHeight = (props.items.size * itemHeight + 16f).coerceAtLeast(triggerSize)
  val transition = spring<Dp>(dampingRatio = 0.72f, stiffness = 420f)
  val animatedWidth by animateDpAsState(
    targetValue = (if (expanded) menuWidth else triggerSize).dp,
    animationSpec = transition,
    label = "LiquidMenuWidth",
  )
  val animatedHeight by animateDpAsState(
    targetValue = (if (expanded) menuHeight else triggerSize).dp,
    animationSpec = transition,
    label = "LiquidMenuHeight",
  )
  val animatedCornerRadius by animateDpAsState(
    targetValue = (if (expanded) 28f else triggerSize / 2f).dp,
    animationSpec = transition,
    label = "LiquidMenuCornerRadius",
  )
  val tint = parseNativeLiquidColor(props.tint) ?: Color.Unspecified
  val surfaceColor = parseNativeLiquidColor(props.surfaceColor) ?: Color(0x1AFFFFFF)
  val contentColor = parseNativeLiquidColor(props.contentColor) ?: Color.White
  val animationScope = rememberCoroutineScope()
  val interactiveHighlight = remember(animationScope) {
    InteractiveHighlight(animationScope = animationScope)
  }
  val highlightWidth = props.highlightWidth.coerceAtLeast(0f)
  val highlightBlurRadius =
    (props.highlightBlurRadius ?: highlightWidth / 2f).coerceAtLeast(0f)

  fun setExpanded(value: Boolean) {
    if (expanded == value) return
    expanded = value
    onExpandedChange(LiquidMenuExpandedEvent(value))
  }

  BackHandler(enabled = expanded) { setExpanded(false) }

  Box(
    modifier = ModifierRegistry
      .applyModifiers(props.modifiers, appContext, composableScope, globalEventDispatcher)
      .fillMaxSize(),
  ) {
    if (expanded) {
      Box(
        modifier = Modifier
          .fillMaxSize()
          .clickable(
            interactionSource = null,
            indication = null,
            role = null,
            onClick = { setExpanded(false) },
          ),
      )
    }

    Box(
      modifier = Modifier
        .align(Alignment.TopEnd)
        .padding(top = props.topInset.dp, end = props.endInset.dp),
    ) {
      Box(
        modifier = Modifier
          .width(animatedWidth)
          .height(animatedHeight)
          .drawBackdrop(
            backdrop = backdrop,
            shape = { RoundedRectangle(animatedCornerRadius) },
            effects = {
              vibrancy()
              blur(props.blurRadius.coerceAtLeast(0f).dp.toPx())
              lens(16f.dp.toPx(), 32f.dp.toPx())
            },
            highlight = if (props.highlightEnabled) {
              {
                Highlight(
                  width = highlightWidth.dp,
                  blurRadius = highlightBlurRadius.dp,
                  alpha = props.highlightAlpha.coerceIn(0f, 1f),
                )
              }
            } else {
              null
            },
            layerBlock = if (!expanded && props.enabled) {
              {
                val width = size.width
                val height = size.height
                val progress = interactiveHighlight.pressProgress
                val pressScaleAmount =
                  props.pressScaleAmount.coerceAtLeast(0f).dp.toPx()
                val scale = lerp(1f, 1f + pressScaleAmount / height, progress)
                val maxOffset = size.minDimension
                val offset = interactiveHighlight.offset

                translationX = maxOffset * tanh(0.05f * offset.x / maxOffset)
                translationY = maxOffset * tanh(0.05f * offset.y / maxOffset)

                val maxDragScale =
                  props.dragStretchAmount.coerceAtLeast(0f).dp.toPx() / height
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
                drawRect(tint.copy(alpha = 0.6f))
              }
              drawRect(surfaceColor)
            },
          )
          .clickable(
            interactionSource = null,
            indication = LocalIndication.current,
            enabled = props.enabled,
            role = Role.Button,
            onClick = { if (!expanded) setExpanded(true) },
          )
          .then(
            if (!expanded && props.enabled) {
              Modifier
                .then(interactiveHighlight.modifier)
                .then(interactiveHighlight.gestureModifier)
            } else {
              Modifier
            },
          ),
        contentAlignment = Alignment.Center,
      ) {
        if (!expanded) {
          Children(UIComposableScope(boxScope = this))
        }

        AnimatedVisibility(
          visible = expanded,
          enter = fadeIn(),
          exit = fadeOut(),
        ) {
          Column(
            modifier = Modifier
              .fillMaxSize()
              .padding(vertical = 8f.dp),
            verticalArrangement = Arrangement.Center,
          ) {
            props.items.forEach { item ->
              val selected = item.id == props.selectedId
              val itemColor = when {
                !item.enabled -> contentColor.copy(alpha = 0.38f)
                item.destructive -> Color(0xFFFF453A)
                else -> contentColor
              }

              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .height(itemHeight.dp)
                  .clickable(
                    interactionSource = null,
                    indication = LocalIndication.current,
                    enabled = item.enabled,
                    role = Role.Button,
                    onClick = {
                      onItemPress(LiquidMenuItemPressEvent(item.id))
                      setExpanded(false)
                    },
                  )
                  .padding(horizontal = 20f.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12f.dp),
              ) {
                BasicText(
                  text = if (selected) "✓" else "",
                  modifier = Modifier.width(20f.dp),
                  style = TextStyle(
                    color = itemColor,
                    fontSize = 20f.sp,
                    fontWeight = FontWeight.Medium,
                  ),
                )
                BasicText(
                  text = item.label,
                  style = TextStyle(
                    color = itemColor,
                    fontSize = 18f.sp,
                    fontWeight = if (selected) FontWeight.Medium else FontWeight.Normal,
                  ),
                )
              }
            }
          }
        }
      }
    }
  }
}
