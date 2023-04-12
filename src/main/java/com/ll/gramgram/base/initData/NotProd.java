package com.ll.gramgram.base.initData;

import com.ll.gramgram.boundedContext.instaMember.service.InstaMemberService;
import com.ll.gramgram.boundedContext.likeablePerson.service.LikeablePersonService;
import com.ll.gramgram.boundedContext.member.entity.Member;
import com.ll.gramgram.boundedContext.member.service.MemberService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile({"dev", "test"})
public class NotProd {
    @Bean
    CommandLineRunner initData(
            MemberService memberService,
            InstaMemberService instaMemberService,
            LikeablePersonService likeablePersonService
    ) {
        return args -> {
            Member memberAdmin = memberService.join("admin", "1234").getData();
            Member memberUser1 = memberService.join("user1", "1234").getData();
            Member memberUser2 = memberService.join("user2", "1234").getData();
            Member memberUser3 = memberService.join("user3", "1234").getData();
            Member memberUser4 = memberService.join("user4", "1234").getData();

            Member memberUser5ByKakao = memberService.whenSocialLogin("KAKAO", "KAKAO__2733149404").getData();
            Member memberUser6ByGoogle= memberService.whenSocialLogin("KAKAO", "GOOGLE__115114450631300606169").getData();

            instaMemberService.connect(memberUser2, "insta_user2", "M");
            instaMemberService.connect(memberUser3, "insta_user3", "W");
            instaMemberService.connect(memberUser4, "insta_user4", "M");
            instaMemberService.connect(memberUser5ByKakao, "master", "M");

            likeablePersonService.like(memberUser3, "insta_user4", 1);
            likeablePersonService.like(memberUser3, "insta_user100", 2);
            likeablePersonService.like(memberUser5ByKakao, "111111111111", 1);
            likeablePersonService.like(memberUser5ByKakao, "22222222222222", 1);
            likeablePersonService.like(memberUser5ByKakao, "333333333333", 1);
            likeablePersonService.like(memberUser5ByKakao, "444444444", 1);
            likeablePersonService.like(memberUser5ByKakao, "55555555555", 1);
            likeablePersonService.like(memberUser5ByKakao, "66666666666", 1);
            likeablePersonService.like(memberUser5ByKakao, "7777777777", 1);
            likeablePersonService.like(memberUser5ByKakao, "88888888888", 1);
            likeablePersonService.like(memberUser5ByKakao, "9999999999", 1);
        };
    }
}
