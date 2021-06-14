import * as React from 'react';

import { StyleSheet, View, Text, TouchableOpacity } from 'react-native';
import { createCollector, VGSCollectInput } from 'vgs-collect-react-native';

export default function App() {
  const { submit, collectorName } = createCollector(
    'tntazhyknp1',
    'sandbox',
    'example-form'
  );

  return (
    <View style={styles.container}>
      <VGSCollectInput
        config={{
          collectorName,
          fieldName: 'data.attributes.cvv2',
          fieldType: 'cvc',
        }}
        fontSize={30}
        placeholder={'CVC'}
        isSecureTextEntry={true}
        style={styles.box}
      />
      <VGSCollectInput
        config={{
          collectorName,
          fieldName: 'data.attributes.expirationDate',
          fieldType: 'expDate',
          formatPattern: '####-##',
        }}
        fontSize={30}
        placeholder={'YYYY-MM'}
        isSecureTextEntry={false}
        style={styles.box}
      />
      <TouchableOpacity
        onPress={() => {
          submit('/cards/2000/activate', 'POST', {
            'Authorization':
              'Bearer v2.public.eyJyb2xlIjoiY3VzdG9tZXIiLCJ1c2VySWQiOm51bGwsInN1YiI6ImN1c3RvbWVyLzE4NTUzL2RvdGFuc2ltaGFAZ21haWwuY29tIiwiZXhwIjoiMjAyMS0wNi0xNVQwODozNDowMi43MjlaIiwianRpIjpudWxsLCJvcmdJZCI6bnVsbCwic2NvcGUiOiJjYXJkcy1zZW5zaXRpdmUtd3JpdGUiLCJjdXN0b21lcklkIjoiMTg1NTMifVknUOifijp3Zwos9_FdF4ZLTTiLXDXaVx-yqgLnUUjJ7MsJ8ADEuu5bb5mdju5qwuuufwTXMmrvNdzxkU93nQc',
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
        <Text>Click to send</Text>
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
