package models;

public class LabelValue {

    public String label;
    public String id;

    public LabelValue() {}
    public LabelValue(String label, String value) {
        this.label = label;
        this.id = value;
    }

    public LabelValue(String label, Long value) {
        this.label = label;
        this.id = value.toString();
    }
}