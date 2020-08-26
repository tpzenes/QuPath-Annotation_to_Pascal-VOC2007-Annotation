import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class deneme_parse {
    public static void main(String argv[]) {
 
        try {
        	
            //JSON parser object to parse read file
            JSONParser jsonParser = new JSONParser();
            
            try (FileReader reader = new FileReader("Path to json Annotation File")){
            	
            	//Read JSON file
                Object obj = jsonParser.parse(reader);

                JSONArray annots = (JSONArray) obj;
                
                for (int i=0; i<annots.size();i++) {
             	    
                  JSONObject annot = (JSONObject) annots.get(i);
              	   
              	  String fname = (String) annot.get("filename");
              	  String wdth =  String.valueOf(annot.get("Width"));
              	  String hght =  String.valueOf(annot.get("Height"));
                	
                  String xmlFilePath = "Path to new files to be created" + fname + ".xml";
                  
             	  DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
                  
                  DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
       
                  Document document = documentBuilder.newDocument();
       
                  // root element
                  Element root = document.createElement("annotation");
                  document.appendChild(root);
       
                  //you can also use staff.setAttribute("id", "1") for this
       
                  // folder element
                  Element folder = document.createElement("folder");
                  folder.appendChild(document.createTextNode("VOC2007"));
                  root.appendChild(folder);
                  	
                  // filename element
                  Element filename = document.createElement("filename");
                  filename.appendChild(document.createTextNode(fname));
                  root.appendChild(filename);
                  
                  // source element
                  Element source = document.createElement("source");
                  root.appendChild(source);
                  
                  // database element
                  Element database = document.createElement("database");
                  database.appendChild(document.createTextNode("The VOC2007 Database"));
                  source.appendChild(database);
                  
                  // annotation element
                  Element annotation = document.createElement("annotation");
                  annotation.appendChild(document.createTextNode("PASCAL VOC2007"));
                  source.appendChild(annotation);
                  
                  // image element
                  Element image = document.createElement("image");
                  image.appendChild(document.createTextNode("flickr"));
                  source.appendChild(image);
                  
                  // flickrid element
                  Element flickrid = document.createElement("flickrid");
                  flickrid.appendChild(document.createTextNode("unspecified"));
                  source.appendChild(flickrid);
                  
                  // owner element
                  Element owner = document.createElement("owner");
                  root.appendChild(owner);
                  
                  // flickrid2 element
                  Element flickrid2 = document.createElement("flickrid");
                  flickrid2.appendChild(document.createTextNode("unspecified"));
                  owner.appendChild(flickrid2);
                  
                  // name element
                  Element name = document.createElement("name");
                  name.appendChild(document.createTextNode("unspecified"));
                  owner.appendChild(name);
                  
                  // size element
                  Element size = document.createElement("size");
                  root.appendChild(size);
                  
                  // width element
                  Element width = document.createElement("width");
                  width.appendChild(document.createTextNode(wdth));
                  size.appendChild(width);
                  
                  // height element
                  Element height = document.createElement("height");
                  height.appendChild(document.createTextNode(hght));
                  size.appendChild(height);
                  
                  // depth element
                  Element depth = document.createElement("depth");
                  depth.appendChild(document.createTextNode("1"));
                  size.appendChild(depth);
			
		  // segmented element
                  Element segmented = document.createElement("segmented");
                  segmented.appendChild(document.createTextNode("unspecified"));
                  root.appendChild(segmented);
                  
                  JSONArray annotArray = (JSONArray) annot.get("isaretler");
            	  
            	  int objcount = annotArray.size() ;
			
                  for (int j=0;j<objcount;j++) {
                	  
                     JSONObject isaret = (JSONObject) annotArray.get(j);
                      
                      
                      String label = (String) isaret.get("label");
                      
                      String xmn = String.valueOf((int) (double) isaret.get("leftTopx"));
                      String ymn = String.valueOf((int) (double) isaret.get("leftTopy"));
                      String xmx = String.valueOf((int) ((double) isaret.get("leftTopx") + (double) isaret.get("xwidth")));
                      String ymx = String.valueOf((int) ((double) isaret.get("leftTopy") + (double) isaret.get("yheight")));
                	  
                      // object element
                      Element object = document.createElement("object");
                      root.appendChild(object);
                      
                      // objname element
                      Element objname = document.createElement("name");
                      objname.appendChild(document.createTextNode(label));
                      object.appendChild(objname);
                      
                      // pose element
                      Element pose = document.createElement("pose");
                      pose.appendChild(document.createTextNode("unspecified"));
                      object.appendChild(pose);
                      
                      // truncated element
                      Element truncated = document.createElement("truncated");
                      truncated.appendChild(document.createTextNode("unspecified"));
                      object.appendChild(truncated);
                      
                      // difficult element
                      Element difficult = document.createElement("difficult");
                      difficult.appendChild(document.createTextNode("unspecified"));
                      object.appendChild(difficult);
                      
                      // bndbx element
                      Element bndbx = document.createElement("bndbx");
                      object.appendChild(bndbx);
                      
                      // xmin element
                      Element xmin = document.createElement("xmin");
                      xmin.appendChild(document.createTextNode(xmn));
                      bndbx.appendChild(xmin);
                      
                      // ymin element
                      Element ymin = document.createElement("ymin");
                      ymin.appendChild(document.createTextNode(ymn));
                      bndbx.appendChild(ymin);
                      
                      // xmax element
                      Element xmax = document.createElement("xmax");
                      xmax.appendChild(document.createTextNode(xmx));
                      bndbx.appendChild(xmax);
                      
                      // ymax element
                      Element ymax = document.createElement("ymax");
                      ymax.appendChild(document.createTextNode(ymx));
                      bndbx.appendChild(ymax);
                  }
       
                  // create the xml file
                  //transform the DOM Object to an XML File
                  TransformerFactory transformerFactory = TransformerFactory.newInstance();
                  Transformer transformer = transformerFactory.newTransformer();
                  DOMSource domSource = new DOMSource(document);
                  StreamResult streamResult = new StreamResult(new File(xmlFilePath));
       
                  transformer.transform(domSource, streamResult);
       
                  System.out.println("Done creating XML File");
             	   
             	   
                }
            	
            }
            catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }    
 
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (TransformerException tfe) {
            tfe.printStackTrace();
        }
    }
	
}
