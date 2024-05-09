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

|                       스플래시 스크린                        |                        온보딩화면 - 1                        |                        온보딩화면 - 2                        |                        온보딩화면 - 3                        |                        권한 요구 화면                        |
| :----------------------------------------------------------: | :----------------------------------------------------------: | :----------------------------------------------------------: | :----------------------------------------------------------: | :----------------------------------------------------------: |
| ![splash](https://firebasestorage.googleapis.com/v0/b/fitmate-e2b03.appspot.com/o/fitmate_images%2Fsplash.png?alt=media&token=18d5ed5d-0fce-4d2f-810c-7c9e7654eddd) | ![onboarding_1](https://firebasestorage.googleapis.com/v0/b/fitmate-e2b03.appspot.com/o/fitmate_images%2Fonboard_1.png?alt=media&token=617f0fd9-61d2-43ff-a689-76eba8a5b836) | ![onboarding_2](https://firebasestorage.googleapis.com/v0/b/fitmate-e2b03.appspot.com/o/fitmate_images%2Fonboard_2.png?alt=media&token=890e2fbc-9b66-476d-84a0-d0cd17d7f0a4) | ![onboarding_3](https://firebasestorage.googleapis.com/v0/b/fitmate-e2b03.appspot.com/o/fitmate_images%2Fonboard_3.png?alt=media&token=89106848-baf7-4e1a-8440-510af561b68c) | ![permission](https://firebasestorage.googleapis.com/v0/b/fitmate-e2b03.appspot.com/o/fitmate_images%2Fpermission.png?alt=media&token=06430974-ed9c-4ae5-bcb1-80b184a5ad4d) |

|                         로그인 화면                          |                       닉네임 설정 화면                       |                          메인 화면                           |                        그룹 열람 화면                        |                      그룹 상세정보 화면                      |
| :----------------------------------------------------------: | :----------------------------------------------------------: | :----------------------------------------------------------: | :----------------------------------------------------------: | :----------------------------------------------------------: |
| ![login_main](https://firebasestorage.googleapis.com/v0/b/fitmate-e2b03.appspot.com/o/fitmate_images%2Flogin_main.png?alt=media&token=1834a867-6ec4-4d6d-9cb1-11f3585d52f7) | ![login_nickname](https://firebasestorage.googleapis.com/v0/b/fitmate-e2b03.appspot.com/o/fitmate_images%2Flogin_nickname.png?alt=media&token=a536cd3d-a4aa-4bba-9263-ec8cf49e6b70) | ![home_main](https://firebasestorage.googleapis.com/v0/b/fitmate-e2b03.appspot.com/o/fitmate_images%2Fhome_main.png?alt=media&token=fa6bba79-ad51-4f12-a870-3e27a0ec1bb8) | ![home_category](https://firebasestorage.googleapis.com/v0/b/fitmate-e2b03.appspot.com/o/fitmate_images%2Fhome_category.png?alt=media&token=be304be0-f000-4e5b-9dc1-e399c7ae3784) | ![home_group](https://firebasestorage.googleapis.com/v0/b/fitmate-e2b03.appspot.com/o/fitmate_images%2Fhome_group.png?alt=media&token=adfae553-1775-4585-8402-c4b62e2834cd) |

|                      내 운동 정보 화면                       |                        운동 기록 화면                        |                        채팅 목록 화면                        |                        채팅 내부 화면                        |                  채팅 드로어 레이아웃 화면                   |
| :----------------------------------------------------------: | :----------------------------------------------------------: | :----------------------------------------------------------: | :----------------------------------------------------------: | :----------------------------------------------------------: |
| ![fit_main](https://firebasestorage.googleapis.com/v0/b/fitmate-e2b03.appspot.com/o/fitmate_images%2Ffit_main.png?alt=media&token=52fcd78f-eb4b-4642-9773-e84abcbb8fc6) | ![fit_certificate](https://firebasestorage.googleapis.com/v0/b/fitmate-e2b03.appspot.com/o/fitmate_images%2Ffit_certificate.png?alt=media&token=93975774-8302-4f27-b5f2-8fe64203aebd) | ![chat_group](https://firebasestorage.googleapis.com/v0/b/fitmate-e2b03.appspot.com/o/fitmate_images%2Fchat_group.png?alt=media&token=d8f32b8f-a951-4294-b495-99490ff753d1) | ![chat_main](https://firebasestorage.googleapis.com/v0/b/fitmate-e2b03.appspot.com/o/fitmate_images%2Fchat_main.png?alt=media&token=9d7ebe4a-b827-41f7-b57d-5680ddf6c3ab) | ![chat_drawer](https://firebasestorage.googleapis.com/v0/b/fitmate-e2b03.appspot.com/o/fitmate_images%2Fchat_drawer.png?alt=media&token=4b2ed25e-f3ef-49d3-9282-fbb95c7f7a4d) |

|                       그룹 진척도 화면                       |                     그룹 투표 현황 화면                      |                     그룹 벌금 내역 화면                      |                         내 정보 화면                         |                      오픈소스 고지 화면                      |
| :----------------------------------------------------------: | :----------------------------------------------------------: | :----------------------------------------------------------: | :----------------------------------------------------------: | :----------------------------------------------------------: |
| ![chat_progress](https://firebasestorage.googleapis.com/v0/b/fitmate-e2b03.appspot.com/o/fitmate_images%2Fchat_progress.png?alt=media&token=a2a25c35-d41a-4ba6-a825-869a9c63ff9f) | ![chat_vote](https://firebasestorage.googleapis.com/v0/b/fitmate-e2b03.appspot.com/o/fitmate_images%2Fchat_vote.png?alt=media&token=302ae983-62a3-4db7-b1be-67e0f45748cf) | ![chat_fine](https://firebasestorage.googleapis.com/v0/b/fitmate-e2b03.appspot.com/o/fitmate_images%2Fchat_fine.png?alt=media&token=da5ecf3e-d7bc-4843-a3bc-de0a0528e843) | ![mypage_main](https://firebasestorage.googleapis.com/v0/b/fitmate-e2b03.appspot.com/o/fitmate_images%2Fmypage_main.png?alt=media&token=62397221-cc06-466b-9eae-1bbe21e08b01) | ![mypage_license](https://firebasestorage.googleapis.com/v0/b/fitmate-e2b03.appspot.com/o/fitmate_images%2Fmypage_license.png?alt=media&token=c229444c-fdfc-4ae9-9ce6-e2d48c13b6c6) |
