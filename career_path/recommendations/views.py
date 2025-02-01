from django.shortcuts import render

# Create your views here.
# recommendations/views.py
from rest_framework.views import APIView
from rest_framework.response import Response
from .filtering import collaborative_filtering, content_based_filtering
from resources.api_fetching import fetch_mdn_resources

class RecommendationView(APIView):
    def get(self, request): 
        user_data = request.GET.get('user_data', [])  # Get user data from query params
        user_query = request.GET.get('query', '')  # Get user query for content-based filtering
        
        # Collaborative filtering
        collaborative_result = collaborative_filtering(user_data)
        
        # Fetch resources from MDN API
        mdn_resources = fetch_mdn_resources(user_query)
        
        # Content-based filtering
        content_based_result = content_based_filtering(user_query, mdn_resources)
        
        return Response({
            'collaborative_result': collaborative_result,
            'content_based_result': content_based_result
        })
