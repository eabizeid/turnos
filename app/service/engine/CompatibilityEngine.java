package service.engine;

import com.google.common.collect.Maps;
import models.Component;
import models.ComponentFeature;
import models.Part;
import models.PartFeature;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import java.util.Map;

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
        Map<PartFeature, Boolean> equalsMap = Maps.newHashMap();
        for (int i = 0; i < part.partFeature.size(); i++) {
            PartFeature partFeature = part.partFeature.get(i);
            equalsMap.put(partFeature, Boolean.FALSE);
            for (ComponentFeature componentFeature : component.compatibility){
                if (componentFeature.specification.equals(partFeature.specification)  && componentFeature.value == partFeature.value) {
                    equalsMap.put(partFeature, Boolean.TRUE);
                    break;
                }
            }

        }
        for (Boolean compatibility : equalsMap.values()) {
            System.out.println(compatibility);
            if(!compatibility) {
                return compatibility;
            }
        }
        return true;
    }

}
