package controllers;

import com.google.common.collect.Lists;
import models.*;
import play.mvc.Controller;

import java.util.List;

/**
 * @author ezeid on 6/18/14.
 */
public class Administration extends Controller {


    //FEATURES

    public static void features() {
        List<Feature> features = Feature.findAll();
        render(features);
    }

    public static void saveFeature(String description) {
        Feature feature = new Feature();
        feature.description = description;
        feature.save();
        features();
    }

    public static void removeFeature(List<Long> selectedFeature) {
        for(Long id : selectedFeature) {
            Feature feature = Feature.findById(id);
            feature.delete();
        }
        features();
    }

    //COMPONENT TYPES

    public static void componentTypes() {
        List<ComponentType> componentTypes = ComponentType.findAll();
        List<Feature> features = Feature.findAll();
        renderArgs.put("features", features);
        render(componentTypes);
    }

    public static void saveType(String description, List<Long> selectedFeatures) {
        List<Feature> features = Lists.newArrayList();
        for(Long id : selectedFeatures) {
            Feature feature = Feature.findById(id);
            features.add(feature);
        }
        ComponentType type = new ComponentType();
        type.description = description;
        type.features = features;
        type.save();
        componentTypes();
    }

    public static void removeTypes(List<Long> selectedTypes) {
        for (Long id : selectedTypes) {
            ComponentType type = ComponentType.findById(id);
            type.delete();
        }
        componentTypes();
    }

    //TRADEMARKS

    public static void trademarks() {
        List<ComponentTrademark> marks = ComponentTrademark.findAll();
        render(marks);
    }

    public static void saveMark(String description) {
        ComponentTrademark mark = new ComponentTrademark();
        mark.description = description;
        mark.save();
        trademarks();
    }

    public static void removeMark(List<Long> selectedMarks) {
        for(Long id : selectedMarks) {
            ComponentTrademark mark = ComponentTrademark.findById(id);
            mark.delete();
        }
        trademarks();
    }

    //PARTS

    public static void parts() {

        List<ComponentType> types = ComponentType.findAll();
        renderArgs.put("types", types);
        List<Part> parts = Part.findAll();
        renderArgs.put("parts", parts);
        render();
    }

    public static void savePart(String description, Long selectedType, List<String> partFeatures) {
        //Busco el tipo seleccionado
        ComponentType type = ComponentType.findById(selectedType);
        if (type != null) {
            int i = 0;
            List<PartFeature> partFeaturesList = Lists.newArrayList();
            Part part = new Part();
            for (Feature feature : type.features) {
                PartFeature partFeature = new PartFeature();
                partFeature.specification = feature;
                partFeature.value = partFeatures.get(i);
                partFeature.part = part;
                partFeaturesList.add(partFeature);
                i++;
            }
            part.description = description;
            part.partFeature = partFeaturesList;
            part.type = type;

            part.save();
        }

        parts();
    }

    public static void removePart(List<Long> selectedComponents) {
        for (Long id : selectedComponents) {
            Part part = Part.findById(id);
            part.delete();
        }
        parts();
    }


    //PARTS

    public static void components() {
        List<ComponentTrademark> trademarks = ComponentTrademark.findAll();
        renderArgs.put("trademarks", trademarks);
        List<ComponentType> types = ComponentType.findAll();
        renderArgs.put("types", types);
        List<Component> components = Component.findAll();
        renderArgs.put("components", components);
        render();
    }

    public static void saveComponent(String model, Long trademark, Long selectedType, String partNumber, List<String> componentFeatures) {
        //Busco el tipo seleccionado
        ComponentType type = ComponentType.findById(selectedType);
        if (type != null) {
            int i = 0;
            List<ComponentFeature> componentFeaturesList = Lists.newArrayList();
            Component component = new Component();
            for (Feature feature : type.features) {
                ComponentFeature componentFeature = new ComponentFeature();
                componentFeature.specification = feature;
                componentFeature.value = componentFeatures.get(i);
                componentFeature.component = component;
                componentFeaturesList.add(componentFeature);
                i++;
            }
            component.model = model;
            component.trademark = ComponentTrademark.findById(trademark);
            component.partNumber = partNumber;
            component.compatibility = componentFeaturesList;
            component.type = type;

            component.save();
        }

        components();
    }

    public static void removeComponent(List<Long> selectedComponent) {
        for (Long id : selectedComponent) {
            Component component = Component.findById(id);
            component.delete();
        }
        components();
    }

}
