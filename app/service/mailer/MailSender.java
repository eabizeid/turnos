package service.mailer;

import models.Component;
import models.Mail;

import org.apache.commons.mail.HtmlEmail;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

public class MailSender {

	private static final String HEMOS_RESUELTO_TU_CONSULTA_MAS_RAPIDO_TITUS = "HEMOS RESUELTO TU CONSULTA . MAS RAPIDO. TITUS.";

	/**
	 * 
	 * @param mail
	 * @throws Exception
	 */
	public void sendEmail(Mail mail, Component component, long responseTime) throws Exception {
		STGroup group = new STGroupFile("templates/mail-group.stg",'$', '$');
		ST template = group.getInstanceOf("pendingResolved");
		template.add("time", responseTime /60000);
		template.add("checkoutURL", "1111");
		template.add("component", component);
		HtmlEmail email = new HtmlEmail();
		email.addTo(mail.mail);
		email.setFrom("eduardo.abizeid@gmail.com", "Titus");
		email.setSubject(HEMOS_RESUELTO_TU_CONSULTA_MAS_RAPIDO_TITUS);
		// embed the image and get the content id
		// set the html message
		System.out.println(template.render());
		email.setHtmlMsg(template.render());
		// set the alternative message
		email.setTextMsg("Your email client does not support HTML, too bad :(");
		play.libs.Mail.send(email);
	}
}
