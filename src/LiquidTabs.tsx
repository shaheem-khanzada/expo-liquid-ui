import { requireNativeView } from 'expo';
import type { ComponentType } from 'react';

import type {
  NativeLiquidPrimitiveProps,
  NativeLiquidTabBarProps,
} from './NativeLiquidTabs.types';

export type LiquidTabsProps = NativeLiquidPrimitiveProps &
  Pick<
    NativeLiquidTabBarProps,
    | 'activeRoute'
    | 'activeTintColor'
    | 'tintColor'
    | 'containerColor'
    | 'resetKey'
    | 'onRoutePress'
  >;

const LiquidTabs: ComponentType<LiquidTabsProps> = requireNativeView(
  'NativeLiquidTabs',
  'LiquidTabs'
);

export default LiquidTabs;
