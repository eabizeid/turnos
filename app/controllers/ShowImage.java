package controllers;

import java.io.ByteArrayInputStream;

import models.Component;

import models.Part;
import org.h2.store.FileStore;

import play.mvc.Controller;

public class ShowImage extends Controller {
	public static void render(String id) {
        Part part = Part.findById(Long.valueOf(id));
        response.setContentTypeIfNotSet(part.image.type());
        response.setHeader("Cache-Control", "public, max-age=31536000");
        renderBinary(part.image.get());
    }
}
