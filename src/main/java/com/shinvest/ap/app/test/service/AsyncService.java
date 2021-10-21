package com.shinvest.ap.app.test.service;


import lombok.extern.slf4j.Slf4j;

import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.shinvest.ap.common.Constant;

@Slf4j
@Service
@Profile({Constant.Profile.DEV,Constant.Profile.LOCAL})
public class AsyncService {

    @Async
    public void asyncHello(int i) {
        log.info("async i = " + i);
    }
}
