import { requireNativeComponent, ViewStyle } from 'react-native';

type VgsCollectReactNativeProps = {
  color: string;
  style: ViewStyle;
};

export const VgsCollectReactNativeViewManager = requireNativeComponent<VgsCollectReactNativeProps>(
'VgsCollectReactNativeView'
);

export default VgsCollectReactNativeViewManager;
