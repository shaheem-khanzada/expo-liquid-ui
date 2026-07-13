package expo.modules.nativeliquidtabs

import expo.modules.kotlin.modules.Module
import expo.modules.kotlin.modules.ModuleDefinition

class NativeLiquidTabsModule : Module() {
  override fun definition() = ModuleDefinition {
    Name("NativeLiquidTabs")

    View(NativeLiquidTabsView::class) {
      // Defines an event that the view can send to JavaScript.
      Events("onTap")
    }
  }
}
