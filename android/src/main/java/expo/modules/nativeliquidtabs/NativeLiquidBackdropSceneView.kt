package expo.modules.nativeliquidtabs

import android.content.Context
import android.graphics.Color as AndroidColor
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.kyant.backdrop.backdrops.layerBackdrop
import com.kyant.backdrop.backdrops.rememberLayerBackdrop
import expo.modules.kotlin.AppContext
import expo.modules.kotlin.viewevent.EventDispatcher
import expo.modules.kotlin.views.ExpoView
import expo.modules.nativeliquidtabs.kyant.components.LiquidBottomTabs

class NativeLiquidBackdropSceneView(context: Context, appContext: AppContext) : ExpoView(context, appContext) {
  private val onRoutePress by EventDispatcher<Map<String, Any>>()

  private val activeRouteState = mutableStateOf("reports")
  private val activeTintColorState = mutableStateOf<String?>(null)
  private val tintColorState = mutableStateOf<String?>(null)
  private val containerColorState = mutableStateOf<String?>(null)

  private val contentContainer = FrameLayout(context).apply {
    layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
    clipChildren = false
    clipToPadding = false
  }

  private val composeView = ComposeView(context).apply {
    setBackgroundColor(android.graphics.Color.TRANSPARENT)
    setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnDetachedFromWindow)
    setContent {
      NativeLiquidBackdropSceneContent(
        contentContainer = contentContainer,
        activeRoute = activeRouteState.value,
        activeTintColor = parseSceneColor(activeTintColorState.value),
        tintColor = parseSceneColor(tintColorState.value),
        containerColor = parseSceneColor(containerColorState.value),
        onRoutePress = { route ->
          activeRouteState.value = route
          onRoutePress(mapOf("route" to route))
        },
      )
    }
  }

  init {
    setBackgroundColor(android.graphics.Color.TRANSPARENT)
    clipChildren = false
    clipToPadding = false
    addView(composeView, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))
  }

  val contentChildCount: Int
    get() = contentContainer.childCount

  fun getContentChildAt(index: Int): View {
    return contentContainer.getChildAt(index)
  }

  fun addContentView(child: View, index: Int) {
    (child.parent as? ViewGroup)?.removeView(child)
    val safeIndex = index.coerceIn(0, contentContainer.childCount)
    contentContainer.addView(
      child,
      safeIndex,
      FrameLayout.LayoutParams(
        FrameLayout.LayoutParams.MATCH_PARENT,
        FrameLayout.LayoutParams.MATCH_PARENT,
      ),
    )
  }

  fun removeContentView(child: View) {
    contentContainer.removeView(child)
  }

  fun removeContentViewAt(index: Int) {
    contentContainer.removeViewAt(index)
  }

  fun setActiveRoute(route: String?) {
    activeRouteState.value = route ?: "reports"
  }

  fun setActiveTintColor(color: String?) {
    activeTintColorState.value = color
  }

  fun setTintColor(color: String?) {
    tintColorState.value = color
  }

  fun setContainerColor(color: String?) {
    containerColorState.value = color
  }

  override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
    super.onLayout(changed, left, top, right, bottom)
    composeView.layout(0, 0, right - left, bottom - top)
  }
}

@Composable
private fun NativeLiquidBackdropSceneContent(
  contentContainer: FrameLayout,
  activeRoute: String,
  activeTintColor: Color?,
  tintColor: Color?,
  containerColor: Color?,
  onRoutePress: (route: String) -> Unit,
) {
  val backdrop = rememberLayerBackdrop()
  val density = LocalDensity.current
  val bottomInset = with(density) {
    WindowInsets.navigationBars.getBottom(this).toDp()
  }

  Box(
    modifier = Modifier.fillMaxSize(),
    contentAlignment = Alignment.BottomCenter,
  ) {
    Box(
      modifier = Modifier
        .fillMaxSize()
        .layerBackdrop(backdrop),
    ) {
      AndroidView(
        factory = {
          (contentContainer.parent as? ViewGroup)?.removeView(contentContainer)
          contentContainer
        },
        modifier = Modifier
          .fillMaxSize()
          .graphicsLayer { alpha = 0.999f },
      )
    }

    LiquidBottomTabs(
      selectedTabIndex = { resolveNativeLiquidTabIndex(activeRoute) },
      onTabSelected = { index -> onRoutePress(NativeLiquidTabs[index].route) },
      backdrop = backdrop,
      tabsCount = NativeLiquidTabs.size,
      accentColor = activeTintColor,
      containerColor = containerColor,
      modifier = Modifier
        .padding(
          start = 36.dp,
          top = 18.dp,
          end = 36.dp,
          bottom = NativeLiquidTabBarBottomPadding + bottomInset,
        )
        .height(NativeLiquidTabBarHeight),
    ) { onTabPress ->
      NativeLiquidTabs.forEachIndexed { index, item ->
        NativeLiquidBottomTabContent(item = item, tintColor = tintColor) {
          onTabPress(index)
        }
      }
    }
  }
}

private fun parseSceneColor(color: String?): Color? {
  return color
    ?.takeIf { it.isNotBlank() }
    ?.let {
      runCatching { Color(AndroidColor.parseColor(it)) }.getOrNull()
    }
}
