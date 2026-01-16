import os
from datetime import datetime
import joblib
import pandas as pd
from fastapi import FastAPI, HTTPException
from pydantic import BaseModel, Field, validator

KM_TO_MILES = 1.0 / 1.60934

app = FastAPI(title="FlightOnTime Predictor")

MODEL = None
THRESHOLD = 0.5
FINAL_FEATURES = None
MAPS = None
FALLBACKS = None



def classificar_periodo(hora):
    if 0 <= hora <= 5:
        return "madrugada"
    if 6 <= hora <= 11:
        return "manha"
    if 12 <= hora <= 17:
        return "tarde"
    return "noite"


class PredictRequest(BaseModel):
    companhia: str = Field(..., min_length=2, max_length=3)
    origem: str = Field(..., min_length=3, max_length=3)
    destino: str = Field(..., min_length=3, max_length=3)
    data_partida: datetime
    distancia_km: float = Field(..., gt=0)

    @validator("companhia", "origem", "destino", pre=True)
    def normalize_codes(cls, value):
        if value is None:
            return value
        return str(value).strip().upper()


class PredictResponse(BaseModel):
    previsao: str
    probabilidade: float


def load_model():
    global MODEL, THRESHOLD, FINAL_FEATURES, MAPS, FALLBACKS

    base_dir = os.path.dirname(os.path.abspath(__file__))
    default_model_path = os.path.abspath(os.path.join(base_dir, "..", "data_science", "modelo", "modelo_final.joblib"))
    model_path = os.getenv("MODEL_PATH", default_model_path)
    if not os.path.exists(model_path):
        raise FileNotFoundError(
            f"Model file not found: {model_path}. Set MODEL_PATH to the joblib file."
        )
    artifacts = joblib.load(model_path)
    MODEL = artifacts["model"]
    THRESHOLD = float(artifacts["threshold"])
    FINAL_FEATURES = artifacts.get("final_features")
    if FINAL_FEATURES is None:
        FINAL_FEATURES = artifacts["base_features"] + artifacts["aggregate_features"]
    MAPS = artifacts["aggregates_maps"]
    FALLBACKS = artifacts["fallback_values"]


def build_features(payload: PredictRequest) -> pd.DataFrame:
    if MODEL is None:
        load_model()

    dt = payload.data_partida
    day_of_week = dt.isoweekday()
    departure_hour = dt.hour
    departure_minute = dt.minute
    departure_minutes_since_midnight = departure_hour * 60 + departure_minute
    is_weekend = 1 if day_of_week in (6, 7) else 0
    periodo_dia = classificar_periodo(departure_hour)
    is_peak_hour = 1 if departure_hour in (6, 7, 8, 9, 16, 17, 18, 19) else 0

    airline = payload.companhia
    origin = payload.origem
    destination = payload.destino

    distance_miles = float(payload.distancia_km) * KM_TO_MILES

    route = f"{origin}_{destination}"
    origin_delay_mean = MAPS.get("origin_delay_mean_map", {}).get(
        origin, FALLBACKS.get("global_origin_mean", 0.0)
    )
    dest_delay_mean = MAPS.get("dest_delay_mean_map", {}).get(
        destination, FALLBACKS.get("global_dest_mean", 0.0)
    )
    route_delay_mean = MAPS.get("route_delay_mean_map", {}).get(
        route, FALLBACKS.get("global_route_mean", 0.0)
    )
    origin_flight_count = MAPS.get("origin_flight_count_map", {}).get(
        origin, FALLBACKS.get("default_origin_count", 0.0)
    )
    dest_flight_count = MAPS.get("dest_flight_count_map", {}).get(
        destination, FALLBACKS.get("default_dest_count", 0.0)
    )
    route_flight_count = MAPS.get("route_flight_count_map", {}).get(
        route, FALLBACKS.get("default_route_count", 0.0)
    )

    airport_country_map = MAPS.get("airport_country_map", {})
    airport_lat_map = MAPS.get("airport_lat_map", {})
    airport_lon_map = MAPS.get("airport_lon_map", {})

    global_airport_country = FALLBACKS.get("global_airport_country", "UNK")
    global_airport_lat = float(FALLBACKS.get("global_airport_lat", 0.0))
    global_airport_lon = float(FALLBACKS.get("global_airport_lon", 0.0))

    origin_country = airport_country_map.get(origin, global_airport_country)
    dest_country = airport_country_map.get(destination, global_airport_country)
    origin_lat = float(airport_lat_map.get(origin, global_airport_lat))
    origin_lon = float(airport_lon_map.get(origin, global_airport_lon))
    dest_lat = float(airport_lat_map.get(destination, global_airport_lat))
    dest_lon = float(airport_lon_map.get(destination, global_airport_lon))

    row = {
        "AIRLINE": airline,
        "ORIGIN_AIRPORT": origin,
        "DESTINATION_AIRPORT": destination,
        "DISTANCE": distance_miles,
        "MONTH": dt.month,
        "DAY_OF_WEEK": day_of_week,
        "DEPARTURE_HOUR": departure_hour,
        "DEPARTURE_MINUTE": departure_minute,
        "DEPARTURE_MINUTES_SINCE_MIDNIGHT": departure_minutes_since_midnight,
        "IS_WEEKEND": is_weekend,
        "PERIODO_DIA": periodo_dia,
        "IS_PEAK_HOUR": is_peak_hour,
        "ORIGIN_COUNTRY": origin_country,
        "DEST_COUNTRY": dest_country,
        "ORIGIN_LAT": origin_lat,
        "ORIGIN_LON": origin_lon,
        "DEST_LAT": dest_lat,
        "DEST_LON": dest_lon,
        "ORIGIN_DELAY_MEAN": origin_delay_mean,
        "DEST_DELAY_MEAN": dest_delay_mean,
        "ROUTE_DELAY_MEAN": route_delay_mean,
        "ORIGIN_FLIGHT_COUNT": origin_flight_count,
        "DEST_FLIGHT_COUNT": dest_flight_count,
        "ROUTE_FLIGHT_COUNT": route_flight_count,
    }

    return pd.DataFrame([row], columns=FINAL_FEATURES)


@app.on_event("startup")
def startup_event():
    load_model()


@app.get("/health")
def health_check():
    return {"status": "ok"}


@app.post("/predict", response_model=PredictResponse)
def predict(payload: PredictRequest):
    try:
        features = build_features(payload)
        proba_atraso = float(MODEL.predict_proba(features)[0, 1])
    except Exception as exc:
        raise HTTPException(status_code=500, detail=str(exc)) from exc

    previsao = "Atrasado" if proba_atraso >= THRESHOLD else "Pontual"
    probabilidade = proba_atraso if previsao == "Atrasado" else 1.0 - proba_atraso

    return PredictResponse(
        previsao=previsao,
        probabilidade=round(probabilidade, 4),
    )









