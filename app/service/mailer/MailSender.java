package service.mailer;

import javax.activation.DataSource;

import models.Component;
import models.Mail;

import org.apache.commons.io.IOUtils;
import org.apache.commons.mail.HtmlEmail;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import com.sun.istack.internal.ByteArrayDataSource;

public class MailSender {

	private static final String HEMOS_RESUELTO_TU_CONSULTA_MAS_RAPIDO_TITUS = "HEMOS RESUELTO TU CONSULTA . MAS RAPIDO. TITUS.";

	/**
	 * 
	 * @param mail
	 * @throws Exception
	 */
	public void sendEmail(Mail mail, Component component, long responseTime, String checkoutURl) throws Exception {
		STGroup group = new STGroupFile("templates/mail-group.stg",'$', '$');
		ST template = group.getInstanceOf("pendingResolved");
		template.add("time", responseTime /60000);
		template.add("checkoutURL", checkoutURl);
		template.add("component", component);
		HtmlEmail email = new HtmlEmail();
		email.addTo(mail.mail);
		email.setFrom("eduardo.abizeid@gmail.com", "Titus");
		email.setSubject(HEMOS_RESUELTO_TU_CONSULTA_MAS_RAPIDO_TITUS);
		// embed the image and get the content id
		// set the html message
	//	byte[] image = IOUtils.toByteArray(component.image.get());
		//DataSource ds = new ByteArrayDataSource(image, "image/jpeg");
		//String cid = email.embed(ds, "componente");
		//template.add("cid", cid);
		email.setHtmlMsg(template.render());
		// set the alternative message
		email.setTextMsg("Your email client does not support HTML, too bad :(");
		play.libs.Mail.send(email);
	}
}
