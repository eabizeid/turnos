package models;

import play.db.jpa.Model;

import javax.persistence.*;
import java.lang.Override;

/**
 * Created by edu on 11/08/15.
 */
@Entity
public class Appointment extends Model {

    @ManyToOne
    public Profesional profesional;
    @ManyToOne
    public Especialidad especialidad;
    @Column(length=1000000)
    public Paciente paciente;
    @OneToOne (cascade = CascadeType.ALL)
    public Cita cita;
    public Boolean sobreturno;
    public String advance;
    public String color = "#3a87ad";
    public String border = "#6871B0";
    public String prestaciones;

}
