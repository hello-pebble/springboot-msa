1. 도메인 분석 및 서비스 분리 전략
* User Service (사용자/프로필): 회원가입, 로그인, 프로필 관리(user, profile).
* Post Service (게시글/미디어): 게시글 생성, 조회, 수정, 삭제 및 미디어 관리(post, reply, repost, quote, media).
* Follow Service (팔로우): 사용자 간 팔로우 관계 및 팔로우 수 관리(follow).
* Interaction Service (활동): 좋아요 등 게시글에 대한 사용자 반응(like).
* Timeline Service (타임라인): 팔로잉 기반 타임라인 생성 및 제공(timeline).

2. 인프라 및 공통 컴포넌트 구성
* API Gateway: 모든 서비스의 단일 진입점 (Spring Cloud Gateway).
* Service Discovery: 서비스 위치 투명성 확보 (Netflix Eureka 또는 Consul).
* Config Server: 환경 설정 중앙 관리 (Spring Cloud Config).
* Message Broker: 서비스 간 비동기 통신 및 데이터 동기화 (Kafka 또는 RabbitMQ).
    * 예: 게시글 작성 시 타임라인 서비스에 이벤트 전송, 팔로우 발생 시 타임라인 갱신 등.
* Distributed Tracing: 서비스 간 호출 추적 (Zipkin 또는 Sleuth/Micrometer).

3. 기술적 변경 사항 및 고려 사항
* 인증/인가: 현재의 세션 기반 인증에서 JWT(JSON Web Token) 기반의 상태가 없는(Stateless) 인증 방식으로 전환이 필요합니다. API Gateway에서 토큰을 검증하거나 공통 인증 모듈을 사용합니다.
* 데이터베이스: 서비스별로 독립된 데이터베이스를 가집니다 (Database per Service). 초기에는 스키마 분리부터 시작하여 점진적으로 물리적 데이터베이스를 분리할 수 있습니다.
* 데이터 일관성: 분산 트랜잭션 대신 Saga 패턴이나 이벤트 기반 아키텍처(EDA)를 통해 결과적 일관성(Eventual Consistency)을 유지합니다.
* 통신 방식:
    * Synchronous: Feign Client 또는 RestTemplate을 통한 내부 REST 호출.
    * Asynchronous: 메시지 큐를 통한 이벤트 전파.

4. 단계별 전환 로드맵
1. 1단계 (준비): 공통 모듈(Core, Common)을 추출하고 인증 방식을 JWT로 변경합니다.
2. 2단계 (인프라 구축): API Gateway와 Service Discovery를 설정합니다.
3. 3단계 (서비스 추출): 가장 의존성이 적은 Follow Service나 Interaction Service부터 하나씩 별도의 스프링 부트 애플리케이션으로 분리합니다.
4. 4단계 (핵심 서비스 분리): User와 Post 서비스를 분리하고, 메시지 브로커를 도입하여 비동기 로직을 처리합니다.
5. 5단계 (최적화): 타임라인 조회 등 읽기 부하가 많은 기능을 위해 CQRS 패턴 도입을 검토합니다.


진행중
1. 인증 서비스 (auth-service) : "문지기 (Gatekeeper)"
   인증 서비스는 "이 사람이 우리 사용자가 맞는가?"와 "어떤 권한을 가지고 있는가?"에만 집중합니다.

* 로그인 처리: 아이디/암호를 검증하거나 소셜 로그인(구글 등)의 결과를 확인합니다.
* 토큰 발급 (JWT): 로그인이 성공하면 Access Token과 Refresh Token을 생성하여 클라이언트에 전달합니다.
* 로그아웃 처리: 발급된 토큰을 무효화하거나 세션을 종료합니다.
* 토큰 검증/갱신: 유효기간이 만료된 토큰을 갱신해 주거나, 게이트웨이에서 토큰의 위변조 여부를 확인할 때 사용됩니다.
* 권한 부여 (Authorization): 이 유저가 'ADMIN'인지 'USER'인지 판별합니다.


2. 사용자 서비스 (user-service) : "정보 보관소 (Resource Keeper)"
   사용자 서비스는 "사용자의 개인정보(프로필)"와 "시스템 내 유저의 상태"를 관리하는 데이터의 주인입니다.

* 회원가입: 새로운 유저 데이터를 DB에 저장하고 초기 프로필을 생성합니다.
* 프로필 관리: 이름(DisplayName), 자기소개(Bio), 프로필 이미지 ID 등을 수정하고 보관합니다.
* 사용자 검색/조회: 유저 ID로 유저의 정보를 찾거나, 닉네임으로 검색하는 기능을 제공합니다. (다른 서비스에서 "이 게시글 작성자 이름이 뭐야?"라고 물을 때 대답해 주는 곳입니다.)
* 유저 상태 관리: 탈퇴 처리(Soft Delete), 계정 정지, 이메일 인증 여부 등을 관리합니다.
* 설정 관리: 알림 설정, 다크모드 여부 등 사용자별 환경설정을 저장합니다.