import type { ReactNode } from 'react';
import type { StyleProp, ViewStyle } from 'react-native';

export type OnRoutePressEventPayload = {
  route: string;
};

export type NativeLiquidPrimitiveProps = {
  modifiers?: Record<string, unknown>[];
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

export type NativeLiquidBackdropSceneViewProps = NativeLiquidTabBarProps & {
  children?: ReactNode;
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
  backgroundColor?: string;
  tintColor?: string;
  activeTintColor?: string;
  onActionPress?: (event: { nativeEvent: { action: string } }) => void;
  style?: StyleProp<ViewStyle>;
};
