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
package org.apache.jackrabbit.oak.security.authentication;

import org.apache.jackrabbit.oak.spi.security.authentication.CredentialsCallback;
import org.apache.jackrabbit.oak.spi.security.principal.EveryonePrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Credentials;
import javax.jcr.GuestCredentials;
import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * The {@code GuestLoginModule} is intended to provide backwards compatibility
 * with the login handling present in the JCR reference implementation located
 * in jackrabbit-core. While the specification claims that {@link javax.jcr.Repository#login}
 * with {@code null} Credentials implies that the authentication process is
 * handled externally, the default implementation jackrabbit-core treated it
 * as 'anonymous' login such as covered by using {@link GuestCredentials}.<p/>
 *
 * This {@code LoginModule} implementation performs the following tasks upon
 * {@link #login()}.
 *
 * <ol>
 *     <li>Try to retrieve JCR credentials from the {@link CallbackHandler} using
 *     the {@link CredentialsCallback}</li>
 *     <li>In case no credentials could be obtained it pushes a new instance of
 *     {@link GuestCredentials} to the shared stated. Subsequent login module
 *     in the authentication process may retrieve the {@link GuestCredentials}
 *     instead of failing to obtain any credentials.</li>
 * </ol>
 *
 * Note however that this implementation does not populate the subject during
 * {@link #commit() phase 2} of the authentication process. This responsibility
 * is delegated to a subsequent login module implementation that may or may not
 * use the {@code GuestCredentials} this module added to the share state.<p/>
 *
 * The authentication configuration using this {@code LoginModule} could for
 * example look as follows:
 *
 * <pre>
 *
 *    jackrabbit.oak {
 *            org.apache.jackrabbit.oak.security.authentication.GuestLoginModule  optional;
 *            org.apache.jackrabbit.oak.security.authentication.LoginModuleImpl required;
 *    };
 *
 * </pre>
 *
 * In this case calling {@link javax.jcr.Repository#login()} would be equivalent
 * to {@link javax.jcr.Repository#login(javax.jcr.Credentials) repository.login(new GuestCredentials()}.
 */
public class GuestLoginModule implements LoginModule {

    private static final Logger log = LoggerFactory.getLogger(GuestLoginModule.class);

    private Subject subject;
    private CallbackHandler callbackHandler;
    private Map sharedState;

    private GuestCredentials guestCredentials;

    //--------------------------------------------------------< LoginModule >---
    @Override
    public void initialize(Subject subject, CallbackHandler callbackHandler, Map<String, ?> sharedState, Map<String, ?> options) {
        this.subject = subject;
        this.callbackHandler = callbackHandler;
        this.sharedState = sharedState;
    }

    @Override
    public boolean login() throws LoginException {
        if (callbackHandler != null) {
            CredentialsCallback ccb = new CredentialsCallback();
            try {
                callbackHandler.handle(new Callback[] {ccb});
                Credentials credentials = ccb.getCredentials();
                if (credentials == null) {
                    Set<Credentials> sharedCredentials;
                    Object sharedObj = sharedState.get(LoginModuleImpl.SHARED_KEY_CREDENTIALS);
                    if (sharedObj == null || !(sharedObj instanceof Set)) {
                        sharedCredentials = new HashSet<Credentials>();
                    } else {
                        sharedCredentials = (Set) sharedObj;
                    }
                    guestCredentials = new GuestCredentials();
                    sharedCredentials.add(guestCredentials);
                    sharedState.put(LoginModuleImpl.SHARED_KEY_CREDENTIALS, sharedCredentials);
                    return true;
                }
            } catch (IOException e) {
                log.debug("Login: Failed to retrieve Credentials from CallbackHandler", e);
            } catch (UnsupportedCallbackException e) {
                log.debug("Login: Failed to retrieve Credentials from CallbackHandler", e);
            }
        }

        // ignore this login module
        return false;
    }

    @Override
    public boolean commit() throws LoginException {
        if (guestCredentials != null) {
            subject.getPublicCredentials().add(guestCredentials);
            subject.getPrincipals().add(EveryonePrincipal.getInstance());
        }
        return true;
    }

    @Override
    public boolean abort() throws LoginException {
        // nothing to do
        return true;
    }

    @Override
    public boolean logout() throws LoginException {
        // nothing to do.
        return true;
    }
}