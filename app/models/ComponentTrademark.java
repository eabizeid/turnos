package models;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import play.db.jpa.Model;

@Entity
public class ComponentTrademark extends Model {

	public String description;
	@OneToMany(mappedBy="trademark",cascade=CascadeType.ALL)
	public List<Component> component;

    @Override
    public String toString() {
        return "{ \"id\" :  " + this.id +", \"description\": \"" + description + "\"  }";
    }
}
