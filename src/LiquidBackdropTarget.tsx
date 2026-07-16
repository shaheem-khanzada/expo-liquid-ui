import { requireNativeView } from 'expo';
import type { ComponentType, ReactNode } from 'react';

import type { NativeLiquidPrimitiveProps } from './NativeLiquidTabs.types';

export type LiquidBackdropTargetProps = NativeLiquidPrimitiveProps & {
  children?: ReactNode;
};

const LiquidBackdropTarget: ComponentType<LiquidBackdropTargetProps> = requireNativeView(
  'NativeLiquidTabs',
  'LiquidBackdropTarget'
);

export default LiquidBackdropTarget;
