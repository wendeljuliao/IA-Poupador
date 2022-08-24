package algoritmo;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

import controle.Constantes;

public class Poupador extends ProgramaPoupador {
	Poupador() {
		for (int i = 0; i < 30; i++) {
			for (int j = 0; j < 30; j++) {
				memoria[i][j] = -9;
			}
		} 
    }
	
//	class Grafo {
//		private HashMap<Coordenada, ArrayList<Coordenada>> grafo = new HashMap<>();
//		
//		public void addEdge(Coordenada origem, Coordenada destino) {
//			if (!grafo.containsKey(origem)) {
//				addVertex(origem);
//			}
//			
//			if (!grafo.containsKey(destino)) {
//				addVertex(destino);
//			}
//			
//			if (!hasEdge(origem, destino)) {
//				grafo.get(origem).add(destino);				
//			}
//			
////			if (!hasEdge(destino, origem)) {
////				grafo.get(destino).add(origem);				
////			}
//		}
//		
//		private boolean hasEdge(Coordenada origem, Coordenada destino) {
//			if (grafo.get(origem).contains(destino)) {
//				return true;
//			}
//			return false;
//		}
//		
//		public void addVertex(Coordenada vertex) {
//			grafo.put(vertex, new ArrayList<Coordenada>());
//		}
//	}
	
//	class Coordenada {
//		private Point coordenada;
//		private int[] vizinhos;
//		
//		public Coordenada(Point coordenada, int[] vizinhos) {
//			this.coordenada = coordenada;
//
//			this.vizinhos = vizinhos;
//		}
//
//		public Point getCoordenada() {
//			return coordenada;
//		}
//
//		public int[] getVizinhos() {
//			return vizinhos;
//		}
//		
//	}
	
	int[][] baseCoordenadaVisao = {
			{-2, -2}, {-1, -2}, {0, -2}, {1, -2}, {2, -2},
			{-2, -1}, {-1, -1}, {0, -1}, {1, -1}, {2, -1},
			{-2, 0}, {-1, 0}, {1, 0}, {2, 0},
			{-2, 1}, {-1, 1}, {0, 1}, {1, 1}, {2, 1},
			{-2, 2}, {-1, 2}, {0, 2}, {1, 2}, {2, 2},
			
	};
	
	int[][] baseCoordenadaMovimentos = {
			{0, 0}, {0, -1}, {0, 1}, {1, 0}, {-1, 0}
	};
	
	int[] baseDirecaoMovimentos = {
			7,
			16,
			12,
			11
	};
	
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
	int[][] visitados = new int[30][30];
	int[][] memoria = new int[30][30];
	ArrayList<Point> memoriaMoedas = new ArrayList<>(); 
	
	Point posicaoAtual;
	
	ArrayList<Integer> ultimosPassos = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0));

	
//	Grafo grafoTeste = new Grafo();
//	HashMap<Point, Coordenada> grafo = new HashMap<Point, Coordenada>();
	
	void atualizarVariaveis() {
		posicaoAtual = this.sensor.getPosicao();
		this.visitados[posicaoAtual.x][posicaoAtual.y]++;
		visao_agente = this.sensor.getVisaoIdentificacao();
		olfato_agente = this.sensor.getAmbienteOlfatoPoupador();
	}
	

	boolean contem(int[] arr, int value) {
		for (int s : arr) {
			if (s == value) {
				return true;
			}
		}
		return false;
	}
	
//	private int movimentoAleatorio(int[] opcoes) {
//		int random = new Random().nextInt(opcoes.length);
//		
//		ArrayList<Integer> arrayOpcoes = new ArrayList<>();
//		
////		if (this.visao_agente[opcoes[random]] == 1) {
////			movimentoAleatorio(opcoes);
////		}
//		
//		return opcoes[random];
//	}
	
	private int movimentoAleatorio(ArrayList<Integer> opcoes) {
		if (opcoes.size() <= 0) {
			System.out.println("Sem Opção");
			return new Random().nextInt(4) + 1;
		}
		int random = new Random().nextInt(opcoes.size());

		boolean temParede = this.visao_agente[this.DISTANCIA_UM[opcoes.get(random) - 1]] == 1;
		boolean foraAmbiente = this.visao_agente[this.DISTANCIA_UM[opcoes.get(random) - 1]] == -1;
		
		if (temParede || foraAmbiente) {
			opcoes.remove(random);
			return movimentoAleatorio(opcoes);
		}
		
		return opcoes.get(random);
	}
	
	private int irEmDirecao(int valor) {
		if (contem(DIRECAO_NORTE, valor)) {
			return NORTE;
		} else if (contem(DIRECAO_SUL, valor)) {
			return SUL;
		} else if (contem(DIRECAO_LESTE, valor)) {
			return LESTE;
		} else if (contem(DIRECAO_OESTE, valor)) {
			return OESTE;
		} else if (contem(DIRECAO_NORDESTE, valor)) {
			return movimentoAleatorio(new ArrayList<Integer>(Arrays.asList( NORTE, LESTE )));
		} else if (contem(DIRECAO_NOROESTE, valor)) {
			return movimentoAleatorio(new ArrayList<Integer>(Arrays.asList( NORTE, OESTE )));
		} else if (contem(DIRECAO_SUDESTE, valor)) {
			return movimentoAleatorio(new ArrayList<Integer>(Arrays.asList( SUL, LESTE )));
		} else if (contem(DIRECAO_SUDOESTE, valor)) {
			return movimentoAleatorio(new ArrayList<Integer>(Arrays.asList( SUL, OESTE )));
		}
		
		return -1;
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
	
//	void mapearMapa() {
//		Coordenada coordenada = new Coordenada(posicaoAtual, this.visao_agente);
//		
////		grafoTeste.addEdge(new Coordenada(posicaoAtual, 0, this.visao_agente), new Coordenada(posicaoAtual, 0, this.visao_agente));
//		
//		grafo.put(this.sensor.getPosicao(), coordenada);
//	}
	
	void memorizar() {
		int x = this.posicaoAtual.x; 
		int y = this.posicaoAtual.y;
		
		// Já que pisei nesse canto, removo a moeda se tiver aqui
		memoriaMoedas.remove(this.posicaoAtual);
		// Cantos que já "pisei!"
		memoria[x][y] = 9;				
				
		for (int i = 0; i < visao_agente.length; i++) {
			int xNovo = x + baseCoordenadaVisao[i][0];
			int yNovo = y + baseCoordenadaVisao[i][1];
			
			
			if (this.visao_agente[i] != -1 && this.visao_agente[i] != -2) {
				Point pontoNovo = new Point (xNovo, yNovo);
				
				// Já pisei ? Se não pisei, coloque o que tem lá
				if (memoria[xNovo][yNovo] != 9) {
					memoria[xNovo][yNovo] = this.visao_agente[i];
				}
				
				if (this.visao_agente[i] == 4 && !memoriaMoedas.contains(pontoNovo)) {
					memoriaMoedas.add(pontoNovo);
				}
				
				
			}
			
		}

	}
	
	private int conhecerMapa() {
		int x = this.posicaoAtual.x;
		int y = this.posicaoAtual.y;
						
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
//				System.out.println("Correr do ladrão");
				if (contem(DIRECAO_NORTE, arr[i])) {
					return movimentoAleatorio(new ArrayList<Integer>(Arrays.asList( SUL, LESTE, OESTE )));
				} else if (contem(DIRECAO_SUL, arr[i])) {
					return movimentoAleatorio(new ArrayList<Integer>(Arrays.asList( NORTE, LESTE, OESTE )));
				} else if (contem(DIRECAO_LESTE, arr[i])) {
					return movimentoAleatorio(new ArrayList<Integer>(Arrays.asList( OESTE, NORTE, SUL )));
				} else if (contem(DIRECAO_OESTE, arr[i])) {
					return movimentoAleatorio(new ArrayList<Integer>(Arrays.asList( LESTE, NORTE, SUL )));
				} else if (contem(DIRECAO_NORDESTE, arr[i])) {
					return movimentoAleatorio(new ArrayList<Integer>(Arrays.asList( SUL, OESTE )));
				} else if (contem(DIRECAO_NOROESTE, arr[i])) {
					return movimentoAleatorio(new ArrayList<Integer>(Arrays.asList( SUL, LESTE )));
				} else if (contem(DIRECAO_SUDESTE, arr[i])) {
					return movimentoAleatorio(new ArrayList<Integer>(Arrays.asList( NORTE, OESTE )));
				} else if (contem(DIRECAO_SUDOESTE, arr[i])) {
					return movimentoAleatorio(new ArrayList<Integer>(Arrays.asList( NORTE, LESTE )));
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
	
	int pegarMoedas() {
		
		return -1;
	}
	
	int seAproximar(int valor) {
		int x = this.posicaoAtual.x;
		int y = this.posicaoAtual.y;
		
		ArrayList<Integer> arrayValores = new ArrayList<>();
		ArrayList<Point> arrayPosicoes = new ArrayList<>();
		for (int i = 0; i < this.visao_agente.length; i++) {
			int xNovo = x + this.baseCoordenadaVisao[i][0];
			int yNovo = y + this.baseCoordenadaVisao[i][1];
			if (this.visao_agente[i] == valor) {
				Point pontoNovo = new Point(xNovo, yNovo);
				arrayValores.add(i);
				arrayPosicoes.add(pontoNovo);
				
			}
		}
		
		int menorDistancia = Integer.MAX_VALUE;
		Point pontoMenorDistancia = null;
		int valorMenorDistancia = Integer.MAX_VALUE;
		int x1, y1;
		
		for (int i = 0; i < arrayPosicoes.size(); i++) {
			x1 = arrayPosicoes.get(i).x;
			y1 = arrayPosicoes.get(i).y;
			if (distanciaManhattan(x1, y1, x, y) < menorDistancia) {
				menorDistancia = distanciaManhattan(x1, y1, x, y);
				pontoMenorDistancia = arrayPosicoes.get(i);
				valorMenorDistancia = arrayValores.get(i);
			}
		}
		
		ArrayList<Point> arrayMenoresPontos = new ArrayList<>();
		ArrayList<Integer> arrayMenoresValores = new ArrayList<>();
		
		for (int i = 0; i < arrayPosicoes.size(); i++) {
			x1 = arrayPosicoes.get(i).x;
			y1 = arrayPosicoes.get(i).y;
			if (distanciaManhattan(x1, y1, x, y) == menorDistancia) {
				arrayMenoresPontos.add(arrayPosicoes.get(i));
				arrayMenoresValores.add(arrayValores.get(i));
			}
		}
		
		if (arrayMenoresPontos.size() > 0) {
			int aleatorio = new Random().nextInt(arrayMenoresValores.size());
//			Point escolhido = new Point(x - arrayMenoresPontos.get(aleatorio).x, y - arrayMenoresPontos.get(aleatorio).y);
			int escolhidoValor = arrayMenoresValores.get(aleatorio);
			return irEmDirecao(escolhidoValor);
			
		}
		
		return -1;
	}
	
	private int pensarMovimento() {
		
		// TEM QUE SER FUGIR DO LADRAO, IR AO BANCO OU PEGAR MOEDA
		// FAZER LOGICA RELACIONADO A FRASE A CIMA
		if (estaLooping()) {
			return (int) (Math.random() * 4) + 1;
		}
		
		int fugir = fugirLadrao();
		
		if (fugir != -1) {
			return fugir;
		}

		int depositar = seAproximar(3);
		if (depositar != -1 && this.sensor.getNumeroDeMoedas() > 0) {
			return depositar;
		}
		
		int temMoedas = seAproximar(4);
		
		if (temMoedas != -1) {
			return temMoedas;
		}
		
		return conhecerMapa(); 
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
		return indicesMenoresManhattan.get(aleatorio) + 1;
		
	}
	
	private int distanciaManhattan(int x1, int y1, int x0, int y0) {
		return Math.abs(x1 - x0) + Math.abs(y1 - y0);
	}
	
	private boolean estaLooping() {
		return (ultimosPassos.get(0).equals(ultimosPassos.get(2))
				&& ultimosPassos.get(1).equals(ultimosPassos.get(3))
				&& ultimosPassos.get(2).equals(ultimosPassos.get(4))
				&& ultimosPassos.get(3).equals(ultimosPassos.get(5))
				&& ultimosPassos.get(4).equals(ultimosPassos.get(6))
				&& ultimosPassos.get(5).equals(ultimosPassos.get(7)));
	}
	
	private void atualizarUltimosPassos(int comando) {
		ultimosPassos.set(8, ultimosPassos.get(7));
		ultimosPassos.set(7, ultimosPassos.get(6));
		ultimosPassos.set(6, ultimosPassos.get(5));
		ultimosPassos.set(5, ultimosPassos.get(4));
		ultimosPassos.set(4, ultimosPassos.get(3));
		ultimosPassos.set(3, ultimosPassos.get(2));
		ultimosPassos.set(2, ultimosPassos.get(1));
		ultimosPassos.set(1, ultimosPassos.get(0));
		ultimosPassos.set(0, comando);
	}
		
	@Override
	public int acao() {
		atualizarVariaveis();
		
//		mapearMapa();
		memorizar();
				
//		for (int i = 0; i < this.visao_agente.length; i++) {
//			System.out.print(this.visao_agente[i] + " ");
//		}
//		System.out.println();
//		System.out.println(Constantes.posicaoBanco);
//		for (int i = 0; i < 30; i++) {
//			for (int j = 0; j < 30; j++) {
//				System.out.print(memoria[i][j] + " ");
//			}
//			System.out.println();
//		}
//		System.out.println("\n");
		int comando = pensarMovimento();
		atualizarUltimosPassos(comando);
		return comando;
	}
}