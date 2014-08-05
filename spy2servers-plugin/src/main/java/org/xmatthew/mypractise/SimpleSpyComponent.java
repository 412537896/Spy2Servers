/**
 * 
 */
package org.xmatthew.mypractise;

import java.util.Date;
import java.util.UUID;

import org.xmatthew.spy2servers.annotation.SpyComponent;
import org.xmatthew.spy2servers.core.AbstractSpyComponent;
import org.xmatthew.spy2servers.core.IVisitor;
import org.xmatthew.spy2servers.core.Message;

/**
 * @author Matthew Xie
 *
 */
@SpyComponent(name = "mySpyComponent")
public class SimpleSpyComponent extends AbstractSpyComponent {

	public void startup() {
		setStatusRun();
		try {
			while (isActive()) {
				onSpy(createMessage());
				Thread.sleep(25000);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private Message createMessage() {
		Message message = new Message();
		message.setId(UUID.randomUUID().toString());
		message.setCreateDate(new Date());
		message.setDescription("message sent by " + getName());
		message.setLevel(Message.LV_INFO);
		message.setType("Test Message");
		return message;
	}

	public void stop() {
		setStatusStop();

	}
	
    public void accept(IVisitor visitor) {
        visitor.visit(this);
    }
}
