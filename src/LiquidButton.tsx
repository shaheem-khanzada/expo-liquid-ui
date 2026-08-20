import { requireNativeView } from 'expo';
import type { ComponentType, PropsWithChildren } from 'react';

import type { NativeLiquidButtonProps } from './NativeLiquidTabs.types';

export type LiquidButtonProps = PropsWithChildren<NativeLiquidButtonProps>;

const LiquidButton: ComponentType<LiquidButtonProps> = requireNativeView(
  'NativeLiquidTabs',
  'LiquidButton'
);

export default LiquidButton;
