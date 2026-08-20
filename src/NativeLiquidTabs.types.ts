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
  onPress?: (event: { nativeEvent: Record<string, never> }) => void;
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

export type NativeLiquidMoreMenuViewProps = {
  open?: boolean;
  storeName?: string;
  activeRoute?: string;
  onDismiss?: (event: { nativeEvent: Record<string, never> }) => void;
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
