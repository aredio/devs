package br.com.rarp.utils;

import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Email {
	private String asunto;
	private StringBuilder destinatarios;
	private StringBuilder messagen;

	public void enviar() {
		Properties props = new Properties();
		/**
		 * Parâmetros de conexão com servidor Gmail props.put("mail.smtp.host",
		 * "smtp.gmail.com"); props.put("mail.smtp.socketFactory.port", "465");
		 * props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		 * props.put("mail.smtp.auth", "true"); props.put("mail.smtp.port", "465");
		 */

		props.put("mail.smtp.host", "smtp.gmail.com");

		props.put("mail.smtp.auth", "true");
		props.put("mail.debug", "true");
		props.put("mail.smtp.debug", "true");
		props.put("mail.mime.charset", "ISO-8859-1");
		props.put("mail.smtp.port", "465");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.fallback", "false");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

		Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("rarpsistesma@gmail.com", "pidsrarp");
			}
		});
		/** Ativa Debug para sessão */
		session.setDebug(true);

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("rarpsistesma@gmail.com")); // Remetente
			Address[] toUser = InternetAddress // Destinatário(s)
					.parse(destinatarios.toString());

			message.setRecipients(Message.RecipientType.TO, toUser);
			message.setSubject(asunto);// Assunto
			message.setText(messagen.toString());
			/** Método para enviar a mensagem criada */
			Transport.send(message);

			System.out.println("Feito!!!");

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

	public String getAsunto() {
		return asunto;
	}

	public void setAsunto(String asunto) {
		this.asunto = asunto;
	}

	public void addMessagem(String texto) {
		if (messagen == null) {
			messagen = new StringBuilder();
		}

		messagen.append(texto);
		messagen.append(" ");
	}

	public void addDestinatario(String texto) {
		boolean primerio = false;
		if (destinatarios == null) {
			destinatarios = new StringBuilder();
			primerio = true;
		}

		if (!primerio) {
			destinatarios.append(",");
		}
		destinatarios.append(texto);

	}

}
