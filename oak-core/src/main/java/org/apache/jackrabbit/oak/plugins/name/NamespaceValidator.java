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
package org.apache.jackrabbit.oak.plugins.name;

import java.util.Locale;
import java.util.Map;

import javax.jcr.PropertyType;

import org.apache.jackrabbit.oak.api.CommitFailedException;
import org.apache.jackrabbit.oak.api.PropertyState;
import org.apache.jackrabbit.oak.spi.commit.DefaultValidator;

class NamespaceValidator extends DefaultValidator {

    private final Map<String, String> map;

    public NamespaceValidator(Map<String, String> map) {
        this.map = map;
    }

    //-----------------------------------------------------< NodeValidator >--

    @Override
    public void propertyAdded(PropertyState after)
            throws CommitFailedException {
        String prefix = after.getName();
        if (map.containsKey(prefix)) {
            throw new NamespaceValidatorException(
                    "Namespace mapping already registered", prefix);
        } else if (Namespaces.isValidPrefix(prefix)) {
            if (after.isArray()
                    || after.getValue().getType() != PropertyType.STRING) {
                throw new NamespaceValidatorException(
                        "Invalid namespace mapping", prefix);
            } else if (prefix.toLowerCase(Locale.ENGLISH).startsWith("xml")) {
                throw new NamespaceValidatorException(
                        "XML prefixes are reserved", prefix);
            }
        }
    }

    @Override
    public void propertyChanged(PropertyState before, PropertyState after)
            throws CommitFailedException {
        if (map.containsKey(after.getName())) {
            throw new NamespaceValidatorException(
                    "Namespace modification not allowed", after.getName());
        }
    }

    @Override
    public void propertyDeleted(PropertyState before)
            throws CommitFailedException {
        if (map.containsKey(before.getName())) {
            // TODO: Check whether this namespace is still used in content
        }
    }

}
