package models;

import play.db.jpa.Model;

import java.util.List;

/**
 * Created by edu on 15/08/14.
 */
public class Client extends Model {

    private static final long serialVersionUID = 1L;

    public Mail mail;
    public List<Pending> generatedPendings;
}
