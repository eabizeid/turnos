package models;

import play.db.jpa.Model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OrderBy;

/**
 * Created by edu on 08/08/15.
 */
@Entity
public class Consultorio extends Model{

    public Integer number;

    @ManyToOne
    public Establecimiento establecimiento;

    @Override
    public String toString() {
        return number + "/" + establecimiento;
    }
}
