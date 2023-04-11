package com.ll.gramgram.boundedContext.likeablePerson.service;

import com.ll.gramgram.base.rsData.RsData;
import com.ll.gramgram.boundedContext.instaMember.entity.InstaMember;
import com.ll.gramgram.boundedContext.instaMember.service.InstaMemberService;
import com.ll.gramgram.boundedContext.likeablePerson.entity.LikeablePerson;
import com.ll.gramgram.boundedContext.likeablePerson.repository.LikeablePersonRepository;
import com.ll.gramgram.boundedContext.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LikeablePersonService {
    private final LikeablePersonRepository likeablePersonRepository;
    private final InstaMemberService instaMemberService;

    public RsData checkLikePermission (String username, int attractiveTypeCode){
        if(findByToInstaMemberUsername(username).isPresent() && findByToInstaMemberUsername(username).orElse(null).getAttractiveTypeCode() == attractiveTypeCode){
            return RsData.of("F-1", "이미 등록된 상대입니다.");
        }
        return RsData.of("S-1", "호감 상대로 등록 가능합니다.");
    }

    @Transactional
    public RsData<LikeablePerson> like(Member member, String username, int attractiveTypeCode) {
        if ( member.hasConnectedInstaMember() == false ) {
            return RsData.of("F-2", "먼저 본인의 인스타그램 아이디를 입력해야 합니다.");
        }

        if (member.getInstaMember().getUsername().equals(username)) {
            return RsData.of("F-1", "본인을 호감상대로 등록할 수 없습니다.");
        }

        InstaMember fromInstaMember = member.getInstaMember();
        InstaMember toInstaMember = instaMemberService.findByUsernameOrCreate(username).getData();
        Optional<LikeablePerson> findLikeablePerson = findByToInstaMemberUsername(username);

        if(findLikeablePerson.isPresent() && findLikeablePerson.orElse(null).getAttractiveTypeCode() == attractiveTypeCode){
            return RsData.of("F-1", "이미 등록된 상대입니다.");
        }else if(findLikeablePerson.isPresent() && findLikeablePerson.orElse(null).getAttractiveTypeCode() != attractiveTypeCode){
            LikeablePerson likeablePerson = findLikeablePerson.get();
            likeablePerson.updateAttractiveTypeCode(attractiveTypeCode);
            return RsData.of("S-2",  "입력하신 인스타유저(%s)의 매력이 수정되었습니다.".formatted(username));
        }

        LikeablePerson likeablePerson = LikeablePerson
                .builder()
                .fromInstaMember(fromInstaMember) // 호감을 표시하는 사람의 인스타 멤버
                .fromInstaMemberUsername(member.getInstaMember().getUsername()) // 중요하지 않음
                .toInstaMember(toInstaMember) // 호감을 받는 사람의 인스타 멤버
                .toInstaMemberUsername(toInstaMember.getUsername()) // 중요하지 않음
                .attractiveTypeCode(attractiveTypeCode) // 1=외모, 2=능력, 3=성격
                .build();

        System.out.println(likeablePerson.getAttractiveTypeCode());

        likeablePersonRepository.save(likeablePerson); // 저장

        // 너가 좋아하는 호감표시 생겼어.
        fromInstaMember.addFromLikeablePerson(likeablePerson);

        // 너를 좋아하는 호감표시 생겼어.
        toInstaMember.addToLikeablePerson(likeablePerson);

        return RsData.of("S-1", "입력하신 인스타유저(%s)를 호감상대로 등록되었습니다.".formatted(username), likeablePerson);
    }

    @Transactional
    public RsData delete(LikeablePerson likeablePerson){
        String toInstaMemberUsername = likeablePerson.getToInstaMember().getUsername();
        likeablePersonRepository.delete(likeablePerson);

        return RsData.of("S-1", "%s님에 대한 호감을 취소하셨습니다.".formatted(toInstaMemberUsername));
    }

    public RsData checkDeletePermission(Member user, LikeablePerson likeablePerson){
        if (likeablePerson == null) return RsData.of("F-1", "이미 삭제되었습니다.");

        if (!Objects.equals(user.getInstaMember().getId(), likeablePerson.getFromInstaMember().getId()))
            return RsData.of("F-2", "권한이 없습니다.");

        return RsData.of("S-1", "삭제가능합니다.");
    }

    public List<LikeablePerson> findByFromInstaMemberId(Long fromInstaMemberId) {
        return likeablePersonRepository.findByFromInstaMemberId(fromInstaMemberId);
    }

    public Optional<LikeablePerson> findById(Long id){
        return likeablePersonRepository.findById(id);
    }

    public Optional<LikeablePerson> findByToInstaMemberUsername(String name){
        return likeablePersonRepository.findByToInstaMemberUsername(name);
    }

    public RsData countLike(List<LikeablePerson> fromLikeablePeople) {
        if(fromLikeablePeople.size() > 10)
            return RsData.of("F-2", "더 이상 등록할 수 없습니다.");

        return RsData.of("S-1", "추가 가능합니다.");
    }



}
