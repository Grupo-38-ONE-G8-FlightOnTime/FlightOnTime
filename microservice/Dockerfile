FROM python:3.11-slim
WORKDIR /app
COPY microservice/requirements.txt /app/microservice/requirements.txt
RUN pip install --no-cache-dir -r /app/microservice/requirements.txt
COPY microservice /app/microservice
COPY data_science/modelo /app/data_science/modelo
ENV MODEL_PATH=/app/data_science/modelo/modelo_final.joblib
EXPOSE 8000
CMD ["uvicorn", "microservice.app:app", "--host", "0.0.0.0", "--port", "8000"]
