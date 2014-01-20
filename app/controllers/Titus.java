package controllers;

import java.io.File;
import java.util.List;

import models.Component;
import models.Pending;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

import play.libs.Mail;
import play.modules.morphia.Model.MorphiaQuery;
import play.modules.morphia.MorphiaPlugin;
import play.mvc.Controller;
import service.feeder.FileFeeder;

import com.google.code.morphia.query.Criteria;
import com.google.code.morphia.query.CriteriaContainer;
import com.google.code.morphia.query.Query;
import com.google.common.collect.Lists;

public class Titus extends Controller {
	public static void search() {
		renderArgs.put("step", 1);
        render();
    }
	
	public static void search(List<Component> components) {
        render(components);
    }
	
	public static void makeASearch(String marca, String modelo, String submodelo, String tipo, String nroDeParte) {
		List<Component> components = Lists.newArrayList();
		try {
			Query<Component> q = MorphiaPlugin.ds().find(Component.class);
			List<Criteria> criteria = Lists.newArrayList();
			if (marca != null && !marca.isEmpty())
				criteria.add(Component.createQuery().criteria("marca").containsIgnoreCase(marca));
			if (modelo != null && !modelo.isEmpty())
				criteria.add(Component.createQuery().criteria("modelo").containsIgnoreCase(modelo));
		
			CriteriaContainer[] andCriteria = criteria.toArray(new CriteriaContainer[criteria.size()]); 

			MorphiaQuery q2 = Component.q();
			q2.and(andCriteria);
			components = q2.asList();
		} catch( Exception e) {
			e.printStackTrace();
		}
		String query = StringUtils.EMPTY;
		if (components.isEmpty()) {
			if (modelo != null && !modelo.isEmpty()) {
				query = "modelo = ".concat(modelo);
			}
			if (marca != null && !marca.isEmpty()) {
				query.concat("marca = ").concat(marca);
			}
		}
		renderArgs.put("query", query);
		renderArgs.put("step", 2);
		render("Titus/search.html", components);
    }
	
	public static void noResults(String mail, String query) {
		MorphiaQuery find = Pending.find("byQuestion", query);
		List<Pending> pendings = find.asList();
		Pending pending = new Pending();
		if (!pendings.isEmpty()) {
			pending = pendings.get(0);
			pending.quantity++;
		}
		pending.mails.add(mail);
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
		Mail.send(email);
		
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
			for (String mail : pending.mails) {
				
				SimpleEmail email = new SimpleEmail();
				try {
					email.setFrom("titus@titus.com.ar");
					email.addTo(mail);
					email.setSubject("We have a result for you");
					email.setMsg("Resuelto");
				} catch (EmailException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Mail.send(email);
				
			}
			Pending.findById(Long.valueOf(pendingToResolve)).delete();
			
			
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
