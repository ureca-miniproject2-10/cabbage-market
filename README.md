<div align="center">
  <img src="/readme_assets/cm_banner.png" alt="CABBAGE MARKET" />
  <h1>배추마켓 (CABBAGE-MARKET)</h1>
</div>

## 목차

1. [**프로젝트 소개**](#1)
2. [**기술 스택**](#2)
3. [**주요 기능**](#3)
4. [**프로젝트 구성도**](#4)
5. [**개발 팀 소개**](#5)
6. [**개발 기간 및 일정**](#6)
7. [**실행 방법**](#7)

<div id="1"></div>

## 프로젝트 소개
사용자가 중고 물품을 등록하고, 게시글을 통해 거래할 수 있는 
**중고거래 특성을 반영한 게시판 기반 웹 플랫폼**

<div id="2"></div>

## 기술 스택


<div id="3"></div>

## 주요 기능

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
| User   | GET    | `/users`      | 전체 사용자 목록 조회 |
|    | GET    | `/users/{id}` | 특정 사용자 상세 조회 |
|   | PUT    | `/users/{id}` | 사용자 정보 수정    |
|    | DELETE | `/users/{id}` | 사용자 삭제       |
| Auth   | POST | `/users/register` | 사용자 회원가입          |
|   | POST | `/users/logout`   | 로그아웃 (세션 무효화)     |
|   | GET  | `/users/me`       | 현재 로그인된 사용자 정보 조회 |
| MyPage | GET    | `/users/mypage`                     | 현재 로그인된 사용자의 마이페이지 정보 조회           |
|  | PUT    | `/users/mypage/profile`             | 사용자 이름(프로필 정보) 변경                  |
|  | PUT    | `/users/mypage/profile-image`       | 프로필 이미지 수정 (`multipart/form-data`) |
|  | DELETE | `/users/mypage/profile-image/reset` | 프로필 이미지를 기본 이미지로 초기화               |




</details>
<div id="4"></div>

## 프로젝트 구성도

<div id="5"></div>

- SW 아키텍처



- ERD

## 개발 팀 소개

<div id="6"></div>

- 현서: Post/Comment/Admin 도메인 구현, GCS 이미지 업로드 구현, UI 설계

- 본규: User/Like/Report 도메인 구현, Spring Security, 조회수 기능(캐시메모리), UI 설계

## 개발 기간 및 일정

<div id="7"></div>

- 2025.05.08 ~2025.05.16

## 실행 방법

