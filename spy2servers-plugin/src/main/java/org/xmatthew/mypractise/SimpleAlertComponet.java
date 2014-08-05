/**
 * 
 */
package org.xmatthew.mypractise;

import org.xmatthew.spy2servers.annotation.AlertComponent;
import org.xmatthew.spy2servers.core.AbstractAlertComponent;
import org.xmatthew.spy2servers.core.IVisitor;
import org.xmatthew.spy2servers.core.Message;

/**
 * @author Matthew Xie
 *
 */
@AlertComponent(name = "myAlertComponent")
public class SimpleAlertComponet extends AbstractAlertComponent{
	

	@Override
	protected void onAlert(Message message) {
		if (isActive()) {
			System.out.println(message);
		}
	}

	public void startup() {
		
		setStatusRun();
	
	}

	public void stop() {
		setStatusStop();
		
	}

    public void accept(IVisitor visitor) {
        visitor.visit(this);
    }
}
