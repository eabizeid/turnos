package models;

import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;

import play.db.jpa.Model;


@Entity
public class Mail extends Model {

	public String mail;
}
