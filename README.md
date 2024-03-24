# 책 검색 앱

## 동작
화면은 검색(Search), 상세(Detail) 2개로 구성됩니다.

- 검색 화면
  - 스와이프 리프레시가 적용되어 있습니다.
  - `SearchState` 값에 따라 노출되는 UI가 달라집니다. 
  - 검색어 입력 후(SearchState - Empty, Result)
    - 검색이 완료되면 리스트가 노출됩니다.
    - 페이징이 가능합니다.
    - 검색 데이터 로딩 중에 새로운 검색어 입력이 불가능합니다.
    - 아이템 레이아웃 변경이 가능합니다.(Linear, Grid)
   
- 상세 화면
  - 검색 화면에서 선택한 아이템을 노출합니다.

예외 처리는 토스트, 메세지 노출로 처리하였습니다.


## 아키텍쳐
![아키텍쳐](/img/아키텍쳐.png)

Presentation(UI), Domain, Data 로 관심사를 분리한 클린 아키텍쳐 및 MVVM 구조를 적용하였습니다.
앱 사이즈가 크지 않은 만큼, 모듈을 나누기보다는 레이어, 관심사별로 패키지 분리하는 방법을 택하였습니다. 
- `feature`: UI 관련 코드 모음
- `domain`:  앱 내의 모든 곳에서 사용하는 데이터 구조, repository interface 포함
- `data`: Repository 관련 코드 모음
- `network`: 네트워크 관련 코드 모음

View에 노출해야하는 데이터는 ViewModel 내의 `UiState` 클래스 내에 묶어서 관리합니다.  
ViewModel -> View에 특정 이벤트를 전달해야 하는 경우 `effect` 를 이용합니다.  
View의 코드를 최대한 줄이고 ViewModel이 상태 관리 및 이벤트를 처리하도록 DataBinding을 최대한 이용하였으나, 애매한 경우에는 View에 관련 코드를 작성하였습니다.   


## 사용한 기술 및 라이브러리
- Coroutine, Flow
- Browser
- Hilt
- Retrofit, Gson
- Glide
