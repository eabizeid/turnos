package controllers;

import java.io.ByteArrayInputStream;

import models.Component;

import org.h2.store.FileStore;

import play.mvc.Controller;

public class ShowImage extends Controller {
	public static void render(String id) {
        Component c = Component.findById(Long.valueOf(id));
      //  response.setContentTypeIfNotSet(c.image.type());
        response.setHeader("Cache-Control", "public, max-age=31536000");
       // renderBinary(c.image.get());
    }
}
