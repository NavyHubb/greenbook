# _Green Book_
## ✅ Subject
### 📚 도서 리뷰 아카이브 서비스
내가 읽은 책을 다른 사람들은 어떻게 읽었을까?   
책에 대한 서로의 감상을 공유하는 공간을 제공한다.

관심있는 도서를 검색하고, 해당 도서만의 아카이브를 생성하여 최초의 리뷰를 남겨보자.   
혹은 다른 사용자가 생성해놓은 아카이브에 방문하여 리뷰를 남긴다.

## ✅ Features
1. 회원 영역
   - 회원 가입 & 탈퇴
     - 사용자는 회원가입을 할 수 있다. 기본적으로 회원가입 시 사용자는 USER 권한을 부여받는다.
     - 회원 가입 시 이메일, 패스워드, 닉네임을 입력받는다.
     - 이메일과 닉네임은 unique 해야 하며 패스워드는 암호화하여 저장한다.
   - 로그 인 & 아웃
     - 회원 가입 시 입력한 이메일과 패스워드가 일치하면 로그인에 성공한다.
     - 로그인에 성공하면 JWT를 발급한다.
   - 스크랩 목록 조회
     - 로그인 한 사용자는 리뷰 스크랩 기능을 통해 등록한 스크랩 목록을 조회할 수 있다.
2. 도서
   - 아카이브 전체 목록 조회
     - 로그인하지 않은 사용자를 포함한 모든 사용자는 아카이브 전체 목록을 조회할 수 있다.
     - 아카이브는 기본적으로 최신순으로 정렬되며, 조회수가 많은 순으로도 정렬할 수 있다.
     - 전체 목록 조회 시 갯수가 많을 수 있으므로 paging 처리하여 결과를 반환한다.
   - 특정 아카이브 조회
     - 로그인하지 않은 사용자를 포함한 모든 사용자는 특정 아카이브를 조회할 수 있다.
     - 아카이브 제목(도서명), 저자, 출판사, ISBN, 소개, 이미지URL, 조회수, 리뷰 목록이 조회된다.
     - 조회 시 마다 조회수가 1만큼 증가한다.
     - 조회에 사용되는 검색 기능은 ElasticSearch를 사용한다.
   - 도서 정보 검색
     - 로그인한 사용자는 원하는 도서의 아카이브가 존재하지 않을 경우 새로이 도서 정보를 검색(Naver API 활용)할 수 있다.
     - Naver API로부터의 응답값을 객체화하여 검색결과 목록을 반환한다.
   - 아카이브 생성
     - 로그인한 사용자는 도서 정보 검색 결과 목록 중 자신이 원하는 도서를 선택하여 아카이브를 생성할 수 있다.
   - 아카이브에 담긴 도서 정보의 업데이트
     - 외부 API에서 제공하는 도서 정보가 갱신되는 경우에 대비하여 변동내용을 DB에도 반영한다.
     - 작업은 scheduler에 의해서 주기적으로 자동 실행된다.
3. 리뷰
   - 리뷰 작성
     - 로그인한 사용자는 아카이브에 리뷰를 작성할 수 있다.
   - 리뷰 삭제 
     - 로그인한 사용자는 자신이 작성한 리뷰를 삭제할 수 있다.
     - 삭제는 논리 삭제(soft delete)이다. 삭제되기 전까지 Review.deletedDate 컬럼은 null 값을 갖고, 삭제되면 삭제된 날짜 값이 등록된다.
   - 리뷰 스크랩
     - 로그인한 사용자는 특정 리뷰를 스크랩하여 자신만의 스크랩 목록에 리뷰를 등록할 수 있다.
     - 사용자가 스크랩하면 해당 리뷰의 스크랩 수가 1만큼 증가한다.
4. 관리자
   - 아카이브 삭제
     - 관리자 계정으로 로그인한 사용자는 아카이브를 삭제할 수 있다.
     - scheduler를 통해 리뷰를 하나도 갖고 있지 않은 아카이브는 자동으로 삭제된다.

## ✅ ERD
<img width="758" alt="Screenshot 2023-05-08 at 10 51 56 AM" src="https://user-images.githubusercontent.com/101810007/236750663-3f2f64ac-3890-437b-8f0e-7680973694b4.png">

## ✅ Tech Stack
- Java(11)
- Spring Boot(2.7.11), Spring MVC, Spring Data JPA
- MySQL
- JUnit5, Mockito
- Gradle
- IntelliJ, DataGrip
- Git, Sourcetree