package testpkg;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Array;
import java.net.URL;
import java.text.FieldPosition;
import java.util.Arrays;

import javax.swing.JOptionPane;

public class AlgoritimoHebb {
	int[] pesos = new int[25];
	int[] input = new int[25];
	int limiar = 0;
	BufferedReader bf;

	public static void main(String[] args) {
		AlgoritimoHebb ah = new AlgoritimoHebb();
		ah.treinar("x.txt", 1);
		ah.treinar("o.txt", -1);
		String a = "1";
		while(a.equals("1")) {
			a = JOptionPane.showInputDialog("1 - Para testar \n   Para sair digite quaquer tecla");
			
			if(a.equals("1")) {
				ah.testar(); 
			}
			
		}
			
	}

	public void treinar(String file, int output) {
		lerArquivo(file);
		for (int i = 0; i < pesos.length; i++) {
			pesos[i] = pesos[i]+input[i]*output;
			//System.out.println(pesos[i]);
		}
	}
	
	public AlgoritimoHebb() {
		Arrays.fill(pesos, 0);
	}

	public void lerArquivo(String path) {
		try {
			
			String line;
			bf = new BufferedReader(new FileReader(path));
			while ((line = bf.readLine()) != null) {
				for (int i = 0; i < line.length(); i++) {
					if (line.charAt(i) == '.')
						input[i] = -1;
					else if (line.charAt(i) == '#')
						input[i] = 1;
					else
						input[i] = 0;
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	public void testar() {
		int total = 0;
		lerArquivo("teste.txt");
		
		for (int i = 0; i < input.length; i++) {
			total = total + input[i]*pesos[i];
		}
		
		if (total > limiar) {
			JOptionPane.showMessageDialog(null,"Parece um X");
		} else if (total < limiar) {
			JOptionPane.showMessageDialog(null,"Parece um O");
		} else {
			JOptionPane.showMessageDialog(null,"Não consigo decifrar.");
		}
	}
}
