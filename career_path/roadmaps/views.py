from django.shortcuts import render

# Create your views here.
# roadmaps/views.py
from rest_framework.views import APIView
from rest_framework.response import Response
from .mermaid import generate_mermaid_code

class RoadmapView(APIView):
    def get(self, request):
        roadmap_data = {
            'stage_1': 'Learn HTML',  # Example data
            'stage_2': 'Learn CSS',
            'stage_3': 'Learn JavaScript'
        }
        
        mermaid_code = generate_mermaid_code(roadmap_data)
        return Response({'mermaid_code': mermaid_code})