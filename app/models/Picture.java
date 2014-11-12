package models;

import play.db.jpa.Blob;
import play.db.jpa.Model;

import javax.persistence.Entity;

/**
 * @author ezeid on 5/27/14.
 */
@Entity
public class Picture extends Model {

    public Blob image;
}
