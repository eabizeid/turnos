package models;

import play.db.jpa.Model;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import java.util.List;
import java.util.Map;

/**
 * Created by edu on 11/08/15.
 */
@Entity
public class Organizacion  extends Model{

    //Map de Mes a Mapa de Fecha a Mapa de hora, a mapa de Consultorio, profesional
    @Lob
    public Map<Integer,Map<Consultorio, Map<String, Map<String, Profesional>>>>  organizacion;


}
