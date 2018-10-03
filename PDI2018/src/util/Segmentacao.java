package util;

import java.awt.image.BufferedImage;
import java.util.Arrays;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class Segmentacao {

	public static Image segmentar(Image img,int cores) {
		long start = System.currentTimeMillis();
		Cluster[] clusters;
		int w = (int)img.getWidth();
		int h = (int)img.getHeight();
		PixelReader pr = img.getPixelReader();
		WritableImage wi = new WritableImage(w, h);
		PixelWriter pw = wi.getPixelWriter();
		// create clusters
		clusters = createClusters(img, cores);
		// create cluster lookup table
		double[] lut = new double[(int)(w * h)];
		Arrays.fill(lut, -1);
		boolean pixelChangedCluster = true;

		int loops = 0;
		while (pixelChangedCluster) {
			pixelChangedCluster = false;
			loops++;
			for (int y = 0; y < h; y++) {
				for (int x = 0; x < w; x++) {
					Pixel pixel = new Pixel(pr,x,y);
					Cluster cluster = findMinimalCluster(pixel,clusters);
					if (lut[w * y + x] != cluster.getId()) {
						// cluster changed
						if (lut[w * y + x] != -1) {
							clusters[(int) lut[w * y + x]].removePixel(pixel);
						}
							// add pixel to cluster
						cluster.addPixel(pixel);
						// continue looping
						pixelChangedCluster = true;

						// update lut
						lut[w * y + x] = cluster.getId();
					}
				}
			}
		}
		// create result image
		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				int clusterId = (int) lut[w * y + x];
				pw.setColor(x, y, clusters[clusterId].getColor());
			}
		}
		long end = System.currentTimeMillis();
		System.out.println("Clustered to " + cores + " clusters in " + loops + " loops in " + (end - start) + " ms.");
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
		// Here the clusters are taken with specific steps,
		// so the result looks always same with same image.
		// You can randomize the cluster centers, if you like.
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
