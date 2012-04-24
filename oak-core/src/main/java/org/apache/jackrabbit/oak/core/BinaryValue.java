/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.jackrabbit.oak.core;

import org.apache.jackrabbit.mk.api.MicroKernel;
import org.apache.jackrabbit.mk.util.MicroKernelInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

/**
 * BinaryValue... TODO: review name (BlobValue? BlobCoreValue? BinaryCoreValue?)
 */
public class BinaryValue {

    /**
     * logger instance
     */
    private static final Logger log = LoggerFactory.getLogger(BinaryValue.class);

    private String binaryID;
    private MicroKernel mk;

    BinaryValue(String binaryID, MicroKernel mk) {
        this.binaryID = binaryID;
        this.mk = mk;
    }

    InputStream getStream() {
        return new MicroKernelInputStream(mk, binaryID);
    }

    long length() {
        return mk.getLength(binaryID);
    }
}