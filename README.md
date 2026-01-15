# FlightOnTime

Projeto para prever atraso de voos. Monorepo com Data Science, microservico Python e backend Java.

## Estrutura
- data_science/: notebook e modelo treinado
- microservice/: FastAPI que faz a previsao
- backend/: Spring Boot que expoe a API e chama o microservico

## Requisitos
- Python 3.9+
- Java 21
- Maven (ou mvnw)

## Como rodar (local)

### 1) Microservico (FastAPI)
```powershell
cd C:\Users\DTI\Desktop\FlightOnTime
pip install -r microservice\requirements.txt
$env:MODEL_PATH="data_science\modelo\modelo_final.joblib"
uvicorn microservice.app:app --reload --host 0.0.0.0 --port 8000
```

### 2) Backend (Spring Boot)
```powershell
cd C:\Users\DTI\Desktop\FlightOnTime\backend
$env:DATASCIENCE_BASE_URL="http://localhost:8000"
.\mvnw spring-boot:run
```

## Contrato da API (backend)

### POST /predict
Body:
```json
{
  "companhia": "AZ",
  "origem": "GIG",
  "destino": "GRU",
  "data_partida": "2025-11-10T14:30:00",
  "distancia_km": 350
}
```

Response:
```json
{
  "previsao": "Atrasado",
  "probabilidade": 0.78
}
```

### GET /stats
Response:
```json
{
  "total": 10,
  "atrasados": 3,
  "pontuais": 7,
  "percentual_atraso": 0.3
}
```

## Persistencia (H2)
- O backend salva as previsoes em um banco H2 local.
- Arquivo gerado em `backend/data/flightontime.mv.db`.
- Console H2: `http://localhost:8081/h2-console` (JDBC URL: `jdbc:h2:file:./data/flightontime`).

## Docker (opcional)
```powershell
docker compose up --build
```

## Endpoints
- GET http://localhost:8000/health (microservice)
- POST http://localhost:8000/predict (microservice)
- GET http://localhost:8081/health (backend)
- POST http://localhost:8081/predict (backend)
- GET http://localhost:8081/stats (backend)

## Dataset
- Flight Delays 2015 - US DOT: https://www.kaggle.com/datasets/usdot/flight-delays

## Observacoes
- O microservico usa o modelo em `data_science/modelo/modelo_final.joblib` por padrao. Use `MODEL_PATH` para sobrescrever.
- O backend usa `datascience.base-url` (ou `DATASCIENCE_BASE_URL`) para chamar o microservico.

