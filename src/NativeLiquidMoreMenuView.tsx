import { requireNativeView } from 'expo';
import * as React from 'react';

import type { NativeLiquidMoreMenuViewProps } from './NativeLiquidTabs.types';

const NativeView = requireNativeView<NativeLiquidMoreMenuViewProps>(
  'NativeLiquidTabs',
  'NativeLiquidMoreMenuView'
);

export default function NativeLiquidMoreMenuView(props: NativeLiquidMoreMenuViewProps) {
  return React.createElement(NativeView, props);
}
