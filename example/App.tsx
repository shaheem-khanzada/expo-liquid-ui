import { NativeLiquidFabMenuView } from 'native-liquid-tabs';
import { SafeAreaView, ScrollView, StyleSheet, Text, View } from 'react-native';

export default function App() {
  return (
    <SafeAreaView style={styles.container}>
      <ScrollView style={styles.container}>
        <Text style={styles.header}>Module API Example</Text>
        <Group name="Views">
          <View style={styles.view}>
            <Text style={styles.demoTitle}>Native Liquid Tabs</Text>
            <NativeLiquidFabMenuView style={styles.fab} />
          </View>
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
  view: {
    height: 420,
    alignItems: 'center',
    justifyContent: 'center',
    backgroundColor: '#f3f4f6',
  },
  fab: {
    position: 'absolute',
    right: 24,
    bottom: 24,
    width: 86,
    height: 86,
  },
  demoTitle: {
    fontSize: 28,
    fontWeight: '700',
    textTransform: 'capitalize',
  },
});
