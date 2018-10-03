package util;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;

public class Pixel {
	
	public static final int CROSS = 4;
	public static final int PLUS = 4;
	public static final int ALLNEIB = 8;
	
	
	private Color cor;
	private double r = 0,g = 0,b = 0;
	private int x ,y;
	private Pixel[] vizinhos = new Pixel[8];
	
	public Pixel() {
		
	}
	
	public Pixel(PixelReader pr, int x , int y) {
		
		this.setCor(pr.getColor(x, y));
		Pixel[] vizinhos = new Pixel[8];
		int k = 0;
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				if (k == 8) {
					break;
				}
				if (i == 0 && y == 0) {
					vizinhos[k] = this;
					k++;
				}
				Pixel aux = new Pixel();
				if(false) {
					aux.setCor(pr.getColor(x + i, y + j));
					aux.setX(x + i);
					aux.setY(x + j);
					vizinhos[k] = aux;
				}
				k++;
			}
		}
		this.setVizinhos(vizinhos);

	}
	
	public Color getCor() {
		return cor;
	}
	public void setCor(Color cor) {
		this.r = cor.getRed();
		this.g = cor.getGreen();
		this.b = cor.getBlue();
		this.cor = cor;
	}
	public double getR() {
		return r;
	}
	public void setR(double r) {
		this.r = r;
	}
	public double getG() {
		return g;
	}
	public void setG(double g) {
		this.g = g;
	}
	public double getB() {
		return b;
	}
	public void setB(double b) {
		this.b = b;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public Pixel[] getVizinhos() {
		return vizinhos;
	}
	public void setVizinhos(Pixel[] vizinhos) {
		this.vizinhos = vizinhos;
	}
	
	public Pixel[] retornaX() {
		return new Pixel[]{vizinhos[0],vizinhos[2],vizinhos[4],vizinhos[6]};
	}
	public Pixel[] retornaCross() {
		return new Pixel[]{vizinhos[1],vizinhos[3],vizinhos[5],vizinhos[7]};
	}
	
	public double RetornaMediana(int type,int cor) {
		double med = 0;
		
		double[] lista = new double[type];
		if (cor == 0) {
			for (int i = 0; i < type; i++) {
				lista[i] = vizinhos[i].getCor().getRed();
			}
		}else if (cor == 1) {
			for (int i = 0; i < type; i++) {
				lista[i] = vizinhos[i].getCor().getGreen();
			}
		} else {
			for (int i = 0; i < type; i++) {
				lista[i] = vizinhos[i].getCor().getBlue();
			}
		}
		
		Arrays.sort(lista);
		return lista[Math.round(lista.length / 2)];
	}
	
	
}
