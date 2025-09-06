from transformers import pipeline
import os 
from dotenv import load_dotenv
# from config import DEVICE

HF_TOKEN= os.getenv("HF_TOKEN")

summarizer_pipeline = pipeline("summarization" , model="facebook/bart-large-cnn")


def generate_summary(text:str , max_length=140,min_length=30):
    summary = summarizer_pipeline(text,max_length=max_length,min_length=min_length,do_sample=False)
    return summary[0]["summary_text"]


# text="""Pipeline
# The Pipeline is a simple but powerful inference API that is readily available for a variety of machine learning tasks with any model from the Hugging Face Hub.

# Tailor the Pipeline to your task with task specific parameters such as adding timestamps to an automatic speech recognition (ASR) pipeline for transcribing meeting notes. Pipeline supports GPUs, Apple Silicon, and half-precision weights to accelerate inference and save memory.


# Transformers has two pipeline classes, a generic Pipeline and many individual task-specific pipelines like TextGenerationPipeline or VisualQuestionAnsweringPipeline. Load these individual pipelines by setting the task identifier in the task parameter in Pipeline. You can find the task identifier for each pipeline in their API documentation.

# Each task is configured to use a default pretrained model and preprocessor, but this can be overridden with the model parameter if you want to use a different model.

# For example, to use the TextGenerationPipeline with Gemma 2, set task="text-generation" and model="google/gemma-2-2b".

# """


# summary = summarizer_pipeline(text,max_length=120)

# print(summary)