package algoritmo;

import java.awt.Point;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Random;

import controle.Constantes;

public class Poupador extends ProgramaPoupador {
	
	int PARADO = 0, NORTE = 1, SUL = 2, LESTE = 3, OESTE = 4;
	int NORDESTE = new Random().nextBoolean() ? NORTE : LESTE;
	int NOROESTE = new Random().nextBoolean() ? NORTE : OESTE;
	int SUDESTE = new Random().nextBoolean() ? SUL : LESTE;
	int SUDOESTE = new Random().nextBoolean() ? SUL : OESTE;
	
	final int[] DIRECAO_NORTE = {2, 7};
	final int[] DIRECAO_SUL = {16, 21};
	final int[] DIRECAO_LESTE = {12, 13};
	final int[] DIRECAO_OESTE = {10, 11};
	final int[] DIRECAO_NORDESTE = {3, 4, 8, 9};
	final int[] DIRECAO_NOROESTE = {0, 1, 5, 6};
	final int[] DIRECAO_SUDESTE = {17, 18, 22, 23};
	final int[] DIRECAO_SUDOESTE = {14, 15, 20, 19};
	
	final int[] DISTANCIA_UM = {7, 16, 12, 11};
	final int[] DISTANCIA_DOIS = {2, 6, 8, 10, 13, 15, 17, 21};
	final int[] DISTANCIA_TRES = {1, 3, 5, 9, 14, 18, 20, 22};
	final int[] DISTANCIA_QUATRO = {0, 4, 19, 23};
	
	final int PAREDE = 1;
	final int BANCO = 3;
	final int MOEDA = 4;
	
	int[] visao_agente;
	int[] olfato_agente;
	int[][] visitados = new int[31][31];
	
	void atualizarVariaveis() {
		visao_agente = this.sensor.getVisaoIdentificacao();
		olfato_agente = this.sensor.getAmbienteOlfatoPoupador();
	}
	

	boolean inDirection(int[] arr, int value) {
		for (int s : arr) {
			if (s == value) {
				return true;
			}
		}
		return false;
	}
	
	private int movimentoAleatorio(int[] opcoes) {
		int random = new Random().nextInt(opcoes.length);
		
		return opcoes[random];
	}
	
	private int getCoin() {
		
		if(visao_agente[7] == 4) { 
			return 1;
		} else if(visao_agente[11] == 4) {
			return 4;
		} else if(visao_agente[12] == 4) {
			return 3;
		} else if(visao_agente[16] == 4) {
			return 2;
		}
		
		return (int) (Math.random() * 5);
	}
	
	private int seAventurar() {
		int x = this.sensor.getPosicao().x;
		int y = this.sensor.getPosicao().y;
		this.visitados[x][y]++;
		
		int []direcoes = new int[4];
		
		// CIMA
		if (this.visao_agente[7] == 0 || this.visao_agente[7] == 4) {
			if (y == 0) {
				direcoes[0] = -1;
			} else {				
				direcoes[0] = this.visitados[x][y - 1];
			}
		} else {
			direcoes[0] = -1;
		}
		
		// ESQUERDA
		if (this.visao_agente[11] == 0 || this.visao_agente[11] == 4) {
			if (x == 0) {
				direcoes[3] = -1;
			} else {
				
				direcoes[3] = this.visitados[x - 1][y];
			}
		} else {
			direcoes[3] = -1;
		}
		
		// DIREITA
		if (this.visao_agente[12] == 0 || this.visao_agente[12] == 4) {
			direcoes[2] = this.visitados[x + 1][y];
		} else {
			direcoes[2] = -1;
		}
		
		// BAIXO
		if (this.visao_agente[16] == 0 || this.visao_agente[16] == 4) {
			direcoes[1] = this.visitados[x][y + 1];
		} else {
			direcoes[1] = -1;
		}
		
		int menor = Integer.MAX_VALUE;
		ArrayList<Integer> indicesMenores = new ArrayList<Integer>();
		
		for (int i = 0; i < direcoes.length; i++) {
//			System.out.print(direcoes[i] +" ");
			if (direcoes[i] < menor && direcoes[i] != -1) {
				menor = direcoes[i];
			}
		}
//		System.out.println();
		for (int i = 0; i < direcoes.length; i++) {
			if (direcoes[i] == menor) {
				indicesMenores.add(i);
			}
		}
		int aleatorio = new Random().nextInt(indicesMenores.size());
		
		return indicesMenores.get(aleatorio) + 1;
		
		//return getCoin();
	}
	
	private int verificarLadrao(int[] arr) {
		for (int i = 0; i < arr.length; i++) {
			if (this.visao_agente[arr[i]] == 200 || this.visao_agente[arr[i]] == 210 || this.visao_agente[arr[i]] == 220 || this.visao_agente[arr[i]] == 230) {
				System.out.println("Correr do ladrão");
				if (inDirection(DIRECAO_NORTE, arr[i])) {
					return movimentoAleatorio(new int[] { SUL, LESTE, OESTE });
				} else if (inDirection(DIRECAO_SUL, arr[i])) {
					return movimentoAleatorio(new int[] { NORTE, LESTE, OESTE });
				} else if (inDirection(DIRECAO_LESTE, arr[i])) {
					return movimentoAleatorio(new int[] { OESTE, NORTE, SUL });
				} else if (inDirection(DIRECAO_OESTE, arr[i])) {
					return movimentoAleatorio(new int[] { LESTE, NORTE, SUL });
				} else if (inDirection(DIRECAO_NORDESTE, arr[i])) {
					return movimentoAleatorio(new int[] {SUL, OESTE});
				} else if (inDirection(DIRECAO_NOROESTE, arr[i])) {
					return movimentoAleatorio(new int[] {SUL, LESTE});
				} else if (inDirection(DIRECAO_SUDESTE, arr[i])) {
					return movimentoAleatorio(new int[] {NORTE, OESTE});
				} else if (inDirection(DIRECAO_SUDOESTE, arr[i])) {
					return movimentoAleatorio(new int[] {NORTE, LESTE});
				}	
			}
		}
		
		return -1;
	}
	
	private int fugirLadrao() {
		
		int verificar_distUm = verificarLadrao(DISTANCIA_UM);
		
		if (verificar_distUm != -1) {
			return verificar_distUm;
		}
		
		int verificar_distDois = verificarLadrao(DISTANCIA_DOIS);
		
		if (verificar_distDois != -1) {
			return verificar_distDois;
		}
		
		int verificar_distTres = verificarLadrao(DISTANCIA_TRES);
		
		if (verificar_distTres != -1) {
			return verificar_distTres;
		}
		
		int verificar_distQuatro = verificarLadrao(DISTANCIA_QUATRO);
		
		if (verificar_distQuatro != -1) {
			return verificar_distQuatro;
		}
		
		return -1;
	}
	
	private int pensarJogada() {
		int fugir = fugirLadrao();
		
		if (fugir != -1) {
			return fugir;
		}
		
		return seAventurar(); 
	}
	
	private int encontrarBanco() {
		int x = (int) this.sensor.getPosicao().x;
		int y = (int) this.sensor.getPosicao().y;
		
		int xBanco = (int) Constantes.posicaoBanco.x;
		int yBanco = (int) Constantes.posicaoBanco.y;
		
//		System.out.println("x: " + x + " | y: " + y);
//		System.out.println("xBanco: " + xBanco + " | yBanco: " + yBanco);
		
		// Cima, Baixo, Direita, Esquerda
		int[] distanciaManhattan = new int[] {(Math.abs(xBanco - x) + Math.abs(yBanco - (y - 1))),
											  (Math.abs(xBanco - x) + Math.abs(yBanco - (y + 1))),
											  (Math.abs(xBanco - (x + 1)) + Math.abs(yBanco - y)),
											  (Math.abs(xBanco - (x - 1)) + Math.abs(yBanco - y))};
		
		int menorManhattan = Integer.MAX_VALUE;
		ArrayList<Integer> indicesMenoresManhattan = new ArrayList<Integer>();
		
		for (int i = 0; i < distanciaManhattan.length; i++) {
//			System.out.println(distanciaManhattan[i]);
			if (distanciaManhattan[i] < menorManhattan && this.visao_agente[DISTANCIA_UM[i]] != 1 && this.visao_agente[DISTANCIA_UM[i]] != 5) {
				menorManhattan = distanciaManhattan[i];		
			}
		}
		
		for (int i = 0; i < distanciaManhattan.length; i++) {
			if (distanciaManhattan[i] == menorManhattan) {
				indicesMenoresManhattan.add(i);
			}
		}
		
		int aleatorio = new Random().nextInt(indicesMenoresManhattan.size());
		// System.out.println(indicesMenoresManhattan.get(aleatorio) + 1);
		System.out.println(indicesMenoresManhattan.get(aleatorio) + 1);
		return indicesMenoresManhattan.get(aleatorio) + 1;
		
	}
		
	@Override
	public int acao() {
		atualizarVariaveis();
		
//		for (int i = 0; i < this.visao_agente.length; i++) {
//			System.out.print(this.visao_agente[i] + " ");
//		}
//		System.out.println();
//		System.out.println(Constantes.posicaoBanco);
		return pensarJogada();
	}
}