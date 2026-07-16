import { requireNativeView } from 'expo';
import * as React from 'react';

import type { NativeLiquidBackdropSceneViewProps } from './NativeLiquidTabs.types';

const NativeView: React.ComponentType<NativeLiquidBackdropSceneViewProps> =
  requireNativeView('NativeLiquidTabs', 'NativeLiquidBackdropSceneView');

export default function NativeLiquidBackdropSceneView(
  props: NativeLiquidBackdropSceneViewProps
) {
  return React.createElement(NativeView, props);
}
