package models;

import com.google.gson.annotations.Expose;
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

    @Expose
    @ManyToOne(cascade=CascadeType.PERSIST)
    public ComponentType type;
    public String description;
    @Expose
    public Blob image;
    public BigDecimal price;
    @Expose
    @OneToMany(mappedBy="part", cascade=CascadeType.ALL)
    public List<PartFeature> partFeature;

    public String toJson() {

        StringBuffer buffer = new StringBuffer();
        buffer.append(" { \"part\": { \"description\": " );
        buffer.append("\"" + this.description + "\", ");
        buffer.append("\"componentType\": \"" + this.type.toString() + "\", ");
        buffer.append("\"price\": \"" + this.price + "\", ");

        buffer.append("\"partfeatures\": [");
        for (PartFeature pf : this.partFeature) {
            buffer.append("{ \"specification\": \"" + pf.specification.description + "\", ");
            buffer.append(" \"value\": \"" + pf.value.toString() + "\" }");
        }
        buffer.append("]");

        buffer.append("}");

        buffer.append("}");


        return buffer.toString();
    }
}
