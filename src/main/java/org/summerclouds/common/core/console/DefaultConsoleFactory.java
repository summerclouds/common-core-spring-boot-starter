/**
 * Copyright (C) 2002 Mike Hummel (mh@mhus.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.summerclouds.common.core.console;

import org.summerclouds.common.core.log.Log;
import org.summerclouds.common.core.tool.MSystem;

public class DefaultConsoleFactory implements ConsoleFactory {

    @Override
    public Console create(String term) {
    	try {
	    	if (term == null) {
	            if (MSystem.isWindows()) {
	                return new CmdConsole();
	            }
	    		term = System.getenv("TERM");
	    	}
            if (term != null) {
                term = term.toLowerCase();
                if (term.indexOf("xterm") >= 0) {
                    return new XTermConsole();
                }
                if (term.indexOf("ansi") >= 0) return new ANSIConsole();
            }
        } catch (Throwable t) {
            Log.getLog(DefaultConsoleFactory.class).d(t);
        }
        return new SimpleConsole();
    }
}
