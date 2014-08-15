package models;

import play.db.jpa.Model;

/**
 * Created by edu on 14/08/14.
 */
public class User extends Model {

    public String email;
    public String password;
    public String fullname;
}
