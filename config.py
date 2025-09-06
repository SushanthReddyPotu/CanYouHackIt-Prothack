import os
from dotenv import load_dotenv
load_dotenv()
HF_TOKEN = os.getenv("HF_TOKEN")
DEVICE = os.getenv("DEVICE","cuda")

# # Toxicity-specific settings
# TOXICITY_THRESHOLD = float(os.getenv("TOXICITY_THRESHOLD","0.8"))
# TOXICITY_MODEL = os.getenv("TOXICITY")
