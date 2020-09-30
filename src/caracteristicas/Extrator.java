package caracteristicas;

import java.io.File;
import java.io.FileOutputStream;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.trees.J48;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class Extrator {

public static double[] extraiCaracteristicas(File f) {
		
		double[] caracteristicas = new double[7];
		
		double roupaLaranjaLisa = 0;
		double cabeloAmareloLisa = 0;
		double sapatoLaranjaLisa = 0;
		double cabeloMarromNed = 0;
		double sueterVerdeNed = 0;
		double camisaRosaNed = 0; 
		
		
		Image img = new Image(f.toURI().toString());
		PixelReader pr = img.getPixelReader();
		
		Mat imagemOriginal = Imgcodecs.imread(f.getPath());
        Mat imagemProcessada = imagemOriginal.clone();
		
		int w = (int)img.getWidth();
		int h = (int)img.getHeight();
		
		
		for(int i=0; i<h; i++) {
			for(int j=0; j<w; j++) {
				
				Color cor = pr.getColor(j,i);
				
				double r = cor.getRed()*255; 
				double g = cor.getGreen()*255;
				double b = cor.getBlue()*255;
				
				if(i < h/2 && isCabeloMarromNed(r, g, b)) {
					cabeloMarromNed++;
					imagemProcessada.put(i, j, new double[]{0, 255, 128});
				}
				if(isSueterVerdeNed(r, g, b)) {
					sueterVerdeNed++;
					imagemProcessada.put(i, j, new double[]{0, 255, 128});
				}
				if (isCamisaRosaNed(r, g, b)) {
					camisaRosaNed++;
					imagemProcessada.put(i, j, new double[]{0, 255, 128});
				}
				if(isRoupaLaranjaLisa(r, g, b)) {
					roupaLaranjaLisa++;
					imagemProcessada.put(i, j, new double[]{0, 255, 255});
				}
				if(i < (h/2 - h/3) && isCabeloAmareloLisa(r, g, b)) {
					cabeloAmareloLisa++;
					imagemProcessada.put(i, j, new double[]{0, 255, 255});
				}
				if (i < (h/2 + h/3) && isSapatoLaranjaLisa(r, g, b)) {
					sapatoLaranjaLisa++;
					imagemProcessada.put(i, j, new double[]{0, 255, 255});
				}
				
			}
		}
		
		// Normaliza as características pelo número de pixels totais da imagem para %
		roupaLaranjaLisa = (roupaLaranjaLisa / (w * h)) * 100;
		cabeloAmareloLisa = (cabeloAmareloLisa / (w * h)) * 100;
		sapatoLaranjaLisa = (sapatoLaranjaLisa / (w * h)) * 100;
		cabeloMarromNed = (cabeloMarromNed / (w * h)) * 100;
		sueterVerdeNed = (sueterVerdeNed / (w * h)) * 100;
		camisaRosaNed = (camisaRosaNed / (w * h)) * 100;
        
        caracteristicas[0] = roupaLaranjaLisa;
        caracteristicas[1] = cabeloAmareloLisa;
        caracteristicas[2] = sapatoLaranjaLisa;
        caracteristicas[3] = cabeloMarromNed;
        caracteristicas[4] = sueterVerdeNed;
        caracteristicas[5] = camisaRosaNed;
        //APRENDIZADO SUPERVISIONADO - JÁ SABE QUAL A CLASSE NAS IMAGENS DE TREINAMENTO
        caracteristicas[6] = f.getName().charAt(4)=='1'?1:0;
		return caracteristicas;
	}
	
	public static boolean isRoupaLaranjaLisa(double r, double g, double b) {
		 if (b >= 0 && b <= 50 &&  g >= 25 && g <= 90 &&  r >= 125 && r <= 240) {                       
         	return true;
         }
		 return false;
	}
	public static boolean isCabeloAmareloLisa(double r, double g, double b) {
		if (b >= 0 && b <= 40 &&  g >= 150 && g <= 220 &&  r >= 170 && r <= 250) {                       
			return true;
		}
		return false;
	}
	public static boolean isSapatoLaranjaLisa(double r, double g, double b) {
		if (b >= 0 && b <= 50 &&  g >= 25 && g <= 90 &&  r >= 125 && r <= 240) {                       
			return true;
		}
		return false;
	}
	public static boolean isCabeloMarromNed(double r, double g, double b) {
		if (b >= 0 && b <= 70 &&  g >= 50 && g <= 90 &&  r >= 65 && r <= 130) {                       
			return true;
		}
		return false;
	}
	public static boolean isSueterVerdeNed(double r, double g, double b) {
		if (b >= 0 && b <= 45 &&  g >= 45 && g <= 130 &&  r >= 20 && r <= 50) {                       
			return true;
		}
		return false;
	}
	public static boolean isCamisaRosaNed(double r, double g, double b) {
		if (b >= 140 && b <= 180 &&  g >= 115 && g <= 160 &&  r >= 185 && r <= 250) {                       
			return true;
		}
		return false;
	}



	public static void extrair() {
				
	    // Cabeçalho do arquivo Weka
		String exportacao = "@relation caracteristicas\n\n";
		exportacao += "@attribute laranja_roupa_lisa real\n";
		exportacao += "@attribute amarelo_cabelo_lisa real\n";
		exportacao += "@attribute laranja_sapato_lisa real\n";
		exportacao += "@attribute marrom_cabelo_ned real\n";
		exportacao += "@attribute verde_sueter_ned real\n";
		exportacao += "@attribute rosa_camisa_ned real\n";
		exportacao += "@attribute classe {Lisa, Ned}\n\n";
		exportacao += "@data\n";
	        
	    // Diretório onde estão armazenadas as imagens
	    File diretorio = new File("src\\imagens");
	    File[] arquivos = diretorio.listFiles();
	    
        // Definição do vetor de características
        double[][] caracteristicas = new double[359][7];
        
        // Percorre todas as imagens do diretório
        int cont = -1;
        for (File imagem : arquivos) {
        	cont++;
        	caracteristicas[cont] = extraiCaracteristicas(imagem);
        	
        	String classe = caracteristicas[cont][6] == 0 ?"Lisa":"Ned";
        	
        	System.out.println("Laranja roupa Lisa: " + caracteristicas[cont][0] 
            		+ " - Amarelo cabelo Lisa: " + caracteristicas[cont][1] 
            		+ " - Laranja sapato Lisa: " + caracteristicas[cont][2] 
            		+ " - Marrom cabelo Ned: " + caracteristicas[cont][3] 
            		+ " - Verde sueter Ned: " + caracteristicas[cont][4] 
            		+ " - Rosa camisa Ned: " + caracteristicas[cont][5] 
            		+ " - Classe: " + classe);
        	
        	exportacao += caracteristicas[cont][0] + "," 
                    + caracteristicas[cont][1] + "," 
        		    + caracteristicas[cont][2] + "," 
                    + caracteristicas[cont][3] + "," 
        		    + caracteristicas[cont][4] + "," 
                    + caracteristicas[cont][5] + "," 
                    + classe + "\n";
        }
        
     // Grava o arquivo ARFF no disco
        try {
        	File arquivo = new File("caracteristicas.arff");
        	FileOutputStream f = new FileOutputStream(arquivo);
        	f.write(exportacao.getBytes());
        	f.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		
	}
	
	public static double[] naiveBayes(double[]caracteristicas) {
		double[] retorno = {0,0};
		try {
			DataSource ds = new DataSource("caracteristicas.arff");
			Instances instancias = ds.getDataSet();
			instancias.setClassIndex(instancias.numAttributes()-1);
			
			//Classifica com base nas características da imagem selecionada
			NaiveBayes nb = new NaiveBayes();
			nb.buildClassifier(instancias);//aprendizagem (tabela de probabilidades)
			
			Instance novo = new DenseInstance(instancias.numAttributes());
			novo.setDataset(instancias);
			novo.setValue(0, caracteristicas[0]);
			novo.setValue(1, caracteristicas[1]);
			novo.setValue(2, caracteristicas[2]);
			novo.setValue(3, caracteristicas[3]);
			novo.setValue(4, caracteristicas[4]);
			novo.setValue(5, caracteristicas[5]);
			
			retorno = nb.distributionForInstance(novo);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retorno;
	}

	public static double[] j48(double[]caracteristicas) {
		double[] retorno = {0,0};
		try {
			//*******carregar arquivo de características
			DataSource ds = new DataSource("caracteristicas.arff");
			Instances instancias = ds.getDataSet();
			instancias.setClassIndex(instancias.numAttributes()-1);
			
			//cria a árvore
			J48 arvore = new J48();
			arvore.buildClassifier(instancias);
			//arvore.setUnpruned(true);//sem podas
			
			//Novo registro
			Instance novo = new DenseInstance(instancias.numAttributes());
			novo.setDataset(instancias);
			novo.setValue(0, caracteristicas[0]);
			novo.setValue(1, caracteristicas[1]);
			novo.setValue(2, caracteristicas[2]);
			novo.setValue(3, caracteristicas[3]);
			novo.setValue(4, caracteristicas[4]);
			novo.setValue(5, caracteristicas[5]);
			
			retorno = arvore.distributionForInstance(novo);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retorno;
	}
	
}
