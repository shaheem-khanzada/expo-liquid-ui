import { requireNativeView } from 'expo';
import type { ComponentType, ReactNode } from 'react';

import type { NativeLiquidPrimitiveProps } from './NativeLiquidTabs.types';

export type NativeLiquidSceneProps = NativeLiquidPrimitiveProps & {
  children?: ReactNode;
};

const NativeLiquidScene: ComponentType<NativeLiquidSceneProps> = requireNativeView(
  'NativeLiquidTabs',
  'NativeLiquidScene'
);

export default NativeLiquidScene;
