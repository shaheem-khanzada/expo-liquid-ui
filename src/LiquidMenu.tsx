import { requireNativeView } from 'expo';
import type { ComponentType, PropsWithChildren } from 'react';

import type { NativeLiquidMenuProps } from './NativeLiquidTabs.types';

export type LiquidMenuProps = PropsWithChildren<NativeLiquidMenuProps>;

const LiquidMenu: ComponentType<LiquidMenuProps> = requireNativeView(
  'NativeLiquidTabs',
  'LiquidMenu'
);

export default LiquidMenu;
