package jobs;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;


import models.Appointment;
import play.jobs.*;

@OnApplicationStart
public class PlazoSeñaJob extends Job{
	
	 public void doJob() {
	        List<Appointment> appointments = Appointment.find("plazoSeña <> '' ").fetch();
	        LocalDate now = LocalDate.now();
	        for(Appointment appointment : appointments) {
	        	LocalDate plazo = LocalDate.parse(appointment.plazoSeña, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
	        	if (plazo.isEqual(now)|| plazo.isBefore(now)) {
	        		appointment.plazoVencido = true;
	        		appointment.save();
	        	}
	        }
	    }
}
