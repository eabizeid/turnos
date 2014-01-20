package models;

import java.util.List;

import com.google.code.morphia.annotations.Entity;
import com.google.common.collect.Lists;

import play.modules.morphia.Model;

@Entity("pendings")
public class Pending extends Model {

	public String question;
	public int quantity = 1;
	public List<String> mails = Lists.newArrayList();
}
