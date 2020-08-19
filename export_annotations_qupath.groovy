import qupath.lib.objects.PathObjects

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

def images = []

def imgs_path = "Path to Images" 

def project = getProject()



for (entry in project.getImageList()) {

    def hierarchy = entry.readHierarchy()

    int index = 0
    
    

    def annot = []
    
    for (annotation in hierarchy.getAnnotationObjects()) {

        def roi = annotation.getROI()

        def isaretleme = ['index': index, 'label': annotation.pathClass.name, 'leftTopx': roi.x, 'leftTopy': roi.y, 'xwidth': roi.x2 - roi.x, 'yheight': roi.y2 - roi.y]

        annot << isaretleme

        index = index + 1

    }
    
    def img = ImageIO.read(new File("/" + imgs_path + "/" + entry));
    
    def server = getCurrentImageData().getServer()
    
    def tarama = ['Width':img.getWidth(),'Height':img.getHeight(),'filename': entry.getImageName(), 'isaretler': annot]

    images << tarama

}

 

boolean prettyPrint = true

def gson = GsonTools.getInstance(prettyPrint)

FileWriter writer = new FileWriter("Annotation export path")

gson.toJson(images,writer)

writer.close()
