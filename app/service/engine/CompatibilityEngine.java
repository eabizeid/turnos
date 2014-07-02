package service.engine;

import models.Component;
import models.Part;

/**
 * @author ezeid on 7/2/14.
 */
public class CompatibilityEngine {

    private static final CompatibilityEngine ENGINE = new CompatibilityEngine();

    private CompatibilityEngine() {}

    public static CompatibilityEngine getEngine() {
        return ENGINE;
    }

    public boolean areThereCompatibility(Part part, Component component) {

        return true;
    }

}
