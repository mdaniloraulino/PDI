package openCV;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

public class OpenCVUtis {
	
	public static Image faceDetection(Image img) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		URL url = OpenCVUtis.class.getResource("haarcascade_frontalface_alt.xml");
		String a = url.getPath();
		a = a.substring(1, a.length());
		CascadeClassifier faceDetector = new CascadeClassifier(a);
		Mat image = imgToMat(img);

		MatOfRect faceDetections = new MatOfRect();
		faceDetector.detectMultiScale(image, faceDetections);

		System.out.println(String.format("Detected %s faces", faceDetections.toArray().length));

		for (Rect rect : faceDetections.toArray()) {
			Imgproc.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height),
					new Scalar(0, 255, 0));
		}

		String filename = "ouput.png";
		System.out.println(String.format("Writing %s", filename));
		Imgcodecs.imwrite(filename, image);

		return matToImg(image);
	}
	
	public static Image matToImg(Mat m) {
		MatOfByte mob = new MatOfByte();
	    Imgcodecs.imencode(".jpg", m, mob);
	    byte ba[] = mob.toArray();

	    BufferedImage bi;
		try {
			bi = ImageIO.read(new ByteArrayInputStream(ba));
			
			Image image = SwingFXUtils.toFXImage(bi, null );
		    
		    return image;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    return null;
	}
	
	public static Mat imgToMat(Image image) {
		BufferedImage bi = SwingFXUtils.fromFXImage(image, null);
		BufferedImage convertedImg = new BufferedImage(bi.getWidth(), bi.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
	    convertedImg.getGraphics().drawImage(bi, 0, 0, null);
	    
		Mat mat = new Mat(bi.getHeight(), bi.getWidth(), CvType.CV_8UC3);
		
		byte[] data = ((DataBufferByte) convertedImg.getRaster().getDataBuffer()).getData();
		mat.put(0, 0, data);
		
		return mat;
	}
	
	public static Image erosaoDilatacao(Image img,Boolean doErosion) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		int kernelSize = 3;
		Mat returnImg = new Mat();
		Mat element = Imgproc.getStructuringElement(0, new Size(2 * kernelSize + 1,2 * kernelSize + 1), new Point(kernelSize,kernelSize));
        if (doErosion) {
            Imgproc.erode(imgToMat(img), returnImg, element);
        } else {
            Imgproc.dilate(imgToMat(img), returnImg, element);
        }
		
		return matToImg(returnImg);
	}
	
	public static Image canny(Image img, int tr) {
		
		Imgproc.blur(src, srcBlur, BLUR_SIZE);
        Imgproc.Canny(srcBlur, detectedEdges, lowThresh, lowThresh * RATIO, KERNEL_SIZE, false);
        dst = new Mat(src.size(), CvType.CV_8UC3, Scalar.all(0));
        src.copyTo(dst, detectedEdges);
		
	}

}