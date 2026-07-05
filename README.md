# CoverLetter RAG

> **사용자 경험 기반 RAG 자기소개서 생성 서비스**  
> 사용자의 경험 데이터를 저장하고,  
> 자기소개서 문항과 의미적으로 관련 있는 경험을 검색한 뒤  
> LLM을 활용해 경험 기반 자기소개서 초안을 생성합니다.

---

<details>
<summary><h2>📂 Data Structure</h2></summary>
<div markdown="1">

```text
📦coverletter-RAG
 ┣ 📂backend
 ┃ ┗ 📂coverletter-rag-api
 ┃   ┣ 📂src
 ┃   ┃ ┗ 📂main
 ┃   ┃   ┣ 📂java
 ┃   ┃   ┃ ┗ 📂com
 ┃   ┃   ┃   ┗ 📂coverletter
 ┃   ┃   ┃     ┗ 📂coverletter_rag_api
 ┃   ┃   ┃       ┣ 📂experience
 ┃   ┃   ┃       ┃ ┣ 📂ctrl
 ┃   ┃   ┃       ┃ ┃ ┗ 📜ExperienceController.java
 ┃   ┃   ┃       ┃ ┣ 📂service
 ┃   ┃   ┃       ┃ ┃ ┗ 📜ExperienceService.java
 ┃   ┃   ┃       ┃ ┗ 📂domain
 ┃   ┃   ┃       ┃   ┣ 📂dto
 ┃   ┃   ┃       ┃   ┃ ┣ 📜ExperienceCreateRequest.java
 ┃   ┃   ┃       ┃   ┃ ┗ 📜ExperienceResponse.java
 ┃   ┃   ┃       ┃   ┣ 📂entity
 ┃   ┃   ┃       ┃   ┃ ┗ 📜ExperienceEntity.java
 ┃   ┃   ┃       ┃   ┗ 📂repository
 ┃   ┃   ┃       ┃     ┗ 📜ExperienceRepository.java
 ┃   ┃   ┃       ┣ 📂document
 ┃   ┃   ┃       ┃ ┣ 📂ctrl
 ┃   ┃   ┃       ┃ ┃ ┗ 📜DocumentController.java
 ┃   ┃   ┃       ┃ ┣ 📂service
 ┃   ┃   ┃       ┃ ┃ ┗ 📜ExperienceExtractionService.java
 ┃   ┃   ┃       ┃ ┗ 📂domain
 ┃   ┃   ┃       ┃   ┗ 📂dto
 ┃   ┃   ┃       ┃     ┣ 📜ExperienceExtractRequest.java
 ┃   ┃   ┃       ┃     ┣ 📜ExperienceExtractResponse.java
 ┃   ┃   ┃       ┃     ┗ 📜ExtractedExperienceDto.java
 ┃   ┃   ┃       ┣ 📂coverletter
 ┃   ┃   ┃       ┃ ┣ 📂ctrl
 ┃   ┃   ┃       ┃ ┃ ┗ 📜CoverLetterController.java
 ┃   ┃   ┃       ┃ ┣ 📂service
 ┃   ┃   ┃       ┃ ┃ ┗ 📜CoverLetterService.java
 ┃   ┃   ┃       ┃ ┗ 📂domain
 ┃   ┃   ┃       ┃   ┣ 📂dto
 ┃   ┃   ┃       ┃   ┃ ┣ 📜CoverLetterGenerateRequest.java
 ┃   ┃   ┃       ┃   ┃ ┣ 📜CoverLetterGenerateResponse.java
 ┃   ┃   ┃       ┃   ┃ ┗ 📜UsedExperienceResponse.java
 ┃   ┃   ┃       ┃   ┣ 📂entity
 ┃   ┃   ┃       ┃   ┃ ┗ 📜CoverLetterEntity.java
 ┃   ┃   ┃       ┃   ┗ 📂repository
 ┃   ┃   ┃       ┃     ┗ 📜CoverLetterRepository.java
 ┃   ┃   ┃       ┣ 📂openai
 ┃   ┃   ┃       ┃ ┣ 📜OpenAiEmbeddingService.java
 ┃   ┃   ┃       ┃ ┗ 📜OpenAiChatService.java
 ┃   ┃   ┃       ┣ 📂qdrant
 ┃   ┃   ┃       ┃ ┣ 📂domain
 ┃   ┃   ┃       ┃drant
 ┃   ┃   ┃       ┃ ┣ 📂domain
 ┃   ┃   ┃       ┃ ┃ ┗ 📂dto
 ┃   ┃   ┃       ┃ ┃   ┗ 📜QdrantExperienceSearchResult.java
 ┃   ┃   ┃       ┃ ┗ 📂service
 ┃   ┃   ┃       ┃   ┗ 📜QdrantService.java
 ┃   ┃   ┃       ┣ 📂global
 ┃   ┃   ┃       ┃ ┗ 📂config
 ┃   ┃   ┃       ┃   ┣ 📜RestClientConfig.java
 ┃   ┃   ┃       ┃   ┗ 📜WebConfig.java
 ┃   ┃   ┃       ┗ 📜CoverletterRagApiApplication.java
 ┃   ┃   ┗ 📂resources
 ┃   ┃     ┗ 📜application.yml
 ┃   ┗ 📜build.gradle
 ┃
 ┣ 📂frontend
 ┃ ┣ 📂src
 ┃ ┃ ┣ 📜api.js
 ┃ ┃ ┣ 📜App.jsx
 ┃ ┃ ┣ 📜App.css
 ┃ ┃ ┗ 📜index.css
 ┃ ┣ 📜package.json
 ┃ ┗ 📜package-lock.json
 ┃
 ┣ 📂infra
 ┃ ┗ 📜docker-compose.yml
 ┃
 ┣ 📜.env.example
 ┗ 📜README.md
```

</div>
</details>

---

## 🔗 Tech Stack

### Language & Framework

![Java](https://img.shields.io/badge/Java%2017-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot%203-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![Spring Data JPA](https://img.shields.io/badge/Spring%20Data%20JPA-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![Hibernate](https://img.shields.io/badge/Hibernate-59666C?style=for-the-badge&logo=hibernate&logoColor=white)

### Frontend

![React](https://img.shields.io/badge/React-61DAFB?style=for-the-badge&logo=react&logoColor=black)
![Vite](https://img.shields.io/badge/Vite-646CFF?style=for-the-badge&logo=vite&logoColor=white)
![JavaScript](https://img.shields.io/badge/JavaScript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black)
![Axios](https://img.shields.io/badge/Axios-5A29E4?style=for-the-badge&logo=axios&logoColor=white)

### Database

![MariaDB](https://img.shields.io/badge/MariaDB-003545?style=for-the-badge&logo=mariadb&logoColor=white)
![Qdrant](https://img.shields.io/badge/Qdrant-DC244C?style=for-the-badge&logo=qdrant&logoColor=white)

### AI

![OpenAI](https://img.shields.io/badge/OpenAI%20API-412991?style=for-the-badge&logo=openai&logoColor=white)
### Infra & Docs

![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![Docker Compose](https://img.shields.io/badge/Docker%20Compose-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![Swagger](https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge&logo=swagger&logoColor=black)

---
## 🖥️ Frontend

본 프로젝트는 간단한 React + Vite 프론트엔드를 제공합니다.

### 주요 화면
<img width="1000" height="400" alt="image" src="https://github.com/user-attachments/assets/64159df9-839f-4745-97bd-a20bbb23866a" />
<img width="1000" height="400" alt="image" src="https://github.com/user-attachments/assets/9337030f-66d8-446c-8185-046b39d3e8d6" />

---


## 📌 Key APIs

| Feature | Method | Endpoint | Description |
|---|---|---|---|
| Document | POST | `/api/documents/extract-experiences` | 긴 텍스트에서 경험 후보 추출 |
| Experience | POST | `/api/experiences` | 경험 카드 등록 및 Qdrant vector 저장 |
| Experience | GET | `/api/experiences` | 경험 목록 조회 |
| Experience | GET | `/api/experiences/{id}` | 경험 단건 조회 |
| Experience Search | GET | `/api/experiences/search?query={query}` | Qdrant 기반 관련 경험 후보 검색 |
| Cover Letter | POST | `/api/cover-letters/generate` | 선택한 경험 기반 자기소개서 초안 생성 |

---

## 🧠 RAG Pipeline

본 프로젝트는 사용자의 경험 데이터를 기반으로 자기소개서 답변을 생성하기 위해  
**Retrieval-Augmented Generation** 구조를 적용했습니다.

- ### 긴 텍스트에서 경험 후보 추출
<img width="1000" height="600" alt="image" src="https://github.com/user-attachments/assets/005aee2f-cfee-4bd2-8253-be46e09770b1" />

```text
이력서 / 자기소개서 / 포트폴리오 텍스트 입력
→ OpenAI Chat API 호출
→ 자기소개서에 활용 가능한 경험 후보 추출
→ 사용자가 확인 후 저장
```

- ### 경험 등록 및 벡터 저장

```text
사용자 경험 입력
→ Spring Boot API 호출
→ MariaDB에 경험 원본 저장
→ OpenAI Embedding API 호출
→ 경험 내용을 1536차원 vector로 변환
→ Qdrant에 vector + payload 저장
```


- ### 의미 기반 경험 후보 검색

```text
회사명 + 지원 직무 + 자기소개서 문항 입력
→ 검색 쿼리 생성
→ 검색 쿼리를 embedding vector로 변환
→ Qdrant에서 cosine similarity 기반 검색
→ 관련 경험 후보 Top-K 반환
→ 프론트엔드에서 후보 카드로 표시
```
- ### 사용자 경험 선택
<img width="750" height="765" alt="image" src="https://github.com/user-attachments/assets/3e97175b-07f7-4767-a496-671e716c79fb" />
<img width="750" height="300" alt="image" src="https://github.com/user-attachments/assets/4fc40265-1d70-4ca3-b203-40f9df775f3e" />

검색된 경험 후보를 바로 LLM에 전달하지 않고,  
사용자가 자기소개서에 사용할 경험 하나를 직접 선택

```text
관련 경험 후보 Top-K
→ 사용자가 경험 1개 선택
→ selectedExperienceId 저장
```

- ### 자기소개서 생성
<img width="1505" height="689" alt="image" src="https://github.com/user-attachments/assets/38c0af23-1468-47ed-b4ad-f42dd435bba8" />

```text
검색된 경험 context 구성
→ 회사명, 직무, 문항, 글자 수 제한과 함께 prompt 구성
→ OpenAI Chat API 호출
→ 자기소개서 초안 생성
→ MariaDB에 생성 결과 저장
→ React 화면에 결과 표시
```

---

## 🗃️ Data Storage Strategy

### MariaDB

MariaDB는 서비스의 원본 데이터를 관리합니다.

```text
- 경험 원본 데이터
- 자기소개서 생성 결과
- 회사명
- 직무
- 자기소개서 문항
- 생성된 답변
- 생성 이력
```

### Qdrant

Qdrant는 의미 기반 검색을 위한 Vector DB로 사용합니다.

```text
- 경험 내용을 embedding한 1536차원 vector 저장
- vector와 함께 payload 저장
- 자기소개서 문항과 의미적으로 가까운 경험 검색
```
---

## 📍 Qdrant Point Structure

Qdrant에는 경험 하나가 하나의 point로 저장됩니다.

```json
{
  "id": 5,
  "vector": [0.012, -0.044, "... 1536 dimensions"],
  "payload": {
    "experienceId": 5,
    "title": "DOF 엔진팀 인턴",
    "category": "internship",
    "period": "2023.12~2024.02",
    "content": "TOF 카메라 SDK를 활용해 포인트클라우드 데이터를 시각화한 경험",
    "tags": ["협업", "C++", "포인트클라우드"]
  }
}
```


---

## ⚙️ Local Development

### 1. Environment Variables

프로젝트 루트에 `.env` 파일을 생성합니다.

```env
MARIADB_ROOT_PASSWORD=
MARIADB_DATABASE=
MARIADB_USER=
MARIADB_PASSWORD=

SPRING_DATASOURCE_URL=
SPRING_DATASOURCE_USERNAME=
SPRING_DATASOURCE_PASSWORD=

OPENAI_API_KEY=
OPENAI_EMBEDDING_MODEL=text-embedding-3-small
OPENAI_CHAT_MODEL=gpt-4.1-mini

QDRANT_URL=http://localhost:6333
QDRANT_COLLECTION_NAME=coverletter_experiences
```

### 2. Docker Compose

MariaDB와 Qdrant를 실행합니다.

```bash
cd infra
docker compose --env-file ../.env up -d
```

실행 확인:

```bash
docker ps
```

MariaDB 접속 테스트:

```bash
docker exec -it coverletter-mariadb mariadb -ucover -pcover1234 coverletter_rag -e "SELECT 1;"
```

---

## 🧩 Qdrant Collection Setup

OpenAI `text-embedding-3-small` 모델의 embedding 차원에 맞춰  
Qdrant collection의 vector size를 `1536`으로 설정합니다.

PowerShell 기준:

```powershell
$body = @{
  vectors = @{
    size = 1536
    distance = "Cosine"
  }
} | ConvertTo-Json -Depth 5 -Compress

Invoke-RestMethod `
  -Method Put `
  -Uri "http://localhost:6333/collections/coverletter_experiences" `
  -ContentType "application/json" `
  -Body $body
```

Qdrant Dashboard:

```text
http://localhost:6333/dashboard
```

---

## 🚀 Run

### Backend

```bash
cd backend/coverletter-rag-api
./gradlew bootRun
```

Windows PowerShell:

```powershell
cd backend/coverletter-rag-api
.\gradlew bootRun
```

Swagger:

```text
http://localhost:8088/swagger-ui.html
```

### Frontend

```bash
cd frontend
npm install
npm run dev
```

Frontend:

```text
http://localhost:5173
```

---
