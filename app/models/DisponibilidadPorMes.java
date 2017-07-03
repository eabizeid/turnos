package models;

import play.db.jpa.Model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

/**
 * Created by edu on 29/02/16.
 */
@Entity
public class DisponibilidadPorMes extends Model {

    public int mes;
    @OneToMany(cascade = CascadeType.ALL)
    public List<DisponibilidadPorDia> disponibilidadPorDia;
}
