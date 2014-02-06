package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import play.db.jpa.Blob;
import play.db.jpa.Model;


@Entity
public class Component extends Model {

	public String model;
	@ManyToOne
	public ComponentTrademark trademark;
	@ManyToOne
	public ComponentType type;
	public String partNumber;
	public Blob image;
//	public Map<String, String> metadata;
//	public List<String> deviceCompatibility;

}
