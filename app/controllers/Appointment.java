package controllers;

import play.mvc.Controller;

import java.util.List;

/**
 * Created by edu on 11/08/15.
 */
public class Appointment extends Controller {

    public static void index() {
        render();
    }

    public static void appointmentTaken() {

        models.Appointment appointment = new models.Appointment();
        List<models.Appointment> appointments = models.Appointment.findAll();

        //tengo que armar el mapa de turnos


        render();
    }
}
