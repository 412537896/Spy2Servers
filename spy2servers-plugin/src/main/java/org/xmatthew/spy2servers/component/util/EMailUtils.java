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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.MultiPartEmail;
import org.apache.commons.mail.SimpleEmail;

/**
 * @author Matthew Xie
 *
 */
public final class EMailUtils {


    /**
     * private constructor
     */
    private EMailUtils() {

    }

    /**
     * <p>
     * send simple plain text mail
     * </p>
     *
     * @param mailBody
     * @throws EmailException email about exception
     */
    public static void sendSimpleEmail(MailBody mailBody)
                                        throws EmailException {
        SimpleEmail email = new SimpleEmail();
        email.setHostName(mailBody.getLoginServer());
        email.setAuthentication(mailBody.getLoginName(), mailBody.getLoginPassword());

        //receivers
        Map<String, String> receivers = mailBody.getReceivers();
        parseReceivers(email, receivers);
        //cc receivers
        receivers = mailBody.getCcReceivers();
        parseCCRecievers(email, receivers);

        email.setFrom(mailBody.getSender(), mailBody.getSender_nick());
        email.setSubject(mailBody.getSubject());
        email.setMsg(mailBody.getBody());
        email.send();
    }


    /**
     * <p>
     * parse email receivers info from the map and add to the email receivers list</p>
     *
     * @param email commons email instance.
     * @param receivers receivers map
     * @throws EmailException Email about exception.
     */
    private static void parseReceivers(Email email,
            Map<String, String> receivers) throws EmailException {
        if (receivers != null && receivers.size() > 0) {
            Iterator<Map.Entry<String, String>> iter = receivers.entrySet().iterator();
            Map.Entry<String, String> receiver;
            while (iter.hasNext()) {
                receiver = iter.next();
                email.addTo(receiver.getKey(), receiver.getValue());
            }
        }
    }

    /**
     * @param email commons email instance.
     * @param receivers receivers map
     * @throws EmailException Email about exception.
     */
    private static void parseCCRecievers(Email email,
            Map<String, String> receivers) throws EmailException {
        if (receivers != null && receivers.size() > 0) {
            Iterator<Map.Entry<String, String>> iter = receivers.entrySet().iterator();
            Map.Entry<String, String> receiver;
            while (iter.hasNext()) {
                receiver = iter.next();
                email.addCc(receiver.getKey(), receiver.getValue());
            }
        }
    }

    /**
     * @param email commons email instance.
     * @param attachments attachment map
     * @throws EmailException Email about exception.
     */
    private static void parseAttatchments(MultiPartEmail email,
            Map<String, EmailAttachment> attachments) throws EmailException {
        if (attachments != null && attachments.size() > 0) {
            Iterator<Map.Entry<String, EmailAttachment>> iter = attachments.entrySet().iterator();
            Map.Entry<String, EmailAttachment> attachment;
            while (iter.hasNext()) {
                attachment = iter.next();
                email.attach(attachment.getValue());
            }
        }

    }


    /**
     * <p>
     * send Multi part mail with attachment
     * </p>
     *
     * @param mailBody
     * @throws EmailException email about exception
     */
    public static void sendMultiPartEmail(MailBody mailBody)
                                            throws EmailException {
        MultiPartEmail email = new MultiPartEmail();
        email.setHostName(mailBody.getLoginServer());
        email.setAuthentication(mailBody.getLoginName(), mailBody.getLoginPassword());

        //receivers
        Map<String, String> receivers = mailBody.getReceivers();
        parseReceivers(email, receivers);
        //cc receivers
        receivers = mailBody.getCcReceivers();
        parseCCRecievers(email, receivers);
        //attatchments
        Map<String, EmailAttachment> attatchments = mailBody.getAttachments();
        parseAttatchments(email, attatchments);

        email.setFrom(mailBody.getSender(), mailBody.getSender_nick());
        email.setSubject(mailBody.getSubject());
        email.setMsg(mailBody.getBody());
        email.send();
    }

    /**
     * <p>
     * send html body mail
     * </p>
     *
     * @param mailBody
     * @throws EmailException email about exception
     */
    public static void sendHtmlEmail(MailBody mailBody)
                                        throws EmailException {

        HtmlEmail email = new HtmlEmail();
        email.setHostName(mailBody.getLoginServer());
        email.setAuthentication(mailBody.getLoginName(), mailBody.getLoginPassword());

        //receivers
        Map<String, String> receivers = mailBody.getReceivers();
        parseReceivers(email, receivers);
        //cc receivers
        receivers = mailBody.getCcReceivers();
        parseCCRecievers(email, receivers);
        //attatchments
        Map<String, EmailAttachment> attatchments = mailBody.getAttachments();
        parseAttatchments(email, attatchments);

        String aHtml = mailBody.getAHtml();
        if (StringUtils.isNotBlank(aHtml)) {
            email.setHtmlMsg(aHtml);
        }

        email.setFrom(mailBody.getSender(), mailBody.getSender_nick());
        email.setSubject(mailBody.getSubject());
        email.setMsg(mailBody.getBody());
        email.send();
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        MailBody mailBody = new MailBody();
        mailBody.setLoginServer("smtp.163.com");
        mailBody.setLoginName("ant_miracle");
        mailBody.setLoginPassword("200010");

        mailBody.setSubject("test subject");
        mailBody.setBody("test\n body");
        mailBody.setSender("ant_miracle@163.com", "matthew");
        mailBody.addReceiver("ant1_miracle@163.com", "xml");
        mailBody.addReceiver("xiemalin@ejianlong.com", "xiematthew");
        try {
            mailBody.addAttachment(new URL("http://www.apache.org/images/asf_logo_wide.gif"));
            mailBody.addAttachment("C:/Spy2Logger/Spy2Logger/lang/chinese.lng");
        } catch (MalformedURLException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        try {
            //sendSimpleEmail(mailBody);
            //sendMultiPartEmail(mailBody);
            sendHtmlEmail(mailBody);
        } catch (EmailException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
