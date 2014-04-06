package models;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.Where;

import play.db.jpa.Model;

import com.google.common.collect.Lists;

@Entity
public class Pending extends Model {

	public String question;
	public int quantity = 1;
	@OneToMany(cascade=CascadeType.PERSIST, fetch=FetchType.EAGER)
    @JoinColumn(name="description")
    @Where(clause="1=1")
	public List<Mail> mails = Lists.newArrayList();
	public long timeInMs;
}
