=======================================================
Jackrabbit Oak - the next generation content repository
=======================================================

Jackrabbit Oak is an effort to implement a scalable and performant
hierarchical content repository for use as the foundation of modern
world-class web sites and other demanding content applications.

The Oak effort is a part of the Apache Jackrabbit project.
Apache Jackrabbit is a project of the Apache Software Foundation.

Oak is currently alpha-level software. Use at your own risk with no
stability or compatibility guarantees.

Getting Started
---------------

To get started with Oak, build the latest sources with
Maven 3 and Java 6 (or higher) like this:

    mvn clean install

To run the build with all integration tests enabled, use:

    mvn clean install -PintegrationTesting

The build consists of the following main components:

  - oak-parent    - parent POM
  - oak-commons   - shared utility code
  - oak-mk        - MicroKernel API and default implementation
  - [oak-core][1] - Oak repository API and implementation
  - oak-jcr       - JCR binding for the Oak repository
  - oak-sling     - integration with Apache Sling
  - oak-run       - runnable jar packaging
  - oak-it        - integration tests
    - oak-it/mk   - integration tests for MicroKernel
    - oak-it/jcr  - integration tests for JCR
    - oak-it/osgi - integration tests for OSGi
  - oak-bench     - performance tests

  [1]: oak-core/README.md

License
-------

(see [LICENSE.txt](LICENSE.txt) for full license details)

Collective work: Copyright 2012 The Apache Software Foundation.

Licensed to the Apache Software Foundation (ASF) under one or more
contributor license agreements.  See the NOTICE file distributed with
this work for additional information regarding copyright ownership.
The ASF licenses this file to You under the Apache License, Version 2.0
(the "License"); you may not use this file except in compliance with
the License.  You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
