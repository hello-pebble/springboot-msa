### Spring-Msa Project
#### 1. core-service - SNS 서비스 ( 분리 시작점 )
#### 1-1. user-service - 사용자 서비스 Core: 사용자 프로필 수정, 팔로우/언팔로우 비즈니스 로직.
#### 1-2. auth-service - 인증 서비스 Core: 토큰 생성(JWT), 비밀번호 검증, 권한 체크.
#### 2. notification-service - 알림 서비스


쪼개기 순서
1   Domain	        User	                규칙 
2	Port (Out)	    SaveUserPort	        저장 장치가 필요하다는 선언 (슬롯)
                    LoadUserPort            불러오기 선언
3	Service	        UserService	            게임 진행자 (규칙에 따라 흐름 제어)
4	Adapter (Out)	UserPersistenceAdapter	실제 저장
5	Adapter (In)	UserController	        컨트롤러 (입력 도구)



#### user-service
com.pebble.user
├── adapter
│   ├── in.web                  # 컨트롤러 (REST API)
│   │   └── UserController.java
│   └── out.persistence         # DB 어댑터 (JPA 구현체)
│       ├── UserPersistenceAdapter.java
│       └── UserJpaRepository.java
├── application
│   ├── port
│   │   ├── in                  # 유스케이스 (입력 포트)
│   │   │   ├── RegisterUserUseCase.java
│   │   │   └── UpdateProfileUseCase.java
│   │   └── out                 # 외부통신 (출력 포트)
│   │       └── LoadUserPort.java
│   │       └── SaveUserPort.java
│   └── service                 # 비즈니스 로직 실행 (포트 구현)
│       └── UserService.java
└── domain                      # 순수 비즈니스 객체
└── User.java