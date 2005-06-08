/*
 * Copyright  2004-2005 The Apache Software Foundation
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

package org.apache.tools.ant.taskdefs.condition;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectComponent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;

/**
 * Test for a host being reachable using ICMP "ping" packets & echo operations.
 * Ping packets are very reliable for assessing reachability in a LAN or WAN,
 * but they do not get through any well-configured firewall. Echo (port 7) may.
 * <p/>
 * This condition turns unknown host exceptions into false conditions. This is
 * because on a laptop, DNS is one of the first services lost when the network
 * goes; you are implicitly offline.
 * <p/>
 * If a URL is supplied instead of a host, the hostname is extracted and used in
 * the test--all other parts of the URL are discarded.
 * <p/>
 * The test may not work through firewalls; that is, something may be reachable
 * using a protocol such as HTTP, while the lower level ICMP packets get dropped
 * on the floor. Similarly, a host may be detected as reachable with ICMP, but not
 * reachable on other ports (i.e. port 80), because of firewalls.
 * <p/>
 * Requires Java1.5+ to work properly. On Java1.4 and earlier, if a hostname
 * can be resolved, the destination is assumed to be reachable.
 *
 * @ant.condition name="isreachable"
 * @since Ant 1.7
 */
public class IsReachable extends ProjectComponent implements Condition {

    private String host;
    private String url;

    /**
     * The default timeout.
     */
    public static final int DEFAULT_TIMEOUT = 30;
    private int timeout = DEFAULT_TIMEOUT;
    /**
     * Error when no hostname is defined
     */
    public static final String ERROR_NO_HOSTNAME = "No hostname defined";
    /**
     * Error when invalid timeout value is defined
     */
    public static final String ERROR_BAD_TIMEOUT = "Invalid timeout value";
    /**
     * Unknown host message is seen.
     */
    public static final String WARN_UNKNOWN_HOST = "Unknown host: ";
    /**
     * Network error message is seen.
     */
    public static final String ERROR_ON_NETWORK = "network error to ";
    public static final String ERROR_BOTH_TARGETS = "Both url and host have been specified";
    public static final String MSG_NO_REACHABLE_TEST
        = "cannot do a proper reachability test on this Java version";
    public static final String ERROR_BAD_URL = "Bad URL ";
    public static final String ERROR_NO_HOST_IN_URL = "No hostname in URL ";
    private static final String METHOD_NAME = "isReachable";

    /**
     * Set the host to ping.
     *
     * @param host the host to ping.
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * Set the URL from which to extract the hostname.
     *
     * @param url a URL object.
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Set the timeout for the reachability test in seconds.
     *
     * @param timeout the timeout in seconds.
     */
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    /**
     * emptyness test
     *
     * @param string param to check
     *
     * @return true if it is empty
     */
    private boolean empty(String string) {
        return string == null || string.length() == 0;
    }

    private static Class[] parameterTypes = {Integer.TYPE};

    /**
     * Evaluate the condition.
     *
     * @return true if the condition is true.
     *
     * @throws org.apache.tools.ant.BuildException
     *          if an error occurs
     */
    public boolean eval() throws BuildException {
        if (empty(host) && empty(url)) {
            throw new BuildException(ERROR_NO_HOSTNAME);
        }
        if (timeout < 0) {
            throw new BuildException(ERROR_BAD_TIMEOUT);
        }
        String target = host;
        if (!empty(url)) {
            if (!empty(host)) {
                throw new BuildException(ERROR_BOTH_TARGETS);
            }
            try {
                //get the host of a url
                URL realURL = new URL(url);
                target = realURL.getHost();
                if (empty(target)) {
                    throw new BuildException(ERROR_NO_HOST_IN_URL + url);
                }
            } catch (MalformedURLException e) {
                throw new BuildException(ERROR_BAD_URL + url, e);
            }
        }
        log("Probing host " + target, Project.MSG_VERBOSE);
        InetAddress address;
        try {
            log(WARN_UNKNOWN_HOST + target);
            address = InetAddress.getByName(target);
        } catch (UnknownHostException e1) {
            return false;

        }
        log("Host address = " + address.getHostAddress(),
                Project.MSG_VERBOSE);
        boolean reachable;
        //Java1.5: reachable = address.isReachable(timeout * 1000);
        Method reachableMethod = null;
        try {
            reachableMethod = InetAddress.class.getMethod(METHOD_NAME,
                    parameterTypes);
            Object[] params = new Object[1];
            params[0] = new Integer(timeout * 1000);
            try {
                reachable = ((Boolean) reachableMethod.invoke(address, params))
                        .booleanValue();
            } catch (IllegalAccessException e) {
                //utterly implausible, but catered for anyway
                throw new BuildException("When calling " + reachableMethod);
            } catch (InvocationTargetException e) {
                //assume this is an IOexception about un readability
                Throwable nested = e.getTargetException();
                log(ERROR_ON_NETWORK + target + ": " + nested.toString());
                //any kind of fault: not reachable.
                reachable = false;
            }
        } catch (NoSuchMethodException e) {
            //java1.4 or earlier
            log("Not found: InetAddress." + METHOD_NAME, Project.MSG_VERBOSE);
            log(MSG_NO_REACHABLE_TEST);
            reachable = true;

        }

        log("host is" + (reachable ? "" : " not") + " reachable", Project.MSG_VERBOSE);
        return reachable;
    }
}