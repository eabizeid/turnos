package models;

import play.db.jpa.Model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.List;

@Entity
public class Feature extends Model {

    public String description;
    @ManyToMany(mappedBy = "features")
    public List<ComponentType> types;
}