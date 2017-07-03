package models;

import java.util.List;

/**
 * Created by edu on 04/03/16.
 */
public class BussinessHour {
    public String start;
    public String end;
    public List<Integer> dow;//day

    public boolean contieneA(BussinessHour other){
        if (other == null) return false;
        if (other == this) return true;

        if (other.start.compareTo(this.start) >= 0
                && other.end.compareTo(this.end) <=0
                && this.dow.contains(other.dow.get(0))) {
            return true;
        }
        return false;

    }
}
