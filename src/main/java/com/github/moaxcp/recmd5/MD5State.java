/*
 * The MIT License
 *
 * Copyright 2014 john.
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

import java.io.Serializable;
import java.util.Arrays;

/**
 * recoverable md5
 * <p>
 * This library is a rewrite of the fast-md5 implementation in java. The goal of
 * this library is to make the MD5 state recoverable for cases where the programmer
 * does not want to recalculate an MD5 for partial hashes of files or streams.
 * It is not meant to be fast and does not use the native code, although, it
 * would be easy for a programmer to implement the native code.
 * <p>
 * Fast implementation of RSA's MD5 hash generator in Java JDK Beta-2 or higher<br>
 * Originally written by Santeri Paavolainen, Helsinki Finland 1996 <br>
 * (c) Santeri Paavolainen, Helsinki Finland 1996 <br>
 * Some changes Copyright (c) 2002 Timothy W Macinta <br>
 * Updates to use MessageDigest abstraction and to allow the internal state to
 * be recoverable in case of crash 2011 John Mercier<br>
 * <p>
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Library General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * <p>
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Library General Public
 * License along with this library; if not, write to the Free
 * Software Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 * <p>
 * See http://www.twmacinta.com/myjava/fast_md5.php for more information
 * on this file.
 * <p>
 * Contains internal state of the MD5MessageDigest and the final state of the
 * MD5MessageDigest. Also holds the final string of the MD5 hash for convienence.
 * <p>
 * "It is my hope that this library will be useful to programmers calculating
 * md5s for input from ranged protocols such as http and ftp." - John Mercier
 *
 * @author	Santeri Paavolainen <sjpaavol@cc.helsinki.fi>
 * @author	Timothy W Macinta (twm@alum.mit.edu) (optimizations and bug fixes)
 * @author      John Mercier <moaxcp@gmail.com>
 **/
public final class MD5State implements Serializable {

    /**
     * 128-bit state
     */
    int state[];

    int finalState[];

    /**
     * 64-bit character count
     */
    long count;

    long finalCount;

    /**
     * 64-byte buffer (512 bits) for storing to-be-hashed characters
     */
    byte buffer[];

    byte finalBuffer[];

    private StringBuilder hash;

    /**
     * performs a deep copy of md5State
     * 
     * @param md5State the state to copy.
     * @return a copy of the md5State
     */
    public static MD5State copy(MD5State md5State) {
        MD5State copy = new MD5State();
        copy.count = md5State.count;
        copy.finalCount = md5State.finalCount;
        copy.hash.delete(0, md5State.hash.length());
        copy.hash.append(md5State.hash);
        copy.buffer = md5State.buffer.clone();
        copy.state = md5State.state.clone();
        copy.finalBuffer = md5State.finalBuffer.clone();
        copy.finalState = md5State.finalState.clone();
        return copy;
    }

    /**
     * creates a default MD5State
     */
    public MD5State() {
        buffer = new byte[64];
        finalBuffer = new byte[64];
        count = 0;
        finalCount = 0;
        state = new int[4];
        finalState = new int[4];

        state[0] = 0x67452301;
        state[1] = 0xefcdab89;
        state[2] = 0x98badcfe;
        state[3] = 0x10325476;

        hash = new StringBuilder();

    }

    /**
     * copies the state to the final variables.
     */
    void copyToFinal() {
        if(finalState == null) {
            finalState = new int[4];
        }
        System.arraycopy(buffer, 0, finalBuffer, 0, finalBuffer.length);
        System.arraycopy(state, 0, finalState, 0, finalState.length);
        finalCount = count;
    }

    /**
     * @return the StringBuilder for this hash.
     */
    StringBuilder getHash() {
        return hash;
    }

    /**
     * returns the String representation of the hash. Users must use digest from
     * MD5MessageDigest for this to be accurate.
     */
    @Override
    public String toString() {
        return hash.toString();
    }

    /**
     * returns the Object hashCode for this object.
     * 
     * @return the hashCode.
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Arrays.hashCode(this.state);
        hash = 89 * hash + (int) (this.count ^ (this.count >>> 32));
        hash = 89 * hash + Arrays.hashCode(this.buffer);
        return hash;
    }

    /**
     * Compares this Object with another object.
     * 
     * @param the object to compare. Should be an MD5State.
     * @return true if o is an MD5State and it is equal to this object.
     */
    @Override
    public boolean equals(Object o) {
        if(o instanceof MD5State) {
            MD5State s = (MD5State) o;
            return Arrays.equals(this.buffer, s.buffer) && Arrays.equals(this.state, s.state) && this.count == s.count;
        }
        return false;
    }
}
