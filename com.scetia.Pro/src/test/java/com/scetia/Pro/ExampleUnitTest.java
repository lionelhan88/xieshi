package com.scetia.Pro;

import com.scetia.Pro.common.Util.DateUtil;

import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * created by ljs
 * on 2020/12/29
 */
public class ExampleUnitTest {
    @Test
    public void getTestingCommissionList(){
        int a = 1>>2;
        System.out.println(a+"");
    }
    @Test
    public void testGanpt() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        Date endDate = sdf.parse("2021-03-08");

        int gapCount = DateUtil.getGapCount(date, endDate);
        int gapCount2 = (int) DateUtil.getGapCount("20210305", "20210308");
        System.out.println(gapCount+"");
        System.out.println(gapCount2+"");
        Date date1 = DateUtil.parseTo("2021-03-08", "yyyy-MM-dd");
        System.out.println(DateUtil.FORMAT_BAR_YMD(date1)+"");
    }

    @Test
    public void testThreadPool(){
    }
}
