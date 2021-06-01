import { requireNativeComponent, ViewStyle, NativeModules } from 'react-native';
const { CollectorManager } = NativeModules;

export type VGSEnvironment = 'sandbox' | 'live';

type VGSCollectInputProps = {
  config: {
    collectorName: string;
    fieldName: string;
    fieldType: 'cardHolderName' | 'expDate' | 'cvc' | 'cardNumber' | 'text';
    validations?: [{ min: number; max: number } | { pattern: string }];
    formatPattern?: string;
    divider?: string;
    keyboardType?: 'numberPad' | 'default';
  };
  isSecureTextEntry?: boolean;
  placeholder?: string;
  textColor?: string;
  fontFamily?: string;
  fontSize?: number;
  style?: ViewStyle;
};

export const VGSCollectInput = requireNativeComponent<VGSCollectInputProps>(
  'VgsCollectReactNativeView'
);

export type SubmitFn<TData> = (
  path: string,
  method: 'GET' | 'POST',
  headers: Record<string, string>
) => Promise<{ code: number; data?: TData }>;

export function createCollector<TData = any>(
  vaultId: string,
  environment: VGSEnvironment,
  name = Date.now().toString()
): {
  collectorName: string;
  submit: SubmitFn<TData>;
} {
  CollectorManager.createNamedCollector(name, vaultId, environment);

  return {
    collectorName: name,
    submit: async (path, method = 'POST', headers = {}) => {
      return await CollectorManager.submit(name, path, method, headers);
    },
  };
}
