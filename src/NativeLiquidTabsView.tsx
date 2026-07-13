import { requireNativeView } from 'expo';
import * as React from 'react';

import { NativeLiquidTabsViewProps } from './NativeLiquidTabs.types';

const NativeView: React.ComponentType<NativeLiquidTabsViewProps> = requireNativeView('NativeLiquidTabs');

export default function NativeLiquidTabsView(props: NativeLiquidTabsViewProps) {
  return <NativeView {...props} />;
}
