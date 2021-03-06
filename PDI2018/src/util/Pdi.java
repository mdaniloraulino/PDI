package util;

import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class Pdi {

	public static Image escalaDeCinza(Image img, int r, int g, int b) {
		try {
			int w = (int) img.getWidth();
			int h = (int) img.getHeight();
			double m;
			boolean pond = false;
			PixelReader pr = img.getPixelReader();
			WritableImage wi = new WritableImage(w, h);
			PixelWriter pw = wi.getPixelWriter();
			pond = !(r == 0 && g == 0 && b == 0);
			for (int i = 0; i < w; i++) {
				for (int j = 0; j < h; j++) {
					Color ca = pr.getColor(i, j);
					if (pond) {
						// System.out.println(" "+(rgb[0] / 100)+" "+(rgb[1] / 100)+" "+(rgb[2] / 100));
						m = ((ca.getRed() * (r / 100.0))) + (ca.getGreen() * (g / 100.0))
								+ (ca.getBlue() * (b / 100.0)) / 100;
					} else {
						m = (ca.getRed() + ca.getGreen() + ca.getBlue()) / 3;
					}
					pw.setColor(i, j, new Color(m, m, m, ca.getOpacity()));
				}
			}
			return wi;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Image negativar(Image img) {
		try {
			int w = (int) img.getWidth();
			int h = (int) img.getHeight();
			double m;
			PixelReader pr = img.getPixelReader();
			WritableImage wi = new WritableImage(w, h);
			PixelWriter pw = wi.getPixelWriter();
			for (int i = 0; i < w; i++) {
				for (int j = 0; j < h; j++) {
					Color ca = pr.getColor(i, j);
					pw.setColor(i, j,
							new Color((1 - ca.getRed()), (1 - ca.getGreen()), (1 - ca.getBlue()), ca.getOpacity()));
				}
			}
			return wi;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Image limiarizacao(Image img, double limiar) {
		try {
			int w = (int) img.getWidth();
			int h = (int) img.getHeight();
			double m;
			PixelReader pr = img.getPixelReader();
			WritableImage wi = new WritableImage(w, h);
			PixelWriter pw = wi.getPixelWriter();
			for (int i = 0; i < w; i++) {
				for (int j = 0; j < h; j++) {
					Color ca = pr.getColor(i, j);
					if (ca.getRed() <= limiar) {
						pw.setColor(i, j, new Color(0, 0, 0, ca.getOpacity()));
					} else {
						pw.setColor(i, j, new Color(1, 1, 1, ca.getOpacity()));
					}

				}
			}
			return wi;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void getGrafico(Image img, BarChart<String, Number> grafico) {
		CategoryAxis eixoX = new CategoryAxis();
		NumberAxis eixoY = new NumberAxis();
		eixoX.setLabel("Intensidade");
		eixoY.setLabel("valor");
		XYChart.Series vlr = new XYChart.Series();
		vlr.setName("Intensidade");
		int[] hist = getHistogram(img);
		// int[] hist = new int[] {3,5,3,0,0,2,1,2};
		for (int i = 0; i < hist.length; i++) {
//	    	vlr.getData().add(new XYChart.Data(i+"", hist[i]/1000));
			vlr.getData().add(new XYChart.Data(i + "", hist[i] / 1000));
		}
		grafico.getData().addAll(vlr);
	}

	public static int[] getHistogram(Image img) {
		try {
			int w = (int) img.getWidth();
			int h = (int) img.getHeight();
			int[] a = new int[256];
			PixelReader pr = img.getPixelReader();
			for (int i = 0; i < w; i++) {
				for (int j = 0; j < h; j++) {
					a[(int) (pr.getColor(i, j).getRed() * 255)]++;
					a[(int) (pr.getColor(i, j).getGreen() * 255)]++;
					a[(int) (pr.getColor(i, j).getBlue() * 255)]++;
				}
			}
			return a;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Image removeRuido(Image img, int type) {
		try {
			int w = (int) img.getWidth();
			int h = (int) img.getHeight();
			PixelReader pr = img.getPixelReader();
			WritableImage wi = new WritableImage(w, h);
			PixelWriter pw = wi.getPixelWriter();
			for (int i = 1; i < w - 1; i++) {
				for (int j = 1; j < h - 1; j++) {
					Pixel px = retornaPixel(pr, i, j);
					Color ca = pr.getColor(i, j);
					double mR = px.RetornaMediana(type, 0);
					double mG = px.RetornaMediana(type, 1);
					double mB = px.RetornaMediana(type, 2);
					pw.setColor(i, j, new Color(mR, mG, mB, ca.getOpacity()));
				}
			}
			return wi;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Pixel retornaPixel(PixelReader pr, int x, int y) {
		Pixel px = new Pixel();
		px.setCor(pr.getColor(x, y));
		Pixel[] vizinhos = new Pixel[8];
		int k = 0;
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				if (k == 8) {
					break;
				}
				if (i == 0 && y == 0) {
					vizinhos[k] = px;
					k++;
				}
				Pixel aux = new Pixel();
				aux.setCor(pr.getColor(x + i, y + j));
				aux.setX(x + i);
				aux.setY(x + j);
				vizinhos[k] = aux;
				k++;
			}
		}
		px.setVizinhos(vizinhos);

		return px;
	}

	private static int[] returnSize(Image img1, Image img2) {
		return new int[] { (int) (img1.getHeight() < img2.getHeight() ? img1.getHeight() : img2.getHeight()),
				(int) (img1.getWidth() < img2.getWidth() ? img1.getWidth() : img2.getWidth()) };
	}

	public static Image adicaoImage(Image img1, Image img2, double pct, double pct2) {
		try {
			int[] size = returnSize(img1, img2);
			int w = size[1];
			int h = size[0];
			PixelReader pr = img1.getPixelReader();
			PixelReader pr2 = img2.getPixelReader();
			WritableImage wi = new WritableImage(w, h);
			PixelWriter pw = wi.getPixelWriter();
			for (int i = 0; i < w; i++) {
				for (int j = 0; j < h; j++) {
					Color c = pr.getColor(i, j);
					Color c2 = pr2.getColor(i, j);
					pw.setColor(i, j,
							new Color((c.getRed() * pct) + (c2.getRed() * pct2),
									(c.getGreen() * pct) + (c2.getGreen() * pct2),
									(c.getBlue() * pct) + (c2.getBlue() * pct2), c.getOpacity()));
				}
			}
			return wi;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Image subtractImage(Image img1, Image img2) {
		try {
			int[] size = returnSize(img1, img2);
			int w = size[1];
			int h = size[0];
			PixelReader pr = img1.getPixelReader();
			PixelReader pr2 = img2.getPixelReader();
			WritableImage wi = new WritableImage(w, h);
			PixelWriter pw = wi.getPixelWriter();
			for (int i = 0; i < w; i++) {
				for (int j = 0; j < h; j++) {
					Color c = pr.getColor(i, j);
					Color c2 = pr2.getColor(i, j);
					double r = c.getRed() - c2.getRed();
					double g = c.getGreen() - c2.getGreen();
					double b = c.getBlue() - c2.getBlue();
					pw.setColor(i, j, new Color(r < 0 ? 0 : r, g < 0 ? 0 : g, b < 0 ? 0 : b, c.getOpacity()));
				}
			}
			return wi;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Image criaBorda(Image img, int sX, int sY, int eX, int eY) {
		try {
			int stX, stY, enX, enY;
			if (sX > eX) {
				stX = eX;
				enX = sX;
			} else {
				stX = sX;
				enX = eX;
			}

			if (sY > eY) {
				stY = eY;
				enY = sY;
			} else {
				stY = sY;
				enY = eY;
			}

			int w = (int) img.getWidth();
			int h = (int) img.getHeight();
			PixelReader pr = img.getPixelReader();

			WritableImage wi = new WritableImage(pr, w, h);
			PixelWriter pw = wi.getPixelWriter();

			for (int i = stX; i < enX; i++) {
				// Color c = pr.getColor(i, i);
				pw.setColor(i, stY, new Color(0, 0, 0, 1));
				pw.setColor(i, enY, new Color(0, 0, 0, 1));
			}

			for (int i = stY; i < enY; i++) {
				// Color c = pr.getColor(i, i);
				pw.setColor(stX, i, new Color(0, 0, 0, 1));
				pw.setColor(enX, i, new Color(0, 0, 0, 1));
			}
			return wi;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Image Equalizar(Image img,boolean valids) {
		try {
			int[] hR = getHistogramBW(img, Constantes.R);
			int[] hG = getHistogramBW(img, Constantes.G);
			int[] hB = getHistogramBW(img, Constantes.B);
			double[] eR = new double[256];
			double[] eG = new double[256];
			double[] eB = new double[256];
			int w = (int) img.getWidth();
			int h = (int) img.getHeight();
			double tons = 0;
			
			double totPix = w * h;
			PixelReader pr = img.getPixelReader();
			WritableImage wi = new WritableImage(pr, w, h);
			PixelWriter pw = wi.getPixelWriter();
			
			if(valids) {
				tons = validPixels(getHistogram(img));
			}
			System.out.println(tons);
			
			eR = incHistograma(hR);
			eG = incHistograma(hG);
			eB = incHistograma(hB);

			double[] R = trataHistogramaCanal(eR, totPix);
			double[] G = trataHistogramaCanal(eG, totPix);
			double[] B = trataHistogramaCanal(eB, totPix);

			for (int i = 0; i < w; i++) {
				for (int j = 0; j < h; j++) {
					Color ca = pr.getColor(i, j);
					
					double nValR = R[(int) (ca.getRed() * 255)];
					double nValG = G[(int) (ca.getGreen() * 255)];
					double nValB = B[(int) (ca.getBlue() * 255)];
					double nValRT = ((tons - 1.0) / totPix) * (nValR * 255.0);
					double nValGT =  ((tons - 1.0) / totPix) * (nValR * 255.0);
					double nValBT =  ((tons - 1.0) / totPix) * (nValR * 255.0);
					//System.out.println( ((tons - 1.0) / totPix) + " " +((tons - 1.0) / totPix) * nValR );
					if(!valids) {
						pw.setColor(i, j, new Color((nValR), (nValG), (nValB), ca.getOpacity()));
					} else {
						pw.setColor(i, j, new Color((nValRT), (nValGT), (nValBT), ca.getOpacity()));
					}
					//System.out.println(nValR + " " + nValG + " " + nValB + " " );
					//System.out.println(nValRT + " " + nValGT + " " + nValBT + " " );
					
					
				}
			}
			return wi;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	

	public static double[] incHistograma(int[] histograma) {
		double[] equalizado = new double[256];
		for (int i = 1; i < 256; i++) {
			equalizado[i] = (double)histograma[i] + equalizado[i - 1];
		}
		return equalizado;
	}

	public static double[] trataHistogramaCanal(double[] histograma ,double totPix) {
		for (int i = 0; i < histograma.length; i++) {
			System.out.println(histograma[i]);
			histograma[i] = (((double)histograma[i]) * 255.0 / totPix) / 255.0;
			System.out.println(histograma[i]);
		}
		return histograma;
	}
	
	public static double[] histogramaAc(double[] hist){
		double[] ret = new double[hist.length];
		double vl = hist[0];
		for(int i=0; i<hist.length-1; i++){
			ret[i] = vl;
			vl += hist[i+1];
		}
		return ret;
	}
	
	public static double validPixels(int[] h) {
		double a = 0;
			for (int i = 0; i < h.length; i++) {
				if(h[i] > 0)
					a++;
			}
		return a;
	}

	public static int[] getHistogramBW(Image img, int canal) {
		try {
			int w = (int) img.getWidth();
			int h = (int) img.getHeight();
			int[] a = new int[256];
			PixelReader pr = img.getPixelReader();
			for (int i = 0; i < w; i++) {
				for (int j = 0; j < h; j++) {
					switch (canal) {
					case Constantes.R:
						a[(int) (pr.getColor(i, j).getRed() * 255)]++;
						break;
					case Constantes.G:
						a[(int) (pr.getColor(i, j).getGreen() * 255)]++;
						break;
					case Constantes.B:
						a[(int) (pr.getColor(i, j).getBlue() * 255)]++;
						break;
					}

				}
			}
			return a;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Image criarMoldura(Image img,int largura,int Cor) {
		try {
			int w = (int) img.getWidth() ;
			int h = (int) img.getHeight();
			Color cLargura = null;
			int xI = 0,yI = 0;
			boolean printMolduraX = false;
			boolean printMolduraY = false;
			PixelReader pr = img.getPixelReader();
			WritableImage wi = new WritableImage(w , h);
			PixelWriter pw = wi.getPixelWriter();
			for (int i = 0; i < w ; i++) {
				for (int j = 0; j < h ; j++) {
					Color ca = pr.getColor(i, j);
					switch(Cor){
					case Constantes.R:
						cLargura = new Color(1,0,0,1);
						break;
					case Constantes.G:
						cLargura = new Color(0,1,0,1);
						break;
					case Constantes.B:
						cLargura = new Color(0,0,1,1);
						break;
					}
					if(i < largura | i > w - largura) {
						pw.setColor(i, j, cLargura);
					} else if (j < largura | j > h - largura) {
						pw.setColor(i, j, cLargura);
					} else {
						pw.setColor(i, j, pr.getColor(i, j));
					}
					
					
				}
			}
			return wi;
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return null;
	}
	
	public static Image divideHorizontal(Image img) {
		try {
			int w = (int) img.getWidth() ;
			int h = (int) img.getHeight();
			Color cLargura = null;
			int xI = 0,yI = 0;
			boolean nextEffect = false;
		
			PixelReader pr = img.getPixelReader();
			WritableImage wi = new WritableImage(w, h);
			PixelWriter pw = wi.getPixelWriter();
			for (int j = 0; j < h ; j++) {
				nextEffect = ((j == (h / 2)))  | nextEffect;
				for (int i = 0; i < w ; i++) {		
					Color ca = pr.getColor(i, j);
					if(!nextEffect) {
						ca = negativaPixel(ca);
					} else {
						ca = blackNwhite(ca);
					}
					pw.setColor(i, j, ca);
				}
			}
			return wi;
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return null;
	}
	
	private static Color negativaPixel(Color c) {
		return new Color(1 - c.getRed(),1 - c.getGreen(),1 - c.getGreen(),c.getOpacity());
	}
	
	private static Color blackNwhite(Color c) {
		double m = (c.getRed() + c.getGreen() + c.getBlue()) / 3;
		return new Color(m,m,m,c.getOpacity());
	}
	
	
	public static int verificaForma(Image img) {
		try {
			int w = (int) img.getWidth() ;
			int h = (int) img.getHeight();
			PixelReader pr = img.getPixelReader();
			WritableImage wi = new WritableImage(w, h);
			PixelWriter pw = wi.getPixelWriter();
			for (int j = 0; j < h ; j++) {
				for (int i = 0; i < w ; i++) {		
					Color ca = pr.getColor(i, j);
					if(ca.getRed() == 0 && ca.getGreen() == 0 && ca.getRed() == 0) {
						Color aux = pr.getColor(i, j+1	);
						if(aux.getRed() == 0 && aux.getGreen() == 0 && aux.getRed() == 0) {
							return Constantes.QUADRADO;
						} else {
							return Constantes.CIRCULO;
						}
						
					} 
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return -1;
	}
	
	
}
