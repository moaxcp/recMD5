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

    /**
     * Test of engineUpdate method, of class MD5MessageDigest.
     */
    @Test
    public void testEngineUpdate_byte() {
        System.out.println("engineUpdate");
        byte input = 0;
        MD5MessageDigest instance = new MD5MessageDigest();
        instance.engineUpdate(input);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of engineUpdate method, of class MD5MessageDigest.
     */
    @Test
    public void testEngineUpdate_3args() {
        System.out.println("engineUpdate");
        byte[] buffer = null;
        int offset = 0;
        int length = 0;
        MD5MessageDigest instance = new MD5MessageDigest();
        instance.engineUpdate(buffer, offset, length);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of engineDigest method, of class MD5MessageDigest.
     */
    @Test
    public void testEngineDigest() {
        System.out.println("engineDigest");
        MD5MessageDigest instance = new MD5MessageDigest();
        byte[] expResult = null;
        byte[] result = instance.engineDigest();
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of engineReset method, of class MD5MessageDigest.
     */
    @Test
    public void testEngineReset() {
        System.out.println("engineReset");
        MD5MessageDigest instance = new MD5MessageDigest();
        instance.engineReset();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getState method, of class MD5MessageDigest.
     */
    @Test
    public void testGetState() {
        System.out.println("getState");
        MD5MessageDigest instance = new MD5MessageDigest();
        MD5State expResult = null;
        MD5State result = instance.getState();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
