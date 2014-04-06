package controllers;

import java.io.File;
import java.util.Date;
import java.util.List;

import mercadopago.MP;
import models.Component;
import models.ComponentTrademark;
import models.ComponentType;
import models.Mail;
import models.Pending;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import play.mvc.Controller;
import service.feeder.FileFeeder;
import service.mailer.MailSender;

import com.google.common.collect.Lists;

public class Titus extends Controller {
	
	public static void search() {
		renderArgs.put("trademarks", ComponentTrademark.findAll());
		renderArgs.put("types", ComponentType.findAll());
		renderArgs.put("allModels", Component.findAll());
		renderArgs.put("step", 1);
        render();
    }
	
	
	public static void makeASearch(String trademark, String model, String type) {
		List<Component> components = Lists.newArrayList();
		try {
			
			components = Component.find("select c from Component c," +
					"								   ComponentTrademark tr, ComponentType ty " +
					"						where c.trademark = tr and c.type = ty and tr.description = ? and" +
					" 							  UPPER(c.model) = ? and ty.description = ?", trademark, StringUtils.upperCase(model), type).fetch();
			

		} catch( Exception e) {
			e.printStackTrace();
		}
		String query = StringUtils.EMPTY;
		if (components.isEmpty()) {
			if (model != null && !model.isEmpty()) {
				query = "modelo: ".concat(model);
			}
			if (trademark != null && !trademark.isEmpty()) {
				query =query.concat("; marca: ").concat(trademark);
			}
			if (type != null && !type.isEmpty()) {
				query = query.concat("; tipo: ").concat(type);
			}
		}
		renderArgs.put("query", query);
		renderArgs.put("step", 2);
		
		render(components);
    }
	
	public static void noResults(String mail, String query) {
		List<Pending> pendings = Pending.find("byQuestion", query).fetch();
		Pending pending = new Pending();
		if (!pendings.isEmpty()) {
			pending = pendings.get(0);
			pending.quantity++;
		}
		List<Mail> mails = Mail.find("byMail", mail).fetch();
		Mail m;
		if (mails == null || mails.isEmpty()) {
			m = new models.Mail();
			m.mail = mail;
		} else {
			m = mails.get(0);
		}
		pending.mails.add(m);
		pending.question = query;
		pending.timeInMs = (new Date()).getTime();
		pending.save();
		SimpleEmail email = new SimpleEmail();
		try {
			email.setFrom("pruebas@pop-tech.com.ar");
			email.addTo("pruebas@pop-tech.com.ar");
			email.setSubject("Se generó un nuevo pendiente");
			email.setMsg(query.concat("\n").concat("Cliente: ".concat(mail)));
		} catch (EmailException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		play.libs.Mail.send(email);
		
		flash.success("Muchas Gracias!");
		search();
		
	}
	
	public static void toFeed() {
        render();
    }
	
	public static void feedHim(File food) {
		try {
			new FileFeeder().process(food);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void pendings() {
		List<Pending> pendings = Pending.findAll();
		renderArgs.put("trademarks", ComponentTrademark.findAll());
		renderArgs.put("types", ComponentType.findAll());
		render(pendings);
	}
	
	public static void resolvePending(Component component, Long idType, Long idTrademark, String pendingToResolve) {
		try { 
			
			component.trademark = ComponentTrademark.findById(idTrademark);
			component.type = ComponentType.findById(idType);
			component.save();
			Pending pending = Pending.findById(Long.valueOf(pendingToResolve));
			long responseTime = (new Date()).getTime() - pending.timeInMs;
			for (Mail mail : pending.mails) {
				
				MailSender sender = new MailSender();
				sender.sendEmail(mail, component, responseTime, generateCheckoutURL(component));
				
			}
			((Pending)Pending.findById(Long.valueOf(pendingToResolve))).delete();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		pendings();
	}
	
	public static void viewDetail(String componentId) {
		Component component = Component.findById(Long.valueOf(componentId));
		renderArgs.put("checkoutURL", generateCheckoutURL(component)); 

		render(component);
	}
	
	private static String generateCheckoutURL(Component component) {
		
		String clientId = "8077350156322774";
		String clientSecret = "8Hl48rbM9UWPjQW3ug0xjCaujPQ5bcpk";

		MP mp = new MP(clientId, clientSecret);

		String checkoutURL = StringUtils.EMPTY;
		try {
			JSONObject preference = mp.createPreference("{'items':[{'title':'"+component.toString()+"','quantity':1,'currency_id':'ARS','unit_price':" + component.price + "}]}");
			checkoutURL = preference.getJSONObject("response").getString("sandbox_init_point");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return checkoutURL;
	}
	
	public static void clients() {
		List<Mail> clients = Mail.findAll(); 
		render(clients);
	}

}
