package service.mailer;

import java.net.URL;

import models.Mail;

import org.apache.commons.mail.HtmlEmail;

public class MailSender {

	public void sendEmail(Mail mail) throws Exception {

		HtmlEmail email = new HtmlEmail();
		email.addTo(mail.mail);
		email.setFrom("eduardo.abizeid@gmail.com", "Titus");
		email.setSubject("Hemos resuleto tu búsqueda");
		// embed the image and get the content id
		// set the html message
		email.setHtmlMsg("<html><h1>Titus</h1></html>");
		// set the alternative message
		email.setTextMsg("Your email client does not support HTML, too bad :(");
		play.libs.Mail.send(email);
	}
}
