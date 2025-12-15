# 📊 FlightOnTime – Data Science
&nbsp;
## **✈️ Contexto**
O setor aéreo sofre diariamente com atrasos de voos, que geram:<p>
•	Insatisfação dos passageiros;<br>
•	Custos extras para companhias aéreas;<br>
•	Problemas de logística em aeroportos.<br>
<p>
  
O projeto **FlightOnTime** busca prever se um voo será **Pontual (0)** ou **Atrasado (1)**, com base em dados históricos e operacionais.&nbsp;<p>
&nbsp;
## **🎯 Objetivo**
Construir um **MVP (produto mínimo viável)** que:<p>
•	Recebe dados de um voo (companhia, origem, destino, data/hora);<br>
•	Retorna uma previsão binária: Pontual ou Atrasado;<br>
•	Exporta o modelo treinado para ser consumido pelo Back-End via API REST.<br>
&nbsp;
## **🗂️ Etapas do Notebook**
**1. Exploração e Limpeza de Dados (EDA)** <p>
Dataset: [Flight Delays 2015 – US DOT](https://www.kaggle.com/datasets/usdot/flight-delays)<br>
Remoção de voos cancelados e desviados;<br>
Exclusão de colunas que causam data leakage.<p><br>

**2. Criação da Variável-Alvo** <p>
Definição: atraso: ≥ 15 minutos<br>
Distribuição:<br>
- Pontual (0): 81,62%<br>
-	Atrasado (1): 18,38%<br>

Dataset desbalanceado → uso de class_weight e scale_pos_weight<p><br>

**3. Modelagem Preditiva** <p>
Logistic Regression (baseline)<br>
XGBoost (modelo avançado)<br>
Pipeline com ColumnTransformer + OneHotEncoder<p><br>

**4. Avaliação dos Modelos** <p>
Métricas: Acurácia, Precisão, Recall, F1-score, ROC AUC;<br>
Matriz de confusão para análise de erros<p><br>

**5. Exportação do Modelo** <p>
Arquivo gerado: flight_model.pkl via joblib.dump()<p><br>


## **📊 Resultados Obtidos**				
**📌 Conclusão:** O XGBoost apresentou melhor desempenho, especialmente em Recall e F1-score da classe minoritária (Atrasado). Ele foi escolhido como modelo final para exportação e integração com o Back-End.<p>
&nbsp;

**⚙️ Como Executar**<p>
1.	Clone o repositório: [FlightOnTime.ipynb](https://github.com/Grupo-38-ONE-G8-FlightOnTime/Data_Science/blob/main/FlightOnTime.ipynb);<br>
2.	Abra o notebook no Jupyter ou Google Colab.<br>
3.	Execute todas as células para:<br>
•	Explorar os dados<br>
•	Treinar o modelo<br>
•	Exportar flight_model.pkl<br>
