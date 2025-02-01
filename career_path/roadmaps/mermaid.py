# roadmaps/mermaid.py
def generate_mermaid_code(roadmap):
    # Example roadmap structure
    mermaid_code = f"""
    graph TD;
        A[Start] --> B[{roadmap['stage_1']}];
        B --> C[{roadmap['stage_2']}];
        C --> D[{roadmap['stage_3']}];
    """
    return mermaid_code