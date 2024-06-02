# 24_1_SW_Engineering_Proj

소프트웨어공학 01분반 Term Project 10조 Github 페이지입니다.

## 멤버

- 20200009 김동영
- 20221809 김연곤
- 20201876 나상현
- 20193645 이기웅
- 20203749 조세영

## 프로젝트 구조

본 프로젝트는 Spring Boot 프로젝트로 이루어져 있으며, web_view 폴더와 python_cli 폴더는 각각 Web 인터페이스와 CLI 인터페이스 프로그램을 담고 있는 폴더입니다.

메인 시스템인 Spring Boot 프로젝트 코드는 `src/main/java/cau/se/issuemanagespring` 폴더 안에 위치하고 있으며 JUnit 테스트 코드는 `src\test\java\cau\se\issuemanagespring` 폴더 안에 위치하고 있습니다.

## 실행 방법

### 스프링 프로젝트 (메인 시스템)

Intellij IDEA로 프로젝트를 열고 `src/main/java/cau/se/issuemanagespring/IssueManageSpringApplication.java`를 실행하는 것을 권장합니다.

초기 DB 파일을 제공합니다. DB 파일의 이름은 `data.mv.db`이며 기본적인 데이터들이 포함되어 있습니다. 스프링 부트를 실행하면 해당 DB 파일을 읽고 쓰며, 프로젝트를 껐다 켜도 데이터들은 DB 파일에 그대로 남아있습니다. 초기 DB 파일의 내용은 `src/main/resources/data.sql` 파일에 기술되어 있습니다. 계정 이름과 비밀번호를 여기에서 확인 가능합니다.

만약 예기치 못하게 DB 파일이 삭제되거나 사용할 수 없게 된다면, `src/main/resources/application.properties` 파일에서 need to inactivate after first running 라고 주석처리되어 있는 부분의 바로 아래 코드의 주석을 해제하여 프로그램이 실행될 때 자동으로 초기 DB 파일을 생성하게 만들 수 있습니다. 다만 설정 파일의 코드 주석을 해제한 이후 프로그램을 껐다 다시 켤 때는 해당 코드를 다시 주석처리해야 합니다. 주석처리 이후에는 몇번이든 프로그램을 껐다 켜도 정상적으로 실행되고 데이터도 그대로 남아있습니다.

빌드된 `issue-manage-spring-0.0.1-SNAPSHOT.jar` 파일을 실행시킬 수도 있습니다. 다만 빌드 파일을 실행하게 되면 위에서 설명한 초기 DB 파일 복구 기능을 사용하지 못합니다.

### Web 인터페이스

next.js 프레임워크를 사용했습니다. 최신 node와 npm 버전을 요구합니다.

`cd web_view`로 web_view 폴더에 접근하여 `npm install`로 프로그램에 필요한 패키지를 모두 다운받습니다.

`npm run dev`로 웹 애플리케이션을 실행합니다.

### CLI 인터페이스

python을 사용했습니다.

`cd python_cli`로 python_cli 폴더에 접근하여 `python main.py`로 프로그램을 실행합니다.
