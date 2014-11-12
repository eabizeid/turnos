package controllers;

import java.io.File;
import java.lang.reflect.Modifier;
import java.util.Date;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import mercadopago.MP;
import models.*;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import play.mvc.Controller;
import service.engine.CompatibilityEngine;
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
        Part salablePart = null;
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
		} else {
            Component searchedComponent = components.get(0);
            renderArgs.put("component", searchedComponent);
            List<Part> parts = Lists.newArrayList();
            parts = Part.find("select p from Part p, ComponentType ty  where ty.description = ?", searchedComponent.type.description).fetch();
            //Analizo compatibilidad
            for (Part part : parts) {
                if (CompatibilityEngine.getEngine().areThereCompatibility(part, searchedComponent)) {
                    salablePart = part;
                    break;
                }
            }

        }
        renderArgs.put("model", model);
        renderArgs.put("trademark", trademark);
        renderArgs.put("type", type);
		renderArgs.put("query", query);
		renderArgs.put("step", 2);
		
		render(salablePart);
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


	public static void viewDetail(String componentId, String partId) {
		Component component = Component.findById(Long.valueOf(componentId));

        Part part = Part.findById(Long.valueOf(partId));
		//renderArgs.put("checkoutURL", generateCheckoutURL(component));
        renderArgs.put("part", part);

		render(component);
	}
	
	private static String generateCheckoutURL(Component component) {
		
		String clientId = "8077350156322774";
		String clientSecret = "8Hl48rbM9UWPjQW3ug0xjCaujPQ5bcpk";

		MP mp = new MP(clientId, clientSecret);

		String checkoutURL = StringUtils.EMPTY;
		try {
			JSONObject preference = mp.createPreference("{'items':[{'title':'"+component.toString()+"','quantity':1,'currency_id':'ARS','unit_price':" + "component.price" + "}]}");
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
	

	public static void about() {
        render();
    }

    public static void getACompatibilityPart(String model, String type, String trademark) {
       Part part = searchAPart(trademark, model, type);
       System.out.println(part);
       renderJSON(part.toJson());
    }

    public static void getAPart(Long id) {
        Part part = Part.findById(id);
        renderJSON(part.toJson());
    }

    /* MOCK */
    private  static List<PartFeature> getPartFeatures() {
        List<PartFeature> partFeatures = Lists.newArrayList();
        PartFeature pf = new PartFeature();
        pf.value = "5V";
        Feature feature = new Feature();
        feature.id = 1L;
        feature.description = "AMPERAJE";
        pf.specification = feature;
        partFeatures.add(pf);

        pf = new PartFeature();
        pf.value = "19V";
        feature = new Feature();
        feature.id = 1L;
        feature.description = "VOLTAJE";
        pf.specification = feature;
        partFeatures.add(pf);
        pf.specification = feature;

        return partFeatures;
    }
    public static Part searchAPart(String trademark, String model, String type) {
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
        Part salablePart = null;
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
        } else {
            Component searchedComponent = components.get(0);
            renderArgs.put("component", searchedComponent);
            List<Part> parts = Lists.newArrayList();
            parts = Part.find("select p from Part p, ComponentType ty  where ty.description = ?", StringUtils.upperCase(searchedComponent.type.description)).fetch();
            //Analizo compatibilidad
            for (Part part : parts) {
                if (CompatibilityEngine.getEngine().areThereCompatibility(part, searchedComponent)) {
                    salablePart = part;
                    break;
                }
            }

        }

        return salablePart;
    }

    public static void getAFeature(Long id) {
        Feature feature = Feature.findById(id);
        renderJSON(feature.toString());
    }

    public static void getATrademark(Long id) {
        ComponentTrademark feature = ComponentTrademark.findById(id);
        renderJSON(feature.toString());
    }

    public static void getAComponentType(Long id) {
        ComponentType feature = ComponentType.findById(id);
        renderJSON(feature.toString());
    }


    public static void showImage(Long id) {
        Picture picture = Picture.findById(id);
        response.setContentTypeIfNotSet(picture.image.type());
        renderBinary(picture.image.getFile());
    }

}
