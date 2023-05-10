# Title: [3Week] 권혜영

### 미션 요구사항 분석 & 체크리스트

---

- [x] [필수 미션] 내가 받은 호감리스트(/usr/likeablePerson/toList)에서 성별 필터링기능 구현
  - [x] 파라미터로 값 받아오기
  - [x] 넘어오는 파라미터로 필터링 할 수 있는 listingToCondition() 메소드 구현
  - [x] 넘어오는 gender에 맞게 필터링 
- [x] [필수 미션] 네이버클라우드플랫폼을 통한 배포, 도메인, HTTPS 까지 적용

- [ ] [선택 미션] 젠킨스를 통해서 리포지터리의 main 브랜치에 커밋 이벤트가 발생하면 자동으로 배포가 진행되도록
- [x] [선택 미션] 내가 받은 호감리스트(/usr/likeablePerson/toList)에서 호감사유 필터링기능 구현
  - [x] 넘어오는 attractiveTypeCode에 맞게 필터링
- [ ] [선택 미션] 내가 받은 호감리스트(/usr/likeablePerson/toList)에서 정렬기능

### 3주차 미션 요약

---

### [필수 미션] 내가 받은 호감리스트(/usr/likeablePerson/toList)에서 성별 필터링기능 구현

**[접근 방법]**

- likeablePersonService에 listingToCondition()이라는 메소드를 만들어 LikeablePersonController에서 호출 하도록 하였다.
    - InstaMember, gender, attractiveTypeCode, sortCode를 파라미터로 받아온다
    - gender와 attractiveTypeCode가 비어있을 경우 전체 호감 목록을 보여준다
    - gender 코드가 넘어왔을 경우 성별에 맞게 필터링한 후 리스트를 돌려준다


### [필수 미션] 네이버클라우드플랫폼을 통한 배포, 도메인, HTTPS 까지 적용

**[접근 방법]**

- 강사님이 알려주신 배포 가이드를 따라했다! `502 bad gateway openresty` 에러가 계속 떠서 이것저것 구글링 해봤는데,
  딱히 같은 상황도 없었고 해결 방법도 없었다. `docker logs`, `nslookup` 등 다양한 명령어를 사용하며 상태를 확인했다.
  컨테이너도 잘 떠있고 이것저것 모두 다 실행이 되고 있는 상태어서 난감했다. nginx proxy manager를 살펴보니 proxy host의
destination 주소에 띄어쓰기가 들어가 있었던 것이 오류의 원인이었다. 😥

### [선택 미션] 내가 받은 호감리스트(/usr/likeablePerson/toList)에서 호감사유 필터링기능 구현

**[접근 방법]**

- 위와 동일하게 listingToCondition() 메소드에 if 문을 추가하여 넘어오는 attractiveTypeCode로 필터링하여
동작하도록 만들었다. 
  - 현재 attractiveTypeCode를 String으로 받아오는데 int로 변경시 오류가 난다. 왜 그런지 찾아봐야겠다🤔
  - Integer.parseInt 역시 오류가 난다! 왜그럴까나

### 알게된 점 🤔
- `isEmpty()`
    - Java 6 이후 사용 가능
    - 공백의 length를 가지고 0이면 true 아니면 false 반환
    - 공백에 띄어쓰기가 들어가면 false -> 즉, 문자열의 길이가 0인 경우에 true
- `isBlank()`
  - Java 11 이후 사용 가능
  - 공백을 제외한 텍스트가 ""라면 true 아니면 false
  - 즉, 문자열이 비어 있거나, 공백으로만 이루어져 있으면 true
- 띄어쓰기를 주의하자! 특히 설정 시에 들어가는 띄어쓰기가 정말 치명적이고 찾기 힘들다는 사실을 이번에 알게 되었다:(

### 리팩토링 시

- 생각보다 배포에 시간을 많이 썼다😥
