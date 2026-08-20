package expo.modules.nativeliquidtabs

import expo.modules.kotlin.modules.Module
import expo.modules.kotlin.modules.ModuleDefinition
import expo.modules.ui.ExpoUIView

class NativeLiquidTabsModule : Module() {
  override fun definition() = ModuleDefinition {
    Name("NativeLiquidTabs")

    ExpoUIView<NativeLiquidSceneProps>("NativeLiquidScene") {
      Content { props ->
        NativeLiquidSceneContent(props)
      }
    }

    ExpoUIView<LiquidBackdropTargetProps>("LiquidBackdropTarget") {
      Content { props ->
        LiquidBackdropTargetContent(props)
      }
    }

    ExpoUIView<LiquidTabsProps>("LiquidTabs") {
      val onRoutePress by Event<NativeLiquidRouteEvent>()

      Content { props ->
        LiquidTabsContent(props) { onRoutePress(it) }
      }
    }

    ExpoUIView<LiquidButtonProps>("LiquidButton") {
      val onPress by Event<Unit>()

      Content { props ->
        LiquidButtonContent(props) { onPress(Unit) }
      }
    }

    View(NativeLiquidMoreMenuView::class) {
      Events("onDismiss", "onRoutePress")

      Prop("open") { view: NativeLiquidMoreMenuView, open: Boolean? ->
        view.setOpen(open)
      }

      Prop("storeName") { view: NativeLiquidMoreMenuView, storeName: String? ->
        view.setStoreName(storeName)
      }

      Prop("activeRoute") { view: NativeLiquidMoreMenuView, activeRoute: String? ->
        view.setActiveRoute(activeRoute)
      }
    }

    View(NativeLiquidFabMenuView::class) {
      Events("onActionPress", "onExpandedChange")

      Prop("expanded") { view: NativeLiquidFabMenuView, expanded: Boolean? ->
        view.setExpanded(expanded)
      }

      Prop("backgroundColor") { view: NativeLiquidFabMenuView, color: String? ->
        view.setFabBackgroundColor(color)
      }

      Prop("tintColor") { view: NativeLiquidFabMenuView, color: String? ->
        view.setTintColor(color)
      }

      Prop("activeTintColor") { view: NativeLiquidFabMenuView, color: String? ->
        view.setActiveTintColor(color)
      }
    }
  }
}
