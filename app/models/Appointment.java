package models;

import play.db.jpa.Model;

import javax.persistence.*;

import org.apache.commons.lang.StringUtils;

import java.lang.Override;

/**
 * Created by edu on 11/08/15.
 */
@Entity
public class Appointment extends Model {

    @ManyToOne
    public Profesional profesional;
   // @ManyToOne
   // public Especialidad especialidad;
    @Column(length=1000000)
    public Paciente paciente;
    @OneToOne (cascade = CascadeType.ALL)
    public Cita cita;
    public Boolean sobreturno;
    public String advance;
    public String plazoSeña;
    public String color = "rgb(246, 178, 107)";
    public String border = "#6871B0";
    public String prestaciones;
    public Boolean plazoVencido = false;
    public boolean particular;
    public String notaOS = StringUtils.EMPTY;

}
