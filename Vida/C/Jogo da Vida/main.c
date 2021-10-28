#include<stdio.h>
#include<stdlib.h>
#include<sys/time.h>
#include<omp.h>

#define NUM_GEN 2000 // Numero de geracoes
#define TAM 2048 // Tamanho N da matriz NxN
#define MAX_THREADS 8
#define c_viva 1
#define c_morta 0

int **grid, **newgrid;

typedef struct {
    int secs;
    int usecs;
}Duracao;

/// Calculo de tempo
//função utilizada a partir da leitura do site:
//https://qastack.com.br/programming/5248915/execution-time-of-c-program
Duracao * tempo_decorrido (struct timeval *start, struct timeval *end){
    Duracao *total = (Duracao*)malloc(sizeof(Duracao));

    if(start->tv_sec == end->tv_sec){
        total->secs = 0;
        total->usecs = end->tv_usec - start->tv_usec;
    }
    else{
        total->usecs = 1000000 - start->tv_usec;
        total->secs = end->tv_sec - (start->tv_sec + 1);
        total->usecs += end->tv_usec;
        if(total->usecs >= 1000000){
            total->usecs -= 1000000;
            total->secs += 1;
        }
    }
    return total;
}

// Retorna a quantidade de vizinhos vivos de cada celula na posicao ​i,j
int getNeighbors(int i, int j) {
    int num_vizinhos=0;

    num_vizinhos += grid[i][((j+1)%TAM)]; // direita
    num_vizinhos += grid[((i+1)%TAM)][((j+1)%TAM)]; // direita baixo
    num_vizinhos += grid[((i+1)%TAM)][j]; //baixo
    num_vizinhos += grid[((i+1)%TAM)][(TAM+(j-1))%TAM]; // esquerda baixo
    num_vizinhos += grid[i][(TAM+(j-1))%TAM]; // esquerda
    num_vizinhos += grid[(TAM+(i-1))%TAM][(TAM+(j-1))%TAM]; //esquerda cima
    num_vizinhos += grid[(TAM+(i-1))%TAM][j]; // cima
    num_vizinhos += grid[(TAM+(i-1))%TAM][((j+1)%TAM)]; // direita cima

    return num_vizinhos;
}

// Cria uma nova geracao de acordo com as regras estabelecidas
void prox_rodada(){
    int i, j;

    #pragma omp parallel private(j) num_threads(MAX_THREADS)
    #pragma omp for
    for(i=0;i<TAM; i++){
        for(j = 0; j<TAM; j++){
            if (grid[i][j] == 1){ //le celulas vivas
                if (getNeighbors(i,j) < 2 || getNeighbors(i,j) > 3) // Regra A e C
                    newgrid[i][j] = c_morta;
                else // Regra B
                    newgrid[i][j] = c_viva;
            }
            else{ // le células mortas
                if(getNeighbors(i,j) == 3) // Regra D
                    newgrid[i][j] = c_viva;
                else // pode-se retirar pois não fez nada com célula
                    newgrid[i][j] = c_morta;
            }
        }
    }

    #pragma omp parallel private(j) num_threads(MAX_THREADS)
    #pragma omp for
    for(i=0;i<TAM; i++){
        for(j = 0; j<TAM; j++){
            grid[i][j] = newgrid[i][j];
        }
    }
}

// Conta quantas celulas estao vivas na geracao
int total_vivos(){
    int i,j,numv = 0;

    #pragma omp parallel private(j) reduction(+:numv) num_threads(MAX_THREADS)
    #pragma omp for
        for(i=0;i<TAM; i++){
            for(j = 0; j<TAM; j++){
                if (grid[i][j])
                    numv++;
            }
        }

    return numv;
}

int main(){
    int i, j;
    int lin, col;
    Duracao *valor;
    struct timeval start, end;

    // Alocacao das matrizes
    grid = malloc(sizeof(int*)*TAM);
    newgrid = malloc(sizeof(int*)*TAM);
    for(i=0;i<TAM;i++){
        grid[i] = malloc(sizeof(int)*TAM);
        newgrid[i] = malloc(sizeof(int)*TAM);
    }

    /// Definição dos pontos iniciais
    for(i=0;i<TAM; i++){
        for(j = 0; j<TAM; j++){
            grid[i][j] = 0;
        }
    }
    //GLIDER
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


    gettimeofday (&start, NULL);
    printf("Condicao Inicial: %d\n", total_vivos());


    // Gera NUM_GEN geracoes a partir da primeira
    for(i=0;i<NUM_GEN;i++){
        prox_rodada();
        printf("Geracao %d: %d\n",i+1, total_vivos());
    }

    printf("Ultima Geracao: %d\n", total_vivos());

    gettimeofday (&end, NULL);
    valor = tempo_decorrido(&start, &end);
    printf("Tempo: %d,%d s\n",valor->secs,valor->usecs);

    return 0;
}
