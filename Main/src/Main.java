import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Scanner;

class Main {

	public static void main(String[] args) {
		Scanner leitor = new Scanner(System.in);
		int[][] entrada = new int[4][4];
		
		for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++){
                entrada[i][j] = leitor.nextInt();
            }
        }
		Estrela a = new Estrela();
		a.aEstrela(entrada);
	}
	
	static class Matriz implements Comparable<Matriz>{
        private int[][] matriz = new int[4][4];
        private int h = 0;
        private int g = 0;
        private int f = 0;
        private String id;
        Matriz pai;

        public int[][] getMatriz() {
            return matriz;
        }

        public void setMatriz(int[][] matriz) {
            this.matriz = matriz;
        }

        public int getH() {
            return h;
        }

        public void setH(int h) {
            this.h = h;
        }

        public int getG() {
            return g;
        }

        public void setG(int g) {
            this.g = g;
        }

        public int getF() {
            return f;
        }

        public void setF(int f) {
            this.f = f;
        }
        
        public String getId() {
			return id;
		}
        
        public void setId(String id) {
			this.id = id;
		}

		@Override
		public int compareTo(Matriz arg0) {
            return this.f - arg0.f;
		}

    }
	
	static class Estrela{
		private int[][] resultadoEsperado = {{4, 3, 2, 1}, {5, 6, 7, 8}, {12, 11, 10, 9}, {13, 14, 15, 0}};
		Long tempoInicio = System.currentTimeMillis();
		
		public void aEstrela(int[][] entrada){    
			PriorityQueue<Matriz> filaPrioridade = new PriorityQueue<Matriz>();
			HashMap<String, Matriz> estadosAbertos = new HashMap<>(); 
	        HashMap<String, Matriz> estadosFechados = new HashMap<>();
	        
	        Matriz matriz = new Matriz();
	        matriz.setMatriz(entrada);
	        matriz.setG(0);
	        matriz.setH(heuristicaTres(entrada));
	        matriz.setF(matriz.getG() + matriz.getH());
	        matriz.setId(gerarId(matriz));
	        
	        estadosAbertos.put(matriz.id, matriz);
	        
	        while (matriz != null && !comparaResultado(matriz.matriz) && (System.currentTimeMillis()-tempoInicio) < 9500) {
	            estadosFechados.put(matriz.id, matriz);
	            estadosAbertos.remove(matriz.id);
	            
	            ArrayList<Matriz> estadosFilhos = geraFilhos(matriz);

	            for (int i = 0; i < estadosFilhos.size(); i++) {
	 
	            	Matriz matrizFilha = estadosFilhos.get(i);
	                Matriz pertenceAberto = estadosAbertos.get(matrizFilha.id);
	                Matriz pertenceFechado = estadosFechados.get(matrizFilha.id);
	                
	                if (pertenceAberto != null && matrizFilha.g < pertenceAberto.g) {
	                    estadosAbertos.remove(matrizFilha.id);
	                    
	                    if (filaPrioridade.contains(matrizFilha)) {
	                        filaPrioridade.remove(matrizFilha);
	                    }
	                }
	                
	                if (pertenceAberto == null && pertenceFechado == null) {
	                	matrizFilha.setH(heuristicaTres(matrizFilha.getMatriz()));
	                	matrizFilha.setG(matriz.getG()+1);
	                	matrizFilha.setF(matrizFilha.getG() + matrizFilha.getH());
	                    estadosAbertos.put(matrizFilha.id, matrizFilha);
	                    filaPrioridade.add(matrizFilha);
	                }
	            }
	            matriz = filaPrioridade.remove();
	        }
	        System.out.print(matriz.f);
	    }
		
		public int heuristicaUm(int[][] entrada){
        	int contador = 0;
            for(int i = 0; i < 4; i++){
                for(int j = 0; j < 4; j++){
                    if (entrada[i][j] != resultadoEsperado[i][j]){
                    	contador++;
                    }
                }
            }
            return contador;
        }
		
		public int heuristicaDois(int[][] entrada) {
			int contador = 0;
			int[] vetorEntrada = new int[16];
			int aux = 0;
			for(int i = 0; i < 4; i++){
                if(i%2 == 0) {
                	for(int j = 3; j >= 0; j--){
                    	vetorEntrada[aux] = entrada[i][j];
                    	aux++;
                    }
                }
                else {
                	for(int j = 0; j < 4; j++) {
                		vetorEntrada[aux] = entrada[i][j];
                		aux++;
                	}
                }
			}
			for(int i = 0; i < 15; i++) {
				if(vetorEntrada[i]+1 != vetorEntrada[i+1] && vetorEntrada[i] != 0) {
					contador++;
				}
			}
			return contador-1;
		}
        
        public int heuristicaTres(int[][] entrada) {
        	int contador = 0;
        	int[] posI = new int[16];
        	int[] posJ = new int[16];
        	for(int i = 0; i < 4; i++){
        		for(int j = 0; j < 4; j++) {
        			posI[resultadoEsperado[i][j]] = i;
        			posJ[resultadoEsperado[i][j]] = j;
        		}
        	}
        	
        	for(int i = 0; i < 4; i++){
        		for(int j = 0; j < 4; j++) {
        			if(entrada[i][j] != 0) {
	        			contador += Math.abs(i - posI[entrada[i][j]]) + Math.abs(j - posJ[entrada[i][j]]);
        			}
        		}
        	}
        	return contador;
        }
        
        public int heuristicaQuatro(int[][] entrada) {
        	return (int) (0.07 * heuristicaUm(entrada) + 0.07 * heuristicaDois(entrada) + 0.86 * heuristicaTres(entrada));
        }
        
        public int heuristicaCinco(int[][] entrada) {
        	int max = Math.max(heuristicaUm(entrada), heuristicaDois(entrada));
        	return Math.max(heuristicaTres(entrada), max);
        }
        
        public boolean comparaResultado(int[][] entrada){
            for(int i = 0; i < 4; i++){
                for(int j = 0; j < 4; j++){
                    if (entrada[i][j] != resultadoEsperado[i][j]){
                        return false;
                    }
                }
            }
            return true;
        }       
        
        public void printaResult(int[][] entrada){
            System.out.println("------------------");
            for(int i = 0; i < 4; i++){
                System.out.print("| ");
                for(int j = 0; j < 4; j++){
                    System.out.print(entrada[i][j] + " | ");
                }
                System.out.println("\n------------------");
            }
        }
        
        public static String gerarId(Matriz entrada) {
            String id = "";
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    id += entrada.matriz[i][j];
                }
            }
            return id;
        }
		
		public ArrayList<Matriz> geraFilhos(Matriz pai){
            ArrayList<Matriz> estados = new ArrayList<>();
            int aux = 0;
            int[][] matriz = new int[4][4];
            matriz = pai.getMatriz();
            
            for(int i = 0; i < 4; i++){
                for(int j = 0; j < 4; j++){
                    if(matriz[i][j] == 0){

                        boolean cima = true;
                        boolean baixo = true;
                        boolean direita = true;
                        boolean esquerda = true;
                        if(i == 0){
                            cima = false;
                        }
                        if(i == 3){
                            baixo = false;
                        }
                        
                        if(j == 0){
                            esquerda = false;
                        }
                        if(j == 3){
                            direita = false;
                        }
                        
                        if(cima){
                            Matriz teste = new Matriz();
                            for(int k = 0; k < 4; k++){
                                for(int l = 0; l < 4; l++){
                                    teste.matriz[k][l] = pai.matriz[k][l];
                                }
                            }
                            aux = teste.matriz[i][j];
                            teste.matriz[i][j] = teste.matriz[i-1][j];
                            teste.matriz[i-1][j] = aux;
                            teste.id = gerarId(teste);
                            estados.add(teste);
                        }
                        if(baixo){
                            Matriz teste2 = new Matriz();
                            for(int k = 0; k < 4; k++){
                                for(int l = 0; l < 4; l++){
                                    teste2.matriz[k][l] = pai.matriz[k][l];
                                }
                            }
                            aux = teste2.matriz[i][j];
                            teste2.matriz[i][j] = teste2.matriz[i+1][j];
                            teste2.matriz[i+1][j] = aux;
                            teste2.id = gerarId(teste2);
                            estados.add(teste2);
                        }
                        if(esquerda){
                            Matriz teste3 = new Matriz();
                            for(int k = 0; k < 4; k++){
                                for(int l = 0; l < 4; l++){
                                    teste3.matriz[k][l] = pai.matriz[k][l];
                                }
                            }
                            aux = teste3.matriz[i][j];
                            teste3.matriz[i][j] = teste3.matriz[i][j-1];
                            teste3.matriz[i][j-1] = aux;
                            teste3.id = gerarId(teste3);
                            estados.add(teste3);
                        }
                        if(direita){
                            Matriz teste4 = new Matriz();
                            for(int k = 0; k < 4; k++){
                                for(int l = 0; l < 4; l++){
                                    teste4.matriz[k][l] = pai.matriz[k][l];
                                }
                            }
                            aux = teste4.matriz[i][j];
                            teste4.matriz[i][j] = teste4.matriz[i][j+1];
                            teste4.matriz[i][j+1] = aux;
                            teste4.id = gerarId(teste4);
                            estados.add(teste4);
                        }
                        
                        return estados;
                    }
                }
            }
            return estados;
        }
	}
}
