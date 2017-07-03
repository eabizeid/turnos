package controllers;

import models.Especialidad;
import play.mvc.Before;

/**
 * Created by edu on 11/08/15.
 */
@CRUD.For(Especialidad.class)
public class Especialidades extends CRUD {
    @Before
    public static void setSort() {
        request.params.put("orderBy", "descripcion");
    }

}
