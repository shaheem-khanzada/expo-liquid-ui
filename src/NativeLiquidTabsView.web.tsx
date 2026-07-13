import { NativeLiquidTabsViewProps } from './NativeLiquidTabs.types';

// NativeLiquidTabsView is not available on the web platform.
export default function NativeLiquidTabsView(_props: NativeLiquidTabsViewProps) {
  throw new Error('NativeLiquidTabsView is not available on the web platform.');
}
