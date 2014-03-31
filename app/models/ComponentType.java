package models;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import play.db.jpa.Model;

@Entity
public class ComponentType extends Model {
	public String description;
	@OneToMany(mappedBy="type", cascade=CascadeType.ALL)
	public List<Component> component;
	@Override
	public String toString() {
		return description;
	}
}
