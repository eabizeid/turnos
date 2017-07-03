package models;

import play.db.jpa.Model;

import javax.persistence.Entity;
import java.util.Date;

/**
 * Created by edu on 11/08/15.
 */
@Entity
public class Organizador extends Model {

    public Profesional profesional;
    public String desde;
    public String hasta;



}
