import pandas as pd
import numpy as np
from sklearn.metrics.pairwise import cosine_similarity
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.metrics import precision_recall_fscore_support

def load_dataset(file_path="Dataset.xlsx"):
    return pd.read_excel(file_path)

def clean_dataset(df):
    df.drop_duplicates(inplace=True)
    df.loc[:, "Career Goal"] = df["Career Goal"].fillna("Unknown")

    tech_columns = ["Technology 1", "Technology 2", "Technology 3", "Technology 4", "Technology 5"]
    for col in tech_columns:
        df.loc[:, col] = df[col].fillna("None")

    df[tech_columns] = df[tech_columns].apply(lambda col: col.map(lambda x: x.strip().lower() if isinstance(x, str) else x))
    df.loc[:, "Year of study"] = pd.to_numeric(df["Year of study"], errors="coerce")
    
    return df

def create_user_technology_matrix(df):
    tech_columns = ["Technology 1", "Technology 2", "Technology 3", "Technology 4", "Technology 5"]
    tech_list = list(filter(pd.notna, df[tech_columns].values.flatten()))
    user_tech_matrix = pd.DataFrame(0, index=df.index, columns=tech_list)

    for _, row in df.iterrows():
        for tech in tech_columns:
            if pd.notna(row[tech]):
                user_tech_matrix.loc[row.name, row[tech]] = 1
    return user_tech_matrix

def compute_user_similarity(user_tech_matrix):
    return cosine_similarity(user_tech_matrix)

def find_best_matching_user(user_skills, study_year, df, user_tech_matrix):
    user_skills_vector = pd.Series(0, index=user_tech_matrix.columns)
    for skill in user_skills:
        if skill in user_skills_vector:
            user_skills_vector[skill] = 1

    similarity_scores = cosine_similarity([user_skills_vector], user_tech_matrix)[0]
    df_filtered = df[df["Year of study"] == study_year]

    if df_filtered.empty:
        best_match_index = np.argmax(similarity_scores)
    else:
        best_match_index = df_filtered.index[np.argmax(similarity_scores[df_filtered.index])]
    
    return best_match_index

def recommend_career_collaborative(user_index, df, similarity_matrix, top_n=3):
    similar_users = np.argsort(similarity_matrix[user_index])[::-1][1:6]
    career_counts = {}

    for similar_user in similar_users:
        career = df.iloc[similar_user]["Career Goal"]
        weight = similarity_matrix[user_index, similar_user] / similarity_matrix[user_index].max()

        if career not in career_counts:
            career_counts[career] = 0
        career_counts[career] += weight

    return sorted(career_counts.items(), key=lambda x: x[1], reverse=True)[:top_n]

def recommend_career_content_based(df, user_skills, study_year):
    df_filtered = df[df["Year of study"] <= study_year]
    if df_filtered.empty:
        df_filtered = df

    df_filtered["Tech Combined"] = df_filtered[["Technology 1", "Technology 2", "Technology 3", "Technology 4", "Technology 5"]].astype(str).agg(' '.join, axis=1)

    vectorizer = TfidfVectorizer()
    tfidf_matrix = vectorizer.fit_transform(df_filtered["Tech Combined"])
    
    user_tfidf = vectorizer.transform([" ".join(user_skills)])
    similarity_scores = cosine_similarity(user_tfidf, tfidf_matrix).flatten()

    df_filtered["Similarity Score"] = similarity_scores / similarity_scores.max()
    df_filtered["Final Score"] = df_filtered["Similarity Score"]
    
    return df_filtered.sort_values(by="Final Score", ascending=False).head(3)[["Career Goal", "Final Score"]]

def evaluate_model(df, user_tech_matrix, similarity_matrix):
    y_true = df["Career Goal"].tolist()
    y_pred = []

    for user_index in range(len(df)):
        recommended_careers = recommend_career_collaborative(user_index, df, similarity_matrix)
        y_pred.append(recommended_careers[0][0] if recommended_careers else "Unknown")

    precision, recall, f1, _ = precision_recall_fscore_support(y_true, y_pred, average="weighted", zero_division=1)

    print("\nModel Evaluation:")
    print(f"Precision: {precision:.3f}")
    print(f"Recall: {recall:.3f}")
    print(f"F1-Score: {f1:.3f}")

if __name__ == "__main__":
    df = load_dataset("Dataset.xlsx")
    df = clean_dataset(df)

    user_tech_matrix = create_user_technology_matrix(df)
    similarity_matrix = compute_user_similarity(user_tech_matrix)

    while True:
        try:
            study_year = int(input("\nEnter your Year of Study (1-4): "))
            if 1 <= study_year <= 4:
                break
            else:
                print("Invalid input. Enter a number between 1 and 4.")
        except ValueError:
            print("Invalid input. Enter a number between 1 and 4.")

    user_skills = input("\nEnter your known skills (comma-separated): ").strip().lower().split(", ")
    
    best_matching_user = find_best_matching_user(user_skills, study_year, df, user_tech_matrix)
    print(f"\nBest-matching user: {best_matching_user}")

    print("\nCareer Recommendations (Content-Based Filtering):")
    print(recommend_career_content_based(df, user_skills, study_year))

    print("\nCareer Recommendations (Collaborative Filtering):")
    for career, score in recommend_career_collaborative(best_matching_user, df, similarity_matrix):
        print(f"{career} ({score:.2f})")

    evaluate_model(df, user_tech_matrix, similarity_matrix)