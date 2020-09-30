package application;

import java.io.File;
import java.text.DecimalFormat;

import caracteristicas.Extrator;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

public class SampleController {

	@FXML
	private ImageView imageView;
	
	@FXML
	public TextField classe;
	@FXML
	public TextField probabilidade;
	@FXML
	public TextField roupaLisa;
	@FXML
	public TextField cabeloLisa;
	@FXML
	public TextField sapatoLisa;
	@FXML
	public TextField cabeloNed;
	@FXML
	public TextField sueterNed;
	@FXML
	public TextField camisaNed;
	
	@FXML public RadioButton algBayes;
	@FXML public RadioButton algJ48;
	
	@FXML
	public void extrairCaracteristicas() {
		Extrator.extrair();
	}
	
	double[] bayes = new double[2];
	double[] j48 = new double[2];
	
	@FXML
	public void selecionaImagem() {
		
		File f = buscaImg();
		if(f != null) {
			Image img = new Image(f.toURI().toString());
			imageView.setImage(img);
			imageView.setFitWidth(img.getWidth());
			imageView.setFitHeight(img.getHeight());
			double[] caracteristicas = Extrator.extraiCaracteristicas(f);
//			for (double d : caracteristicas) {
//				System.out.println(d);
//			}
			
			if (algBayes.isSelected()) {
				bayes = Extrator.naiveBayes(caracteristicas);
				DecimalFormat df = new DecimalFormat("###.##");
				
				if(bayes[1] > bayes[0]) {
					classe.setText("Ned Flanders");
					probabilidade.setText(df.format(bayes[1]*100)+"%");
				} else {
					classe.setText("Lisa Simpson");
					probabilidade.setText(df.format(bayes[0]*100)+"%");
				}
				
				roupaLisa.setText(""+caracteristicas[0]);
				cabeloLisa.setText(""+caracteristicas[1]);
				sapatoLisa.setText(""+caracteristicas[2]);
				
				cabeloNed.setText(""+caracteristicas[3]);
				sueterNed.setText(""+caracteristicas[4]);
				camisaNed.setText(""+caracteristicas[5]);
				
//				System.out.println("chance de ser lisa: "+bayes[0]);
//				System.out.println("chance de ser ned: "+bayes[1]);
			} else if(algJ48.isSelected()) {
				j48 = Extrator.j48(caracteristicas);
				DecimalFormat df = new DecimalFormat("###.##");
				
				if(j48[1] > j48[0]) {
					classe.setText("Ned Flanders");
					probabilidade.setText(df.format(j48[1]*100)+"%");
				} else {
					classe.setText("Lisa Simpson");
					probabilidade.setText(df.format(j48[0]*100)+"%");
				}
				
				roupaLisa.setText(""+caracteristicas[0]);
				cabeloLisa.setText(""+caracteristicas[1]);
				sapatoLisa.setText(""+caracteristicas[2]);
				
				cabeloNed.setText(""+caracteristicas[3]);
				sueterNed.setText(""+caracteristicas[4]);
				camisaNed.setText(""+caracteristicas[5]);
				
			} else {
				Alert alert = new Alert(AlertType.ERROR);
	    		alert.setTitle("Erro!");
	    		alert.setHeaderText("Nenhum algoritmo selecionado.");
	    		alert.setContentText("Selecione um algoritmo.");
			}
			
		}
	}
	
	private File buscaImg() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add(new 
				   FileChooser.ExtensionFilter(
						   "Imagens", "*.jpg", "*.JPG", 
						   "*.png", "*.PNG", "*.gif", "*.GIF", 
						   "*.bmp", "*.BMP")); 	
		 fileChooser.setInitialDirectory(new File("src/imagens"));
		 File imgSelec = fileChooser.showOpenDialog(null);
		 try {
			 if (imgSelec != null) {
			    return imgSelec;
			 }
		 } catch (Exception e) {
			e.printStackTrace();
		 }
		 return null;
	}
	
}
