package controllers;

import play.mvc.Before;

/**
 * Created by edu on 08/08/15.
 */
public class Profesionals extends CRUD {
    @Before
    public static void setSort() {
        request.params.put("orderBy", "apellido, nombre");
    }


}
