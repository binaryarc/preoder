# 프리오더(Preoder) - 키오스크 사전메뉴 선택 앱

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Python](https://img.shields.io/badge/Python-3776AB?style=for-the-badge&logo=python&logoColor=white)
![TensorFlow](https://img.shields.io/badge/TensorFlow-FF6F00?style=for-the-badge&logo=tensorflow&logoColor=white)
![Keras](https://img.shields.io/badge/Keras-D00000?style=for-the-badge&logo=keras&logoColor=white)
![SQLite](https://img.shields.io/badge/SQLite-07405E?style=for-the-badge&logo=sqlite&logoColor=white)
![Firebase](https://img.shields.io/badge/Firebase-FFCA28?style=for-the-badge&logo=firebase&logoColor=black)
![Naver](https://img.shields.io/badge/Naver_API-03C75A?style=for-the-badge&logo=naver&logoColor=white)
![Kakao](https://img.shields.io/badge/Kakao_API-FFCD00?style=for-the-badge&logo=kakao&logoColor=black)

## 프로젝트 개요

프리오더(Preoder)는 키오스크 사전메뉴 선택 앱으로, 사용자가 매장에 방문하기 전에 미리 메뉴를 선택하고 준비할 수 있도록 도와주는 시스템입니다. 이 앱은 다음과 같은 주요 기능을 제공합니다:

1. **메뉴 추천**: 사용자의 취향(매운맛, 단맛, 신맛, 고소한맛 등)에 기반하여 메뉴를 추천
2. **음성 인식**: 음성으로 취향을 입력 가능
3. **위치 기반 서비스**: 가까운 매장 찾기
4. **소셜 로그인**: 카카오, 네이버, 구글 로그인 지원

## 시스템 아키텍처

이 프로젝트는 크게 두 부분으로 구성되어 있습니다:

1. **안드로이드 앱(클라이언트)**:
   - 사용자 인터페이스 제공
   - 음성 인식 및 처리
   - 서버와의 통신
   - 소셜 로그인 처리
   - 네이버 지도 API를 통한 위치 서비스

2. **Python 서버**:
   - 자연어 처리(NLP)를 통한 메뉴 추천
   - LSTM 모델을 이용한 사용자 취향 분석
   - SQLite 데이터베이스 관리
   - 소켓 통신을 통한 클라이언트와의 연결

## 기술 스택

### 안드로이드 앱
- **언어**: 
  ![Java](https://img.shields.io/badge/Java-007396?style=flat-square&logo=java&logoColor=white)
- **API 및 라이브러리**:
  - ![Naver Maps](https://img.shields.io/badge/Naver_Maps-03C75A?style=flat-square&logo=naver&logoColor=white)
  - ![Kakao Login](https://img.shields.io/badge/Kakao_Login-FFCD00?style=flat-square&logo=kakao&logoColor=black)
  - ![Google Firebase](https://img.shields.io/badge/Firebase-FFCA28?style=flat-square&logo=firebase&logoColor=black)
  - ![Glide](https://img.shields.io/badge/Glide-25BAA2?style=flat-square)
  - ![AsyncTask](https://img.shields.io/badge/AsyncTask-3DDC84?style=flat-square&logo=android&logoColor=white)
  - ![Speech Recognition](https://img.shields.io/badge/Speech_Recognition-4285F4?style=flat-square&logo=google&logoColor=white)

### 서버
- **언어**: 
  ![Python](https://img.shields.io/badge/Python-3776AB?style=flat-square&logo=python&logoColor=white)
- **프레임워크 및 라이브러리**:
  - ![TensorFlow](https://img.shields.io/badge/TensorFlow-FF6F00?style=flat-square&logo=tensorflow&logoColor=white)
  - ![Keras](https://img.shields.io/badge/Keras-D00000?style=flat-square&logo=keras&logoColor=white)
  - ![KoNLPy](https://img.shields.io/badge/KoNLPy-3775A9?style=flat-square)
  - ![Socket](https://img.shields.io/badge/Socket-010101?style=flat-square)
  - ![SQLite](https://img.shields.io/badge/SQLite-07405E?style=flat-square&logo=sqlite&logoColor=white)
  - ![JSON](https://img.shields.io/badge/JSON-000000?style=flat-square&logo=json&logoColor=white)

## 주요 코드 구조

### 안드로이드 앱

#### 주요 액티비티
- **MainActivity.java**: 앱의 메인 화면, 메뉴 추천과 매장 검색 기능으로 이동할 수 있음
- **Menu_recommend.java**: 메뉴 추천 화면, 음성 인식과 서버 통신을 통해 메뉴 추천 기능 제공
- **Nmaps_test.java**: 네이버 지도를 통해 주변 매장을 찾을 수 있는 화면
- **KakaoLogin.java/NaverLogin.java/GoogleLogin.java**: 각 소셜 로그인 처리를 담당

#### 네트워크 통신
- **MyAsyncTask.java**: 네이버 검색 API를 사용하여 매장 정보를 검색하는 비동기 태스크
- **MyAsyncTask2.java**: 추가적인 네트워크 요청을 처리하는 비동기 태스크
- **Menu_recommend.java의 Connect 클래스**: 서버와 소켓 통신을 통해 메뉴 추천 요청 및 결과 수신

### 서버

#### 주요 파일
- **proj_server.py**: 메인 서버 프로그램, 소켓 통신과 LSTM 모델을 통한 메뉴 추천 처리
- **proj_lstm.py**: LSTM 모델 학습 및 테스트를 위한 스크립트
- **proj_db.py**: 데이터베이스 쿼리 및 처리를 담당
- **proj_client.py**: 서버 테스트를 위한 클라이언트 시뮬레이션
- **train_data.csv**: LSTM 모델 학습을 위한 훈련 데이터

## 주요 기능 분석

### 1. 메뉴 추천 시스템

메뉴 추천 시스템은 LSTM(Long Short-Term Memory) 모델을 사용하여 사용자의 자연어 입력을 분석하고 적절한 메뉴를 추천합니다.

**작동 방식**:
1. 사용자가 앱에서 음성 또는 텍스트로 취향 입력 (예: "매운 음식 추천해줘")
2. 입력은 안드로이드 앱에서 서버로 전송
3. 서버는 KoNLPy를 사용해 텍스트를 토큰화하고 불용어 제거
4. LSTM 모델이 처리된 텍스트를 분석하여 사용자의 취향 카테고리 예측
5. 예측된 카테고리에 맞는 메뉴 목록을 데이터베이스에서 조회
6. 추천 결과를 JSON 형태로 앱에 반환
7. 앱은 추천된 메뉴 목록을 화면에 표시

### 2. 음성 인식 시스템

안드로이드의 SpeechRecognizer를 사용하여 사용자의 음성 입력을 텍스트로 변환합니다.

**작동 방식**:
1. 사용자가 음성 버튼을 누름
2. 음성 인식이 시작되고 사용자가 말함
3. 인식된 음성이 텍스트로 변환되어 검색창에 자동 입력
4. 사용자가 검색 버튼을 누르면 변환된 텍스트가 서버로 전송

### 3. 소셜 로그인

카카오, 네이버, 구글 계정을 통한 로그인을 지원합니다.

**지원 플랫폼**:
- ![Kakao](https://img.shields.io/badge/Kakao-FFCD00?style=flat-square&logo=kakao&logoColor=black) 카카오 로그인
- ![Naver](https://img.shields.io/badge/Naver-03C75A?style=flat-square&logo=naver&logoColor=white) 네이버 로그인
- ![Google](https://img.shields.io/badge/Google-4285F4?style=flat-square&logo=google&logoColor=white) 구글 로그인(Firebase 기반)

### 4. 위치 기반 서비스

네이버 지도 API를 활용하여 사용자 주변의 매장을 찾고 표시합니다.

**기능**:
- 현재 위치 주변 매장 검색
- 매장 정보 표시
- 길 찾기 연동

## 데이터베이스 구조

SQLite 데이터베이스(proj.db)가 사용되며, 다음과 같은 정보를 저장합니다:

- 메뉴 이름(name)
- 메뉴 이미지 URL(img)
- 가격(price)
- 재료(inside)
- 브랜드/매장(brand)
- 맛 카테고리(flavour)

## 보안 고려사항

이 프로젝트에는 다음과 같은 민감한 정보가 포함되어 있었으나, 보안을 위해 실제 값은 마스킹 처리되었습니다:

1. **네이버 API 클라이언트 ID 및 시크릿**: 
   - 클라이언트 ID: `***********`
   - 클라이언트 시크릿: `*********`

2. **서버 IP 주소**: 
   - 실제 서버 IP는 `*.*.*.*`로 마스킹 처리됨

3. **소셜 로그인 관련 키**: 
   - 각 소셜 로그인 인증 키는 마스킹 처리됨

## 개선 가능성

프로젝트 분석 결과, 다음과 같은 개선 가능성이 있습니다:

1. **보안 강화**:
   - API 키 및 시크릿을 안전하게 관리하기 위한 환경 변수 또는 보안 저장소 사용
   - HTTPS 통신 적용 및 데이터 암호화

2. **사용자 경험 개선**:
   - UI/UX 현대화
   - 다양한 필터링 옵션 추가
   - 사용자 피드백 및 평점 시스템 도입

3. **기술적 개선**:
   - AsyncTask 대신 현대적인 비동기 처리 방식(Coroutine, RxJava) 적용
   - 서버 확장성 개선을 위한 웹 프레임워크(Flask, Django) 적용
   - 추천 알고리즘 정확도 향상

4. **기능 확장**:
   - 실제 주문 및 결제 시스템 연동
   - 사용자 취향 학습 및 개인화된 추천 기능 강화
   - 다양한 음식점 및 메뉴 데이터 확대

## 결론

프리오더(Preoder)는 AI 기반 메뉴 추천 및 키오스크 사전 주문 시스템으로, 머신러닝과 모바일 애플리케이션 기술을 결합하여 사용자 경험을 향상시키는 프로젝트입니다. 자연어 처리와 음성 인식을 통해 편리한 인터페이스를 제공하며, 위치 기반 서비스로 실용성을 높였습니다.

LSTM 기반의 메뉴 추천 알고리즘은 사용자의 자연어 입력을 효과적으로 해석하고 적절한 메뉴를 추천하는 데 사용되며, 이는 키오스크 주문 시스템의 사용자 경험을 크게 개선할 수 있는 가능성을 보여줍니다.