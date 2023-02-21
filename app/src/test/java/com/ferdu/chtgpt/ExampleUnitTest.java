package com.ferdu.chtgpt;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    private void myTest() {
        int i=0;
        if (i == 0) {
            i++;
            if (i == 1) {
                System.out.println("里面");
                return;
            }
        }else {
            System.out.println("else");
        }
        System.out.println("外面");
    }
}