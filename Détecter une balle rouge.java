/**
* @author DI SANTOLO Richard 
* Programme pour détecter une balle de couleur rouge
**/
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
 
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
 
//Bibliothèque externe
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;
 
public class Main {
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }
    static Mat frame = null;
 
    public static void main(String arg[]) {
    //Paramétrage de la fenetre pour le flux vidéo
        JFrame jframe = new JFrame("Suivre une balle rouge");// Titre de la fenetre
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// Fermeture de l'application
        JLabel vidpanel = new JLabel();
        jframe.setContentPane(vidpanel);
        jframe.setSize(640, 480);// Dimension de la fenetre
        jframe.setVisible(true);// Rendre visible la fenetre
        
    //Capture de flux video de la caméra par defaut
        VideoCapture capture = new VideoCapture(0);// "0" pour avoir le flux de la caméra ou webcam

    //Déclaration des matrices (pour le stockage)
        Mat frame = new Mat();
        Mat hsv_image = new Mat();
        Mat thresholded = new Mat();
        Mat thresholded2 = new Mat();

        /**
         *   Nous utilisons le modèle TSV (ou HSV en anglais),
         *   ceci offre une plus grande plage de couleur que
         *   le modèle RVB (ou RGB en anglais)    
         **/
        Scalar hsv_min = new Scalar(0, 50, 50, 0);
        Scalar hsv_max = new Scalar(6, 255, 255, 0);
        Scalar hsv_min2 = new Scalar(175, 50, 50, 0);
        Scalar hsv_max2 = new Scalar(179, 255, 255, 0);
 
        Size sz = new Size(640, 480);
 
        capture.read(frame);
        if (capture.isOpened()) {
            while (true) {
                capture.read(frame);
                if (!frame.empty()) {
 
                    Imgproc.resize(frame, frame, sz);
 
                    Imgproc.cvtColor(frame, hsv_image, Imgproc.COLOR_BGR2HSV);
                    Core.inRange(hsv_image, hsv_min, hsv_max, thresholded);
                    Core.inRange(hsv_image, hsv_min2, hsv_max2, thresholded2);
                    Core.bitwise_or(thresholded, thresholded2, thresholded);
 
                    ImageIcon image = new ImageIcon(Mat2bufferedImage(thresholded));
                    vidpanel.setIcon(image);
                    vidpanel.repaint();
 
                }
            }
        }
 
    }

 /**
  * Convertion de la structure Mat en une BufferedImage
  * @param Mat, c'est une matrice contenant l'image
  */
    public static BufferedImage Mat2bufferedImage(Mat image) {
        MatOfByte bytemat = new MatOfByte();
        Highgui.imencode(".jpg", image, bytemat);
        byte[] bytes = bytemat.toArray();
        InputStream in = new ByteArrayInputStream(bytes);
        BufferedImage img = null;
        try {
            img = ImageIO.read(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return img;
    }//FIN Mat2bufferedImage
}