import 'package:flutter/material.dart';
import 'package:webview_flutter/webview_flutter.dart';
import '../api.dart';

class RoadmapScreen extends StatefulWidget {
  const RoadmapScreen({super.key});

  @override
  _RoadmapScreenState createState() => _RoadmapScreenState();
}

class _RoadmapScreenState extends State<RoadmapScreen> {
  String mermaidCode = "";
  late WebViewController controller;

  @override
  void initState() {
    super.initState();
    controller = WebViewController()
      ..setJavaScriptMode(JavaScriptMode.unrestricted);

    fetchRoadmapData();
  }

  Future<void> fetchRoadmapData() async {
    try {
      var data = await fetchRoadmap();
      print("Fetched Roadmap Data: $data"); // Debug

      if (!data.containsKey("mermaid_code")) {
        print("Invalid response: $data");
        return;
      }

      setState(() {
        mermaidCode = data["mermaid_code"];
      });

      print("Updated mermaidCode: $mermaidCode");
      loadMermaidDiagram();
    } catch (e) {
      print("Error fetching roadmap: $e");
    }
  }

  void loadMermaidDiagram() {
    if (mermaidCode.isEmpty) {
      print("Mermaid code is empty, not loading WebView.");
      return;
    }

    String htmlData = '''
      <html>
        <head>
          <script type="module">
            import mermaid from 'https://cdn.jsdelivr.net/npm/mermaid@10/dist/mermaid.esm.min.mjs';
            mermaid.initialize({ startOnLoad: true });
          </script>
        </head>
        <body>
          <div class="mermaid">$mermaidCode</div>
        </body>
      </html>
    ''';

    print("Generated HTML: $htmlData");

    controller.loadRequest(Uri.dataFromString(htmlData, mimeType: 'text/html'));
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text("Learning Roadmap")),
      body: mermaidCode.isEmpty
          ? Center(child: Text("Loading roadmap...")) // Temporary message
          : WebViewWidget(controller: controller),
    );
  }
}