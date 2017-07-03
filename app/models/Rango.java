package models;

import play.db.jpa.Model;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import java.util.List;

/**
 * Created by edu on 08/08/15.
 */
@Entity
public class Rango extends Model {

    public String desde;
    public String hasta;
    @OneToOne
    public Consultorio consultorio;

}
