import * as React from 'react';

import { StyleSheet, View, Text, TouchableOpacity } from 'react-native';
import { createCollector, VGSCollectInput } from 'vgs-collect-react-native';

export default function App() {
  const { submit, collectorName } = createCollector(
    'tntazhyknp1',
    'sandbox',
    'example-form'
  );

  const collector2 = createCollector('tntazhyknp1', 'sandbox', 'example2-form');
  const submit2 = collector2.submit;
  const collectorName2 = collector2.collectorName;

  return (
    <View style={styles.container}>
      <VGSCollectInput
        config={{
          collectorName,
          fieldName: 'data.attributes.cvv2',
          fieldType: 'cvc',
        }}
        fontSize={50}
        placeholder={'CVC'}
        isSecureTextEntry={true}
        style={styles.box}
      />
      <VGSCollectInput
        config={{
          collectorName,
          fieldName: 'data.attributes.expirationDate',
          fieldType: 'expDate',
          divider: '-',
          inputDateFormat: 'shortYear',
          outputDateFormat: 'longYearThenMonth',
          formatPattern: '##-##',
        }}
        fontSize={50}
        placeholder={'MM-YY'}
        isSecureTextEntry={false}
        style={styles.box}
      />
      <TouchableOpacity
        onPress={() => {
          submit('/cards/15438/activate', 'POST', {
            'Authorization':
              'Bearer v2.public.eyJyb2xlIjoiY3VzdG9tZXIiLCJ1c2VySWQiOm51bGwsInN1YiI6ImN1c3RvbWVyLzM2ODQ4L2dpbGdhcmRvc2grNkBnbWFpbC5jb20iLCJleHAiOiIyMDIxLTA2LTIzVDE1OjQ0OjExLjA5MFoiLCJqdGkiOm51bGwsIm9yZ0lkIjpudWxsLCJzY29wZSI6ImNhcmRzLXNlbnNpdGl2ZS13cml0ZSIsImN1c3RvbWVySWQiOiIzNjg0OCJ97EZMxeIbAg01SfwxZurOYzeh2IvzkENYg4N1gs2Ct_xevk_n9D5WKCH-g9Z360dpjHFyJb45akMdfpDl86zTAA',
            'Content-Type': 'application/vnd.api+json',
          })
            .then((r) => {
              console.log('submit done, unit response', r);
            })
            .catch((e) => {
              console.error('submit failed', e);
            });
        }}
      >
        <Text>Click to activate card</Text>
      </TouchableOpacity>
      <VGSCollectInput
        config={{
          collectorName: collectorName2,
          fieldName: 'data.attributes.pin',
          fieldType: 'pin',
          keyboardType: 'numberPad',
        }}
        placeholder={'_ _ _ _'}
        fontSize={50}
        isSecureTextEntry={true}
        style={styles.box}
      />
      <TouchableOpacity
        onPress={() => {
          submit2('/cards/15438/secure-data/pin', 'POST', {
            'mapDotToObject': 'true',
            'Authorization':
              'Bearer v2.public.eyJyb2xlIjoiY3VzdG9tZXIiLCJ1c2VySWQiOm51bGwsInN1YiI6ImN1c3RvbWVyLzM2ODQ4L2dpbGdhcmRvc2grNkBnbWFpbC5jb20iLCJleHAiOiIyMDIxLTA2LTIzVDE1OjQ0OjExLjA5MFoiLCJqdGkiOm51bGwsIm9yZ0lkIjpudWxsLCJzY29wZSI6ImNhcmRzLXNlbnNpdGl2ZS13cml0ZSIsImN1c3RvbWVySWQiOiIzNjg0OCJ97EZMxeIbAg01SfwxZurOYzeh2IvzkENYg4N1gs2Ct_xevk_n9D5WKCH-g9Z360dpjHFyJb45akMdfpDl86zTAA',
            'Content-Type': 'application/vnd.api+json',
          })
            .then((r) => {
              console.log('submit done, unit response', r);
            })
            .catch((e) => {
              console.error('submit failed', JSON.stringify(e));
            });
        }}
      >
        <Text>Click to change PIN</Text>
      </TouchableOpacity>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  box: {
    width: 200,
    borderColor: 'gray',
    borderWidth: 1,
    height: 50,
    marginVertical: 20,
  },
});
