package controllers;

import java.io.File;
import java.util.List;

import models.Component;
import models.ComponentTrademark;
import models.ComponentType;
import models.Mail;
import models.Pending;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

import play.mvc.Controller;
import service.feeder.FileFeeder;

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
					"								   ComponentTrademark tr " +
					"						where c.trademark = tr and tr.description = ? and" +
					" 							  c.model = ?", trademark, model).fetch();
			

		} catch( Exception e) {
			e.printStackTrace();
		}
		String query = StringUtils.EMPTY;
		if (components.isEmpty()) {
			if (model != null && !model.isEmpty()) {
				query = "modelo: ".concat(model);
			}
			if (trademark != null && !trademark.isEmpty()) {
				query =query.concat(" marca:").concat(trademark);
			}
			if (type != null && !type.isEmpty()) {
				query = query.concat(" tipo: ").concat(type);
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
		Mail m = new models.Mail();
		m.mail = mail;
		pending.mails.add(m);
		pending.question = query;
		pending.save();
		SimpleEmail email = new SimpleEmail();
		try {
			email.setFrom("titus@titus.com.ar");
			email.addTo("eduardo.abizeid@gmail.com");
			email.setSubject("You have a new pending");
			email.setMsg(query.concat("\n").concat("Cliente: ".concat(mail)));
		} catch (EmailException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		play.libs.Mail.send(email);
		
		flash.success("Muchas Gracias!");
		render("Titus/search.html");
		
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
		render(pendings);
	}
	
	public static void resolvePending(Component component, String pendingToResolve) {
		try { 
			component.save();
			Pending pending = Pending.findById(Long.valueOf(pendingToResolve));
			for (Mail mail : pending.mails) {
				
				SimpleEmail email = new SimpleEmail();
				try {
					email.setFrom("titus@titus.com.ar");
					email.addTo(mail.mail);
					email.setSubject("We have a result for you");
					email.setMsg("Resuelto");
				} catch (EmailException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				play.libs.Mail.send(email);
				
			}
			((Pending)Pending.findById(Long.valueOf(pendingToResolve))).delete();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		pendings();
	}
	
	public static void viewDetail(String componentId) {
		Component component = Component.findById(Long.valueOf(componentId));
		render(component);
	}

}
