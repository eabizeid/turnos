package models;


import play.data.validation.MaxSize;
import play.db.jpa.Model;

import javax.persistence.*;
import java.util.List;


@Entity
public class Profesional extends Model  {

    public String apellido;
    public String nombre;

    public String slotTime;
    public boolean mustChargeSign;
    @MaxSize(1000)
    public String notas;

    @ManyToMany
    @OrderBy("descripcion ASC")
    public List<Especialidad> especialidades;

    @OneToMany
    @OrderBy("nombre ASC")
    public List<ObraSocial> obraSociales;

    @Override
    public String toString() {
        return apellido + ", " + nombre;
    }

}
