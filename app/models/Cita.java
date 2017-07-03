package models;

import play.db.jpa.Model;

import javax.persistence.Entity;

/**
 * Created by edu on 11/08/15.
 */
@Entity
public class Cita extends Model {
    public String fecha;
    public String hora;
}
