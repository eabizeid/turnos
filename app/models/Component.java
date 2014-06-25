package models;

import java.math.BigDecimal;
import java.util.List;

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
	@ManyToOne(cascade=CascadeType.PERSIST)
	public ComponentTrademark trademark;
	@ManyToOne(cascade=CascadeType.PERSIST)
	public ComponentType type;
	public String partNumber;
    @OneToMany(mappedBy="component",cascade=CascadeType.PERSIST)
    List<ComponentFeature> compatibility;

	@Override
	public String toString() {
		return type.description + " " + trademark.description + " " + model;
	}
}
