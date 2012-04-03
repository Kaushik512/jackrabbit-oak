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
package org.apache.jackrabbit.oak.api;

import java.text.ParseException;
import java.util.List;
import java.util.Map;
import org.apache.jackrabbit.oak.query.CoreValue;

/**
 * The query engine allows to parse and execute queries.
 * <p>
 * At least the query languages {@code QueryEngine.XPATH} and {@code
 * QueryEngine.SQL2} are supported. Other query languages might be supported
 * depending on the configured query parsers.
 */
public interface QueryEngine {

    /**
     * The XPath query language.
     */
    String XPATH = "xpath";

    /**
     * The SQL-2 query language.
     */
    String SQL2 = "sql2";

    /**
     * Parse the query (check if it's valid) and get the list of bind variable names.
     *
     * @param statement
     * @param language
     * @return the list of bind variable names
     * @throws ParseException
     */
    List<String> getBindVariableNames(String statement, String language) throws ParseException;

    /**
     * Execute a query and get the result.
     *
     * @param statement the query statement
     * @param language the language
     * @param bindings the bind variable value bindings
     * @return the result
     * @throws ParseException if the statement could not be parsed
     */
    Result executeQuery(String statement, String language, Map<String, CoreValue> bindings) throws ParseException;

}