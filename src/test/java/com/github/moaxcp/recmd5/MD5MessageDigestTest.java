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
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Random;


import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author john
 */
public class MD5MessageDigestTest {
    
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
    
    private boolean testAgainstJavaMD5(byte[] bytes) {
        MessageDigest expected = null;
        MD5MessageDigest test = null;
        try {
            expected = MessageDigest.getInstance("md5");
            test = new MD5MessageDigest();
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(MD5MessageDigestTest.class.getName()).log(Level.SEVERE, null, ex);
            throw new IllegalStateException("could not create MessageDigest", ex);
        }

        expected.update(bytes);
        String s = new BigInteger(1, expected.digest()).toString(16);
        if (s.length() == 31) {
            s = "0" + s;
        }
        test.digest(bytes);
        Logger.getLogger(MD5MessageDigestTest.class.getName()).info(s);
        Logger.getLogger(MD5MessageDigestTest.class.getName()).info(test.getState().toString());
        return s.equals(test.getState().toString());
    }
    
    private byte[] getBytes(int size) {
        byte[] bytes = new byte[size];
        new Random().nextBytes(bytes);
        return bytes;
    }
    
    @Test
    public void test0() {
        assertTrue(testAgainstJavaMD5(getBytes(0)));
    }
    
    @Test
    public void test1024() {
        assertTrue(testAgainstJavaMD5(getBytes(1024)));
    }
    
    @Test
    public void test2048() {
        assertTrue(testAgainstJavaMD5(getBytes(2048)));
    }
    
    @Test
    public void test1048576() {
        assertTrue(testAgainstJavaMD5(getBytes(1048576)));
    }
}
