package expo.modules.nativeliquidtabs

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import expo.modules.nativeliquidtabs.kyant.components.LiquidBottomTab

@Composable
internal fun RowScope.NativeLiquidBottomTabContent(
  item: NativeLiquidTabItem,
  tintColor: Color?,
  onPress: () -> Unit,
) {
  val isLightTheme = !isSystemInDarkTheme()
  val color = tintColor ?: if (isLightTheme) Color.Black else Color.White

  LiquidBottomTab(onClick = onPress) {
    NativeLiquidTabIcon(item.icon, color)
    BasicText(
      item.label,
      style = TextStyle(
        color = color,
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium,
        fontFamily = FontFamily.SansSerif,
        textAlign = TextAlign.Center,
      ),
    )
  }
}

@Composable
private fun ColumnScope.NativeLiquidTabIcon(icon: NativeLiquidIcon, color: Color) {
  Image(
    painter = painterResource(icon.drawableRes),
    contentDescription = null,
    colorFilter = ColorFilter.tint(color),
    modifier = Modifier
      .size(if (icon == NativeLiquidIcon.More) 30.dp else 28.dp),
  )
}

internal fun resolveNativeLiquidTabIndex(route: String): Int {
  val index = NativeLiquidTabs.indexOfFirst { it.route == route }
  return if (index >= 0) index else 0
}

internal data class NativeLiquidTabItem(
  val route: String,
  val label: String,
  val icon: NativeLiquidIcon,
)

internal enum class NativeLiquidIcon(val drawableRes: Int) {
  Chart(R.drawable.chart_bar_xaxis),
  Layers(R.drawable.square_2_layers_3d_top_filled),
  Cart(R.drawable.cart_fill),
  More(R.drawable.ellipsis),
}

internal val NativeLiquidTabs = listOf(
  NativeLiquidTabItem("reports", "Reports", NativeLiquidIcon.Chart),
  NativeLiquidTabItem("products", "Products", NativeLiquidIcon.Layers),
  NativeLiquidTabItem("orders", "Orders", NativeLiquidIcon.Cart),
  NativeLiquidTabItem("more", "More", NativeLiquidIcon.More),
)
