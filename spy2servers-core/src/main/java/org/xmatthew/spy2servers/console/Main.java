/*
 * Copyright 2008- the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.xmatthew.spy2servers.console;

import org.apache.log4j.Logger;

import java.util.ArrayList;

import org.xmatthew.spy2servers.command.Command;
import org.xmatthew.spy2servers.command.ShutdownCommand;
import org.xmatthew.spy2servers.command.StartCommand;


/**
 * @author Matthew Xie
 *
 */
public class Main {
    /**
     * Logger for this class
     */
    private static final Logger LOGGER = Logger.getLogger(Main.class);
    
    private static final String START_C = "start";
    private static final String STOP_C = "stop";
    
    
	/**
	 * @param args
	 */
	public static void main(String[] args) {
        
        String control = START_C;
        
        if (args != null && args.length == 1
                && (args[0].equals(START_C) || args[0].equals(STOP_C))) {
            control = args[0];
        }
		
        Command command;
        if (START_C.equals(control)) {
            LOGGER.info("Server starting...");
            command = new StartCommand();
        } else {
            LOGGER.info("Stop server starting...");
            command = new ShutdownCommand();
        }
        command.execute(new ArrayList<String>());
        
        System.exit(0);
	}

}
