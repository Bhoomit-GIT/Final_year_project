import 'package:flutter/material.dart';
import '../api.dart';

class RecommendationsScreen extends StatefulWidget {
  const RecommendationsScreen({super.key});

  @override
  _RecommendationsScreenState createState() => _RecommendationsScreenState();
}

class _RecommendationsScreenState extends State<RecommendationsScreen> {
  List<dynamic> recommendations = [];

  void getRecommendations(String query) async {
    try {
      var results = await fetchRecommendations(query);
      setState(() {
        recommendations = results;
      });
    } catch (e) {
      print("Error fetching recommendations: $e");
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text("Recommendations")),
      body: Column(
        children: [
          TextField(
            decoration: InputDecoration(labelText: "Enter Topic"),
            onSubmitted: (query) => getRecommendations(query),
          ),
          Expanded(
            child: ListView.builder(
              itemCount: recommendations.length,
              itemBuilder: (context, index) {
                return ListTile(
                  title: Text(recommendations[index][0]['title']),
                  subtitle: Text(recommendations[index][0]['tags']),
                );
              },
            ),
          ),
        ],
      ),
    );
  }
}