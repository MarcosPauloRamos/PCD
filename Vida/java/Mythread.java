class Mythread implements Runnable {
	private int TAMANHO;
	private int MaxThreads;
	private int Offset;
	private int grid[][];
	 int gridAux[][];
    
     /* construtor */
	public Mythread(int tam, int maxThreads, int offset,int grafico[][]) {
		this.TAMANHO = tam;
		this.MaxThreads = maxThreads;
		this.Offset = offset;
		this.grid = grafico;
		this.gridAux = new int[(tam/this.MaxThreads)][tam];
	}

	/* Retorna a quantidade de vizinhos vivos de cada celula na posicao ​i,j */
	public int getNeighbors(int i, int j) {
		int num_vizinhos=0;
	
		num_vizinhos += grid[i][((j+1)%this.TAMANHO)]; // direita
		num_vizinhos += grid[((i+1)%this.TAMANHO)][((j+1)%this.TAMANHO)]; // direita baixo
		num_vizinhos += grid[((i+1)%this.TAMANHO)][j]; //baixo
		num_vizinhos += grid[((i+1)%this.TAMANHO)][(this.TAMANHO+(j-1))%this.TAMANHO]; // esquerda baixo
		
		num_vizinhos += grid[i][(this.TAMANHO+(j-1))%this.TAMANHO]; // esquerda
		num_vizinhos += grid[(this.TAMANHO+(i-1))%this.TAMANHO][(this.TAMANHO+(j-1))%this.TAMANHO]; //esquerdacima
		num_vizinhos += grid[(this.TAMANHO+(i-1))%this.TAMANHO][j]; // cima
		num_vizinhos += grid[(this.TAMANHO+(i-1))%this.TAMANHO][((j+1)%this.TAMANHO)]; // direita cima

		return num_vizinhos;
	}
    
    /* Metodo rodado pela thread */
	public void run() {
		for(int i=0;i<(this.TAMANHO/this.MaxThreads); i++){     
            for(int j = 0; j<this.TAMANHO; j++){
                if (this.grid[(i+this.Offset)][j] == 1){ // //le celulas vivas
                    if (getNeighbors((i+this.Offset),j) < 2 || getNeighbors((i+this.Offset),j) > 3){ // Regra A e C
                        this.gridAux[i][j] = 0;
                    }
                    else // Regra B
                        this.gridAux[i][j] = 1; 
                }
                else{ // le células mortas
                    if(getNeighbors((i+this.Offset),j) == 3) // Regra D
                        this.gridAux[i][j] = 1;
                    else // pode-se retirar pois não fez nada com célula
                        this.gridAux[i][j] = 0;
                }
            }
        }
	}
}
