import { requireNativeView } from 'expo';
import * as React from 'react';

import type { NativeLiquidFabMenuViewProps } from './NativeLiquidTabs.types';

const NativeView = requireNativeView<NativeLiquidFabMenuViewProps>(
  'NativeLiquidTabs',
  'NativeLiquidFabMenuView'
);

export default function NativeLiquidFabMenuView(props: NativeLiquidFabMenuViewProps) {
  return React.createElement(NativeView, props);
}
