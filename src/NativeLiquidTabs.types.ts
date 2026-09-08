import type { StyleProp, ViewStyle } from 'react-native';

export type OnRoutePressEventPayload = {
  route: string;
};

export type NativeLiquidPrimitiveProps = {
  modifiers?: Record<string, unknown>[];
};

export type NativeLiquidButtonProps = NativeLiquidPrimitiveProps & {
  backdropEnabled?: boolean;
  interactive?: boolean;
  enabled?: boolean;
  tint?: string;
  surfaceColor?: string;
  contentPaddingHorizontal?: number;
  contentSpacing?: number;
  /** Enables the resting border highlight. Defaults to true. */
  highlightEnabled?: boolean;
  /** Border highlight opacity from 0 to 1. Defaults to 1. */
  highlightAlpha?: number;
  /** Border highlight width in density-independent pixels. Defaults to 0.5. */
  highlightWidth?: number;
  /** Border highlight blur radius in density-independent pixels. Defaults to half the width. */
  highlightBlurRadius?: number;
  /** Press expansion amount in density-independent pixels. Defaults to 8. */
  pressScaleAmount?: number;
  /** Directional drag stretch amount in density-independent pixels. Defaults to 4. */
  dragStretchAmount?: number;
  onPress?: (event: { nativeEvent: Record<string, never> }) => void;
};

export type LiquidMenuItem = {
  id: string;
  label: string;
  enabled?: boolean;
  destructive?: boolean;
};

export type NativeLiquidMenuProps = NativeLiquidPrimitiveProps & {
  items: LiquidMenuItem[];
  selectedId?: string;
  enabled?: boolean;
  surfaceColor?: string;
  tint?: string;
  contentColor?: string;
  topInset?: number;
  endInset?: number;
  triggerSize?: number;
  menuWidth?: number;
  itemHeight?: number;
  /** Backdrop blur radius in density-independent pixels. Defaults to 8. */
  blurRadius?: number;
  /** Enables the glass border highlight. Defaults to true. */
  highlightEnabled?: boolean;
  /** Border highlight opacity from 0 to 1. Defaults to 0.7. */
  highlightAlpha?: number;
  /** Border highlight width in density-independent pixels. Defaults to 0.5. */
  highlightWidth?: number;
  /** Border highlight blur radius in density-independent pixels. */
  highlightBlurRadius?: number;
  /** Collapsed-trigger press expansion in density-independent pixels. */
  pressScaleAmount?: number;
  /** Collapsed-trigger directional stretch in density-independent pixels. */
  dragStretchAmount?: number;
  onExpandedChange?: (event: { nativeEvent: { expanded: boolean } }) => void;
  onItemPress?: (event: { nativeEvent: { id: string } }) => void;
};

export type NativeLiquidTabBarProps = {
  activeRoute?: string;
  activeTintColor?: string;
  tintColor?: string;
  containerColor?: string;
  resetKey?: number;
  onRoutePress?: (event: { nativeEvent: OnRoutePressEventPayload }) => void;
  style?: StyleProp<ViewStyle>;
};

export type NativeLiquidFabMenuViewProps = {
  expanded?: boolean;
  backgroundColor?: string;
  tintColor?: string;
  activeTintColor?: string;
  onExpandedChange?: (event: { nativeEvent: { expanded: boolean } }) => void;
  onActionPress?: (event: { nativeEvent: { action: string } }) => void;
  style?: StyleProp<ViewStyle>;
};
