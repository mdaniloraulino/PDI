package util;

import javafx.scene.paint.Color;

public class Cluster {
	int id;
	double pixelCount;
	double red;
	double green;
	double blue;
	double reds;
	double greens;
	double blues;

	public Cluster(int id, Pixel pi) {
		double r = pi.getR();
		double g = pi.getG();
		double b = pi.getB();
		red = r;
		green = g;
		blue = b;
		this.id = id;
		addPixel(pi);
	}

	public void clear() {
		red = 0;
		green = 0;
		blue = 0;
		reds = 0;
		greens = 0;
		blues = 0;
		pixelCount = 0;
	}

	int getId() {
		return id;
	}

	Color getColor() {
		double r = reds / pixelCount;
		double g = greens / pixelCount;
		double b = blues / pixelCount;
		return new Color(r, g, b, 1);
	}

	void addPixel(Pixel pi) {
		double r = pi.getR();
		double g = pi.getG();
		double b = pi.getB();
		reds += r;
		greens += g;
		blues += b;
		pixelCount++;
		red = reds / pixelCount;
		green = greens / pixelCount;
		blue = blues / pixelCount;
	}

	void removePixel(Pixel pi) {
		double r = pi.getR();
		double g = pi.getG();
		double b = pi.getB();
		reds -= r;
		greens -= g;
		blues -= b;
		pixelCount--;
		red = reds / pixelCount;
		green = greens / pixelCount;
		blue = blues / pixelCount;
	}

	double distance(Pixel pi) {
		double r = pi.getR();
		double g = pi.getG();
		double b = pi.getB();
		int rx = Math.abs((int) (red * 255) - (int) (r * 255));
		int gx = Math.abs((int) (green * 255) - (int) (g * 255));
		int bx = Math.abs((int) (blue * 255) - (int) (b * 255));
		double d = (((double) rx / 255.0) + ((double) gx / 255.0) + ((double) bx / 255.0)) / 3.0;
		return d;
	}
}

