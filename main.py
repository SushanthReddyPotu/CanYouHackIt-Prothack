
from typing import Union
from fastapi import FastAPI
from pydantic import BaseModel
from models.summarizer import generate_summary
from pydantic import BaseModel
from models.sentiment_check import analyze_sentiment


app=FastAPI()

class SummaryRequest(BaseModel):
    text: str

class SentimentRequest(BaseModel):
    text:str

@app.post("/summarize/")
def summarize(req:SummaryRequest):
    result = generate_summary(req.text)
    return{"summary" : result}
    # return{"summary" : req.text}

@app.post("/sentiment/")
def sentiment(req: SentimentRequest):
    result=analyze_sentiment(req.text)
    return {"label": result["label"], "score": result["score"]}

@app.get("/")
def read_root():
    return{"Hello" : "World"}

# @app.get("/items/{item_id}")
# def read_item(item_id: int, q: Union[str, None] = None):
#     return {"item_id": item_id, "q": q}


# text=""" The Hugging Face Transformers library provides
# general-purpose architectures for Natural Language Understanding
# and Generation. With pretrained models, you can quickly fine-tune
# them for various downstream tasks like summarization, sentiment
# analysis, translation,and more."""


text="""Pipeline
The Pipeline is a simple but powerful inference API that is readily available for a variety of machine learning tasks with any model from the Hugging Face Hub.

Tailor the Pipeline to your task with task specific parameters such as adding timestamps to an automatic speech recognition (ASR) pipeline for transcribing meeting notes. Pipeline supports GPUs, Apple Silicon, and half-precision weights to accelerate inference and save memory.


Transformers has two pipeline classes, a generic Pipeline and many individual task-specific pipelines like TextGenerationPipeline or VisualQuestionAnsweringPipeline. Load these individual pipelines by setting the task identifier in the task parameter in Pipeline. You can find the task identifier for each pipeline in their API documentation.

Each task is configured to use a default pretrained model and preprocessor, but this can be overridden with the model parameter if you want to use a different model.

For example, to use the TextGenerationPipeline with Gemma 2, set task="text-generation" and model="google/gemma-2-2b".

"""

# # print("Summary" , generate_summary(text))



