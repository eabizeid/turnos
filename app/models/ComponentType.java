package models;

import javax.persistence.Entity;

import play.db.jpa.Model;

@Entity
public class ComponentType extends Model {
	public String description;
	
	@Override
	public String toString() {
		return description;
	}
}
