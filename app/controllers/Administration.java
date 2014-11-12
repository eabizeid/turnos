package controllers;

import com.google.common.collect.Lists;
import mercadopago.MP;
import models.*;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.junit.Before;
import play.db.jpa.Blob;
import play.mvc.Controller;
import play.mvc.With;
import service.mailer.MailSender;

import java.util.Date;
import java.util.List;

/**
 * @author ezeid on 6/18/14.
 */
@With(Secure.class)
public class Administration extends Controller {

    @Before
    static void setConnectedUser() {
        if(Security.isConnected()) {
            User user = User.find("byEmail", Security.connected()).first();
            renderArgs.put("user", user.fullname);
        }
    }
    public static void index() {
        render();
    }
    //FEATURES

    public static void features() {
        List<Feature> features = Feature.findAll();
        render(features);
    }

    public static void saveFeature(String description, Long selectedFeature) {
        Feature feature = null;
        if (selectedFeature != null) {
            feature = Feature.findById(selectedFeature);
        } else {
            feature = new Feature();
        }
        feature.description = StringUtils.upperCase(description);
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

    public static void saveType(String description, List<Long> selectedFeatures, Long selectedTypes) {
        List<Feature> features = Lists.newArrayList();
        if (selectedFeatures != null)
            for(Long id : selectedFeatures) {
                Feature feature = Feature.findById(id);
                features.add(feature);
            }
        ComponentType type = null;
        if (selectedTypes != null) {
            type = ComponentType.findById(selectedTypes);
        } else {
            type = new ComponentType();
        }
        type.description = StringUtils.upperCase(description);
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

    public static void saveMark(String description, Long selectedMarks) {

        ComponentTrademark mark = null;

        if (selectedMarks == null ) {
            mark = new ComponentTrademark();
        } else {
            mark = ComponentTrademark.findById(selectedMarks);
        }
        mark.description = StringUtils.upperCase(description);
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

    public static void savePart(String description, Long selectedType, List<String> partFeatures, Blob image) {
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
            Picture picture = new Picture();
            picture.image = image;
            part.image = picture;
            part.description = StringUtils.upperCase(description);
            part.partFeature = partFeaturesList;
            part.type = type;

            part.save();
        }

        parts();
    }

    public static void removeParts(List<Long> selectedParts) {
        for (Long id : selectedParts) {
            Part part = Part.findById(id);
            part.delete();
        }
        parts();
    }


    public static void editAPart(Long partId) {
        Part part = Part.findById(partId);
        render(part);

    }
    //COMPONENTS

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
            component.model = StringUtils.upperCase(model);
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

    public static void pendings() {
        List<Pending> pendings = Pending.findAll();
        renderArgs.put("trademarks", ComponentTrademark.findAll());
        renderArgs.put("types", ComponentType.findAll());
        render(pendings);
    }

    public static void resolvePending(Component component, Long idType, Long idTrademark, String pendingToResolve, List<String> componentFeatures, Blob image) {
        try {

            component.trademark = ComponentTrademark.findById(idTrademark);
            ComponentType type = ComponentType.findById(idType);
            component.type = type;
            List<ComponentFeature> compatibility = Lists.newArrayList();
            List<Feature> features = type.features;
            for  (int i=0; i < features.size(); i++) {
                ComponentFeature componentFeature = new ComponentFeature();
                componentFeature.specification = features.get(i);
                componentFeature.value = componentFeatures.get(i);
                componentFeature.component = component;
                compatibility.add(componentFeature);
            }
            component.compatibility = compatibility;
            component.save();
            generatePart(component, type, image);
            Pending pending = Pending.findById(Long.valueOf(pendingToResolve));
            long responseTime = (new Date()).getTime() - pending.timeInMs;
            for (Mail mail : pending.mails) {

                MailSender sender = new MailSender();
                sender.sendEmail(mail, component, responseTime, generateCheckoutURL(component));

            }
            ((Pending)Pending.findById(Long.valueOf(pendingToResolve))).delete();


        } catch (Exception e) {
            e.printStackTrace();
        }
        pendings();
    }

    private static void generatePart(Component component, ComponentType type,  Blob image) {
        Part part = new Part();
        part.description= component.type.description + " " + component.model;
        List<PartFeature> features = Lists.newArrayList();
        for (ComponentFeature componentFeature : component.compatibility) {
            Feature feature = Feature.findById(componentFeature.specification.getId());
            PartFeature partFeature = new PartFeature();
            partFeature.specification = feature;
            partFeature.value = componentFeature.value;
            partFeature.part = part;
            features.add(partFeature);
        }
        part.partFeature = features;
        part.type= type;
        Picture picture = new Picture();
        picture.image = image;
        part.image = picture;
        part.save();
    }

    private static String generateCheckoutURL(Component component) {

        String clientId = "8077350156322774";
        String clientSecret = "8Hl48rbM9UWPjQW3ug0xjCaujPQ5bcpk";

        MP mp = new MP(clientId, clientSecret);

        String checkoutURL = StringUtils.EMPTY;
        try {
            JSONObject preference = mp.createPreference("{'items':[{'title':'"+component.toString()+"','quantity':1,'currency_id':'ARS','unit_price':" + "component.price" + "}]}");
            checkoutURL = preference.getJSONObject("response").getString("sandbox_init_point");
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return checkoutURL;
    }
    public static void clients() {
        List<Mail> clients = Mail.findAll();
        render(clients);
    }
}
