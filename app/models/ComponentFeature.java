package models;

import play.db.jpa.Model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * @author ezeid on 5/28/14.
 */
@Entity
public class ComponentFeature extends Model{
    @ManyToOne(cascade= CascadeType.PERSIST)
    public Feature specification;
    public String value;
    @ManyToOne(cascade=CascadeType.PERSIST)
    public Component component;


}
