package models;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

/**
 * Created by edu on 10/08/15.
 */
public enum Mes {
    ENERO {
        @Override
        public String value() {
            return "ENERO";
        }
    },
    FEBRERO {
        @Override
        public String value() {
            return "FEBRERO";
        }
    },
    MARZO {
        @Override
        public String value() {
            return "FEBRERO";
        }
    },
    ABRIL {
        @Override
        public String value() {
            return "ABRIL";
        }
    },
    MAYO {
        @Override
        public String value() {
            return "MAYO";
        }
    },
    JUNIO {
        @Override
        public String value() {
            return "JUNIO";
        }
    },
    JULIO {
        @Override
        public String value() {
            return "JULIO";
        }
    },
    AGOSTO {
        @Override
        public String value() {
            return "AGOSTO";
        }
    },
    SEPTIEMBRE {
        @Override
        public String value() {
            return "SEPTIEMBRE";
        }
    },
    OCTUBRE {
        @Override
        public String value() {
            return "OCTUBRE";
        }
    },
    NOVIEMBRE {
        @Override
        public String value() {
            return "NOVIEMBRE";
        }
    },
    DICIEMBRE {
        @Override
        public String value() {
            return "DICIEMBRE";
        }
    };

    public abstract String value();

    public List<Dia> dias;
}
