package util;

import java.util.Arrays;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;


public class Segmentacao {

	public static Image segmentar(Image img,int cores) {
		Cluster[] clusters;
		int w = (int)img.getWidth();
		int h = (int)img.getHeight();
		PixelReader pr = img.getPixelReader();
		WritableImage wi = new WritableImage(w, h);
		PixelWriter pw = wi.getPixelWriter();
		
		clusters = createClusters(img, cores);
		
		double[] totPix = new double[(int)(w * h)];
		Arrays.fill(totPix, -1);
		boolean pixelChangedCluster = true;

		int loops = 0;
		while (pixelChangedCluster) {
			pixelChangedCluster = false;
			loops++;
			for (int y = 0; y < h; y++) {
				for (int x = 0; x < w; x++) {
					Pixel pixel = new Pixel(pr,x,y);
					Cluster cluster = findMinimalCluster(pixel,clusters);
					if (totPix[w * y + x] != cluster.getId()) {
						
						if (totPix[w * y + x] != -1) {
							clusters[(int) totPix[w * y + x]].removePixel(pixel);
						}
							
						cluster.addPixel(pixel);
						
						pixelChangedCluster = true;

						
						totPix[w * y + x] = cluster.getId();
					}
				}
			}
		}
		
		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				int clusterId = (int) totPix[w * y + x];
				pw.setColor(x, y, clusters[clusterId].getColor());
			}
		}
		return wi;
	}
	
	private static Cluster findMinimalCluster(Pixel pixel,Cluster[] clusters) {
		Cluster cluster = null;
		double min = Integer.MAX_VALUE;
		for (int i = 0; i < clusters.length; i++) {
			double distance = clusters[i].distance(pixel);
			if (distance < min) {
				min = distance;
				cluster = clusters[i];
			}
		}
		return cluster;
	}

	public static Cluster[] createClusters(Image image, int k) {
		Cluster[] result = new Cluster[k];
		int x = 0;
		int y = 0;
		int dx = (int) (image.getWidth() / k);
		int dy = (int) (image.getHeight() / k);
		for (int i = 0; i < k; i++) {
			result[i] = new Cluster(i, new Pixel(image.getPixelReader(),x,y));
			x += dx;
			y += dy;
		}
		return result;
	}

}
