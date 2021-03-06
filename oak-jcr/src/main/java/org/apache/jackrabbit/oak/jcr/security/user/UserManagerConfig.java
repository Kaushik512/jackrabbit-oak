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
package org.apache.jackrabbit.oak.jcr.security.user;

import org.apache.jackrabbit.oak.jcr.security.user.action.AuthorizableAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nonnull;

/**
 * UserManagerConfig...
 */
public class UserManagerConfig {

    private static final Logger log = LoggerFactory.getLogger(UserManagerImpl.class);

    /**
     * Configuration option to define the path underneath which user nodes
     * are being created.
     */
    public static final String PARAM_USER_PATH = "usersPath";

    /**
     * Configuration option to define the path underneath which group nodes
     * are being created.
     */
    public static final String PARAM_GROUP_PATH = "groupsPath";

    /**
     * Parameter used to change the number of levels that are used by default
     * store authorizable nodes.<br>The default number of levels is 2.
     */
    public static final String PARAM_DEFAULT_DEPTH = "defaultDepth";

    /**
     * If this parameter is present group members are collected in a node
     * structure below a {@link UserConstants#REP_MEMBERS} node instead of the
     * default multi valued property {@link UserConstants#REP_MEMBERS}.
     * Its value determines the maximum number of member properties until
     * additional intermediate nodes are inserted.
     */
    public static final String PARAM_GROUP_MEMBERSHIP_SPLIT_SIZE = "groupMembershipSplitSize";

    /**
     * Configuration parameter to change the default algorithm used to generate
     * password hashes.
     */
    public static final String PARAM_PASSWORD_HASH_ALGORITHM = "passwordHashAlgorithm";

    /**
     * Configuration parameter to change the number of iterations used for
     * password hash generation.
     */
    public static final String PARAM_PASSWORD_HASH_ITERATIONS = "passwordHashIterations";

    /**
     * Configuration parameter to change the number of iterations used for
     * password hash generation.
     */
    public static final String PARAM_PASSWORD_SALT_SIZE = "passwordSaltSize";

    private final String adminId;
    private final Map<String, Object> options;
    private final Set<AuthorizableAction> actions;

    public UserManagerConfig(String adminId, Map<String, Object> options, Set<AuthorizableAction> actions) {
        assert adminId != null;

        this.adminId = adminId;
        this.options = (options == null) ? Collections.<String, Object>emptyMap() : Collections.unmodifiableMap(options);
        this.actions = (actions == null) ? Collections.<AuthorizableAction>emptySet() : Collections.unmodifiableSet(actions);
    }

    @Nonnull
    public String getAdminId() {
        return adminId;
    }

    public <T> T getConfigValue(String key, T defaultValue) {
        if (options != null && options.containsKey(key)) {
            return convert(options.get(key), defaultValue);
        } else {
            return defaultValue;
        }
    }

    @Nonnull
    public AuthorizableAction[] getAuthorizableActions() {
        return actions.toArray(new AuthorizableAction[actions.size()]);
    }

    //--------------------------------------------------------< private >---
    @SuppressWarnings("unchecked")
    private static <T> T convert(Object configProperty, T defaultValue) {
        T value;
        String str = configProperty.toString();
        Class targetClass = (defaultValue == null) ? String.class : defaultValue.getClass();
        try {
            if (targetClass == String.class) {
                value = (T) str;
            } else if (targetClass == Integer.class) {
                value = (T) Integer.valueOf(str);
            } else if (targetClass == Long.class) {
                value = (T) Long.valueOf(str);
            } else if (targetClass == Double.class) {
                value = (T) Double.valueOf(str);
            } else if (targetClass == Boolean.class) {
                value = (T) Boolean.valueOf(str);
            } else {
                // unsupported target type
                log.warn("Unsupported target type {} for value {}", targetClass.getName(), str);
                throw new IllegalArgumentException("Cannot convert config entry " + str + " to " + targetClass.getName());
            }
        } catch (NumberFormatException e) {
            log.warn("Invalid value {}; cannot be parsed into {}", str, targetClass.getName());
            value = defaultValue;
        }
        return value;
    }
}