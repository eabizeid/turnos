package service.engine;

import models.Component;
import models.ComponentFeature;
import models.Part;
import models.PartFeature;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

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
        for (final PartFeature partFeature : part.partFeature) {
            boolean exists = CollectionUtils.exists(component.compatibility, new Predicate() {
                @Override
                public boolean evaluate(Object o) {
                    ComponentFeature componentFeature = (ComponentFeature) o;
                    System.out.println("Component Specification : " + componentFeature.specification + " Part soecification: " + partFeature.specification);
                    System.out.println("Component feature value: " +  componentFeature.value + " Part feature value: " + partFeature.value);
                    return componentFeature.specification.equals(partFeature.specification) && componentFeature.value == partFeature.value;
                }
            });
            if (!exists) return false;
        }
        return true;
    }

}
