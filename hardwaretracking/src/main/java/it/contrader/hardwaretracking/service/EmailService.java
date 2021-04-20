package it.contrader.hardwaretracking.service;

import com.sun.mail.smtp.SMTPTransport;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

@Service
public class EmailService {

        public void confirmLoanEmail(String firstName, String operationCode, String model, String email)
                throws MessagingException {
            SMTPTransport smtpTransport = mySmtpTransport(loanMessage(firstName, operationCode, model, email));
        }

        public void sendNewPasswordEmail(String firstName, String password, String email)
                throws AddressException, MessagingException{
            SMTPTransport smtpTransport = mySmtpTransport(welcomeMessage(firstName, password, email));
        }

        private Message loanMessage(String firstName, String operationCode, String model, String email) throws MessagingException {
            Message message = new MimeMessage(getEmailSession());
            message.setFrom(new InternetAddress("briffasimone94sr@gmail.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email, false));
            message.setRecipients(Message.RecipientType.CC, InternetAddress.parse("", false));
            message.setSubject("CONTRADER - Conferma prestito dispositivo");
            message.setText("Ciao " + firstName + ", \n\nConfermato il prestito per " + model + "."
                    + "\n Il tuo codice operazione: " + operationCode + "." + "\n\nBuon lavoro!");
            message.setSentDate(new Date());
            message.saveChanges();

            return message;
        }

        private Message welcomeMessage(String firstName, String password, String email)
                throws AddressException, MessagingException {

            Message message = new MimeMessage(getEmailSession());
            message.setFrom(new InternetAddress("briffasimone94sr@gmail.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email, false));
            message.setRecipients(Message.RecipientType.CC, InternetAddress.parse("", false));
            message.setSubject("CONTRADER - Welcome to Hardware Tracking!");
            message.setText("Hello " + firstName + ", \n\nYour account password is: " + password
                    + "\n\n Simone");
            message.setSentDate(new Date());
            message.saveChanges();

            return message;
        }

        SMTPTransport mySmtpTransport(Message message) throws MessagingException {
            SMTPTransport smtpTransport = (SMTPTransport) getEmailSession().getTransport("smtps");
            smtpTransport.connect("smtp.gmail.com", "briffasimone94sr@gmail.com", "negrita8023");
            smtpTransport.sendMessage(message, message.getAllRecipients());
            smtpTransport.close();
            return smtpTransport;
        }

        private Session getEmailSession() {
            Properties properties = System.getProperties();
            properties.put("mail.smtp.host", "smtp.gmail.com");
            properties.put("mail.smtp.auth", true);
            properties.put("mail.smtp.port", 465);
            properties.put("mail.smtp.starttls.enable", true);
            properties.put("mail.smtp.starttls.required", true);

            return Session.getInstance(properties, null);
        }

    }


