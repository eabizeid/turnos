package models;

import play.data.validation.MaxSize;
import play.db.jpa.Model;

import javax.persistence.Entity;

/**
 * Created by edu on 25/10/15.
 */
@Entity
public class ObraSocial extends Model {
    public String nombre;
    @MaxSize(1000)
    public String contacto;
    public String telefono;
    public String mail;

    @Override
    public String toString() {
        return nombre;
    }
}
