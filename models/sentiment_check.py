import os 
from dotenv import load_dotenv
from transformers import pipeline
HF_TOKEN= os.getenv("HF_TOKEN")

load_dotenv()
MODEL_NAME="cardiffnlp/twitter-roberta-base-sentiment-latest"

sentiment_pipeline=pipeline(
    "sentiment-analysis",
    model=MODEL_NAME,
    token=HF_TOKEN
)

def analyze_sentiment(text: str):
    result=sentiment_pipeline(text)
    return result[0]