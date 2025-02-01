import 'dart:convert';
import 'package:http/http.dart' as http;

const String BASE_URL = "http://127.0.0.1:8000/career_path";

// Fetch Learning Roadmap
Future<Map<String, dynamic>> fetchRoadmap() async {
  final url = Uri.parse("$BASE_URL/roadmaps/generate/");
  print("Fetching roadmap from: $url");

  try {
    final response = await http.get(url);
    print("Response Status Code: ${response.statusCode}");
    print("Response Body: ${response.body}");

    if (response.statusCode == 200) {
      return jsonDecode(response.body);
    } else {
      throw Exception("Failed to fetch roadmap. Status Code: ${response.statusCode}");
    }
  } catch (e) {
    print("Error fetching roadmap: $e");
    throw Exception("Network error");
  }
}

// Fetch Recommendations
Future<List<dynamic>> fetchRecommendations(String query) async {
  final response = await http.get(Uri.parse("$BASE_URL/recommendations/recommend/?query=$query"));
  if (response.statusCode == 200) {
    return jsonDecode(response.body)['content_based_result'];
  } else {
    throw Exception("Failed to fetch recommendations");
  }
}


