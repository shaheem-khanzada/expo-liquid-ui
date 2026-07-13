import { NativeModule, requireNativeModule } from 'expo';

declare class NativeLiquidTabsModule extends NativeModule<{}> {}

export default requireNativeModule<NativeLiquidTabsModule>('NativeLiquidTabs');
