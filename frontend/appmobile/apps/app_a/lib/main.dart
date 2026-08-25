import 'package:flutter/material.dart';
import 'package:ui/ui.dart';

void main() {
  runApp(const AppA());
}

class AppA extends StatelessWidget {
  const AppA({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'App A',
      theme: AppTheme.light,
      home: const HomePage(),
    );
  }
}

class HomePage extends StatefulWidget {
  const HomePage({super.key});

  @override
  State<HomePage> createState() => _HomePageState();
}

class _HomePageState extends State<HomePage> {
  int _counter = 0;

  void _increment() => setState(() => _counter++);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        backgroundColor: Theme.of(context).colorScheme.inversePrimary,
        title: const Text('App A'),
      ),
      body: Padding(
        padding: AppInsets.page,
        child: Center(
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              Text(
                'Count: $_counter',
                style: Theme.of(context).textTheme.headlineMedium,
              ),
              AppGaps.v16,
              PrimaryButton(
                label: 'Increment',
                onPressed: _increment,
              ),
            ],
          ),
        ),
      ),
    );
  }
}
