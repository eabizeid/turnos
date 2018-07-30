package controllers;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import models.Appointment;
import models.*;
import play.db.jpa.JPABase;
import play.mvc.Before;
import play.mvc.Controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.ListUtils;

/**
 * Created by edu on 10/08/15.
 */
public class Organizador extends Controller {

    private static final String[] hours = {"08:00","08:30","09:00","09:30","10:00","10:30","11:00","11:30","12:00","12:30","13:00","13:30","14:00","14:30","15:00",
            "15:30","16:00","16:30","17:00","17:30", "18:00", "18:30", "19:00", "19:30"};



    @Before
    public static void setNovedades() {
        List<Novedad> news  = Novedad.findAll();
        renderArgs.put("news", news);
    }

    public static void index() {
        renderArgs.put("profesionales", Profesional.findAll());
        render();
    }



    public static void addTurnero(Long professionalId, String fecha, String desde, String hasta) {

        Profesional profesional = Profesional.findById(professionalId);
        models.Organizador organizador = new models.Organizador();
        organizador.profesional = profesional;
        organizador.desde =  fecha+ "T"+ desde;
        organizador.hasta =  fecha+ "T"+ hasta;
        organizador.save();
        renderJSON(organizador);
    }


    public static void deleteAppointment(Long appointmentId) {
        Appointment a = Appointment.findById(appointmentId);
        a.delete();
    }

    public static void cancelAppointment(Long appointmentId) {
        Appointment a = Appointment.findById(appointmentId);
        a.delete();
    }

    public static void profesionales() {
       renderArgs.put("profesionales", Profesional.findAll());
        render();
    }

    public static void profesional(Long profesionalId) {
        renderJSON(Profesional.findById(profesionalId));

    }

    private static Paciente paciente(String paciente) {
        Paciente pacienteObj = new Paciente();
        String[] pacienteSplitted = paciente.split(" ");
        pacienteObj.nombres = pacienteSplitted[0];
        pacienteObj.apellido = pacienteSplitted[1];
        pacienteObj.save();

        return pacienteObj;
    }

    private static Cita cita(String start, String hora) {
        Cita cita = new Cita();


        cita.fecha = start;
        cita.hora = hora;
        return cita;

    }

    public static void showOfficeOrganization(Long profesionalId, Integer mes) {
        List<Event> events = Lists.newArrayList();
        List<Organizacion> organizacionList = Organizacion.findAll();
        if (organizacionList != null  && !organizacionList.isEmpty()) {
            Organizacion organizacion = organizacionList.get(0);
            if (organizacion.organizacion.containsKey(mes)) {
                Map<Consultorio, Map<String, Map<String, Profesional>>>organizacionDelMes = organizacion.organizacion.get(mes);
                for (Consultorio consultorio: organizacionDelMes.keySet() ) {
                    Map<String, Map<String, Profesional>> organizacionDelConsultorio = organizacionDelMes.get(consultorio);
                    //ya puedo armar los eventos
                    for (String day: organizacionDelConsultorio.keySet()) {
                        Map<String, Profesional> organizacionPorHoraMap = organizacionDelConsultorio.get(day);
                        for (String hour: organizacionPorHoraMap.keySet()) {
                            Event event = new Event();
                            event.title = organizacionPorHoraMap.get(hour).toString();
                            event.start = day.concat("T").concat(hour).concat(":00");
                            events.add(event);
                        }
                    }
                }
            }
        }
        render(events);
    }

    public static void calculateNewOfficeOrganization(Integer month, String startHour, String endHour ) {
        List<Organizacion> organizacionList = Organizacion.findAll();
        if (organizacionList != null  && !organizacionList.isEmpty()) {
            Organizacion organizacion = organizacionList.get(0);
            if (organizacion.organizacion.containsKey(month)) {

//                Map<Consultorio, Map<String, Map<String, Profesional>>> monthOrganization = organizacion.organizacion.get(month);
//                monthOrganization.
            }

        } else {
             Calendar calendar = Calendar.getInstance();
            // I should charge all structure
            Map<Integer,Map<Consultorio, Map<String, Map<String, Profesional>>>> organizacionPorMes = Maps.newHashMap();
            for(Integer mes =0; mes<12; mes++) {
                List<Consultorio> consultorios = Consultorio.findAll();
                Map<Consultorio, Map<String, Map<String, Profesional>>> organizacionPorConsultorio = Maps.newHashMap();
                for (Consultorio consultorio : consultorios) {
                    Map<String, Map<String, Profesional>> organizacionPorDia = Maps.newHashMap();
                    for (Integer day = 0; day < calendar.getActualMaximum(Calendar.DAY_OF_MONTH); day++) {
                        Map<String, Profesional> organizacionPorHora = Maps.newHashMap();
                        for (String hour : hours) {
                            organizacionPorHora.put(hour, null);
                        }
                        organizacionPorDia.put(day.toString(), organizacionPorHora);
                    }
                    organizacionPorConsultorio.put(consultorio, organizacionPorDia);
                }
                organizacionPorMes.put(mes, organizacionPorConsultorio);
            }
            Organizacion organizacion = new Organizacion();
            organizacion.organizacion = organizacionPorMes;
            organizacion.save();

        }
    }


    public static void dispoForProfesional() {
        renderArgs.put("profesionales", Profesional.find("order by apellido asc").fetch());
        render();
    }

    public static void disponibilidadPorProfesional(Long profesionalId) {
        renderArgs.put("professionalId",profesionalId);
        Profesional profesional = Profesional.findById(profesionalId);
        List<DisponibilidadPorProfesional> dispoByProfesionalList = DisponibilidadPorProfesional.find("byProfesional", profesional).fetch();
        if (dispoByProfesionalList != null && dispoByProfesionalList.size() != 0) {
            DisponibilidadPorProfesional byProfesional = (DisponibilidadPorProfesional) dispoByProfesionalList.get(0);
            renderArgs.put("dispoPorDia",byProfesional.disponibilidadPorDia);
            List<String> specificDays = byProfesional.anSpecificDays;
            Collections.sort(specificDays);
            renderArgs.put("specificDays", specificDays);
        }
        render();
    }

    public static void showNewDispo(Long professionalId) {
        getDataTo(professionalId);
        render();
    }

    private static void getDataTo(Long profesionalId) {
        renderArgs.put("professionalId",profesionalId);
        Profesional profesional = Profesional.findById(profesionalId);
        List<DisponibilidadPorProfesional> dispoByProfesionalList = DisponibilidadPorProfesional.find("byProfesional", profesional).fetch();
        if (dispoByProfesionalList != null && dispoByProfesionalList.size() != 0) {
            DisponibilidadPorProfesional byProfesional = dispoByProfesionalList.get(0);
            renderArgs.put("dispoPorDia",byProfesional.disponibilidadPorDia);
        }
    }
    
    public static void saveNewDispo(Long professionalId, Integer month,  boolean lunes, boolean martes, boolean miercoles, boolean jueves
            , boolean viernes, String desdel1, String desdel2, String desdel3, String desdel4, String desdem1, String desdem2,
                                String desdem3, String desdem4, String desdemi1, String desdemi2, String desdemi3,
                                String desdemi4, String desdej1, String desdej2, String desdej3, String desdej4,
                                String desdev1, String desdev2, String desdev3, String desdev4,
                                String hastal1, String hastal2, String hastal3, String hastal4, String hastam1, String hastam2,
                                String hastam3, String hastam4, String hastami1, String hastami2, String hastami3,
                                String hastami4, String hastaj1, String hastaj2, String hastaj3, String hastaj4,
                               String hastav1, String hastav2, String hastav3, String hastav4, String anSpecificDay) {


        List<DisponibilidadPorProfesional> byProfesional = DisponibilidadPorProfesional.find("byProfesional", Profesional.findById(professionalId)).fetch();
        DisponibilidadPorProfesional disponibilidadPorProfesional = null;
        if (anSpecificDay!= null && !anSpecificDay.isEmpty()) {
            ArrayList<String> anSpecificDays = Lists.newArrayList(anSpecificDay);
            if (byProfesional.isEmpty()) {
                disponibilidadPorProfesional = new DisponibilidadPorProfesional();
                disponibilidadPorProfesional.profesional = Profesional.findById(professionalId);
                disponibilidadPorProfesional.anSpecificDays = anSpecificDays;
                disponibilidadPorProfesional.disponibilidadPorDia = Lists.newArrayList();
                disponibilidadPorProfesional.save();

            } else {
                disponibilidadPorProfesional =  byProfesional.get(0);
                disponibilidadPorProfesional.anSpecificDays.addAll(anSpecificDays);
                disponibilidadPorProfesional.disponibilidadPorDia = Lists.newArrayList();
                disponibilidadPorProfesional.save();
            }
        } else {
            List<DisponibilidadPorDia> byDays = Lists.newArrayList();
            if (lunes) {
                byDays.add(disponibilidadPorDia(1, desdel1, desdel2, desdel3, desdel4, hastal1, hastal2, hastal3, hastal4));
            }
            if (martes) {
                byDays.add(disponibilidadPorDia(2, desdem1, desdem2, desdem3, desdem4, hastam1, hastam2, hastam3, hastam4));
            }
            if (miercoles) {
                byDays.add(disponibilidadPorDia(3, desdemi1, desdemi2, desdemi3, desdemi4, hastami1, hastami2, hastami3, hastami4));
            }
            if (jueves) {
                byDays.add(disponibilidadPorDia(4, desdej1, desdej2, desdej3, desdej4, hastaj1, hastaj2, hastaj3, hastaj4));
            }
            if (viernes) {
                byDays.add(disponibilidadPorDia(5, desdev1, desdev2, desdev3, desdev4, hastav1, hastav2, hastav3, hastav4));
            }

            if (byProfesional.isEmpty()) {
                disponibilidadPorProfesional = new DisponibilidadPorProfesional();
                disponibilidadPorProfesional.profesional = Profesional.findById(professionalId);
                disponibilidadPorProfesional.disponibilidadPorDia = byDays;
                disponibilidadPorProfesional.anSpecificDays = Lists.newArrayList();
                disponibilidadPorProfesional.save();

            } else {
                disponibilidadPorProfesional = byProfesional.get(0);
                disponibilidadPorProfesional.anSpecificDays = Lists.newArrayList();
                for(DisponibilidadPorDia newDispo : byDays) {
                    boolean found = false;
                    for (DisponibilidadPorDia oldDispo : disponibilidadPorProfesional.disponibilidadPorDia){
                        if (newDispo.dia.equals(oldDispo.dia)) {
                            oldDispo.rangos =  newDispo.rangos;
                            found = true;
                            break;
                        }
                    }
                    if (!found ) {
                        disponibilidadPorProfesional.disponibilidadPorDia.addAll(byDays);
                    }
                }
                disponibilidadPorProfesional.save();
            }

        }
        dispoForProfesional();

    }


    private static DisponibilidadPorDia disponibilidadPorDia (Integer dia, String desde1, String desde2, String desde3,
                                                       String desde4, String hasta1, String hasta2, String hasta3, String hasta4) {

        List<Rango> rangos = Lists.newArrayList();

        Rango rango = getRango(desde1, hasta1);
        if (rango != null) rangos.add(rango);
        rango = getRango(desde2, hasta2);
        if (rango != null) rangos.add(rango);
        rango = getRango(desde3, hasta3);
        if (rango != null) rangos.add(rango);
        rango = getRango(desde4, hasta4);
        if (rango != null) rangos.add(rango);

        DisponibilidadPorDia disponibilidadPorDia = new DisponibilidadPorDia();
        disponibilidadPorDia.dia = dia;
        disponibilidadPorDia.rangos = rangos;

        return disponibilidadPorDia;
    }

    private static Rango getRango(String desde, String hasta) {
        Rango rango = null;
        if (!desde.isEmpty() && !hasta.isEmpty()) {
            rango = new Rango();
            rango.desde = desde;
            rango.hasta = hasta;
        }
        return rango;
    }

}
