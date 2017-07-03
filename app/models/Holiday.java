package models;

import play.db.jpa.Model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.util.Date;

/**
 * Created by edu on 10/08/15.
 */
@Entity
public class Holiday extends Model {

    public Date desde;
    public Date hasta;
    @ManyToOne
    public Profesional profesional;

    @Override
    public String toString() {
        return desde + " - " + hasta + " - "+ profesional.toString();
    }
}
