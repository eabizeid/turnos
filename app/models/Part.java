package models;

import play.db.jpa.Blob;
import play.db.jpa.Model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author ezeid on 5/27/14.
 */
@Entity
public class Part extends Model {

    @ManyToOne(cascade=CascadeType.PERSIST)
    public ComponentType type;
    public String description;
    public Blob image;
    public BigDecimal price;
    @OneToMany(mappedBy="part", cascade=CascadeType.ALL)
    public List<PartFeature> partFeature;

}
