package models;

import play.db.jpa.Model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import java.util.List;

/**
 * Created by edu on 11/08/15.
 */
@Entity
public class Especialidad extends Model {
    public String descripcion;

    @OneToMany(cascade = CascadeType.ALL)
    public List<Prestacion> prestaciones;
    @Override
    public String toString() {
        return descripcion;
    }
}
