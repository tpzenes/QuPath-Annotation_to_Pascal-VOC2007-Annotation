import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class xml_json_parse {
   public static void main(String[] args) {
	   
       int xmin_arr[ ] = new int[300];
       int ymin_arr[ ] = new int[300];
       int xmax_arr[ ] = new int[300];
       int ymax_arr[ ] = new int[300];
       String label_arr[] = new String[300];
       String filename;
       long width,height;
       
       //JSON parser object to parse read file
       JSONParser jsonParser = new JSONParser();
        
       try (FileReader reader = new FileReader("QuPath Annotation Json File (After executing QuPath annotation export script)"))
       {
           //Read JSON file
           Object obj = jsonParser.parse(reader);

           JSONArray annots = (JSONArray) obj;
           
           
           for (int i=0; i<annots.size();i++) {
        	   
               JSONObject annot = (JSONObject) annots.get(i);        	   
               double xmin,ymin,xmax,ymax;
    	   	   
    	       filename = (String) annot.get("filename");
    	       width = (long) annot.get("width");
    	       height = (long) annot.get("height");
    	       
    	       JSONArray annotArray = (JSONArray) annot.get("isaretler");
    	       
    	       
    	       for (int j = 0; j < annotArray.size(); j++)
    	       {
    	       	JSONObject isaret = (JSONObject) annotArray.get(j);
    	       	
    	       	xmin = (double) isaret.get("leftTopx");
    	       	ymin = (double) isaret.get("leftTopy");
    	       	xmax = (double) isaret.get("leftTopx") + (double) isaret.get("xwidth");
    	       	ymax = (double) isaret.get("leftTopy") + (double) isaret.get("yheight");
    	       	
    	       	String label = (String) isaret.get("label");
    	       	
    	       	xmin_arr[j] = (int) xmin;
    	       	ymin_arr[j] = (int) ymin;
    	       	xmax_arr[j] = (int) xmax;
    	       	ymax_arr[j] = (int) ymax;
    	       	
    	       	label_arr[j] = label;
    	       }
        	   
        	   try {
        	         File inputFile = new File("PascalVoc207 Annotation Xml File Path");
        	         DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        	         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        	         Document doc = dBuilder.parse(inputFile);
        	         doc.getDocumentElement().normalize();
        	         
        	         NodeList nList = doc.getElementsByTagName("object");
        	         
        	         doc.getElementsByTagName("filename").item(0).setTextContent(filename);
        	         doc.getElementsByTagName("segmented").item(0).setTextContent("unspecified");
        	         
        	         NodeList sList = doc.getElementsByTagName("source");
        	         
        	         for (int temp3 = 0; temp3 < sList.getLength(); temp3++) {
        	        	 
        	        	 Node sNode = sList.item(temp3);
        	        	 
        	        	 if (sNode.getNodeType() == Node.ELEMENT_NODE) {
        	        		 
        	        		 Element sElement = (Element) sNode;
        	        		 
        	        		 sElement.getElementsByTagName("flickrid").item(0).setTextContent("unspecified");
        	        	 }
        	        	 
        	         }
        	         
        	         NodeList oList = doc.getElementsByTagName("owner");
        	         
        	         for (int temp4 = 0; temp4 < sList.getLength(); temp4++) {
        	        	 
        	        	 Node oNode = oList.item(temp4);
        	        	 
        	        	 if (oNode.getNodeType() == Node.ELEMENT_NODE) {
        	        		 
        	        		 Element oElement = (Element) oNode;
        	        		 
        	        		 oElement.getElementsByTagName("flickrid").item(0).setTextContent("unspecified");
        	        		 oElement.getElementsByTagName("name").item(0).setTextContent("unspecified");
        	        	 }
        	        	 
        	         }
        	         
        	         NodeList szList = doc.getElementsByTagName("size");
        	         
        	         for (int temp5 = 0; temp5 < sList.getLength(); temp5++) {
        	        	 
        	        	 Node szNode = szList.item(temp5);
        	        	 
        	        	 if (szNode.getNodeType() == Node.ELEMENT_NODE) {
        	        		 
        	        		 Element szElement = (Element) szNode;
        	        		 
        	        		 szElement.getElementsByTagName("width").item(0).setTextContent(String.valueOf(width));
        	        		 szElement.getElementsByTagName("height").item(0).setTextContent(String.valueOf(height));
        	        		 szElement.getElementsByTagName("depth").item(0).setTextContent("1");
        	        	 }
        	        	 
        	         }
        	         
        	         for (int temp = 0; temp < nList.getLength(); temp++) {
        	            Node nNode = nList.item(temp);
        	            
        	            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
        	               Element eElement = (Element) nNode;
        	               
        	               eElement.getElementsByTagName("xmin").item(0).setTextContent(String.valueOf(xmin_arr[temp]));
        	               eElement.getElementsByTagName("ymin").item(0).setTextContent(String.valueOf(ymin_arr[temp]));
        	    	       eElement.getElementsByTagName("xmax").item(0).setTextContent(String.valueOf(xmax_arr[temp]));
        	    	       eElement.getElementsByTagName("ymax").item(0).setTextContent(String.valueOf(ymax_arr[temp]));
        	    	       eElement.getElementsByTagName("name").item(0).setTextContent(label_arr[temp]);
        	    	       eElement.getElementsByTagName("truncated").item(0).setTextContent("unspecified");
        	    	       eElement.getElementsByTagName("difficult").item(0).setTextContent("unspecified");
        	    	       eElement.getElementsByTagName("pose").item(0).setTextContent("unspecified");
        	            }
        	         }
        	         
        	         TransformerFactory transformerFactory = TransformerFactory.newInstance();
        	         Transformer transformer = transformerFactory.newTransformer();
        	         DOMSource domSource = new DOMSource(doc);
        	         StreamResult streamResult = new StreamResult(new File("Export Xml File Path" + filename + ".xml"));
        	         transformer.transform(domSource, streamResult);
        	         
        	      } catch (Exception e) {
        	         e.printStackTrace();
        	      }
        	   
           }
           

       } catch (FileNotFoundException e) {
           e.printStackTrace();
       } catch (IOException e) {
           e.printStackTrace();
       } catch (ParseException e) {
           e.printStackTrace();
       }
      
   }
   
}
