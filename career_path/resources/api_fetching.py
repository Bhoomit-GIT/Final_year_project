# resources/api_fetching.py
import requests

def fetch_mdn_resources(query):
    url = f"https://developer.mozilla.org/api/v1/search?q={query}"  # MDN API endpoint
    response = requests.get(url)
    if response.status_code == 200:
        return response.json()['documents']
    else:
        return {'error': 'Failed to fetch from MDN API'}