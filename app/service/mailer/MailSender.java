package service.mailer;

import models.Component;
import models.Mail;

import org.apache.commons.mail.HtmlEmail;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

public class MailSender {

	/**
	 * 
	 * @param mail
	 * @throws Exception
	 */
	public void sendEmail(Mail mail, Component component) throws Exception {
		STGroup group = new STGroupFile("templates/mail-group.stg",'$', '$');
		ST template = group.getInstanceOf("pendingResolved");
		template.add("price", "1222");
		template.add("time", "1222");
		template.add("checkoutURL", "1111");
		
		HtmlEmail email = new HtmlEmail();
		email.addTo(mail.mail);
		email.setFrom("eduardo.abizeid@gmail.com", "Titus");
		email.setSubject("HEMOS RESUELTO TU CONSULTA . MAS RAPIDO. TITUS.");
		// embed the image and get the content id
		// set the html message
		System.out.println(template.render());
		email.setHtmlMsg(template.render());
		// set the alternative message
		email.setTextMsg("Your email client does not support HTML, too bad :(");
		play.libs.Mail.send(email);
	}
}
