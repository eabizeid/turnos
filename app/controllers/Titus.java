package controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import models.Component;
import models.Pending;
import play.modules.morphia.Model;
import play.modules.morphia.MorphiaPlugin;
import play.modules.morphia.Model.MorphiaQuery;
import play.mvc.Controller;
import play.mvc.results.Result;
import service.feeder.FileFeeder;

import com.google.code.morphia.query.Criteria;
import com.google.code.morphia.query.CriteriaContainer;
import com.google.code.morphia.query.Query;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.ning.http.multipart.FilePart;

public class Titus extends Controller {
	public static void search() {
        render();
    }
	
	public static void search(List<Component> components) {
        render(components);
    }
	
	public static void makeASearch(String query) {
		List<Component> components = Lists.newArrayList();
		String[] parameters = query.split(" ");
		try {
			Query<Component> q = MorphiaPlugin.ds().find(Component.class);
			List<Criteria> criteria = Lists.newArrayList();
			for(String parameter : parameters) {
				String[] split = parameter.split("=");
				if (split[0].equals("marca")) {
					criteria.add(Component.createQuery().criteria("marca").containsIgnoreCase(split[1]));
				} else if (split[0].equals("modelo")) {
					criteria.add(Component.createQuery().criteria("modelo").containsIgnoreCase(split[1]));
				} else if (split[0].equals("submodelo")) {
					criteria.add(Component.createQuery().criteria("submodelo").containsIgnoreCase(split[1]));
				} else if (split[0].equals("tipo")) {
					criteria.add(Component.createQuery().criteria("tipo").containsIgnoreCase(split[1]));
				} else if (split[0].equals("parte")) {
					criteria.add(Component.createQuery().criteria("parte").containsIgnoreCase(split[1]));
				}
				
			}
			CriteriaContainer[] andCriteria = criteria.toArray(new CriteriaContainer[criteria.size()]); 

			MorphiaQuery q2 = Component.q();
			q2.and(andCriteria);
			components = q2.asList();
		} catch( Exception e) {
			e.printStackTrace();
		}
		if (components.isEmpty()) {
			MorphiaQuery find = Pending.find("byQuestion", query);
			List<Pending> pendings = find.asList();
			Pending pending = new Pending();
			if (!pendings.isEmpty()) {
				pending = pendings.get(0);
				pending.quantity++;
			}
			pending.question = query;
			pending.save();
		}
			
		render("Titus/search.html", components);
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
			Pending.findById(Long.valueOf(pendingToResolve)).delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
		pendings();
	}

}
