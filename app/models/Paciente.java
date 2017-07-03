package models;

import play.db.jpa.Model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class Paciente extends Model {

    private static final long serialVersionUID = 5757163273053457756L;

    public String apellido;
    public String nombres;
    public String dni;
    public String telefono;
    public String celular;
    public String mail;
    public String nroAfiliado;
    public TipoPago tipoPago;
    @ManyToOne
    public ObraSocial obraSocial;
    public Boolean planObesidad;

    @Override
    public String toString() {
        return apellido + ", " + nombres;
    }
}
