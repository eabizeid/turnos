package service.engine;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import models.Component;
import models.ComponentFeature;
import models.Part;
import models.PartFeature;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import java.util.List;
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
        List<Boolean> equalsMap = Lists.newArrayList();
        for (int i = 0; i < part.partFeature.size(); i++) {
            System.out.println("index: " + i);
            PartFeature partFeature = part.partFeature.get(i);
            System.out.println("Part Feature: " + partFeature);
            System.out.println("Part Feature: " + partFeature.specification);
            for (ComponentFeature componentFeature : component.compatibility){
                System.out.println("Component Feature: " + componentFeature);
                System.out.println("Component Feature: " + componentFeature.specification);
                if (componentFeature.specification.description.equals(partFeature.specification.description)  && componentFeature.value == partFeature.value) {
                    equalsMap.add( Boolean.TRUE);
                    break;
                }
            }

        }

        return equalsMap.size() == part.partFeature.size();
    }

}
