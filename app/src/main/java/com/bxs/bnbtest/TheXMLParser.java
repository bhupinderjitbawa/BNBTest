package com.bxs.bnbtest;

import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by bhupinder on 5/4/15.
 */
public class TheXMLParser {

    public Document getDomElement(String xml){
        Document doc = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {

            DocumentBuilder db = dbf.newDocumentBuilder();

            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(xml));
            doc = db.parse(is);

        } catch (ParserConfigurationException e) {
            Log.e("Error: ", e.getMessage());
            return null;
        } catch (SAXException e) {
            Log.e("Error: ", e.getMessage());
            return null;
        } catch (IOException e) {
            Log.e("Error: ", e.getMessage());
            return null;
        }
        // return DOM
        return doc;
    }

    public String getValue(Element item, String str) {
        NodeList n = item.getElementsByTagName(str);
        return this.getElementValue(n.item(0));
    }

    public final String getElementValue( Node elem ) {
        Node child;
        if( elem != null){
            if (elem.hasChildNodes()){
                for( child = elem.getFirstChild(); child != null; child = child.getNextSibling() ){
                    if( child.getNodeType() == Node.TEXT_NODE  ){
                        return child.getNodeValue();
                    }
                }
            }
        }
        return "";
    }

    public ArrayList<PropertyModel> getAvailability(String xml){
        ArrayList<PropertyModel> propertyModelArrayList = new ArrayList<PropertyModel>();

        final String KEY_ITEM = "AvailableProperty";
        Document doc = getDomElement(xml); // getting DOM element
        PropertyModel model =null;
        RoomModel roomModel = null;

        NodeList nl = doc.getElementsByTagName(KEY_ITEM);
        for (int i = 0; i < nl.getLength(); i++) {
            // creating new HashMap

            Element e = (Element) nl.item(i);
            model = new PropertyModel();
            model.setId(getValue(e, "PropertyId"));
            model.setName(getValue(e, "PropertyName"));
            model.setAddress(getValue(e, "Address1"));
            model.setDescription(getValue(e, "GeneralDescription"));
            model.setPhone(getValue(e, "Phone"));
            model.setPicture(getValue(e, "MainPictureUrl"));
            if(getValue(e, "CVVRequired").equals("TRUE")){
                model.setCvvRequired(true);
            } else {
                model.setCvvRequired(false);
            }
            model.setTaxRate(getValue(e, "ApplicableTaxRate"));

            NodeList n2 = doc.getElementsByTagName("AvailableRooms").item(i).getChildNodes();
            Log.i("n2", n2.getLength() +"");
            ArrayList<RoomModel> roomModels = new ArrayList<RoomModel>();
            for (int j = 0; j < n2.getLength(); j++) {
                Element e1 = (Element) n2.item(j);

                roomModel = new RoomModel();
                roomModel.setId(getValue(e1,"RoomId"));
                roomModel.setName(getValue(e1, "RoomName"));
                roomModel.setMaxPeople(getValue(e1, "MaxPeople"));
                roomModel.setPrice(getValue(e1, "PriceList"));
                roomModel.setPicture(getValue(e1, "PictureUrl"));

                roomModels.add(roomModel);




            }
            Log.i("roommodelsize", roomModels.size()+"");
            model.setRoomModelArrayList(roomModels);


            // adding HashList to ArrayList
                propertyModelArrayList.add(model);
        }
        return propertyModelArrayList;

    }


    public ArrayList<GeoModel> getGeoList(String xml){
        ArrayList<GeoModel> geoList = new ArrayList<GeoModel>();
        final String KEY_ITEM = "Geography";
        Document doc = getDomElement(xml); // getting DOM element
        GeoModel model =null;

        NodeList nl = doc.getElementsByTagName(KEY_ITEM);
        for (int i = 0; i < nl.getLength(); i++) {
            // creating new HashMap

            Element e = (Element) nl.item(i);
            model = new GeoModel();
            model.setId(getValue(e, "GeoId"));
            model.setName(getValue(e, "GeoName"));
            model.setState(getValue(e, "StateName"));
            model.setCountry(getValue(e, "CountryName"));

            // adding HashList to ArrayList
            geoList.add(model);
        }
        return geoList;

    }
    public HashMap<String, String> getUniqueId(String xml){
        HashMap<String, String> unique= new HashMap<String, String>();
        final String KEY_ITEM = "CRSResponse";
        Document doc = getDomElement(xml); // getting DOM element
        GeoModel model =null;

        NodeList nl = doc.getElementsByTagName(KEY_ITEM);
        // looping through all item nodes <item>
        for (int i = 0; i < nl.getLength(); i++) {
            // creating new HashMap
            Element e = (Element) nl.item(i);
            // adding each child node to HashMap key => value
            unique.put("id",getValue(e, "UniqueBookingId"));
            unique.put("Error",getValue(e, "ErrorMessage"));
        }

            // creating new HashMap

            Element e = (Element) nl.item(0);
//            unique = getValue(e, "UniqueBookingId");


            // adding HashList to ArrayList

        return unique;

    }
}
