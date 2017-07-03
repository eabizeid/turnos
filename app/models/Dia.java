package models;

import javax.persistence.Entity;

/**
 * Created by edu on 10/08/15.
 */
public enum Dia {
    LUNES {
        @Override
        public Integer value() {
            return 1;
        }
    },
    MARTES {
        @Override
        public Integer value() {
            return 2;
        }
    },
    MIERCOLES {
        @Override
        public Integer value() {
            return 3;
        }
    },
    JUEVES {
        @Override
        public Integer value() {
            return 4;
        }
    },
    SABADO {
        @Override
        public Integer value() {
            return 5;
        }
    };

    public abstract Integer value();
}
