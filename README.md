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

## 실행 방법
### 스프링 프로젝트 (메인 시스템)
Intellij IDEA로 프로젝트를 열고 `src/main/java/cau/se/issuemanagespring/IssueManageSpringApplication.java`를 실행하는 것을 권장합니다.

빌드된 "" 파일을 실행시킬 수도 있습니다. 다만 빌드 파일을 실행하게 되면 아래 DB 설정에 어려움이 있습니다.

DB 파일은 포함되어 있지 않으며, 프로그램을 처음으로 실행하게 되면 `src/main/resources/data.sql`에 의해 자동으로 DB 파일이 생성되어 초기 설정됩니다. 프로그램 첫 실행 후 종료하고 다시 실행하기 위해서는 `src/main/resources/application.properties` 파일에서 need to inactivate after first running 라고 주석처리되어 있는 부분의 바로 아래 코드를 주석처리 시켜야 합니다. 주석처리 이후에는 별도의 조작 없이 프로그램을 껐다 켜도 데이터가 그대로 남아있습니다.

### Web 인터페이스
next.js 프레임워크를 사용했습니다. 최신 node와 npm 버전을 요구합니다.

`cd web_view`로 web_view 폴더에 접근하여 `npm install`로 프로그램에 필요한 패키지를 모두 다운받습니다.

`npm run dev`로 웹 애플리케이션을 실행합니다.

### CLI 인터페이스
python을 사용했습니다.

`cd python_cli`로 python_cli 폴더에 접근하여 `python main.py`로 프로그램을 실행합니다.