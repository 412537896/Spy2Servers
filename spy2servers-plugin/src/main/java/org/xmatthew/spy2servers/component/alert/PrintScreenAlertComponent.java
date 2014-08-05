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
package org.xmatthew.spy2servers.component.alert;

import org.xmatthew.spy2servers.core.AbstractAlertComponent;
import org.xmatthew.spy2servers.core.IVisitor;
import org.xmatthew.spy2servers.core.Message;

/**
 * @author Matthew Xie
 *
 */
public class PrintScreenAlertComponent extends AbstractAlertComponent {

    /* (non-Javadoc)
     * @see org.xmatthew.spy2servers.core.Component#startup()
     */
    public void startup() {
        setStatusRun();

    }

    /* (non-Javadoc)
     * @see org.xmatthew.spy2servers.core.Component#stop()
     */
    public void stop() {
        setStatusStop();

    }

    @Override
    protected void onAlert(Message message) {
        System.out.println(this.getClass().toString() + message);
        
    }

    public void accept(IVisitor visitor) {
        visitor.visit(this);
    }
}
