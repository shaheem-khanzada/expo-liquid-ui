import { registerWebModule, NativeModule } from 'expo';

// NativeLiquidTabsModule is not available on the web platform.
class NativeLiquidTabsModule extends NativeModule<{}> {}

export default registerWebModule(NativeLiquidTabsModule, 'NativeLiquidTabsModule');
