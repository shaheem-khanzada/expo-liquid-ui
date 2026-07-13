// Reexport the native module. On web, it will be resolved to NativeLiquidTabsModule.web.ts
// and on native platforms to NativeLiquidTabsModule.ts
export { default } from './NativeLiquidTabsModule';
export { default as NativeLiquidTabsView } from './NativeLiquidTabsView';
export * from './NativeLiquidTabs.types';
