/* 
 * Copyright  2002,2004 Apache Software Foundation
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

package org.apache.tools.ant.taskdefs;

import org.apache.tools.ant.BuildFileTest;

public class DynamicTest extends BuildFileTest { 
    
    public DynamicTest(String name) { 
        super(name);
    }
    
    public void setUp() { 
        configureProject("src/etc/testcases/taskdefs/dynamictask.xml");
    }

    public void testSimple() {
        executeTarget("simple");
        assertEquals("1", project.getProperty("prop1"));
        assertEquals("2", project.getProperty("prop2"));
        assertEquals("3", project.getProperty("prop3"));
        assertEquals("4", project.getProperty("prop4"));
    }
}
