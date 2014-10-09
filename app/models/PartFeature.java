package models;

import com.google.gson.annotations.Expose;
import play.db.jpa.Model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * @author ezeid on 5/28/14.
 */
@Entity
public class PartFeature extends Model{

    @ManyToOne(cascade= CascadeType.PERSIST)
    public Feature specification;
    public String value;
    @Expose
    @ManyToOne(cascade=CascadeType.PERSIST)
    public Part part;


}
