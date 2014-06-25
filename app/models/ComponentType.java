package models;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import play.db.jpa.Model;

@Entity
public class ComponentType extends Model {
	public String description;

    @OneToMany(mappedBy="type", cascade=CascadeType.ALL)
	public List<Component> component;

    @ManyToMany(cascade = CascadeType.ALL)
    public List<Feature> features;

	@Override
	public String toString() {
		return description;
	}
}
