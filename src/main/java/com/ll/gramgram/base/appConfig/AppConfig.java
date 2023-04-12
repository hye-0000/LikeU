package com.ll.gramgram.base.appConfig;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Getter
    private static long likeablePersonFromMaxSize;

    @Value("${custom.likeablePerson.from.maxSize}")
    public void setLikeablePersonFromMaxSize(long likeablePersonFromMaxSize){
        AppConfig.likeablePersonFromMaxSize = likeablePersonFromMaxSize;
    }
}
