package models;

import javax.persistence.MappedSuperclass;

import play.db.jpa.Model;


public class BaseModel extends Model {

	public String description;
}
