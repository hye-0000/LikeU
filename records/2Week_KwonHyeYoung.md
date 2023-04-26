# Title: [2Week] 권혜영

### 미션 요구사항 분석 & 체크리스트

---

- [x] [필수 미션] 호감 표시 할 때 예외케이스 3가지 처리하기
    - [x] 케이스 4: 한 명의 인스타 회원이 다른 인스타회원에게 중복으로 호감표시 불가
    - [x] 케이스 5: 한 명이 11명 이상의 호감상대 등록 불가
    - [x] 케이스 6: 케이스 4 발생 시 호감 사유가 다르다면 다른 이유로 업데이트 되도록


- [  [선택 미션] 네이버 로그인
    - [ ] 네이버 계정으로 로그인이 가능하게 만들기

### 2주차 미션 요약

---

### [필수 미션] 호감 표시 할 때 예외케이스 3가지 처리하기

* 편의를 위해 현재 로그인 한 회원을 '나/내가'로 지칭함

**[접근 방법]**

### **케이스 4 ❗**

- 목적: 한 명의 인스타 회원이 다른 인스타회원에게 중복으로 호감표시 불가하도록
    - 중복의 의미 : 동일한 다른 회원에게 동일한 사유로 호감 표시
- 구현을 위해 내가 생각한 로직
    1. 현재 로그인 한 유저가 호감을 누른 모든 리스트를 받아옴
    2. 호감을 보내고자 하는 유저가 내가 호감을 보낸 회원인지 확인함(findLikeablePerson 메소드를 통해 id를 비교함)
    3. 이미 리스트에 존재하는 회원이고 attractiveTypeCode도 같다면
    4. 동일한 회원을 동일 사유로 호감 표시 했으므로 이미 등록된 상대라는 에러 문구 표출
- 결과 : 동일 회원이 등록 불가하다는 에러 문구가 표출됨

### **케이스 5 ❗**

- 목적: 한 명이 11명 이상의 호감상대 등록 불가
- 구현을 위해 내가 생각한 로직
    1. 내가 호감을 보낸 회원의 리스트를 받아옴
    2. size() 메소드를 통해 크기 체크
    3. 10보다 크다면 더 이상 추가 되지 않도록 함
- 결과: 이미 10명의 회원이 등록 되어 있을 경우 11번째 회원부터는 등록 되지 않음

### **케이스 6 ❗**

- 목적: 케이스 4 발생 시 호감 사유가 다르다면 다른 이유로 업데이트 되도록
- 구현을 위해 내가 생각한 로직
    1. 케이스 4에서 확인 된 호감을 보낸 회원인지를 활용해
    2. 호감을 받은 회원이 맞고 이유가 다르다면
    3. entity에 추가한 updateAttractiveTypeCode 메소드를 통해 업데이트 되도록 함
- 결과 : 같은 상대이지만 사유가 다르다면 사유가 업데이트 됨

### [선택 미션] 네이버 로그인

네이버 역시 '네이버 클라이언트 아이디'와 '네이버 클라이언트 시크릿키'를 사용해 로그인을 구현하였다. 네이버는
독특하게 response가 json 형태로 넘어오기 때문에 CustomOAuth2UserService에서 메소드를 추가해 id 값만 가져올 수
있도록 구현해보았다.

**[특이사항]**

#### 알게된 점 🤔

1. @Dynamicupdate

```mysql
update likeable_person
set
    attractive_type_code=?,
    create_date=?,
    from_insta_member_id=?,
    from_insta_member_username=?,
    modify_date=?,
    to_insta_member_id=?,
    to_insta_member_username=?
where
    id=?
```

```mysql
update likeable_person
set
    attractive_type_code=?,
    modify_date=?
where
    id=?
```

엔티티에서 메소드를 사용해 업데이트 하는 방식을 사용해보았는데, 처음에는 내가 수정한 부분 외에도 모든 column이 수정되었다.
원하는 부분만 update가 일어나길 원해서 찾아봤더니 `@Dynamicupdate`를 엔티티에 사용하면 변경된 column만 수정이 되긴 하지만 이를 위해
Hibernate가 현재 엔티티의 상태를 추적해야 한다는 단점이 있다. 즉 해당 어노테이션과 관련해 성능 오버헤드가 일어날 수 있다. <br>
엔티티가 많은 열이 있는 테이블을 나타내고 이러한 열 중 몇 개만 자주 업데이트 해야 하는 경우, 혹은 버전이 없는 optimistic locking을
사용해야 할 경우 해당 어노테이션을 사용하는 것이 좋다고 한다.

2. 네이버 소셜 로그인 시

```
redirect-uri: http://localhost:8080/login/oauth2/code/naver
```

application.yml 파일에 위 내용을 추가를 하지 않았었는데 계속
`Error creating bean with name 'org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration$EnableWebMvcConfiguration'`
이런 오류가 출력 됐었다.

### 리팩토링 시

- LikeablePersonService의 like 메소드에서 너무 많은 처리 과정이 일어나고 있음
- 메소드명 잘 지어서 분리해보기