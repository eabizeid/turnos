package models;

import com.google.gson.annotations.Expose;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import play.db.jpa.Model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.List;

@Entity
public class Feature extends Model {


    public String description;
    @Expose
    @ManyToMany(mappedBy = "features")
    public List<ComponentType> types;

    @Override
    public String toString() {
        return "{ \"id\" :  " + this.id +", \"description\": \"" + description + "\"  }";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) { return false; }
        if (obj == this) { return true; }
        if (obj.getClass() != getClass()) {
            return false;
        }
        Feature rhs = (Feature) obj;
        return new EqualsBuilder()
                .appendSuper(super.equals(obj))
                .append(description, rhs.description)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(description).toHashCode();
    }
}