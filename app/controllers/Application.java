package controllers;

import play.*;
import play.mvc.*;

import java.util.*;

import models.*;

public class Application extends Controller {

    public static void index() {
    	Component component = new Component();
    	component.marca = "ASUS";
    	component.modelo ="1005p";
    	component.tipo ="FLEX";
    	component.nroDeParte = "14G2235HA 10G05311683";
    	component.save();
    	
    	Component component1 = new Component();
    	component1.marca = "EXO";
    	component1.modelo ="x355";
    	component1.tipo ="FLEX";
    	component1.nroDeParte = "29ge10050-51 YD";
    	component1.save();
    	
    	Component component2 = new Component();
    	component2.marca = "COMPAQ";
    	component2.modelo ="CQ60";
    	component2.tipo ="FLEX";
    	component2.nroDeParte = "29ge10050-51 YD";
    	component2.save();
    	
    	Component component3 = new Component();
    	component3.marca = "GFAST";
    	component3.modelo ="";
    	component3.tipo ="FLEX";
    	component3.nroDeParte = "DC020011R10";
    	component3.save();
    	
    	Component component4 = new Component();
    	component4.marca = "GATEWAY";
    	component4.tipo =  "KEYBOARD";
    	component4.nroDeParte = "PK130C81017";
    	component4.save();
    	
    	
    	Component component5 = new Component();
    	component5.marca = "ACER";
    	component5.modelo ="5734z";
    	component5.tipo ="KEYBOARD";
    	component5.nroDeParte = "mp-08g63u4-6983";
    	component5.save();
    	
    	Component component6 = new Component();
    	component6.marca = "ACER";
    	component6.modelo ="3050";
    	component6.tipo ="FLEX";
    	component6.nroDeParte = "DD0ZR1LC008";
    	component6.save();
    	
    	Component component7 = new Component();
    	component7.marca = "ACER";
    	component7.modelo ="5750";
    	component7.tipo ="FLEX";
    	component7.nroDeParte = "DC020017K10";
    	component7.save();
    	
    	Component component8 = new Component();
       	component8.marca = "BANGHO";
    	component8.modelo ="A5000";
    	component8.tipo ="USB BOARD";
    	component8.nroDeParte = "";
    	component8.save();
    	
    	Component component9 = new Component();
    	component9.marca = "BANGHO";
    	component9.modelo ="A5000";
    	component9.tipo ="BATTERY";
    	component9.nroDeParte = "";
    	component9.save();
    	Component component10 = new Component();
    	component10.marca = "BGH";
    	component10.modelo ="M407";
    	component10.tipo ="KEYBOARD";
    	component10.nroDeParte = "82R-14B223-4091";
    	component10.save();
        render();
    }
    
    

}