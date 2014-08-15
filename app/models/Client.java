package models;

import play.db.jpa.Model;

import java.util.List;

/**
 * Created by edu on 15/08/14.
 */
public class Client extends Model {
    public Mail mail;
    public List<Pending> generatedPendings;
}
