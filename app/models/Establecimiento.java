package models;


import play.db.jpa.Model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
public class Establecimiento extends Model{

    public String nombre;
    public String Direccion;
    public Integer interno;


    @Override
    public String toString() {
        return nombre;
    }
}
