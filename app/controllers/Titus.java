package controllers;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;

import models.Component;
import play.modules.morphia.Model;
import play.modules.morphia.Model.MorphiaQuery;
import play.mvc.Controller;
import play.mvc.With;

@With(Secure.class)
public class Titus extends Controller {
	public static void search() {
        render();
    }
	
	public static void search(List<Component> components) {
        render(components);
    }
	
	public static void makeASearch(String query) {
		List<Component> components = Lists.newArrayList();
		try {
			MorphiaQuery q = Component.createQuery();
			q.or(q.criteria("marca").contains(query), q.criteria("modelo").contains(query),
					q.criteria("submodelo").contains(query), q.criteria("tipo").contains(query),
					q.criteria("nroDeParte").contains(query));
			components = q.asList();
			
		} catch( Exception e) {
			e.printStackTrace();
		}
			
		render("Titus/search.html", components);
    }

}
