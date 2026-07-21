package expo.modules.nativeliquidtabs

import android.content.Context
import android.graphics.Color as AndroidColor
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingActionButtonMenu
import androidx.compose.material3.FloatingActionButtonMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.painterResource
import expo.modules.kotlin.AppContext
import expo.modules.kotlin.viewevent.EventDispatcher
import expo.modules.kotlin.views.ExpoView

class NativeLiquidFabMenuView(context: Context, appContext: AppContext) : ExpoView(context, appContext) {
  private val onActionPress by EventDispatcher<Map<String, Any>>()
  private val onExpandedChange by EventDispatcher<Map<String, Any>>()
  private val backgroundColorState = mutableStateOf<String?>(null)
  private val tintColorState = mutableStateOf<String?>(null)
  private val activeTintColorState = mutableStateOf<String?>(null)
  private val expandedState = mutableStateOf(false)

  private val composeView = ComposeView(context).apply {
    setBackgroundColor(AndroidColor.TRANSPARENT)
    setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnDetachedFromWindow)
    setContent {
      NativeLiquidFabMenuContent(
        backgroundColor = parseFabColor(backgroundColorState.value) ?: Color(0xFF111827),
        tintColor = parseFabColor(tintColorState.value) ?: Color.White,
        activeTintColor = parseFabColor(activeTintColorState.value) ?: Color(0xFF00C48C),
        expanded = expandedState.value,
        onExpandedChange = ::updateExpanded,
        onActionPress = { action -> onActionPress(mapOf("action" to action)) },
      )
    }
  }

  init {
    setBackgroundColor(AndroidColor.TRANSPARENT)
    clipChildren = false
    clipToPadding = false
    addView(composeView, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))
  }

  fun setExpanded(expanded: Boolean?) {
    expandedState.value = expanded == true
  }

  fun setFabBackgroundColor(color: String?) {
    backgroundColorState.value = color
  }

  fun setTintColor(color: String?) {
    tintColorState.value = color
  }

  fun setActiveTintColor(color: String?) {
    activeTintColorState.value = color
  }

  override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
    super.onLayout(changed, left, top, right, bottom)
    composeView.layout(0, 0, right - left, bottom - top)
  }

  private fun updateExpanded(expanded: Boolean) {
    if (expandedState.value == expanded) {
      return
    }

    expandedState.value = expanded
    onExpandedChange(mapOf("expanded" to expanded))
  }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun NativeLiquidFabMenuContent(
  backgroundColor: Color,
  tintColor: Color,
  activeTintColor: Color,
  expanded: Boolean,
  onExpandedChange: (Boolean) -> Unit,
  onActionPress: (String) -> Unit,
) {
  Box(
    modifier = Modifier.fillMaxSize(),
    contentAlignment = Alignment.BottomEnd,
  ) {
    FloatingActionButtonMenu(
      expanded = expanded,
      button = {
        ToggleFloatingActionButton(
          checked = expanded,
          onCheckedChange = onExpandedChange,
          containerColor = { if (expanded) activeTintColor else backgroundColor },
        ) {
          Image(
            painter = painterResource(R.drawable.ellipsis),
            contentDescription = null,
            colorFilter = ColorFilter.tint(tintColor),
          )
        }
      },
    ) {
      FloatingActionButtonMenuItem(
        onClick = {
          onExpandedChange(false)
          onActionPress("expenses")
        },
        containerColor = backgroundColor,
        contentColor = tintColor,
        icon = {
          Icon(
            painter = painterResource(R.drawable.dollarsign_arrow_circlepath),
            contentDescription = null,
          )
        },
        text = {
          Text("Expense")
        },
      )

      FloatingActionButtonMenuItem(
        onClick = {
          onExpandedChange(false)
          onActionPress("settings")
        },
        containerColor = backgroundColor,
        contentColor = tintColor,
        icon = {
          Icon(
            painter = painterResource(R.drawable.setting),
            contentDescription = null,
          )
        },
        text = {
          Text("Settings")
        },
      )
    }
  }
}

private fun parseFabColor(value: String?): Color? {
  if (value.isNullOrBlank()) {
    return null
  }

  return runCatching {
    Color(AndroidColor.parseColor(value))
  }.getOrNull()
}
