/*
 * The MIT License
 *
 * Copyright 2015 john.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.github.moaxcp.recmd5;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Random;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author john
 */
public class MD5MessageDigestTest {

    private static final int TESTS = 25;
    private int[] sizes = {0, 1024, 1024 * 2, 1024 * 1024, 1024 * 1024 * 2, 1024 * 1024 * 10, 1024 * 1024 * 50};

    public class TestResult {

        long expectedTime;
        long testTime;
        byte[] expectedBytes;
        byte[] testBytes;

        public TestResult(long expectedTime, long testTime, byte[] expectedBytes, byte[] testBytes) {
            this.expectedTime = expectedTime;
            this.testTime = testTime;
            this.expectedBytes = expectedBytes;
            this.testBytes = testBytes;
        }
    }

    public class TestAverages {

        long averageExpectedTime;
        long averageTestTime;

        public TestAverages(List<TestResult> tests) {
            for (TestResult r : tests) {
                averageExpectedTime += r.expectedTime;
                averageTestTime += r.testTime;
            }
            averageExpectedTime /= tests.size();
            averageTestTime /= tests.size();
        }
    }

    public MD5MessageDigestTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    private long timeUpdate(MessageDigest md, byte[] bytes) {
        return timeUpdate(md, bytes, 0, bytes.length);
    }

    private long timeUpdate(MessageDigest md, byte[] bytes, int offset, int length) {
        long start = System.nanoTime();
        md.update(bytes, offset, length);
        return System.nanoTime() - start;
    }

    private long timeDigest(MessageDigest md) {
        long start = System.nanoTime();
        md.digest();
        return System.nanoTime() - start;
    }

    private long timeDigest(MessageDigest md, byte[] bytes) {
        return timeDigest(md, bytes, 0, bytes.length);
    }

    private long timeDigest(MessageDigest md, byte[] bytes, int offset, int length) {
        long start = System.nanoTime();
        md.digest(bytes);
        return System.nanoTime() - start;
    }

    private String toTimeString(long time) {
        return new SimpleDateFormat("mm:ss:SSS").format(new Date(time / 1000000)).concat(" ").concat(Long.toString(time % 1000000));
    }

    public static String toSizeString(long bytes) {
        boolean si = false;
        int unit = si ? 1000 : 1024;
        if (bytes < unit) {
            return bytes + " B";
        }
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1) + (si ? "" : "i");
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }

    private TestResult testAgainstJava(byte[] bytes) {
        MessageDigest expected = null;
        MD5MessageDigest test = null;
        try {
            expected = MessageDigest.getInstance("md5");
            test = new MD5MessageDigest();
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(MD5MessageDigestTest.class.getName()).log(Level.SEVERE, null, ex);
            throw new IllegalStateException("could not create MessageDigest", ex);
        }

        long expectedStart = System.nanoTime();
        byte[] expectedBytes = expected.digest(bytes);
        long expectedTime = System.nanoTime() - expectedStart;
        long testStart = System.nanoTime();
        byte[] testBytes = test.digest(bytes);
        long testTime = System.nanoTime() - testStart;

        String s = new BigInteger(1, expectedBytes).toString(16);
        if (s.length() == 31) {
            s = "0" + s;
        }

        return new TestResult(expectedTime, testTime, expectedBytes, testBytes);
    }

    private TestResult testAgainstJavaWithSwap(byte[] bytes) {
        MessageDigest expected = null;
        MD5MessageDigest test = null;
        try {
            expected = MessageDigest.getInstance("md5");
            test = new MD5MessageDigest();
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(MD5MessageDigestTest.class.getName()).log(Level.SEVERE, null, ex);
            throw new IllegalStateException("could not create MessageDigest", ex);
        }

        long testTime = 0;
        long expectedTime = 0;

        if (bytes.length >= 2) {
            expectedTime += timeUpdate(expected, bytes, 0, bytes.length / 2);
            testTime += timeUpdate(test, bytes, 0, bytes.length / 2);

            test = new MD5MessageDigest(test.getState());

            expectedTime += timeUpdate(expected, bytes, bytes.length / 2, bytes.length / 2);
            testTime += timeUpdate(test, bytes, bytes.length / 2, bytes.length / 2);
        } else {
            expectedTime += timeUpdate(expected, bytes);
            testTime += timeUpdate(test, bytes);
        }

        long expectedStart = System.nanoTime();
        byte[] expectedBytes = expected.digest(bytes);
        expectedTime += System.nanoTime() - expectedStart;

        long testStart = System.nanoTime();
        byte[] testBytes = test.digest(bytes);
        testTime += System.nanoTime() - testStart;

        String s = new BigInteger(1, expectedBytes).toString(16);
        if (s.length() == 31) {
            s = "0" + s;
        }

        return new TestResult(expectedTime, testTime, expectedBytes, testBytes);
    }

    private byte[] getBytes(int size) {
        byte[] bytes = new byte[size];
        new Random().nextBytes(bytes);
        return bytes;
    }

    private List<TestResult> testAgainstJava(int length, int tests) {
        List<TestResult> list = new ArrayList<>();
        byte[] bytes = getBytes(length);
        for (int i = 0; i < tests; i++) {
            TestResult r = testAgainstJava(bytes);
            list.add(r);
        }
        return list;
    }

    private List<TestResult> testAgainstJavaWithSwap(int length, int tests) {
        List<TestResult> list = new ArrayList<>();
        byte[] bytes = getBytes(length);
        for (int i = 0; i < tests; i++) {
            TestResult r = testAgainstJavaWithSwap(bytes);
            list.add(r);
        }
        return list;
    }

    private String toResultString(TestAverages t) {
        return toTimeString(t.averageExpectedTime) + ", " + toTimeString(t.averageTestTime) + " " + (t.averageExpectedTime < t.averageTestTime ? "Java wins" : "recmd5 wins") + " " + String.format("%1$.3f", (t.averageExpectedTime / (double) t.averageTestTime) * 100);
    }

    @Test
    public void testAgainstJava() {
        for (int i : sizes) {
            List<TestResult> list = testAgainstJava(i, TESTS);

            for (TestResult r : list) {
                assertArrayEquals(r.expectedBytes, r.testBytes);
            }

            System.out.println("testAgainstJava " + toSizeString(i) + " " + toResultString(new TestAverages(list)));
        }
    }

    @Test
    public void testAgainstJavaWithSwap() {
        for (int i : sizes) {
            List<TestResult> list = testAgainstJavaWithSwap(i, TESTS);

            for (TestResult r : list) {
                assertArrayEquals(r.expectedBytes, r.testBytes);
            }

            System.out.println("testAgainstJavaWithSwap " + toSizeString(i) + " " + toResultString(new TestAverages(list)));
        }
    }
}
