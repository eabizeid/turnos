package models;

import play.db.jpa.Model;

import javax.persistence.*;
import java.util.List;
import java.util.Map;

/**
 * Created by edu on 08/08/15.
 */
@Entity
public class DisponibilidadPorProfesional extends Model {

    @ManyToOne
    @JoinColumn(name="professional_id", referencedColumnName="id")
    public Profesional profesional;
    @ElementCollection
    public List<String> anSpecificDays ;
    @OneToMany( cascade = CascadeType.ALL)
    public List<DisponibilidadPorDia>  disponibilidadPorDia;

}
