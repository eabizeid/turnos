package models;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;


import play.modules.morphia.Blob;
import play.modules.morphia.Model;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Index;

@Entity("components")
public class Component extends Model {


	public String modelo;
	public String submodelo;
	public String marca;
	public String tipo;
	public String nroDeParte;
	public Blob image;
	public Map<String, String> metadata;
	public List<String> deviceCompatibility;

	public void setImage(File file) {
		String type = "image/" + FilenameUtils.getExtension(file.getName());
		image = new Blob(file, type);
	}
}
