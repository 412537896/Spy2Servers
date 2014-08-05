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

import org.apache.commons.mail.EmailException;
import org.apache.log4j.Logger;

import java.util.List;

import org.xmatthew.spy2servers.component.util.EMailUtils;
import org.xmatthew.spy2servers.component.util.MailBody;
import org.xmatthew.spy2servers.core.AbstractAlertComponent;
import org.xmatthew.spy2servers.core.IVisitor;
import org.xmatthew.spy2servers.core.Message;
import org.xmatthew.spy2servers.util.CollectionUtils;

/**
 * @author Matthew Xie
 *
 */
public class EmailAlertComponent extends AbstractAlertComponent {
    /**
     * Logger for this class
     */
    private static final Logger LOGGER = Logger.getLogger(EmailAlertComponent.class);
    
    private EmailAccount emailAccount;
    
    private List<String> emails;
    
    private MailBody mailBody;
    
    private final static String MAIL_HEAD = "EmailAlertComponent alert message!";

    /**
     * 
     */
    public EmailAlertComponent() {
        
    }

    /* (non-Javadoc)
     * @see org.xmatthew.spy2servers.core.AbstractAlertComponent#onAlert(org.xmatthew.spy2servers.core.Message)
     */
    @Override
    protected void onAlert(Message message) {
        if (message == null) {
            return;
        }
        if (mailBody == null) {
            return;
        }
        if (CollectionUtils.isBlankCollection(emails)) {
            return;
        }
        for (String email : emails) {
            sendMail(email, message);
        }
    }

    private void sendMail(String email, Message message) {
        String body = message.toString();
        mailBody.setBody(body);
        try {
            EMailUtils.sendSimpleEmail(mailBody);
        } catch (EmailException e) {
            LOGGER.error(e.getMessage(), e);
        }
        
    }

    public void startup() {
        if (emailAccount == null) {
            LOGGER.error("email account not set, startup failed.");
            return;
        }
        setStatusRun();
        
        if (emails == null) {
            LOGGER.error("no email notifier list");
        }
        
        //start main setting
        if (mailBody == null) {
            mailBody = new MailBody();
            
            mailBody.setLoginServer(emailAccount.getServer());
            mailBody.setLoginName(emailAccount.getLoginName());
            mailBody.setLoginPassword(emailAccount.getLoginPwd());
            mailBody.setLoginPort(emailAccount.getServerPort());
            mailBody.setSender(emailAccount.getSender(), emailAccount.getSendNick());
            mailBody.setSubject(MAIL_HEAD);
            
            if (emails != null && emails.size() > 0) {
                for (String email : emails) {
                    mailBody.addReceiver(email);
                }
            }
        }
        
        
    }

    /* (non-Javadoc)
     * @see org.xmatthew.spy2servers.core.Component#stop()
     */
    public void stop() {
        setStatusStop();
        emailAccount = null;
    }

    /**
     * @return the emails
     */
    public List<String> getEmails() {
        return emails;
    }

    /**
     * @param emails the emails to set
     */
    public void setEmails(List<String> emails) {
        this.emails = emails;
    }

    /**
     * @param emailAccount the emailAccount to set
     */
    public void setEmailAccount(EmailAccount emailAccount) {
        this.emailAccount = emailAccount;
    }

    public void accept(IVisitor visitor) {
        visitor.visit(this);
    }
}
