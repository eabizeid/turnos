package models;

import java.math.BigDecimal;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.apache.commons.lang.StringUtils;

import play.db.jpa.Blob;
import play.db.jpa.Model;


@Entity
public class Component extends Model {

	public String model;
	@ManyToOne(cascade=CascadeType.ALL)
	public ComponentTrademark trademark;
	@ManyToOne(cascade=CascadeType.ALL)
	public ComponentType type;
	public String partNumber;
	public Blob image;
	public BigDecimal price;
//	public Map<String, String> metadata;
//	public List<String> deviceCompatibility;

	@Override
	public String toString() {
		return type.description + " " + trademark.description + " " + model;
	}
}
