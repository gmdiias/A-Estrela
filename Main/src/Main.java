import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

/*
    Algoritmo A* para solucuionar o Jogo dos 15
    Nome: Gustavo Mendonça Dias     RA: 88410
*/

class Main {
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
            matriz.setH(heuristicaUm(entrada));
            matriz.setF(matriz.getH() + matriz.getG());
            int max = matriz.getF();
            
            //estadosAbertos.put("Teste", matriz);
            estadosAbertos.add(matriz);
            
            while(!estadosAbertos.isEmpty()){
                matriz = estadosAbertos.get(0);
                printaResult(matriz.getMatriz());
               
                if(comparaResultado(matriz.getMatriz())){
                    System.out.println(estadosAbertos.get(0).getF());
                    return;
                }
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
                
                if(estadosAbertos.get(0).getF() <= max){
                    estadosAbertos.addAll(geraFilhos(matriz));
                    estadosAbertos.remove(matriz);
                }
                else{
                    estadosAbertos.remove(matriz);
                }     
            }
            System.out.println(max);
        }
        
        public void entradaDados(){
            for(int i = 0; i < 4; i++){
                for(int j = 0; j < 4; j++){
                    entrada[i][j] = leitor.nextInt();
                }
            }
        }

        public int heuristicaUm(int[][] matriz){
            h = 0;
            for(int i = 0; i < 4; i++){
                for(int j = 0; j < 4; j++){
                    if (matriz[i][j] != resultadoEsperado[i][j]){
                        h++;
                    }
                }
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
                            teste.setH(heuristicaUm(teste.getMatriz()));
                            teste.setG(pai.getG()+1);
                            teste.setF(teste.getG() + teste.getH());
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
                            teste2.setH(heuristicaUm(teste2.getMatriz()));
                            teste2.setG(pai.getG()+1);
                            teste2.setF(teste2.getG() + teste2.getH());
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
                            teste3.setH(heuristicaUm(teste3.getMatriz()));
                            teste3.setG(pai.getG()+1);
                            teste3.setF(teste3.getG() + teste3.getH());
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
                            teste4.setH(heuristicaUm(teste4.getMatriz()));
                            teste4.setG(pai.getG()+1);
                            teste4.setF(teste4.getG() + teste4.getH());
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
