package com.sinqupa.cliente.presenter;

import android.content.Context;
import android.os.StrictMode;
import com.sdsmdg.tastytoast.TastyToast;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class HelpPresenterImpl implements IHelpPresenter {
    private Context context;
    private static Session session;         // Sesion de correo

    // Credenciales de usuario
    private static String email = "SinQupa2019@gmail.com";   // Dirección de correo
    private static String password = "SinQupa@123456";       // Contraseña

    // Correo al que enviaremos el mensaje
    private static String emailToSend = "SinQupa2019@gmail.com";

    @Override
    public void getFragmentContext(Context context) {
        this.context = context;
    }

    @Override
    public void sendEmail(String subject, String description) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        // Propiedades de la sesion
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.googlemail.com");
        properties.put("mail.smtp.socketFactory.port", "465");
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.port", "465");

        try {
            //Configuramos la sesión
            session = Session.getDefaultInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(email,password);
                }
            });

            if (session != null){
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(email));
                message.setSubject(subject);
                message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(emailToSend));
                message.setContent(description,"text/html;charset=utf-8");
                Transport.send(message);
                TastyToast.makeText(context, "Mensaje Enviado", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
            }
        } catch (MessagingException e) {}
    }
}
