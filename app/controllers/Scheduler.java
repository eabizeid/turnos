package controllers;

import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import models.*;
import models.Appointment;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import play.db.jpa.GenericModel;
import play.db.jpa.JPABase;
import play.modules.pdf.PDF;
import play.mvc.Before;
import play.mvc.Controller;

import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import static play.modules.pdf.PDF.*;
/**
 * Created by edu on 10/08/15.
 */
public class Scheduler extends Controller {

    @Before
    public static void setNovedades() {
        List<Novedad> news  = Novedad.findAll();
        renderArgs.put("news", news);
    }
    
    public static void vencimientos () {
    	List<Appointment> appointments = Appointment.find("byPlazoVencido", true).fetch();
        render(appointments);
    }
    
    
    public static void index() {
        renderArgs.put("profesionales", Profesional.find("order by apellido ASC").fetch());
        renderArgs.put("especialidades", Especialidad.find("order by descripcion ASC").fetch());
        renderArgs.put("consultorios", Consultorio.findAll());
        render();
    }

    public static void profesionales(Long especialidadId) {
        Especialidad especialidad = Especialidad.findById(especialidadId);

        List<Profesional> profesionales = Profesional.find("select p from Profesional p join p.especialidades e where e.id = ? order by p.apellido asc", especialidad.id).fetch();
        renderJSON(profesionales);
    }

    public static void organizador(Long professionalId) {
        Profesional profesional = Profesional.findById(professionalId);

        List<Event> events = Lists.newArrayList();
        if (profesional != null) {
            List<models.Appointment> appointments = models.Appointment.find("byProfesional", profesional).fetch();
            for (models.Appointment appointment : appointments ) {
                Event event = new Event();
                //paciente, practicas, si es particular, obrasocial, plazo de seña
                event.title = String.format("%s - %s - %s - %s - %s", appointment.paciente.toString() + ", " + appointment.id, 
                		(appointment.prestaciones != null) ? appointment.prestaciones : StringUtils.EMPTY, appointment.particular ? "PART" : "NO PART",  
                				appointment.paciente.obraSocial != null ? appointment.paciente.obraSocial.nombre: "SIN OS", "plazo: " + appointment.plazoSeña);
                String[] fechaSplitted = appointment.cita.fecha.split("-"); 
                String fecha = fechaSplitted[2] + "-" + fechaSplitted[1] + "-" + fechaSplitted[0];
                event.start = fecha + "T" + appointment.cita.hora + ":00";
                event.end = fecha + "T" + appointment.cita.hora + ":00";
                String[] hourSplitted = appointment.cita.hora.split(":");
                event.color = (appointment.color != StringUtils.EMPTY) ? appointment.color : "rgb(246, 178, 107)";
                event.border = appointment.border;
                event.description = (appointment.advance != null) ? appointment.advance : StringUtils.EMPTY;
                event.plazo = appointment.plazoSeña; 
                events.add(event);
            }

            List<Holiday> holidays = Holiday.find("byProfesional", profesional).fetch();
            for(Holiday h : holidays) {
                Event event = new Event();
                event.title = "Vacaciones";
                event.start =  h.desde.toString();
                event.end   = h.hasta.toString();
                event.color = "gray";
                event.allDay = true;
                event.description = StringUtils.EMPTY;
                events.add(event);
            }

        }
        SchedulerResponse response = new SchedulerResponse();
        response.events = events;
        response.profesional = profesional;
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm").create();
        renderJSON(gson.toJson(response));
    }


    public static void addAppointment(Long professionalId, Long especialidadId, Long pacienteId, String fecha,
                                      String hora, Boolean st, String advance, String plazo, String prestaciones, String notaOS, String color) {

        Profesional profesional = Profesional.findById(professionalId);
        models.Appointment appointment = new models.Appointment();
        appointment.cita = cita(fecha, hora);
        appointment.paciente = Paciente.findById(pacienteId);
        appointment.profesional = profesional;
        appointment.sobreturno = (st != null) ? st : false;
        if ( st!= null && st )
            appointment.border = "red";
        if (!color.isEmpty())
        	appointment.color = color;
        
        appointment.advance = advance;
        appointment.plazoSeña = plazo;
        appointment.prestaciones = prestaciones;
        appointment.notaOS = notaOS;
        appointment.save();
        renderJSON(appointment);
    }

    public static void deleteAppointment(Long appointmentId) {
        models.Appointment a = models.Appointment.findById(appointmentId);
        a.delete();
    }

    public static void cancelAppointment(Long appointmentId) {
        models.Appointment a = models.Appointment.findById(appointmentId);
        a.delete();
    }

    public static void pacientes() {
       renderArgs.put("pacientes", Paciente.findAll());
        render();
    }

    public static void paciente(Long pacienteId, Long profesionalId) {
    	Paciente paciente = Paciente.findById(pacienteId);
    	Profesional profesional = Profesional.findById(profesionalId);
    	
    	Boolean tieneObraSocial = profesional.obraSociales.contains(paciente.obraSocial);
    	renderArgs.put("tieneObraSocial", tieneObraSocial);
    	renderJSON(paciente);

    }

    public static void availableDays(Long professionalId, int month) {
        Profesional profesional = Profesional.findById(professionalId);
        List<DisponibilidadPorProfesional> byProfesional = DisponibilidadPorProfesional.find("byProfesional", profesional).fetch();
        AvailableDaysResponse availableDaysResponse = new AvailableDaysResponse();
        List<BussinessHour> bussinessHours = Lists.newArrayList();
        if (!byProfesional.isEmpty()) {
            DisponibilidadPorProfesional byProfesional1 = byProfesional.get(0);
            if (!byProfesional1.anSpecificDays.isEmpty()) {
            	
                List<String> specificDays = byProfesional1.anSpecificDays;
                Collections.sort(specificDays);
                availableDaysResponse.anSpecificDays = specificDays;
                availableDaysResponse.withAnSpecificDays = true;
            } else {
                bussinessHours = searchDispo(month, byProfesional1);
                if (bussinessHours.isEmpty()) {
                    bussinessHours = searchDispo(month == 1 ? 12 : month - 1, byProfesional1);
                }
                availableDaysResponse.businessHours = bussinessHours;

            }
        }
        renderJSON(availableDaysResponse );
    }

    public static void showReport() {
        render();
    }

    public static void report(String date) {
        List<models.Appointment> appointments = models.Appointment.find("cita.fecha = ? order by profesional.apellido ASC, profesional.nombre ASC", date).fetch();

        render(appointments);
    }

        public static Boolean isBusinessHour(String hora, Integer month, Integer dow, Long professionalId) {
        BussinessHour b = new BussinessHour();
        b.dow = Lists.newArrayList(dow);
        b.end = hora;
        b.start = hora;


        List<DisponibilidadPorProfesional> byProfesional = DisponibilidadPorProfesional.find("byProfesional", Profesional.findById(professionalId)).fetch();
        List<BussinessHour> bussinessHours = Lists.newArrayList();
        if (!byProfesional.isEmpty()) {
            DisponibilidadPorProfesional byProfesional1 = byProfesional.get(0);
                bussinessHours = searchDispo(month, byProfesional1);
                if (bussinessHours.isEmpty()) {
                    bussinessHours = searchDispo(month == 1 ? 12 : month - 1, byProfesional1);
                }

            for (BussinessHour bh : bussinessHours)
                if (bh.contieneA(b))
                    return Boolean.TRUE;

        }

        return Boolean.FALSE;
    }
    private static List<BussinessHour> searchDispo(int month, DisponibilidadPorProfesional byProfesional) {
        List<BussinessHour> bussinessHours = Lists.newArrayList();
        for (DisponibilidadPorDia dia : byProfesional.disponibilidadPorDia) {
            for (Rango r : dia.rangos) {
                BussinessHour bh = new BussinessHour();
                bh.start = r.desde.length() < 5 ? "0" + r.desde : r.desde;
                bh.end = r.hasta.length() < 5 ? "0" + r.hasta : r.hasta;
                bh.dow = Lists.newArrayList(dia.dia);
                bussinessHours.add(bh);

            }
        }

        return bussinessHours;
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





}
