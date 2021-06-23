import {
  requireNativeComponent,
  ViewStyle,
  NativeModules,
  Platform,
} from 'react-native';
const { CollectorManager } = NativeModules;

export type VGSEnvironment = 'sandbox' | 'live';
export type Validation = { min: number; max: number } | { pattern: string };

type VGSCollectInputProps = {
  config: {
    collectorName: string;
    fieldName: string;
    fieldType:
      | 'cardHolderName'
      | 'expDate'
      | 'cvc'
      | 'cardNumber'
      | 'pin'
      | 'text';
    validations?: Validation[];
    formatPattern?: string;
    divider?: string;
    inputDateFormat?:
      | 'shortYear'
      | 'longYear'
      | 'shortYearThenMonth'
      | 'longYearThenMonth';
    outputDateFormat?:
      | 'shortYear'
      | 'longYear'
      | 'shortYearThenMonth'
      | 'longYearThenMonth';
    keyboardType?: 'numberPad' | 'default';
  };
  isSecureTextEntry?: boolean;
  placeholder?: string;
  placeholderColor?: string;
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
      const fn = Platform.select({
        ios: () => CollectorManager.submit(name, path, method, headers),
        android: () => {
          return new Promise<{ code: number; data?: TData }>(
            (resolve, reject) => {
              CollectorManager.submit(
                name,
                path,
                method,
                headers,
                (payload: any) => {
                  if ('error' in payload) {
                    reject(new Error(payload.error || payload.code));
                  } else {
                    resolve(
                      typeof payload.data === 'string'
                        ? {
                            ...payload,
                            data: JSON.parse(payload.data),
                          }
                        : payload
                    );
                  }
                }
              );
            }
          );
        },
      });

      return fn!();
    },
  };
}
