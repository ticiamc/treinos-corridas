## Integrantes do grupo

* Sarah Pereira de Araújo Lima - sarahpereiradal@gmail.com
* Letícia Medeiros Cavalcante - ticiamedeiroscavalcante@gmail.com
* Yadson Florêncio Leite - yadsonflorencio7@gmail.com
* João Miguel Oliveira de Carvalho - joaomigueloliveira@ufrpe.br

## link do UML para edição: 
https://www.mermaidchart.com/app/projects/78973b59-9842-44bc-82c2-aaf3eeb54dce/diagrams/627160cb-7caf-47cf-b3a8-0d3613aa87dd/version/v0.1/edit

# Sistema de Gestão de Treinos e Corridas

## Descrição

Este sistema tem como objetivo gerenciar treinos e corridas de usuários, permitindo o registro de atividades físicas, definição de metas, acompanhamento de desempenho e histórico de treinos. Ele deve possibilitar a criação de planos de treino personalizados, monitoramento de progresso e emissão de estatísticas detalhadas.

O sistema deve registrar dados de corridas e treinos (distância, tempo, calorias queimadas, frequência cardíaca), permitir desafios entre usuários e gerar alertas de metas atingidas ou atrasadas. Relatórios e gráficos devem ajudar o usuário a analisar seu desempenho ao longo do tempo.

## Requisitos Funcionais

### 1. Gerenciamento de Usuários

- **REQ01**: Permitir cadastro de usuários com nome, idade, peso, altura e e-mail.
- **REQ02**: Atualizar informações pessoais e métricas físicas.
- **REQ03**: Registrar histórico de atividades e metas do usuário.

### 2. Registro de Corridas e Treinos

- **REQ04**: Permitir cadastro de corridas e treinos com data, hora, distância e tempo.
- **REQ05**: Registrar tipo de treino (corrida, caminhada, treino intervalado, etc.).
- **REQ06**: Calcular métricas automáticas como velocidade média e calorias queimadas.

### 3. Definição de Metas

- **REQ07**: Permitir definição de metas de distância, tempo ou calorias para períodos específicos.
- **REQ08**: Notificar usuário quando uma meta é atingida ou ultrapassada.
- **REQ09**: Atualizar progresso das metas automaticamente conforme novos treinos são registrados.

### 4. Planos de Treino Personalizados

- **REQ10**: Permitir criação de planos de treino com sequência de atividades e datas.
- **REQ11**: Associar planos de treino a usuários específicos.
- **REQ12**: Atualizar planos de treino conforme desempenho ou ajustes do usuário.

### 5. Desafios e Competição

- **REQ13**: Permitir criação de desafios entre usuários (ex.: correr 50 km em um mês).
- **REQ14**: Registrar participação e progresso dos usuários nos desafios.
- **REQ15**: Gerar rankings semanais ou mensais com base nos resultados dos desafios.

### 6. Relatórios e Estatísticas

- **REQ16**: Gerar relatório de atividades por período (semanal, mensal, anual).
- **REQ17**: Relatório de evolução do desempenho do usuário (distância, velocidade, calorias).
- **REQ18**: Relatórios de desafios concluídos e rankings.
- **REQ19**: Permitir exportação de relatórios em **PDF** e **CSV**.

### 7. Alertas e Notificações

- **REQ20**: Alertar usuários sobre metas próximas de serem atingidas.
- **REQ21**: Notificar sobre novos desafios ou convites de competição.
- **REQ22**: Lembretes de treinos planejados ou pendentes.

### 8. Regras e Restrições

- **REQ23**: Não permitir registro de treino com dados inconsistentes (ex.: distância ou tempo negativos).
- **REQ24**: Não permitir criação de meta com período inválido ou já vencido.
- **REQ25**: Impedir participação em desafios sem registro de atividade mínima prévia.
- **REQ26**: Garantir que cada treino registrado esteja associado a um usuário válido.

## Possíveis APIs/Bibliotecas a Serem Usadas

- **JavaFX** – Interface gráfica para registro de treinos, planos e desafios.
- **JDBC / Hibernate** – Persistência de dados de usuários, treinos e desafios.
- **Java Time API** – Controle de datas, horários e períodos de metas.
- **JFreeChart / JavaFX Charts** – Geração de gráficos de desempenho e progresso.
- **iText / JasperReports** – Geração de relatórios em PDF.
- **Apache POI** – Exportação de relatórios em CSV ou Excel.
- **JUnit / Mockito** – Testes de regras de negócio (cálculo de métricas, validação de treinos e metas).  
