# MSA로 구현한 헬스용품 사이트
## 💻Function
1. 로그인/회원가입
2. 상품등록
3. 주문

- - - -
## 💡Role
> 1. JWT 토큰을 이용한 로그인, 회원가입 구현  
> 2. 용도(삽입,삭제,수정,조회)에 따라 Database 분리  
> 3. Docker를 사용해 어떤 환경에서든 운영   
> 4. Kafka를 사용해 데이터 처리.
- - - -
## 🖥Tech Stack
사진

1. Spring boot: Spring Cloud의 프로젝트를 이용하여 MSA 구축을 위하기 위해서 Spring boot를 선택했다.

2. Github: 1명의 팀원과 프로젝트의 변경사항을 관리하고, 프로젝트 병합을 위해 Github을 사용했다. 서로 맡은 모듈(기능)을 구현하고 완벽하게 동작할 경우에 브랜치(모듈명으로 작성)로 올리기로 약속.

3. Docker: 각 모듈들을 각자의 로컬환경에서 개발하고 AWS에서 전체 모듈이 운영되도록 개발하기 위해 도커를 사용했다. 기존에 이미지가 있을 경우는 Docker Hub의 이미지를 사용하고, 이미지를 없을 경우 직접 도커 이미지를 만들었다.

4. Kafka: kafka Connect를 이용해서 데이터 처리를 하기위함이다.

- - - -
## 📁library list
1. Spring Cloud Gateway: 인증 및 라우팅을 위한 API Gateway

2. netflix Eureka: 각 마이크로 서비스를 naming으로 관리하기 위한 service discovery

3. feign Client: 각 마이크로서비스간 통신을 위해

4. Spring cloud Config, Spring cloud Bus: 각 설정정보를 중앙화 및 암호화하고 설정정보가 바뀔시 무중단 빌드

5. JPA: SQL 중심적 개발의 문제점을 해결하고 객체 중심으로 개발하기 위해

6. bcrypt: 비밀번호 암호화
- - - -
## ⚙️시스템 구성도

시스템 구성도 작성해서 넣기

- 마이크로 서비스를 naming으로 관리하고, 각 인스턴스는 eureka server에 등록한다.

- Spring Cloud Gateway를 이용하여 각 서비스간 통신과 라우팅/인증을 처리한다.


- - - -
## 📚요구사항 분석
 [요구사항 분석하고 파일 첨부하기]
- - - -
## ✏️API 설계

[설계문서 작성]

- - - -
## 📌ERD

[erd 작성]
