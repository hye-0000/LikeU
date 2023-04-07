## Title: [1Week] 권혜영

### 미션 요구사항 분석 & 체크리스트

---

- [x] [필수 미션] 호감 표시 삭제
    - [x] 유효성 검사(삭제 하려는 호감 표시의 주인과 현재 로그인 한 사람이 같은 사람인지)
    - [x] 삭제
    - [x] 호감 목록 페이지로 돌아오기


- [x] [선택 미션] 구글 로그인
    - [x] 구글 계정으로 로그인이 가능하게 만들기


### 1주차 미션 요약

---
### [필수 미션] 호감 표시 삭제

**[접근 방법]**
#### 유효성 검사 ❗

- 목적: 현재 내가 삭제하고자 하는 호감을 삭제할 권한이 있는지 확인
    - 호감을 삭제 할 수 있는 권한의 의미: 현재 로그인한 객체가 보낸 호감이 맞는지


- 구현을 위해 내가 생각한 로직
    1. 현재 로그인 한 객체의 id를 가져옴
    2. 삭제하고자 하는 호감 객체에서 from_insta_member_id 값을 가져옴
    3. 위 두 id값이 동일하다면 호감을 삭제할 권한이 있다고 판단

- 결과 : 호감을 삭제할 권한이 있다면 delete가 실행됨


#### 호감 삭제 & 호감 목록 페이지로 리다이렉팅

- 목적: 권한이 확인 된 호감에 대한 삭제 실행 후 목록으로 리다이렉팅


- 구현을 위해 내가 생각한 로직
    1. 삭제 버튼 클릭 시 삭제하고자 하는 호감의 id가 넘어옴
    2. 해당 id 값을 통해 삭제하고자 하는 객체를 선택
    3. Repository에서 제공하는 delete() 메소드를 통해 2에서 선택한 객체 삭제
    4. 삭제 후 rq.redirectWithMsg를 통해 결과를 반환하고 호감 리스트 페이지로 리다이렉팅 해줌


- 결과: 실제로 호감이 화면/DB에서 모두 삭제되고 호감 리스트 화면으로 돌아와짐


### [선택 미션] 구글 로그인

**[접근 방법]**

- 목적: 소셜 로그인 중 구글 로그인 기능 연동하기

https://console.cloud.google.com/ 를 활용 해 프로잭트를 새로 생성하고 사용자 인증을 추가했다.<br>
📕[Reference] https://tweety1121.tistory.com/194




**[특이사항]**

#### 알게된 점 🤔
1. 호감 삭제 권한에 대한 객체를 확인 할 때
```java
Object check = likeablePersonService.findById(id);
System.out.println(check);
```
```shell
Optional[LikeablePerson(id=3, createDate=2023-04-05T15:24:52.414144, 
modifyDate=2023-04-05T15:24:52.414144, 
fromInstaMember=InstaMember(id=5, createDate=2023-04-05T15:24:40.723778, modifyDate=2023-04-05T15:24:40.723778, username=master, gender=W), 
fromInstaMemberUsername=master, toInstaMember=InstaMember(id=6, createDate=2023-04-05T15:24:52.409157, modifyDate=2023-04-05T15:24:52.409157, username=hye_0000_, gender=U), 
toInstaMemberUsername=hye_0000_, attractiveTypeCode=1)]
```
단순하게 값을 확인해보려고 object로 받아와서 작업을 진행해보려고 했었는데 타입이 맞지 않아 .get~()메소드를 통해 값을 가져올 수 없었다.<br>
위와 같이 객체와 관련된 모든 정보가 찍혀 나왔다. 이 후 InstaMember 객체로 타입을 맞춰주었더니 원하는 메소드들을 사용할 수 있었다. <br>
사용할 객체의 타입을 항상 잘 확인해야겠다는 생각을 하게되었다.

2. **어노테이션** 주의하기<br>
   LikeablePersonService의 진입 지점에 `@Transactional(readOnly = true)`가 걸려있는 것을 간과했다. <br>
   작성한 메소드 상 논리적인 오류가 없어 보였고 페이지 리다이렉팅도 성공적으로 되고 화면에서도 삭제가 되지만 DB에서 내용이 삭제가 되지 않았다.<br>
   코드만 들여다보고 있다가 도저히 이유를 모르겠어서 멘토님께 여쭤봤더니 코드상 오류는 없고 어노테이션에 관련된 힌트를 주셔서 바로 해결할 수 있었다.
```
readOnly = ture
  - 스프링 프레임워크가 하이버네이트 세션 플러시 모드를 MANUAL로 설정해 강제로 플러시를 호출하지 않는 한 플러시가 일어나지 않는다
  - 트랜잭션을 커밋하더라도 영속성 컨텍스트가 플러시 되지 않아 엔티티의 등록/수정/삭제가 동작하지 않는다
      - 등록/수정/삭제를 하기 위해서는 원하는 메소드 위에 @ Transactional을 붙여줘야 한다!
      - 이거 까먹어서 delete가 안되는 부분이 뭐가 문제일까 한동안 고민하고 있었다 😥
  - 읽기 전용이므로, 영속성 컨텍스트는 변경 감지를 위한 스냅샷을 보관하지 않으므로 성능이 향상된다
```

3. 권한 체크✅<br>
권한 체크를 어디서 하는 것이 맞는건지 아직도 정확히는 모르겠지만, 
인턴 나갔을 때는 컨트롤러 단에서 권한 체크가 일어나고 있다는걸 보여주는게 좋다고 하셨던 말씀이 기억나서 
컨트롤러 단에서 권한 체크를 하도록 코드를 작성했었다.<br>
하지만 아무래도 출력 메시지가 있다보니 컨트롤러 단에 너무 많은 내용이 들어 있는 것처럼 보여서 서비스로 빼봤더니
delete내에서 너무 많은 일이 일어나서 다시 컨트롤러단에 코드를 넣어뒀었다.<br>
코드 리뷰에도 컨트롤러단에서 메시지가 처리돼 통일성이 떨어져 보인다는 같은 고민을 남겨주셔서 고민하고 있던 찰나에
강사님이 올려주신 영상에 서비스단에서 그냥 메소드를 하나 만들어버려서 처리하는 것을 보고 같은 방식으로 리팩토링 했다.<br>
아직 상황이 생겼을 때 유연하게 대처하는 방법이 부족한 것 같다 😥

4. Literally Refactoring
```java
//controller
@PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") Integer id){
        InstaMember instaMember = rq.getMember().getInstaMember();  //현재 로그인 된 멤버
        LikeablePerson likeablePerson = likeablePersonService.findById(id).orElse(null); //넘어온  id로 삭제할 객체

        if(likeablePerson == null) return rq.historyBack("이미 취소된 호감입니다!");

        if(!Objects.equals(instaMember.getId(), likeablePerson.getFromInstaMember().getId())){
            return rq.redirectWithMsg("/likeablePerson/list", "해당 호감을 삭제할 권한이 없습니다.");
        }

        RsData deleteRs = likeablePersonService.delete(id);

        if(deleteRs.isFail()) return rq.historyBack(deleteRs);

        return rq.redirectWithMsg("/likeablePerson/list", deleteRs);
    }
```
```java
//service
@Transactional
    public RsData delete(Integer id){
        LikeablePerson likeablePerson = likeablePersonRepository.getReferenceById(id);
        String toInstaMemberUsername = likeablePerson.getToInstaMember().getUsername();
        likeablePersonRepository.delete(likeablePerson);

        return RsData.of("S-1", "%s님에 대한 호감을 취소하셨습니다.".formatted(toInstaMemberUsername));
    }
```
원래는 컨트롤러단에서 id 값만을 가지고 delete를 수행하게 만들었다. 그래서 서비스단에서 쿼리를 한번 덜 날려보겠다고
getReferenceById()라는 메소드를 사용해봤다. (해당 메소드 실행시 그 시점에서는 프록시객체의 값을 참조만 하고 .getName()같은
메소드가 실행 될 때 select 쿼리가 실행되고 프록시는 초기화 됨)<br>
그런데 코드를 계속 봤더니 컨트롤러단에서 이미 삭제하고자 하는 객체를 찾아두고 있어서 파라미터로 넘길때 그 객체 자체를 넘겨주면
해결이 되는 문제였던 것이다❗❗ 항상 코드 뭐 그거 더 들여다본다고 바뀌나..?뭐가 다시 보이나..? 싶었는데 처음으로 
뭔가를 본 경험을 해서 즐거웠다.