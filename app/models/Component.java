package models;

import java.util.List;
import java.util.Map;


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
	public Map<String, String> metadata;
	public List<String> deviceCompatibility;
	
}
