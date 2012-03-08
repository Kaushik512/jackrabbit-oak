/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.jackrabbit.mk.model;

import org.apache.jackrabbit.mk.util.StringUtils;

import java.util.Arrays;

/**
 * Represents an internal identifier, uniquely identifying 
 * a {@link Node} or a {@link Commit}.
 * <p/>
 * This implementation aims at minimizing the in-memory footprint
 * of an identifier instance. therefore it doesn't cash e.g. the hashCode
 * or the string representation.
 * <p/>
 * <b>Important Note:</b><p/>
 * An {@link Id} is considered immutable. The <code>byte[]</code> 
 * passed to {@link Id#Id(byte[])} must not be reused or modified, the same
 * applies for the <code>byte[]</code> returned by {@link Id#getBytes()}.
 */
public class Id {

    // the raw bytes making up this identifier
    private final byte[] raw;

    /**
     * Creates a new instance based on the passed <code>byte[]</code>.
     * <p/>
     * The passed <code>byte[]</code> mus not be reused, it's assumed
     * to be owned by the new <code>Id</code> instance.
     *
     * @param raw the byte representation
     */
    public Id(byte[] raw) {
        // don't copy the buffer for efficiency reasons
        this.raw = raw;
    }

    /**
     * Creates an <code>Id</code> instance from its
     * string representation as returned by {@link #toString()}.
     * <p/>
     * The following condition holds true:
     * <pre>
     * Id someId = ...;
     * assert(Id.fromString(someId.toString()).equals(someId));
     * </pre>
     *
     * @param s a string representation of an <code>Id</code>
     * @return an <code>Id</code> instance
     */
    public static Id fromString(String s) {
        return new Id(StringUtils.convertHexToBytes(s));
    }

    @Override
    public int hashCode() {
        // the hashCode is intentionally not stored
        return Arrays.hashCode(raw);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Id) {
            Id other = (Id) obj;
            return Arrays.equals(raw, other.raw);
        }
        return false;
    }

    @Override
    public String toString() {
        // the string representation is intentionally not stored
        return StringUtils.convertBytesToHex(raw);
    }

    /**
     * Returns the raw byte representation of this identifier.
     * <p/>
     * The returned <code>byte[]</code> <i>MUST NOT</i> be modified!
     *
     * @return the raw byte representation
     */
    public byte[] getBytes() {
        // don't copy the buffer for efficiency reasons
        return raw;
    }
}