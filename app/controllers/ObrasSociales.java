package controllers;

import models.ObraSocial;
import play.mvc.Before;

/**
 * Created by edu on 25/10/15.
 */
@CRUD.For(ObraSocial.class)
public class ObrasSociales extends CRUD {
    @Before
    public static void setSort() {
        request.params.put("orderBy", "nombre");
    }

}
