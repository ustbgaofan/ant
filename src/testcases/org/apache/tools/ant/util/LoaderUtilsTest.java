/* 
 * Copyright  2003-2004 Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 * 
 */
package org.apache.tools.ant.util;

import java.io.File;
import junit.framework.TestCase;

/**
 * @since Ant 1.6
 */
public class LoaderUtilsTest extends TestCase {

    public LoaderUtilsTest(String name) {
        super(name);
    }

    public void testGetXyzSource() {
        File f1 = LoaderUtils.getClassSource(LoaderUtils.class);
        assertNotNull(f1);

        File f2 = LoaderUtils.getResourceSource(null,
                                                "org/apache/tools/ant/taskdefs/defaults.properties");
        assertNotNull(f2);

        assertEquals(f1.getAbsolutePath(), f2.getAbsolutePath());
    }

}
