//package monitoring.core.email;
//
//import java.io.UnsupportedEncodingException;
//import java.util.Date;
//import java.util.Properties;
//
//import javax.activation.DataHandler;
//import javax.activation.DataSource;
//import javax.activation.FileDataSource;
//import javax.mail.BodyPart;
//import javax.mail.Message;
//import javax.mail.MessagingException;
//import javax.mail.Multipart;
//import javax.mail.Session;
//import javax.mail.Transport;
//import javax.mail.internet.InternetAddress;
//import javax.mail.internet.MimeBodyPart;
//import javax.mail.internet.MimeMessage;
//import javax.mail.internet.MimeMultipart;
//
//public class EmailUtil {
//
//    /**
//     * Utility method to send simple HTML email
//     * @param session
//     * @param toEmail
//     * @param subject
//     * @param body
//     */
//    private void setMailServerProperties()
//    {
//        Properties emailProperties = System.getProperties();
//        emailProperties.put("mail.smtp.port", "586");
//        emailProperties.put("mail.smtp.auth", "true");
//        emailProperties.put("mail.smtp.starttls.enable", "true");
//        mailSession = Session.getDefaultInstance(emailProperties, null);
//    }
//
//    private MimeMessage draftEmailMessage() throws AddressException, MessagingException
//    {
//        String[] toEmails = { "computerbuzz@gmail.com" };
//        String emailSubject = "Test email subject";
//        String emailBody = "This is an email sent by http://www.computerbuzz.in.";
//        MimeMessage emailMessage = new MimeMessage(mailSession);
//        /**
//         * Set the mail recipients
//         * */
//        for (int i = 0; i < toEmails.length; i++)
//        {
//            emailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmails[i]));
//        }
//        emailMessage.setSubject(emailSubject);
//        /**
//         * If sending HTML mail
//         * */
//        emailMessage.setContent(emailBody, "text/html");
//        /**
//         * If sending only text mail
//         * */
//        //emailMessage.setText(emailBody);// for a text email
//        return emailMessage;
//    }
//
//    private void sendEmail() throws AddressException, MessagingException
//    {
//        /**
//         * Sender's credentials
//         * */
//        String fromUser = "user-email@gmail.com";
//        String fromUserEmailPassword = "*******";
//
//        String emailHost = "smtp.gmail.com";
//        Transport transport = mailSession.getTransport("smtp");
//        transport.connect(emailHost, fromUser, fromUserEmailPassword);
//        /**
//         * Draft the message
//         * */
//        MimeMessage emailMessage = draftEmailMessage();
//        /**
//         * Send the mail
//         * */
//        transport.sendMessage(emailMessage, emailMessage.getAllRecipients());
//        transport.close();
//        System.out.println("Email sent successfully.");
//    }
//}
//
//    public static void main(String[] args) {
//        sendEmail();
//    }
//}