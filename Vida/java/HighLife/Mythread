class Mythread implements Runnable {
	private int TAMANHO;
	private int MaxThreads;
	private int T_atual;
	private int grid[][];
	 int gridAux[][];
    
     /* construtor */
	public Mythread(int tam, int maxThreads, int t_atual,int grafico[][]) {
		this.TAMANHO = tam;
		this.MaxThreads = maxThreads;
		this.T_atual = t_atual;
		this.grid = grafico;
		this.gridAux = new int[(tam/this.MaxThreads)][tam];
	}

	/* Retorna a quantidade de vizinhos vivos de cada celula na posicao ​i,j */
	public int getNeighbors(int i, int j) {
		int num_vizinhos=0;
	
		num_vizinhos += grid[i][((j+1)%this.TAMANHO)]; 
		num_vizinhos += grid[((i+1)%this.TAMANHO)][((j+1)%this.TAMANHO)]; 
		num_vizinhos += grid[((i+1)%this.TAMANHO)][j]; 
		num_vizinhos += grid[((i+1)%this.TAMANHO)][(this.TAMANHO+(j-1))%this.TAMANHO]; 
		num_vizinhos += grid[i][(this.TAMANHO+(j-1))%this.TAMANHO]; 
		num_vizinhos += grid[(this.TAMANHO+(i-1))%this.TAMANHO][(this.TAMANHO+(j-1))%this.TAMANHO]; 
		num_vizinhos += grid[(this.TAMANHO+(i-1))%this.TAMANHO][j];
		num_vizinhos += grid[(this.TAMANHO+(i-1))%this.TAMANHO][((j+1)%this.TAMANHO)];

		return num_vizinhos;
	}
    
    /* thread */
	public void run() {
		for(int i=0;i<(this.TAMANHO/this.MaxThreads); i++){     
		    for(int j = 0; j<this.TAMANHO; j++){
			if (this.grid[(i+this.T_atual)][j] == 1){ // //le celulas vivas
			    if (getNeighbors((i+this.T_atual),j) < 2 || getNeighbors((i+this.T_atual),j) > 3){ // Regra 1
				this.gridAux[i][j] = 0;
			    }
			    else // Regra 3
				this.gridAux[i][j] = 1; 
			}
			else{ // le células mortas
			    if(getNeighbors((i+this.T_atual),j) == 3) // Regra 2
				this.gridAux[i][j] = 1;
			    else // Regra 3
				this.gridAux[i][j] = 0;
			}
		    }
		}
	}
}
