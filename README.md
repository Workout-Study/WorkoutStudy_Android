# FITMATE Application

<div>
	<img src="https://img.shields.io/badge/Android Studio-3DDC84?style=for-the-badge&logo=Android Studio&logoColor=white" />
  <img src="https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=Kotlin&logoColor=white" />
  <img src="https://img.shields.io/badge/Material%20Design-757575?style=for-the-badge&logo=Material%20Design&logoColor=white" />
  <img src="https://img.shields.io/badge/Firebase%20Storage-FFCA28?style=for-the-badge&logo=Firebase&logoColor=white" />
  </div>


<div>
  <img src="https://img.shields.io/badge/Hilt-836FFF?style=for-the-badge" />
  <img src="https://img.shields.io/badge/Coroutine-211951?style=for-the-badge" />
	<img src="https://img.shields.io/badge/Flow-F0F3FF?style=for-the-badge" />
  <img src="https://img.shields.io/badge/Retrofit-15F5BA?style=for-the-badge" />
  </div>


![initial](https://firebasestorage.googleapis.com/v0/b/fitmate-e2b03.appspot.com/o/fitmate_images%2Ffitmate_mainphoto.png?alt=media&token=fd41b4b0-303c-4047-8c43-8ce09acf75ee)



## Team FITMATE

| 팀원   | 역할         | 작업 내용                                       | GitHub Link                                   |
| ------ | ------------ | :---------------------------------------------- | --------------------------------------------- |
| 김성호 | 기획, 백엔드 | 실시간 단체 채팅                                | [kimsunfang](https://github.com/kimsunfang)   |
| 정정일 | 백엔드       | 운동 인증, 그룹 관리                            | [12OneTwo12](https://github.com/12OneTwo12)   |
| 박찬규 | 백엔드       | 로그인, 사용자 정보 관리                        | [chan0966](https://github.com/chan0966)       |
| 서경원 | 안드로이드   | 모듈 설계, 디자인, 채팅, 로그인, 통신           | [woojugoing](https://github.com/woojugoing)   |
| 강현구 | 안드로이드   | 아키텍처 설계, 디자인, 인증, 운동기록 정보 관리 | [Ganghyungoo](https://github.com/Ganghyungoo) |



## Functions

- Social Network Service의 OAuth 프로토콜을 활용한 로그인
- 다양한 카테고리와 넓은 범주의 운동 소모임 생성 및 가입
- Application의 작동 여부에 관계없이 기능하는 운동 기록 타이머
- 자신이 진행하고 있는 운동의 현황 및 그룹 내 다른 팀원들과 현황 공유
- 그룹 내 채팅을 통한 다른 팀원들과의 실시간 소통



## Application Architecture

FITMATE 애플리케이션의 아키텍처 구조 입니다.

'Android Developer'의 공식 권장 아키텍쳐를 기반으로 하여 

**단방향 데이터 흐름**으로 통신하도록 Data, Domain, Presentation(UI) 를 분리하였습니다.

![application-architecture](https://firebasestorage.googleapis.com/v0/b/fitmate-e2b03.appspot.com/o/fitmate_images%2Fandroid_architectrue.png?alt=media&token=ea0b9e12-4f52-4439-beec-b3947dfced75)



## Tools

| What                          | How                                                          |
| ----------------------------- | ------------------------------------------------------------ |
| Design Tool                   | [**Figma**](https://www.figma.com/file/iqkGWZ0tqTRbDtKQw3sntf/UI-Design?type=design&node-id=0%3A1&mode=design&t=YjfriBHUPmIPBgeW-1) |
| Collaboration Tool            | **Confluence, Jira, Slack**                                  |
| Configuration Management Tool | **Git**                                                      |
| Programming Tool              | **Android Studio**                                           |



## Skills

| What                   | How                                               |
| ---------------------- | ------------------------------------------------- |
| Asynchronous Process   | **Coroutine, Flow**                               |
| Design Framework       | **Material Design 3**                             |
| Media Process          | **Lottie, Glide, Compressor**                     |
| Local Storage Solution | **Room,  SharedPreferences**                      |
| Cloud Storage Solution | **Firebase Storage**                              |
| REST API Client        | **Retrofit, OKHttp**                              |
| UI Enhancement         | **Shimmer(Skeleton), horizontalnestedscrollview** |
| Jetpack                | **LiveData, ViewModel, DataBinding, Navigation**  |
| Dependency Injection   | **Hilt**                                          |



## Demo Video

해당 이미지를 클릭 시 영상으로 이동합니다.

[![FITMATE Demo Video](https://firebasestorage.googleapis.com/v0/b/fitmate-e2b03.appspot.com/o/fitmate_images%2Fyoutube_screenshot.png?alt=media&token=e047ec98-4205-431b-9ec9-8d1b666586ed)](https://www.youtube.com/watch?v=hU05MZoTvVI)



## Screenshots

|                       온보딩 화면 - 1                        |                        온보딩화면 - 2                        |                        온보딩화면 - 3                        |                        온보딩화면 - 4                        |                        권한 요구 화면                        |
| :----------------------------------------------------------: | :----------------------------------------------------------: | :----------------------------------------------------------: | :----------------------------------------------------------: | :----------------------------------------------------------: |
| ![onboarding_1](https://github.com/user-attachments/assets/2bfd73b7-674c-4aa0-a79c-cfcb4b72b9ee) | ![onboarding_2](https://github.com/user-attachments/assets/a30e9d73-5e51-455a-8a22-58a36c5b9ebd) | ![onboarding_3](https://github.com/user-attachments/assets/b9572e45-5956-4acd-b14d-171c5d66d53c) | ![onboarding_4](https://github.com/user-attachments/assets/2c0db90a-90f8-4873-9160-9ee274267160) | ![permission](https://github.com/user-attachments/assets/4a157222-71a5-466e-a436-b7da0c26d27a) |

|                         로그인 화면                          |                       닉네임 설정 화면                       |                          메인 화면                           |                        그룹 열람 화면                        |                      그룹 상세정보 화면                      |
| :----------------------------------------------------------: | :----------------------------------------------------------: | :----------------------------------------------------------: | :----------------------------------------------------------: | :----------------------------------------------------------: |
| ![login_main](https://github.com/user-attachments/assets/aaf005da-8dc1-4002-b158-6c9e0acc085c) | ![login_nickname](https://firebasestorage.googleapis.com/v0/b/fitmate-e2b03.appspot.com/o/fitmate_images%2Flogin_nickname.png?alt=media&token=a536cd3d-a4aa-4bba-9263-ec8cf49e6b70) | ![home_main](https://github.com/user-attachments/assets/90353596-4052-4724-8d93-2ec4b7346a7e) | ![home_category](https://github.com/user-attachments/assets/6d706bc1-57f7-4e79-a010-b2c7e9bb84ed) | ![home_group](https://github.com/user-attachments/assets/2d39fa4e-e72f-4fc9-a6fd-ae189735b33b) |

|                      내 운동 정보 화면                       |                        운동 기록 화면                        |                        캘린더 화면                       |                        채팅 내부 화면                        |                  채팅 드로어 레이아웃 화면                   |
| :----------------------------------------------------------: | :----------------------------------------------------------: | :----------------------------------------------------------: | :----------------------------------------------------------: | :----------------------------------------------------------: |
| ![fit_main](https://github.com/user-attachments/assets/5d343128-7f4c-45ab-a060-a6192c1a8cd5) | ![fit_certificate](https://github.com/user-attachments/assets/8e5bb696-e8b2-4bb9-833b-ff5ad2489eb8) | ![calendar](https://github.com/user-attachments/assets/12ba8fb9-7d84-43f1-b156-adf2981e7d81) | ![chat_main](https://github.com/user-attachments/assets/b66b3409-8dff-4f6b-a503-4121ca3f4f06) | ![chat_drawer](https://github.com/user-attachments/assets/84a53c4f-765c-4205-954f-cd239a3e9fc4) |

|                       그룹 진척도 화면                       |                     그룹 투표 현황 화면                      |                     그룹 벌금 내역 화면                      |                         내 정보 화면                         |                      오픈소스 고지 화면                      |
| :----------------------------------------------------------: | :----------------------------------------------------------: | :----------------------------------------------------------: | :----------------------------------------------------------: | :----------------------------------------------------------: |
| ![chat_progress](https://github.com/user-attachments/assets/69a91ff7-2c78-4829-98fb-15a08eedd727) | ![chat_vote](https://github.com/user-attachments/assets/5c7a133a-3d58-4f0a-bbe7-83282fe509f3) | ![chat_fine](https://github.com/user-attachments/assets/4900e472-7380-442a-a309-337c83c21aa6) | ![mypage_main](https://github.com/user-attachments/assets/217b7093-886f-42ef-a5c5-94ba4304f87c) | ![mypage_license](https://firebasestorage.googleapis.com/v0/b/fitmate-e2b03.appspot.com/o/fitmate_images%2Fmypage_license.png?alt=media&token=c229444c-fdfc-4ae9-9ce6-e2d48c13b6c6) |
