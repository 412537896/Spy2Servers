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
package org.xmatthew.spy2servers.component.util;

import java.io.Serializable;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.mail.EmailAttachment;
import org.apache.log4j.Logger;


/**
 * <p>
 * Email body text</p>
 *
 * @author Matthew Xie
 * @version 2006-9-22
 */
public class MailBody implements Serializable {
        /**
         * Logger for this class
         */
        private static final Logger LOGGER = Logger.getLogger(MailBody.class);

        /**
         * serial Version UID
         */
        private static final long serialVersionUID = -484635208377114897L;

        private String subject;
        private String sender;
        private String sender_nick;
        private String body;

        private String loginServer;
        private int loginPort;
        private String loginName;
        private String loginPassword;

        private String aHtml;

        private Map<String, String> receivers;
        private Map<String, String> ccReceivers;
        private Map<String, EmailAttachment> attachments;

        /**
         * default constructor
         */
        public MailBody() {
            loginPort = 25;
            receivers = new HashMap<String, String>();
            ccReceivers = new HashMap<String, String>();
            attachments = new HashMap<String, EmailAttachment>();
            sender_nick = "";
        }

        /**
         * @param receiver receiver's email address.
         */
        public void addReceiver(String receiver) {
            if (receiver == null) {
                LOGGER.warn("null pointer of email receiver detected.");
                return;
            }
            receivers.put(receiver, "");
        }

        /**
         * add receiver email address and nick name.
         *
         * @param receiver receiver's email address.
         * @param nickname receiver's nickname
         */
        public void addReceiver(String receiver, String nickname) {
            if (receiver == null) {
                LOGGER.warn("null pointer of email receiver detected.");
                return;
            }
            if (nickname == null) {
                addReceiver(receiver);
                return;
            }
            receivers.put(receiver, nickname);
        }

        /**
         * @param receivers add receivers.
         */
        public void addReceivers(Map<String, String> receivers) {
            this.receivers = receivers;
        }

        /**
         * clear all the receivers
         */
        public void clearReceivers() {
            receivers.clear();
        }

        /**
         * @param receiver receiver's email address.
         */
        public void addCC(String receiver) {
            if (receiver == null) {
                LOGGER.warn("null pointer of email receiver detected.");
                return;
            }
            ccReceivers.put(receiver, "");
        }

        /**
         * add CC receiver email address and nick name.
         *
         * @param receiver receiver's email address.
         * @param nickname receiver's nickname
         */
        public void addCC(String receiver, String nickname) {
            if (receiver == null) {
                LOGGER.warn("null pointer of email receiver detected.");
                return;
            }
            if (nickname == null) {
                addCC(receiver);
                return;
            }
            ccReceivers.put(receiver, nickname);
        }

        /**
         * @param ccReceivers add cc Receivers
         */
        public void addCCs(Map<String, String> ccReceivers) {
            this.ccReceivers = ccReceivers;
        }

        /**
         * clear all the cc receivers
         */
        public void clearCCReceivers() {
            ccReceivers.clear();
        }

        /**
         * @param url attachment from url file
         */
        public void addAttachment(URL url) {
            EmailAttachment emailAttachment = new EmailAttachment();
            emailAttachment.setURL(url);
            emailAttachment.setDisposition(EmailAttachment.ATTACHMENT);
            attachments.put(url.toString(), emailAttachment);
        }

        /**
         * @param url attachment from url file
         * @param name name of attachment
         * @param description description of attachment
         */
        public void addAttachment(URL url, String name, String description) {
            EmailAttachment emailAttachment = new EmailAttachment();
            emailAttachment.setURL(url);
            emailAttachment.setDisposition(EmailAttachment.ATTACHMENT);
            emailAttachment.setName(name);
            emailAttachment.setDescription(description);
            attachments.put(url.toString(), emailAttachment);
        }

        /**
         * @param path attachment from local file
         */
        public void addAttachment(String path) {
            EmailAttachment emailAttachment = new EmailAttachment();
            emailAttachment.setPath(path);
            emailAttachment.setDisposition(EmailAttachment.ATTACHMENT);
            attachments.put(path, emailAttachment);
        }

        /**
         * @param path attachment from local file
         * @param name name of attachment
         * @param description description of attachment
         */
        public void addAttachment(String path, String name, String description) {
            EmailAttachment emailAttachment = new EmailAttachment();
            emailAttachment.setPath(path);
            emailAttachment.setDisposition(EmailAttachment.ATTACHMENT);
            emailAttachment.setName(name);
            emailAttachment.setDescription(description);
            attachments.put(path, emailAttachment);
        }

        /**
         * clear all the attachments setting
         */
        public void clearAttachments() {
            attachments.clear();
        }

        /**
         * @return Returns the body.
         */
        public String getBody() {
            return body;
        }

        /**
         * @param body The body to set.
         */
        public void setBody(String body) {
            this.body = body;
        }

        /**
         * @return Returns the loginName.
         */
        public String getLoginName() {
            return loginName;
        }

        /**
         * @param loginName The loginName to set.
         */
        public void setLoginName(String loginName) {
            this.loginName = loginName;
        }

        /**
         * @return Returns the loginPassword.
         */
        public String getLoginPassword() {
            return loginPassword;
        }

        /**
         * @param loginPassword The loginPassword to set.
         */
        public void setLoginPassword(String loginPassword) {
            this.loginPassword = loginPassword;
        }

        /**
         * @return Returns the loginPort.
         */
        public int getLoginPort() {
            return loginPort;
        }

        /**
         * @param loginPort The loginPort to set.
         */
        public void setLoginPort(int loginPort) {
            this.loginPort = loginPort;
        }

        /**
         * @return Returns the loginServer.
         */
        public String getLoginServer() {
            return loginServer;
        }

        /**
         * @param loginServer The loginServer to set.
         */
        public void setLoginServer(String loginServer) {
            this.loginServer = loginServer;
        }

        /**
         * @return Returns the sender.
         */
        public String getSender() {
            return sender;
        }

        /**
         * @param sender The sender to set.
         */
        public void setSender(String sender) {
            this.sender = sender;
        }

        /**
         * @param sender The sender to set.
         * @param nickname The sender_nick to set.
         */
        public void setSender(String sender, String nickname) {
            this.sender = sender;
            this.sender_nick = nickname;
        }

        /**
         * @return Returns the sender_nick.
         */
        public String getSender_nick() {
            return sender_nick;
        }

        /**
         * @param sender_nick The sender_nick to set.
         */
        public void setSender_nick(String sender_nick) {
            this.sender_nick = sender_nick;
        }

        /**
         * @return Returns the subject.
         */
        public String getSubject() {
            return subject;
        }

        /**
         * @param subject The subject to set.
         */
        public void setSubject(String subject) {
            this.subject = subject;
        }

        /**
         * @return Returns the attachments.
         */
        public Map<String, EmailAttachment> getAttachments() {
            return attachments;
        }

        /**
         * @return Returns the ccReceivers.
         */
        public Map<String, String> getCcReceivers() {
            return ccReceivers;
        }

        /**
         * @return Returns the receivers.
         */
        public Map<String, String> getReceivers() {
            return receivers;
        }

        /**
         * @return Returns the aHtml.
         */
        public String getAHtml() {
            return aHtml;
        }

        /**
         * @param html The aHtml to set.
         */
        public void setAHtml(String html) {
            aHtml = html;
        }

}
