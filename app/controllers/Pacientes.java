package controllers;

import models.LabelValue;
import models.Paciente;
import play.mvc.Before;
import play.mvc.Http;

import java.util.List;

/**
 * Created by edu on 08/08/15.
 */
public class Pacientes extends CRUD {
    public static void pacientes(String term) {
        String termino = "%" + term + "%";
        List<LabelValue> pacientes = Paciente.find("select NEW models.LabelValue(p.apellido , p.id)  from Paciente as p where p.nombres like ? or p.apellido like ?", termino, termino).fetch();
        renderJSON(pacientes);
    }

    @Before
    public static void setSort() {
        request.params.put("orderBy", "apellido, nombres");
    }


}
