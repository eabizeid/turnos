package models;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import com.google.gson.annotations.Expose;
import org.apache.commons.lang.StringUtils;
import play.db.jpa.Model;

@Entity
public class  ComponentType extends Model {


  	public String description;
    @Expose
    @OneToMany(mappedBy="type", cascade={ CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST})
	public List<Component> component;
    @Expose
    @ManyToMany(cascade = CascadeType.DETACH)
    public List<Feature> features;

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer(StringUtils.EMPTY);
        buffer.append("{ \"id\" :  " + this.id +", \"description\": \"" + description + "\"," );
        buffer.append("\"features\": [ ");
        for (int i = 0; i < features.size() -1; i++) {
            buffer.append(features.get(i) + "," );
        }
        buffer.append(features.get(features.size()-1)  );
        buffer.append(" ]");
        buffer.append("  }");
        return buffer.toString();
    }
}
