import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

/*
    Algoritmo A* para solucuionar o Jogo dos 15
    Nome: Gustavo Mendonça Dias     RA: 88410
*/

class Main2 {
    public static void main(String[] args) {
        aEstrela a = new aEstrela();
        
        a.entradaDados();
        
        a.aEstrela();
        
    }
    
    static class Matriz{
        private int[][] matriz = new int[4][4];
        private int h = 0;
        private int g = 0;
        private int f = 0;
        
        private Matriz pai = null;

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

        public Matriz getPai() {
            return pai;
        }

        public void setPai(Matriz pai) {
            this.pai = pai;
        }
        
        

    }
    
    static class aEstrela{
//        HashMap<String, Matriz> estadosAbertos = new HashMap<String, Matriz>();
//        HashMap<String, Matriz> estadosFechados = new HashMap<String, Matriz>();
        Queue<Matriz> estadosIniciais = new LinkedList<Matriz>();
        ArrayList<Matriz> estadosFinais = new ArrayList<Matriz>();
        ArrayList<Matriz> estadosAbertos = new ArrayList<Matriz>();
        ArrayList<Matriz> estadosFechados = new ArrayList<Matriz>();
        
        
        Scanner leitor = new Scanner(System.in);
        private int[][] resultadoEsperado = {{4, 3, 2, 1}, {5, 6, 7, 8}, {12, 11, 10, 9}, {13, 14, 15, 0}};
        private int[][] entrada = new int[4][4];
        private int[][] entradaAux = new int[4][4];
        int h = 0;

        public aEstrela() {
        }
        
        public void aEstrela(){            
            Matriz matriz = new Matriz();
            matriz.setMatriz(entrada);
            
            //estadosAbertos.put("Teste", matriz);
            estadosAbertos.add(matriz);
            
            while(!estadosAbertos.isEmpty()){
                System.out.println("Meu F e: " + estadosAbertos.get(0).getF());
                if(comparaResultado(matriz.getMatriz())){
                    System.out.println(matriz.getH());
                    return;
                }
                estadosAbertos.remove(matriz);
                try{
                    if(estadosAbertos.get(0).getH() == 0){
                        System.out.println(estadosAbertos.get(0).getF());
                        return;
                    }
                }
                catch (IndexOutOfBoundsException ex){
                    
                }
                
                // ESTOU PARADO AQUI NUM LOOP INFINITO POIS PRECISO DE UMA FILA DE PRIORIDADES
                
                //estadosFechados.put("Teste", matriz);
                estadosFechados.add(matriz);
                
                matriz.setF(matriz.getG() + matriz.getH());
                estadosAbertos = geraFilhos(matriz);
            }
            
            System.out.println("Resposta não escontrada !!");
        }
        
        public void entradaDados(){
            for(int i = 0; i < 4; i++){
                for(int j = 0; j < 4; j++){
                    entrada[i][j] = leitor.nextInt();
                }
            }
        }

        public int heuristicaUm(int[][] matriz){
            for(int i = 0; i < 4; i++){
                for(int j = 0; j < 4; j++){
                    if (matriz[i][j] != resultadoEsperado[i][j]){
                        h++;
                    }
                }
            }
            if(h > 15){
                h--;
            }
            return h;
        }
        
        public boolean comparaResultado(int[][] matriz){
            for(int i = 0; i < 4; i++){
                for(int j = 0; j < 4; j++){
                    if (matriz[i][j] != resultadoEsperado[i][j]){
                        return false;
                    }
                }
            }
            return true;
        }        
        
        public void printaResult(int[][] matriz){
            System.out.println("------------------");
            for(int i = 0; i < 4; i++){
                System.out.print("| ");
                for(int j = 0; j < 4; j++){
                    System.out.print(matriz[i][j] + " | ");
                }
                System.out.println("\n------------------");
            }
        }
        
        public void geraNovaMatriz(int [][] matriz){
            for(int i = 0; i < 4; i++){
                for(int j = 0; j < 4; j++){
                    entradaAux[i][j] = matriz[i][j];
                }
            }
        }
        
        public ArrayList<Matriz> geraFilhos(Matriz pai){
            ArrayList<Matriz> estados = new ArrayList<>();
            Matriz teste = new Matriz();
            int linha = -1, coluna = -1, aux = 0;
            int[][] matriz = new int[4][4];
            matriz = pai.getMatriz();
            
            printaResult(pai.getMatriz());
            
            for(int i = 0; i < 4; i++){
                for(int j = 0; j < 4; j++){
                    if(matriz[i][j] == 0){
                        System.out.println("Achei o 0 !!");
                        
                        try{
                            geraNovaMatriz(matriz);
                            entradaAux[i][j] = matriz[i+1][j];
                            entradaAux[i+1][j] = matriz[i][j];
                            
                            teste.setH(heuristicaUm(entradaAux));
                            teste.setG(pai.getG()+1);
                            teste.setF(teste.getG() + teste.getH());
                            teste.setMatriz(entradaAux);
                            estados.add(teste);
                        }
                        catch (ArrayIndexOutOfBoundsException x){
                            
                        }
                        
                        try{
                            geraNovaMatriz(matriz);
                            entradaAux[i][j] = matriz[i-1][j];
                            entradaAux[i-1][j] = matriz[i][j];

                            teste.setH(heuristicaUm(entradaAux));
                            teste.setG(pai.getG()+1);
                            teste.setF(teste.getG() + teste.getH());
                            teste.setMatriz(entradaAux);
                            estados.add(teste);
                        }
                        catch (ArrayIndexOutOfBoundsException x){
                            
                        }
                        
                        try{
                            geraNovaMatriz(matriz);
                            entradaAux[i][j] = matriz[i][j+1];
                            entradaAux[i][j+1] = matriz[i][j];

                            teste.setH(heuristicaUm(entradaAux));
                            teste.setG(pai.getG()+1);
                            teste.setF(teste.getG() + teste.getH());
                            teste.setMatriz(entradaAux);
                            estados.add(teste);
                        }
                        catch (ArrayIndexOutOfBoundsException x){
                            
                        }
                        
                        try{
                            geraNovaMatriz(matriz);
                            entradaAux[i][j] = matriz[i][j-1];
                            entradaAux[i][j-1] = matriz[i][j];

                            teste.setH(heuristicaUm(entradaAux));
                            teste.setG(pai.getG()+1);
                            teste.setF(teste.getG() + teste.getH());
                            teste.setMatriz(entradaAux);
                            estados.add(teste);
                        }
                        catch (ArrayIndexOutOfBoundsException x){
                            
                        }
                        
                        return estados;
                    }
                }
            }
            return estados;
        }
    }
}
