package models;

import play.db.jpa.Model;

import javax.persistence.Entity;

/**
 * Created by edu on 16/05/16.
 */
@Entity
public class Prestacion extends Model {
    public String nombre;

    @Override
    public String toString() {
        return nombre;
    }
}
