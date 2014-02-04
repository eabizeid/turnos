package models;

import javax.persistence.Entity;

import play.db.jpa.Blob;
import play.db.jpa.Model;


@Entity
public class Component extends Model {

	public ComponentModel model;
	public ComponentTrademark tradeMark;
	public ComponentType type;
	public String partNumber;
	public Blob image;
//	public Map<String, String> metadata;
//	public List<String> deviceCompatibility;

}
