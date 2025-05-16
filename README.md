<div align="center">
  <img src="/readme_assets/cm_banner.png" alt="CABBAGE MARKET" />
  <h1>배추마켓 (CABBAGE-MARKET)</h1>
</div>

## 목차

1. [**프로젝트 소개**](#1)
2. [**기술 스택**](#2)
3. [**주요 기능**](#3)
4. [**실행 화면**](#4)
5. [**프로젝트 구성도**](#5)
6. [**개발 팀 소개**](#6)
7. [**개발 기간**](#7)

<div id="1"></div>

## 프로젝트 소개
사용자가 중고 물품을 등록하고, 게시글을 통해 거래할 수 있는 
**중고거래 특성을 반영한 게시판 기반 웹 플랫폼**

<div id="2"></div>

## 기술 스택

### **Front-end**

| <img src="https://profilinator.rishav.dev/skills-assets/html5-original-wordmark.svg" alt="HTML5" width="50px" height="50px" /> | <img src="https://profilinator.rishav.dev/skills-assets/css3-original-wordmark.svg" alt="CSS3" width="50px" height="50px" /> | <img src="https://profilinator.rishav.dev/skills-assets/javascript-original.svg" alt="JavaScript" width="50px" height="50px" /> | 
| :----------------------------------------------------------------------------------------------------------------------------: | :--------------------------------------------------------------------------------------------------------------------------: | :-----------------------------------------------------------------------------------------------------------------------------: |
|                                                             HTML5                                                              |                                                             CSS3                                                             |                                                         JavaScript                                                         | 

### **Back-end**

| <img src="https://profilinator.rishav.dev/skills-assets/springio-icon.svg" alt="Spring" width="50px" height="50px" /> | <img src="https://profilinator.rishav.dev/skills-assets/mysql-original-wordmark.svg" alt="MySQL" width="50px" height="50px" /> | <img src="https://profilinator.rishav.dev/skills-assets/google_cloud-icon.svg" alt="GCS" width="50px" height="50px" /> |
| :---------------------------------------------------------------------------------------------------------------------: | :---------------------------------------------------------------------------------------------------------------------: | :--------------------------------------------------------------------------------------------------------------------------------------: |
|                                                         Spring                                                          |                                                         MySQL                                                          |                                                                GCP                                                                |

<div id="3"></div>

## 주요 기능
| 기능          | 설명                                                                   |
| ----------- |----------------------------------------------------------------------|
| 게시글 관리      | 게시글 작성, 수정, 삭제, 조회 및 검색 기능 지원<br>이미지 포함 가능, 제한된 게시글은 작성자 또는 관리자만 조회 가능 |
| 댓글 기능       | 게시글에 댓글 작성, 수정, 삭제 가능                                                |
| 좋아요 기능      | 게시글에 좋아요 토글(등록/취소) 및 현재 사용자의 좋아요 여부 확인 가능                            |
| 게시글 신고      | 게시글에 대한 신고 기능 제공 (사유 포함), 관리자 페이지에서 신고 내역 확인 가능                      |
| 반성문 제출 및 승인 | 제한된 게시글에 대해 반성문 제출 가능 (100자 이상), 관리자는 반성문 조회 및 승인/거절 처리              |
| 게시글 제한 관리   | 신고 누적으로 제한된 게시글 목록을 관리자 페이지에서 확인 가능                                  |
| 마이페이지 관리    | 현재 로그인 사용자의 정보 조회 및 이름, 프로필 이미지 수정/초기화 가능                            |
| 회원가입 및 인증   | 사용자 회원가입, 로그아웃, 현재 로그인 사용자 정보 조회 가능                                  |
| 조회수 추적  | 게시글을 열람할 때마다 조회수 증가 처리 가능 (IP주소 당 30초에 1번)                           |

<details>
  <summary>API 목록</summary>

| API 종류  | 메서드  | URL                                 | 설명                                  |
| ------- | ---- | ----------------------------------- | ----------------------------------- |
| Admin  | GET  | `/posts/restricted`                 | 신고 누적으로 제한된 게시글 목록 조회               |
|   | GET  | `/reports`                          | 전체 신고 내역 조회                         |
|  | GET  | `/apologies`                        | 승인 대기 중인 반성문 목록 조회                  |
|  | GET  | `/apologies/{id}`                   | 특정 반성문 상세 조회                        |
|  | POST | `/apologies/{id}/review?accept=...` | 반성문 승인 또는 거절 처리 (accept=true/false) |
| Comment | POST   | `/posts/{postId}/comments` | 특정 게시글에 댓글 작성 |
|  | PUT    | `/comments/{commentId}`    | 댓글 수정         |
|  | DELETE | `/comments/{commentId}`    | 댓글 삭제         |
| Like   | POST | `/{postId}` | 게시글 좋아요 또는 좋아요 취소 (토글)    |
|   | GET  | `/{postId}` | 현재 로그인 사용자의 게시글 좋아요 여부 조회 |
| Post   | GET    | `/posts`               | 전체 게시글 목록 조회 (페이징 지원)                    |
|   | GET    | `/posts/{postId}`      | 게시글 상세 조회<br>※ 제한 게시글은 작성자 또는 관리자만 접근 가능 |
|   | POST   | `/posts`               | 게시글 작성 (이미지 포함, `multipart/form-data`)   |
|  | PUT    | `/posts/{postId}`      | 게시글 수정 (이미지 포함, `multipart/form-data`)   |
|   | DELETE | `/posts/{postId}`      | 게시글 삭제 (작성자만 가능)                         |
|    | POST   | `/posts/{postId}/view` | 게시글 조회수 증가                               |
|  | GET    | `/posts/search`        | 게시글 검색<br>검색어, 가격 범위, 상태(enum) 등 조건 지원   |
| Report | POST | `/apologies/{postId}` | 제한된 게시글에 대한 반성문 제출 (최소 100자 이상) |
| | POST | `/reports/{postId}` | 특정 게시글에 대한 신고 등록 (신고 사유 포함) |
| Auth   | POST | `/users/register` | 사용자 회원가입          |
|   | POST | `/users/logout`   | 로그아웃 (세션 무효화)     |
|   | GET  | `/users/me`       | 현재 로그인된 사용자 정보 조회 |
| MyPage | GET    | `/users/mypage`                     | 현재 로그인된 사용자의 마이페이지 정보 조회           |
|  | PUT    | `/users/mypage/profile`             | 사용자 이름(프로필 정보) 변경                  |
|  | PUT    | `/users/mypage/profile-image`       | 프로필 이미지 수정 (`multipart/form-data`) |
|  | DELETE | `/users/mypage/profile-image/reset` | 프로필 이미지를 기본 이미지로 초기화               |




</details>
<div id="4"></div>

## 실행 화면

| 메인화면                     | 게시글 목록 + 검색 필터 화면              |
|:--------------------------:|--------------------------------|
| <img src = "https://github.com/user-attachments/assets/98108697-6024-40df-a0f1-c22f198e419d" width="500" /> | <img src = "https://github.com/user-attachments/assets/7707cf20-6137-4d5b-89ba-cf8a45adc099" width="500" /> | 

| 게시글 작성 화면                    | 게시글 상세 조회 화면                    |
|------------------------------|---------------------------------|
| <img src = "https://github.com/user-attachments/assets/d261ded2-cbd0-4904-a346-9d34801504d0" width="500" /> | <img src = "https://github.com/user-attachments/assets/69ce1f27-101d-49f7-a2e7-d9b27164dad5" width="500" /> | 

| 반성문 작성 화면                    | 관리자 반성문 승인 화면                  |
|------------------------------|--------------------------------|
| <img src = "https://github.com/user-attachments/assets/7e9def2c-68c0-42d5-93d6-22a14990752d" width="500" /> | <img src = "https://github.com/user-attachments/assets/b0a4d149-3022-4057-824e-48691f6141dd" width="500" /> | 

| 마이페이지                       | 신고 등록 화면                       |
|-----------------------------|--------------------------------|
| <img src = "https://github.com/user-attachments/assets/03a81537-95ef-4a3d-a4a8-a0af42de9014" width="500" /> | <img src = "https://github.com/user-attachments/assets/3afdc367-922b-463a-9f72-321713acfad1" width="500" /> | 

<div id="5"></div>

## 프로젝트 구성도

- SW 아키텍처

![스크린샷 2025-05-16 오전 10 20 17](https://github.com/user-attachments/assets/065f5b58-0a4e-410b-96ee-ff5147f377ae)

- ERD

![스크린샷 2025-05-16 오전 10 22 19](https://github.com/user-attachments/assets/132ed29c-e900-4899-9ad1-1c3f451fa4fe)




<div id="6"></div>

## 개발 팀 소개
<table>
  <tr>
    <td align="center">
      <a href="https://github.com/Jang-GO">
        <img src="https://avatars.githubusercontent.com/u/88973153?v=4" alt="장현서 프로필" width = "200"/>
      </a>
    </td>
    <td align="center">
      <a href="https://github.com/lbk00">
        <img src="https://avatars.githubusercontent.com/u/99525751?v=4" alt="이본규 프로필" width = "200"/>
      </a>
    </td>
  </tr>
  <tr>
    <td align="center">
      <a href="https://github.com/Jang-GO">
        장현서<br />
      </a>
    </td>
    <td align="center">
      <a href="https://github.com/lbk00">
        이본규<br />
      </a>
    </td>
  </tr>
</table>

|  이름  |   역할    | <div align="center">개발 내용</div>                                                                                                                                                                                                              |
| :----: | :-------: | :----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| 장현서 |  Frontend, Backend  | - Post/Comment/Admin 도메인 구현<br />- GCS 이미지 업로드 구현<br />- 좋아요 동시성 해결<br />- UI 설계                                                                                  |
| 이본규 |  Frontend, Backend  | - User/Like/Report 도메인 구현<br />- Spring Security <br />- 조회수 기능(캐시메모리)<br />- UI 설계                                                                                             |

<div id="7"></div>

## 개발 기간 및 일정

- 2025.05.08 ~2025.05.16(1주)
