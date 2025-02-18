# recommendations/filtering.py
from surprise import SVD, Dataset, Reader
import pandas as pd

def collaborative_filtering(user_data):
    # Example user-item interaction data
    data = pd.DataFrame(user_data)
    reader = Reader(rating_scale=(1, 5))
    dataset = Dataset.load_from_df(data[['user_id', 'resource_id', 'rating']], reader)

    # Train the model
    trainset = dataset.build_full_trainset()
    model = SVD()
    model.fit(trainset)

    # Predict for a user-item pair
    pred = model.predict(1, 102)  # Predicting for user 1 and resource 102
    return pred.est  # Returning the predicted rating
# recommendations/filtering.py
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.metrics.pairwise import cosine_similarity

def content_based_filtering(user_query, resources):
    # Example resources with tags
    resource_tags = [res['tags'] for res in resources]
    
    vectorizer = TfidfVectorizer()
    tfidf_matrix = vectorizer.fit_transform([user_query] + resource_tags)  # User query + resource tags
    cosine_sim = cosine_similarity(tfidf_matrix[0:1], tfidf_matrix[1:]).flatten()
    
    recommendations = sorted(zip(resources, cosine_sim), key=lambda x: x[1], reverse=True)
    return recommendations  # Returning sorted resources based on similarity