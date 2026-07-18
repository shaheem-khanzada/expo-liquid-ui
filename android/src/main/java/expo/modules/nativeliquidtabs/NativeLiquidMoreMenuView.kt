package expo.modules.nativeliquidtabs

import android.content.Context
import android.graphics.Color as AndroidColor
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import expo.modules.kotlin.AppContext
import expo.modules.kotlin.viewevent.EventDispatcher
import expo.modules.kotlin.views.ExpoView

class NativeLiquidMoreMenuView(context: Context, appContext: AppContext) : ExpoView(context, appContext) {
  private val onDismiss by EventDispatcher<Map<String, Any>>()
  private val onRoutePress by EventDispatcher<Map<String, Any>>()

  private val openState = mutableStateOf(false)
  private val storeNameState = mutableStateOf("Apple Inc")
  private val activeRouteState = mutableStateOf("reports")

  private val composeView = ComposeView(context).apply {
    setBackgroundColor(android.graphics.Color.TRANSPARENT)
    setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnDetachedFromWindow)
    setContent {
      NativeLiquidMoreMenuContent(
        open = openState.value,
        storeName = storeNameState.value,
        activeRoute = activeRouteState.value,
        onDismiss = { onDismiss(emptyMap()) },
        onRoutePress = { route -> onRoutePress(mapOf("route" to route)) },
      )
    }
  }

  init {
    setBackgroundColor(AndroidColor.TRANSPARENT)
    clipChildren = false
    clipToPadding = false
    addView(composeView, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))
  }

  fun setOpen(open: Boolean?) {
    openState.value = open == true
  }

  fun setStoreName(storeName: String?) {
    storeNameState.value = storeName?.takeIf { it.isNotBlank() } ?: "Apple Inc"
  }

  fun setActiveRoute(route: String?) {
    activeRouteState.value = route ?: "reports"
  }

  override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
    super.onLayout(changed, left, top, right, bottom)
    composeView.layout(0, 0, right - left, bottom - top)
  }
}

@Composable
private fun NativeLiquidMoreMenuContent(
  open: Boolean,
  storeName: String,
  activeRoute: String,
  onDismiss: () -> Unit,
  onRoutePress: (String) -> Unit,
) {
  val progress by animateFloatAsState(
    targetValue = if (open) 1f else 0f,
    animationSpec = tween(
      durationMillis = if (open) 260 else 150,
      easing = FastOutSlowInEasing,
    ),
    label = "more-menu-progress",
  )

  if (!open && progress <= 0.001f) {
    return
  }

  val isLightTheme = !isSystemInDarkTheme()
  val scrimColor = if (isLightTheme) Color.Black else Color.Black
  val sheetColor = if (isLightTheme) Color(0xFFF7F7F8) else Color(0xFF1B1B1C)
  val textColor = if (isLightTheme) Color(0xFF111827) else Color.White
  val mutedColor = if (isLightTheme) Color(0xFF445064) else Color(0xFFE5E7EB)
  val dividerColor = if (isLightTheme) Color(0xFFE2E4E8) else Color(0xFF34363A)
  val selectedColor = 
  if (isLightTheme) {
    Color(0xFFDE9F1F)
  } else {
    Color(0xFFF8D86F)
  }
  val selectedBackgroundColor =
    if (isLightTheme) {
        Color.Black.copy(alpha = 0.10f)
    } else {
        Color.White.copy(alpha = 0.10f)
    }
  val density = LocalDensity.current
  val bottomInset = with(density) {
    WindowInsets.navigationBars.getBottom(this).toDp()
  }

  val scrimAlpha = 0.16f * progress
  val sheetHeight = 400.dp * progress
  val sheetBottomOffset =
    bottomInset + NativeLiquidTabBarBottomPadding + NativeLiquidTabBarHeight + NativeLiquidMenuTabBarGap
  val scaleX = squeezeScale(progress, open)
  val cornerRadius = 28.dp + (6.dp * progress)

  Box(modifier = Modifier.fillMaxSize()) {
    Box(
      modifier = Modifier
        .fillMaxSize()
        .background(scrimColor.copy(alpha = scrimAlpha))
        .clickable(
          interactionSource = remember { MutableInteractionSource() },
          indication = null,
          onClick = onDismiss,
        ),
    )

    Column(
      modifier = Modifier
        .align(Alignment.BottomCenter)
        .padding(start = 40.dp, end = 40.dp, bottom = sheetBottomOffset)
        .height(sheetHeight)
        .fillMaxWidth()
        .graphicsLayer {
          transformOrigin = androidx.compose.ui.graphics.TransformOrigin(0.5f, 1f)
          this.scaleX = scaleX
          alpha = progress
        }
        .clip(RoundedCornerShape(cornerRadius))
        .background(sheetColor)
        .padding(start = 22.dp, top = 16.dp, end = 22.dp, bottom = 30.dp),
    ) {
      MoreMenuHeader(
        storeName = storeName,
        color = textColor,
        iconColor = mutedColor,
        progress = delayedProgress(progress, 0.34f, 0.68f),
        onSettingsPress = { onRoutePress("settings") },
      )

      Spacer(modifier = Modifier.height(8.dp))

      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(1.dp)
          .graphicsLayer { alpha = delayedProgress(progress, 0.44f, 0.72f) }
          .background(dividerColor),
      )

      Spacer(modifier = Modifier.height(16.dp))

      Column(
        verticalArrangement = Arrangement.spacedBy(9.dp),
      ) {
        MoreMenuRoutes.forEachIndexed { index, item ->
          val isSelected = activeRoute == item.route
          MoreMenuRow(
            item = item,
            color = if (isSelected) selectedColor else mutedColor,
            backgroundColor = if (isSelected) selectedBackgroundColor else Color.Transparent,
            progress = delayedProgress(progress, 0.52f + (index * 0.055f), 0.88f + (index * 0.035f)),
            onPress = { onRoutePress(item.route) },
          )
        }
      }
    }
  }
}

@Composable
private fun MoreMenuHeader(
  storeName: String,
  color: Color,
  iconColor: Color,
  progress: Float,
  onSettingsPress: () -> Unit,
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .graphicsLayer {
        alpha = progress
        translationY = (1f - progress) * 10f
      },
    verticalAlignment = Alignment.CenterVertically,
  ) {
    BasicText(
      storeName,
      style = TextStyle(
        color = color,
        fontSize = 20.sp,
        fontWeight = FontWeight.SemiBold,
        fontFamily = FontFamily.SansSerif,
      ),
      modifier = Modifier.weight(1f),
    )

    Image(
      painter = painterResource(R.drawable.slider),
      contentDescription = null,
      colorFilter = ColorFilter.tint(iconColor),
      modifier = Modifier
        .size(30.dp)
        .clip(RoundedCornerShape(18.dp))
        .clickable(
          interactionSource = remember { MutableInteractionSource() },
          indication = null,
          onClick = onSettingsPress,
        )
        .padding(8.dp),
    )
  }
}

@Composable
private fun MoreMenuRow(
  item: MoreMenuItem,
  color: Color,
  backgroundColor: Color,
  progress: Float,
  onPress: () -> Unit,
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .height(42.dp)
      .graphicsLayer {
        alpha = progress
        translationY = (1f - progress) * 16f
      }
      .clip(RoundedCornerShape(18.dp))
      .background(backgroundColor)
      .clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = null,
        onClick = onPress,
      )
      .padding(horizontal = 12.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.Start,
  ) {
    Image(
      painter = painterResource(item.iconRes),
      contentDescription = null,
      colorFilter = ColorFilter.tint(color),
      modifier = Modifier.size(22.dp),
    )

    Spacer(modifier = Modifier.width(20.dp))

    BasicText(
      item.label,
      style = TextStyle(
        color = color,
        fontSize = 19.sp,
        fontWeight = FontWeight.Medium,
        fontFamily = FontFamily.SansSerif,
      ),
    )
  }
}

private fun delayedProgress(progress: Float, start: Float, end: Float): Float {
  return ((progress - start) / (end - start)).coerceIn(0f, 1f)
}

private fun squeezeScale(progress: Float, open: Boolean): Float {
  if (!open) return 1f
  return when {
    progress < 0.16f -> lerp(1f, 0.92f, progress / 0.16f)
    progress < 0.36f -> lerp(0.92f, 0.97f, (progress - 0.16f) / 0.20f)
    progress < 0.62f -> lerp(0.97f, 1f, (progress - 0.36f) / 0.26f)
    else -> 1f
  }
}

private fun lerp(start: Float, stop: Float, fraction: Float): Float {
  return start + ((stop - start) * fraction.coerceIn(0f, 1f))
}

private data class MoreMenuItem(
  val route: String,
  val label: String,
  val iconRes: Int,
)

private val MoreMenuRoutes = listOf(
  MoreMenuItem("reports", "Reports", R.drawable.chart_bar_xaxis),
  MoreMenuItem("products", "Products", R.drawable.square_2_layers_3d_top_filled),
  MoreMenuItem("inventory", "Inventory", R.drawable.shippingbox_fill),
  MoreMenuItem("orders", "Orders", R.drawable.cart_fill),
  MoreMenuItem("expenses", "Expenses", R.drawable.dollarsign_arrow_circlepath),
  MoreMenuItem("settings", "Settings", R.drawable.setting),
)
