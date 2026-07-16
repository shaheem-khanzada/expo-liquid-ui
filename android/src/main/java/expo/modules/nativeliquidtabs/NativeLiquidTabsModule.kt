package expo.modules.nativeliquidtabs

import android.view.View
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

    View(NativeLiquidBackdropSceneView::class) {
      Events("onRoutePress")

      Prop("activeRoute") { view: NativeLiquidBackdropSceneView, activeRoute: String? ->
        view.setActiveRoute(activeRoute)
      }

      Prop("activeTintColor") { view: NativeLiquidBackdropSceneView, color: String? ->
        view.setActiveTintColor(color)
      }

      Prop("tintColor") { view: NativeLiquidBackdropSceneView, color: String? ->
        view.setTintColor(color)
      }

      Prop("containerColor") { view: NativeLiquidBackdropSceneView, color: String? ->
        view.setContainerColor(color)
      }

      GroupView<NativeLiquidBackdropSceneView> {
        AddChildView<View> { parent, child, index ->
          parent.addContentView(child, index)
        }

        GetChildCount { parent ->
          parent.contentChildCount
        }

        GetChildViewAt<View> { parent, index ->
          parent.getContentChildAt(index)
        }

        RemoveChildView<View> { parent, child ->
          parent.removeContentView(child)
        }

        RemoveChildViewAt { parent, index ->
          parent.removeContentViewAt(index)
        }
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
      Events("onActionPress")

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
