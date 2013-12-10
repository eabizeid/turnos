package models;

import com.google.code.morphia.annotations.Entity;

import play.modules.morphia.Model;

@Entity("pendings")
public class Pending extends Model {

	public String question;
	public int quantity = 1;
}
