package controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import models.Component;
import play.modules.morphia.Model.MorphiaQuery;
import play.mvc.Controller;
import play.mvc.results.Result;
import service.feeder.FileFeeder;

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

}
