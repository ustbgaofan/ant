/*
 * Copyright  2000,2004-2005 The Apache Software Foundation
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

/**
 * Implementation of FileNameMapper that always returns the source file name.
 *
 * <p>This is the default FileNameMapper for the copy and move
 * tasks.</p>
 *
 */
public class IdentityMapper implements FileNameMapper {

    /**
     * Ignored.
     * @param from ignored.
     */
    public void setFrom(String from) {
    }

    /**
     * Ignored.
     * @param to ignored.
     */
    public void setTo(String to) {
    }

    /**
     * Returns an one-element array containing the source file name.
     * @param sourceFileName the name to map.
     * @return the source filename in a one-element array.
     */
    public String[] mapFileName(String sourceFileName) {
        return new String[] {sourceFileName};
    }
}
