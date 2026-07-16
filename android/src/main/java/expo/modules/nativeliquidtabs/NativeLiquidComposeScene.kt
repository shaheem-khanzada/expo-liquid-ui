package expo.modules.nativeliquidtabs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kyant.backdrop.backdrops.LayerBackdrop
import com.kyant.backdrop.backdrops.layerBackdrop
import com.kyant.backdrop.backdrops.rememberLayerBackdrop
import expo.modules.kotlin.records.Field
import expo.modules.kotlin.records.Record
import expo.modules.kotlin.views.ComposeProps
import expo.modules.kotlin.views.FunctionalComposableScope
import expo.modules.kotlin.views.OptimizedComposeProps
import expo.modules.nativeliquidtabs.kyant.components.LiquidBottomTabs
import expo.modules.ui.ModifierList
import expo.modules.ui.ModifierRegistry
import expo.modules.ui.UIComposableScope

private val LocalNativeLiquidBackdrop = staticCompositionLocalOf<LayerBackdrop?> { null }

@OptimizedComposeProps
data class NativeLiquidSceneProps(
  val modifiers: ModifierList = emptyList(),
) : ComposeProps

@OptimizedComposeProps
data class LiquidBackdropTargetProps(
  val modifiers: ModifierList = emptyList(),
) : ComposeProps

@OptimizedComposeProps
data class LiquidTabsProps(
  val activeRoute: String = "reports",
  val activeTintColor: String? = null,
  val tintColor: String? = null,
  val containerColor: String? = null,
  val modifiers: ModifierList = emptyList(),
) : ComposeProps

data class NativeLiquidRouteEvent(
  @Field val route: String,
) : Record

@Composable
fun FunctionalComposableScope.NativeLiquidSceneContent(props: NativeLiquidSceneProps) {
  val backdrop = rememberLayerBackdrop()

  Box(
    modifier = ModifierRegistry
      .applyModifiers(props.modifiers, appContext, composableScope, globalEventDispatcher)
  ) {
    CompositionLocalProvider(LocalNativeLiquidBackdrop provides backdrop) {
      Children(UIComposableScope(boxScope = this@Box))
    }
  }
}

@Composable
fun FunctionalComposableScope.LiquidBackdropTargetContent(props: LiquidBackdropTargetProps) {
  val backdrop = LocalNativeLiquidBackdrop.current

  Box(
    modifier = ModifierRegistry
      .applyModifiers(props.modifiers, appContext, composableScope, globalEventDispatcher)
      .then(if (backdrop != null) Modifier.layerBackdrop(backdrop) else Modifier)
  ) {
    Children(UIComposableScope(boxScope = this@Box))
  }
}

@Composable
fun FunctionalComposableScope.LiquidTabsContent(
  props: LiquidTabsProps,
  onRoutePress: (NativeLiquidRouteEvent) -> Unit,
) {
  val fallbackBackdrop = rememberLayerBackdrop()
  val backdrop = LocalNativeLiquidBackdrop.current ?: fallbackBackdrop
  val selectedIndex = resolveNativeLiquidTabIndex(props.activeRoute)
  val activeTintColor = parseNativeLiquidColor(props.activeTintColor)
  val tintColor = parseNativeLiquidColor(props.tintColor)
  val containerColor = parseNativeLiquidColor(props.containerColor)

  Box(
    modifier = ModifierRegistry
      .applyModifiers(props.modifiers, appContext, composableScope, globalEventDispatcher)
      .fillMaxSize(),
    contentAlignment = Alignment.BottomCenter,
  ) {
    LiquidBottomTabs(
      selectedTabIndex = { selectedIndex },
      onTabSelected = { index ->
        NativeLiquidTabs.getOrNull(index)?.let { item ->
          onRoutePress(NativeLiquidRouteEvent(item.route))
        }
      },
      backdrop = backdrop,
      tabsCount = NativeLiquidTabs.size,
      accentColor = activeTintColor,
      containerColor = containerColor,
      modifier = Modifier
        .padding(horizontal = 34.dp)
        .height(64.dp)
        .fillMaxWidth(),
    ) { onTabPress ->
      NativeLiquidTabs.forEachIndexed { index, item ->
        NativeLiquidBottomTabContent(
          item = item,
          tintColor = tintColor,
          onPress = {
            onTabPress(index)
          },
        )
      }
    }
  }
}

private fun parseNativeLiquidColor(value: String?): Color? {
  if (value.isNullOrBlank()) {
    return null
  }

  return runCatching {
    Color(android.graphics.Color.parseColor(value))
  }.getOrNull()
}
