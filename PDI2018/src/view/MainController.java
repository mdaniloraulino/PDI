package view;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import openCV.OpenCVUtis;
import util.Constantes;
import util.Pdi;
import util.Pixel;
import util.Segmentacao;

public class MainController {
	
	int sX,sY;
	
	@FXML ImageView imageView1;
	@FXML ImageView imageView2;
	@FXML ImageView imageView3;
	
	@FXML Label lblR;
	@FXML Label lblG;
	@FXML Label lblB;
		
	//Escala de Cinza
	@FXML TextField txtRed;
	@FXML TextField txtGreen;
	@FXML TextField txtBlue;
	
	//RUIDOSSSS
	@FXML RadioButton rCross;
	@FXML RadioButton rPlus;
	@FXML RadioButton rMax;
	//Cor Moldura
	@FXML RadioButton rMoldura;
	@FXML RadioButton gMoldura;
	@FXML RadioButton bMoldura;
	@FXML TextField larguraMoldura;
	
	//Adição/Subtração
	@FXML TextField pImg1;
	@FXML TextField pImg2;
	
	//Segmentação
	@FXML TextField quantidadeSegment;
	
	private File f;
	private Image img1;
	private Image img2;
	private Image img3;
	
	
	
	@FXML Slider sliderLimia;
	@FXML Slider sliderCanny;
	
	@FXML
	public void abreImagem() {
		f = selecionaImagem();
		if (f != null) {
			img1 = new Image(f.toURI().toString());
			imageView1.setImage(img1);
			imageView1.setFitWidth(img1.getWidth());
			imageView1.setFitHeight(img1.getHeight());
		}
	}
	
	@FXML
	public void abreImagem2() {
		f = selecionaImagem();
		if (f != null) {
			img2 = new Image(f.toURI().toString());
			imageView2.setImage(img2);
			imageView2.setFitWidth(img2.getWidth());
			imageView2.setFitHeight(img2.getHeight());
		}
	}
	
	@FXML
	public void limparLabels() {
		lblR.setText("R: ");
		lblG.setText("G: ");
		lblB.setText("B: ");
	}
	
	@FXML
	public void rasterImg(MouseEvent evt) {
		ImageView iv = (ImageView)evt.getTarget();
		if(iv.getImage() != null) {
			verificaCor(iv.getImage(), (int)evt.getX(), (int)evt.getY());
		}
	}
	
	private void verificaCor(Image img, int x, int y) {
		try {
			Color cor = img.getPixelReader().getColor(x-1,y-1);
			lblR.setText("R: "+ (int) (cor.getRed() * 255));
			lblG.setText("G: "+ (int) (cor.getGreen() * 255));
			lblB.setText("B: "+ (int) (cor.getBlue() * 255));
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	
	private File selecionaImagem() {
		try {
			FileChooser fc = new FileChooser();
			fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Ibagens","*.jpg","*.png","*.PNG","*.gif","*.GIF","*.bmp","*.BMP"));
			File imgSelec = fc.showOpenDialog(null);
			if (imgSelec != null) {
				return imgSelec;
			}
		} catch (Exception e) {
			return null;
		}
		return null;
	}
	
	private void atualizaImage() {
		if(img3 != null) {
			imageView3.setImage(img3);
			imageView3.setFitHeight(img3.getHeight());
			imageView3.setFitWidth(img3.getWidth());
		} else {
			showMsg("Erro", "Erro NULL", "Imagem 3 nula", AlertType.ERROR);
		}
		
	}
	
	public void escalaDeCinzaMedia() {
		img3 = Pdi.escalaDeCinza(img1,0,0,0);
		atualizaImage();
	}
	
	public void escalaDeCinzaPond() {
		int r = Integer.parseInt(txtRed.getText());
		int g = Integer.parseInt(txtGreen.getText());
		int b = Integer.parseInt(txtBlue.getText());
		
		if((r + g + b) != 100) {
			showMsg("Escala de cinza ponderada", "Erro", "A soma das porcentagens devem dar 100", AlertType.ERROR);
			return;
		}
		img3 = Pdi.escalaDeCinza(img1,r,g,b);
		atualizaImage();
	}
	
	private void showMsg(String title,String cab,String msg,AlertType type) {
		Alert a = new Alert(type);
		a.setTitle(title);
		a.setHeaderText(cab);
		a.setContentText(msg);
		a.showAndWait();		
	}
	
	public void salvar() {
		if (img3 != null) {
			FileChooser fc = new FileChooser();
			fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image","*.png"));
			File f = fc.showSaveDialog(null);
			
			if (f != null ) {
				BufferedImage bImg = SwingFXUtils.fromFXImage(img3, null);
				try {
					ImageIO.write(bImg, "PNG", f);
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else {
			showMsg("Salvar Imagem", "Não é possível salvar a imagem.", "Não há nenhuma imagem modificada", AlertType.ERROR); 
		}
	}
	
	public void negativar() {
		img3 = Pdi.negativar(img1);
		atualizaImage();
	}
	
	public void limiarizacao() {
		double valor = sliderLimia.getValue();
		valor = valor / 255;
		img1 = Pdi.escalaDeCinza(img1, 0,0,0);
		img3 = Pdi.limiarizacao(img1,valor);
		atualizaImage();
	}
	
	public void removerRuidos() {
		int type = 0 ;
		if(rCross.isSelected()) {
			type = Pixel.CROSS;
		}
		if(rPlus.isSelected()) {
			type = Pixel.PLUS;
		}
		if(rMax.isSelected()) {
			type = Pixel.ALLNEIB;
		}
		img3 = Pdi.removeRuido(img1, type);
		atualizaImage();
	}
	
	public void addImage() {
		double p1 = Double.parseDouble(pImg1.getText()) /100;
		double p2 = Double.parseDouble(pImg2.getText()) /100;
		img3 = Pdi.adicaoImage(img1, img2, p1, p2);
		atualizaImage();
	}
	public void subImage() {
		img3 = Pdi.subtractImage(img1, img2);
		atualizaImage();
	}
	
	@FXML
	public void MouseClickM(MouseEvent evt) {
		sX =(int) evt.getX();
		sY =(int) evt.getY();
	}
	@FXML
	public void MouseReleaseM(MouseEvent evt) {
		img3 = Pdi.criaBorda(img1, sX, sY , (int)evt.getX(), (int)evt.getY());
		atualizaImage();
	}
	
	@FXML
	public void abreModalHistograma(ActionEvent event) {
			  try{
				  Stage stage = new Stage();
				  FXMLLoader loader = new FXMLLoader(getClass().
						  getResource("ModalHistograma.fxml"));
				  Parent root = loader.load();
				  stage.setScene(new Scene(root));
				  stage.setTitle("Histograma");
				 
				  stage.initOwner(((Node)event.getSource()).
						  getScene().getWindow() 
				  );
				  stage.show();
				  
				  HistogramaModalController controller = 
						  (HistogramaModalController)loader.getController();
				  
				  if(img1 != null)
					  Pdi.getGrafico(img1, controller.chart1);
				  if(img2 != null)
					  Pdi.getGrafico(img2, controller.chart2);
				  if(img3 != null)
					  Pdi.getGrafico(img3, controller.chart3);
				  
				  
			  }catch (Exception e) {
				e.printStackTrace();
			}
	}
	
	@FXML
	public void SegmentarImagem() {
		img3 = Segmentacao.segmentar(img1, Integer.parseInt(quantidadeSegment.getText()));
		atualizaImage();
	}
	
	@FXML
	public void equaliza() {
		img3 = Pdi.Equalizar(img1,true);
		atualizaImage();
	}
	public void equalizaValids() {
		img3 = Pdi.Equalizar(img1,true);
		atualizaImage();
	}
	
	@FXML
	public void dividir() {
		img3 = Pdi.divideHorizontal(img1);
		atualizaImage();
	}
	@FXML
	public void Moldura() {
		int cor = 0;
		
		if(rMoldura.isSelected()) {
			cor = Constantes.R;
		}
		if(gMoldura.isSelected()) {
			cor = Constantes.G;
		}
		if(bMoldura.isSelected()) {
			cor = Constantes.B;
		}
		
		img3 = Pdi.criarMoldura(img1, Integer.parseInt(larguraMoldura.getText()), cor);
		atualizaImage();
	}
	@FXML
	public void identificaQuadrado() {
		switch(Pdi.verificaForma(img1)) {
		case Constantes.QUADRADO:
			showMsg("É Quadrado ou ciruclo?", "Resposta", "QUADRADO", AlertType.INFORMATION);
			break;
		case Constantes.CIRCULO:
			showMsg("É Quadrado ou ciruclo?", "Resposta", "CIRCULO", AlertType.INFORMATION);
			break;
		case -1:
			showMsg("É Quadrado ou ciruclo?", "Resposta", "NENHUM DOS DOIS", AlertType.INFORMATION);
			break;
		}
	}
	@FXML
	public void erodirImg() {
		img3 = OpenCVUtis.erosaoDilatacao(img1, true);
		atualizaImage();
	}
	
	@FXML
	public void dialtarImg() {
		img3 = OpenCVUtis.erosaoDilatacao(img1, false);
		atualizaImage();
	}
	
	public void faceDetection() {
		img3 = OpenCVUtis.faceDetection(img1);
		atualizaImage();
	}
	
	public void doCanny() {
		img3 = OpenCVUtis.canny(img1, (int)sliderCanny.getValue());
		atualizaImage();
	}
	
	public void doSobel() {
		img3 = OpenCVUtis.aplicaSobel(img1);
		atualizaImage();
	}
}
