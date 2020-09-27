package com.hjg.middle.ware.mongo;

import org.junit.Test;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * @description:
 * @author: hjg
 * @createdOn: 2020/9/27
 */
public class MongoTest {

    @Test
    public void objectIdTest() {
        //这里是北京时间
        LocalDateTime dateTime0909 = LocalDateTime.parse("2020-09-09T00:00:00");

        LocalDateTime dateTime0921 = LocalDateTime.parse("2020-09-21T00:00:00");

        System.out.println(this.getObjectId(dateTime0909));
        System.out.println(this.getObjectId(dateTime0921));
    }

    private String getObjectId(LocalDateTime localDateTime) {
        Long second = localDateTime.toEpochSecond(ZoneOffset.of("+8"));
        String fourZeros = "0000";
        return Long.toHexString(second)
                .concat(fourZeros).concat(fourZeros)
                .concat(fourZeros).concat(fourZeros);
    }
}
