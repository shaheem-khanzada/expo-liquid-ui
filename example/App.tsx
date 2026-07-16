import { NativeLiquidBackdropSceneView } from 'native-liquid-tabs';
import { useState } from 'react';
import { SafeAreaView, ScrollView, StyleSheet, Text, View } from 'react-native';

export default function App() {
  const [activeRoute, setActiveRoute] = useState('reports');

  return (
    <SafeAreaView style={styles.container}>
      <ScrollView style={styles.container}>
        <Text style={styles.header}>Module API Example</Text>
        <Group name="Views">
          <NativeLiquidBackdropSceneView
            activeRoute={activeRoute}
            onRoutePress={(event) => setActiveRoute(event.nativeEvent.route)}
            style={styles.view}>
            <View style={styles.demoContent}>
              <Text style={styles.demoTitle}>{activeRoute}</Text>
            </View>
          </NativeLiquidBackdropSceneView>
        </Group>
      </ScrollView>
    </SafeAreaView>
  );
}

function Group(props: { name: string; children: React.ReactNode }) {
  return (
    <View style={styles.group}>
      <Text style={styles.groupHeader}>{props.name}</Text>
      {props.children}
    </View>
  );
}

const styles = StyleSheet.create({
  header: { fontSize: 30, margin: 20 },
  groupHeader: { fontSize: 20, marginBottom: 20 },
  group: { margin: 20, backgroundColor: '#fff', borderRadius: 10, padding: 20 },
  container: { flex: 1, backgroundColor: '#eee' },
  view: { flex: 1, height: 420 },
  demoContent: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
    backgroundColor: '#f3f4f6',
  },
  demoTitle: {
    fontSize: 28,
    fontWeight: '700',
    textTransform: 'capitalize',
  },
});
