package com.imooc.jvminaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TLABTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(TLABTest.class);
    private TLABObj tlabObj;

    // -XX:+UseTLAB: 484ms
    // -XX:-UseTLAB: 1239ms
    public static void main(String[] args) {
        TLABTest test = new TLABTest();
        long start = System.currentTimeMillis();
        for (int i = 0; i < 1_0000_0000; i++) {
            test.tlabObj = new TLABObj();
        }
        System.out.println(test.tlabObj);
        long end = System.currentTimeMillis();
        LOGGER.info("花费{}ms", end - start);
    }
}

class TLABObj {
}