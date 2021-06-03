import * as React from 'react';

import { StyleSheet, View, Text, TouchableOpacity } from 'react-native';
import { createCollector, VGSCollectInput } from 'vgs-collect-react-native';

export default function App() {
  const { submit, collectorName } = createCollector('tntazhyknp1', 'sandbox');

  return (
    <View style={styles.container}>
      <VGSCollectInput
        config={{
          collectorName,
          fieldName: 'data.attributes.pin',
          fieldType: 'text',
          validations: [{ min: 4, max: 6 }],
          formatPattern: '####',
          divider: '__',
          keyboardType: 'numberPad',
        }}
        fontSize={30}
        placeholder={'test'}
        isSecureTextEntry={true}
        style={styles.box}
      />
      {/* <VGSCollectInput
        config={{
          collectorName: COLLECTOR_NAME,
          fieldName: 'field.exp',
          fieldType: 'expDate',
        }}
        style={styles.box}
      />
      <VGSCollectInput
        config={{
          collectorName: COLLECTOR_NAME,
          fieldName: 'field.cvv',
          fieldType: 'cvc',
        }}
        style={styles.box}
      /> */}
      <TouchableOpacity
        onPress={() => {
          submit('/cards/4073/secure-data/pin', 'POST', {
            'Authorization':
              'Bearer v2.public.eyJyb2xlIjoiY3VzdG9tZXIiLCJ1c2VySWQiOm51bGwsInN1YiI6ImN1c3RvbWVyLzI1MTI4L2RvdGFuc2ltaGFAZ21haWwuY29tIiwiZXhwIjoiMjAyMS0wNi0wMVQxMjo1MzowNC4xODZaIiwianRpIjpudWxsLCJvcmdJZCI6bnVsbCwic2NvcGUiOiJjYXJkcy1zZW5zaXRpdmUtd3JpdGUiLCJjdXN0b21lcklkIjoiMjUxMjgifaZfeTHZdSebasPXfbxzLcViCcjmU487koS_4OSNHQA8Q3tcU_jTQCze3rRIW_kJGd_mMvpvdBPd0cbW5sa7UAs',
            'Content-Type': 'application/vnd.api+json',
          })
            .then(console.log)
            .catch((e) => {
              console.log('submit failed', e);
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
    width: 100,
    borderColor: 'red',
    borderWidth: 1,
    backgroundColor: 'red',
    height: 50,
    marginVertical: 20,
  },
});
