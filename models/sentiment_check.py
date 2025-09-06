# import os 
# from dotenv import load_dotenv
# from transformers import pipeline
# HF_TOKEN= os.getenv("HF_TOKEN")

# load_dotenv()
# MODEL_NAME="cardiffnlp/twitter-roberta-base-sentiment-latest"

# sentiment_pipeline=pipeline(
#     "sentiment-analysis",
#     model=MODEL_NAME,
#     token=HF_TOKEN
# )

# def analyze_sentiment(text: str):
#     result=sentiment_pipeline(text)
#     return result[0]
import os
from dotenv import load_dotenv
from transformers import pipeline

# Load environment variables
load_dotenv()
HF_TOKEN = os.getenv("HF_TOKEN")

# Model name
MODEL_NAME = "cardiffnlp/twitter-roberta-base-sentiment-latest"

# Create pipeline
sentiment_pipeline = pipeline(
    "sentiment-analysis",
    model=MODEL_NAME # only if the model requires authentication
)

def analyze_sentiment(text: str):
    result = sentiment_pipeline(text)[0]  # result is a dict: {'label': 'Positive', 'score': 0.98}
    return {
        "label": result["label"],   # Positive / Negative / Neutral
        "score": float(result["score"])  # confidence score
    }

# Example usage
print(analyze_sentiment("I love this app, it's amazing!"))
print(analyze_sentiment("This is the worst thing ever."))