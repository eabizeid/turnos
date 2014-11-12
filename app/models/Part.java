package models;

import com.google.gson.annotations.Expose;
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
    @OneToOne(cascade=CascadeType.ALL)
    public Picture image;
    public BigDecimal price;
    @Expose
    @OneToMany(mappedBy="part", cascade=CascadeType.ALL)
    public List<PartFeature> partFeature;

    public String toJson() {

        StringBuffer buffer = new StringBuffer();
        buffer.append(" { \"part\": { \"id\":" + this.id +
                ", \"description\": " );
        buffer.append("\"" + this.description + "\", ");
        buffer.append("\"componentType\": " + this.type.toString() + ", ");
        buffer.append("\"price\": \"" + this.price + "\", ");
        buffer.append("\"image\": \"/images/" + this.image.id + "\", ");

        buffer.append("\"partfeatures\": [");
        if (partFeature != null  && this.partFeature.size() > 0) {
            for (int i = 0; i < this.partFeature.size() - 1; i++) {
                PartFeature pf = this.partFeature.get(i);
                buffer.append("{ \"specification\": \"" + pf.specification.description + "\", ");
                buffer.append(" \"value\": \"" + pf.value.toString() + "\" },");
            }
            PartFeature pf = this.partFeature.get(this.partFeature.size() - 1);
            buffer.append("{ \"specification\": \"" + pf.specification.description + "\", ");
            buffer.append(" \"value\": \"" + pf.value.toString() + "\" }");
        }
        buffer.append("]");

        buffer.append("}");

        buffer.append("}");


        return buffer.toString();
    }
}
