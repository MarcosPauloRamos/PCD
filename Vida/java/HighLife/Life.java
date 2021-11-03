public class Life {
    private int TAMANHO;
    public static int num_t = 8;
    public static int grid[][];
    public static int newgrid[][];
    public static int lin, col;

    // Construtor
    public Life(int tamanho){
        this.TAMANHO = tamanho;
        this.grid = new int[this.TAMANHO][this.TAMANHO];
        this.newgrid = new int[this.TAMANHO][this.TAMANHO];
        //GERA AS POSIÇÕES
        for(int i = 0; i<this.TAMANHO; i++) {   
            for(int j = 0; j<this.TAMANHO; j++) {
                grid[i][j] = 0;  
            }
        }
        //SEGUE AS REGRAS
        lin = 1; col = 1;
        grid[lin  ][col+1] = 1;
        grid[lin+1][col+2] = 1;
        grid[lin+2][col  ] = 1;
        grid[lin+2][col+1] = 1;
        grid[lin+2][col+2] = 1;

        //R-pentomino
        lin = 10; col = 30;
        grid[lin  ][col+1] = 1;
        grid[lin  ][col+2] = 1;
        grid[lin+1][col  ] = 1;
        grid[lin+1][col+1] = 1;
        grid[lin+2][col+1] = 1;
    }


    // Define a nova geração como a geração atual
    public void copia(){
        for(int i=0;i<this.TAMANHO; i++){     
            for(int j = 0; j<this.TAMANHO; j++){
                grid[i][j] = newgrid[i][j];
            }
        }
    }

    // Copia o quarto do grid gerado para a proxima geração
    public void atualizacao(int atual, int aux_grid[][]){
        for(int i=0;i<(this.TAMANHO/num_t); i++){     
            for(int j = 0; j<this.TAMANHO; j++){
                newgrid[(atual+i)][j] = aux_grid[i][j];
            }
        }
    }

    // Conta quantas celulas estao vivas na geracao
    public int total_vivos(){
        int vivos = 0;

        for(int i=0;i<this.TAMANHO; i++){     
            for(int j = 0; j<this.TAMANHO; j++){
                if (this.grid[i][j] == 1)
                    vivos++;
            }
        }

        return vivos;
    }

    public static void main(String[] args) throws InterruptedException{
        int lado = 2048;
        int t_atual;//para auxiliar qual é o campo de cada thread
	    //valores de 1 até 249 são referentes a thread1 (i*t_atual = 0), 250 a 499 é thread 2 (i*t_atual = 1)...
        Thread[] th;
        Mythread[] mh;
        mh = new Mythread[num_t];
		    th = new Thread[num_t];
        Life gen1 = new Life(lado);
        
        long startTime = System.currentTimeMillis();
        System.out.println("Condicao Inicial: " + gen1.total_vivos());

        t_atual = lado/num_t;
        for(int k=0;k<2000;k++){
            for(int i=0; i<num_t; i++) {
                mh[i] = new Mythread(lado, num_t, (i*t_atual), grid);
                th[i] = new Thread(mh[i]);
                th[i].start();
            }
            for(int i=0; i<num_t; i++) {
                th[i].join();
                gen1.atualizacao((i*t_atual),mh[i].gridAux);
            }
            gen1.copia();
           System.out.println("Geracao " + k + ": " + gen1.total_vivos());
        }
        System.out.println("Ultima Geracao: " + gen1.total_vivos());
        double calcTime = (System.currentTimeMillis() - startTime)/1000.;
        System.out.println(calcTime + "seg");
    }
}
