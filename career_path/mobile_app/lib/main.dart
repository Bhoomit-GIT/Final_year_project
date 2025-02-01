import 'package:flutter/material.dart';
import 'screens/roadmap.dart';
import 'screens/recommendations.dart';


void main() {
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      title: "Smart Learning System",
      theme: ThemeData(primarySwatch: Colors.blue),
      home: RoadmapScreen(),
      routes: {
        "/roadmap": (context) => RoadmapScreen(),
        "/recommendations": (context) => RecommendationsScreen(),
      },
    );
  }
}