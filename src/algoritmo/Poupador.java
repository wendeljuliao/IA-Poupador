package algoritmo;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Random;

import controle.Constantes;

public class Poupador extends ProgramaPoupador {
//	Poupador() {
//		for (int i = 0; i < 30; i++) {
//			for (int j = 0; j < 30; j++) {
//				memoria[i][j] = -9;
//			}
//		} 
//    }
//	
	class Grafo {
		private HashMap<Point, ArrayList<Point>> grafo = new HashMap<>();
		
		public void addEdge(Point origem, Point destino) {
			if (!hasVertex(origem)) {
				addVertex(origem);
			}
			
			if (!hasVertex(destino)) {
				addVertex(destino);
			}
			
			if (!hasEdge(origem, destino)) {
				grafo.get(origem).add(destino);				
			}
			
			if (!hasEdge(destino, origem)) {
				grafo.get(origem).add(destino);				
			}
			
//			if (!hasEdge(destino, origem)) {
//				grafo.get(destino).add(origem);				
//			}
		}
		
		private boolean hasEdge(Point origem, Point destino) {
			if (grafo.get(origem).contains(destino)) {
				return true;
			}

			return false;
		}
		
		public boolean hasVertex(Point vertex) {
			return grafo.containsKey(vertex);
		}
		
		public void addVertex(Point vertex) {
			grafo.put(vertex, new ArrayList<Point>());
		}
		
		public void mostrarVertexs() {
			System.out.println(this.grafo.keySet());
		}
		
		public boolean temBanco() {
			return grafo.containsKey(Constantes.posicaoBanco);
		}
	}
	
	public class Coordenada {
		Point coordenada;
		Coordenada parent = null;
		
		Coordenada (Point coordenada ) {
			this.coordenada = coordenada;
		}
		
        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Coordenada coordenadaNova = (Coordenada) o;
            return coordenada.equals(coordenadaNova.coordenada);
        }
	}
	
//	public class Edge {
//		Point origem;
//		Point destino;
//		int peso;
//		
//		Edge (Point origem, Point destino, int peso) {
//			this.origem = origem;
//			this.destino = destino;
//			this.peso = peso;
//		}
//		
//		@Override
//        public boolean equals(Object o) {
//            if (this == o) {
//                return true;
//            }
//            if (o == null || getClass() != o.getClass()) {
//                return false;
//            }
//            Edge edge = (Edge) o;
//            return origem.equals(edge.origem) &&
//                    destino.equals(edge.destino) &&
//                    peso == edge.peso;
//        }
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
	int[] olfato_ladrao;
	int[][] visitados = new int[31][31];
	int[][] memoria = new int[30][30];
	ArrayList<Point> memoriaMoedas = new ArrayList<>(); 
	
	Point posicaoAtual;
	
	ArrayList<Integer> ultimosPassos = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0));

	Grafo grafo = new Grafo();
	
	void atualizarVariaveis() {
		this.posicaoAtual = this.sensor.getPosicao();
		this.visitados[posicaoAtual.x][posicaoAtual.y]++;
		this.visao_agente = this.sensor.getVisaoIdentificacao();
		this.olfato_ladrao = this.sensor.getAmbienteOlfatoLadrao();
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
//			System.out.println("Sem Opção");
			return new Random().nextInt(4) + 1;
		}
		int random = new Random().nextInt(opcoes.size());

		boolean temParede = this.visao_agente[this.DISTANCIA_UM[opcoes.get(random) - 1]] == 1;
		boolean foraAmbiente = this.visao_agente[this.DISTANCIA_UM[opcoes.get(random) - 1]] == -1;
		boolean pastilhaDoPoder = this.visao_agente[this.DISTANCIA_UM[opcoes.get(random) - 1]] == 5;
		
		if (temParede || foraAmbiente || pastilhaDoPoder) {
			opcoes.remove(random);
			return movimentoAleatorio(opcoes);
		}
		
		return opcoes.get(random);
	}
	
	private int selecionarAleatorio(ArrayList<Integer> opcoes) {
		int random = new Random().nextInt(opcoes.size());
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
	
	void memorizar() {
		int x = this.posicaoAtual.x; 
		int y = this.posicaoAtual.y;
		
		int xNovo, yNovo;
		Point pontoNovo;
		
		int bloco;
		
		for (int i = 0; i < visao_agente.length; i++) {
			bloco = this.visao_agente[i];
			if (bloco == 0 || bloco == 3 || bloco == 4 || bloco == 5 ||
				bloco == 200 || bloco == 210 || bloco == 220 || bloco == 230) {
				xNovo = x + baseCoordenadaVisao[i][0];
				yNovo = y + baseCoordenadaVisao[i][1];	
				pontoNovo = new Point (xNovo, yNovo);
				
				grafo.addEdge(this.posicaoAtual, pontoNovo);
				
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
		ArrayList<Integer> aux = new ArrayList<>();
		for (int i = 0; i < arr.length; i++) {
			if (this.visao_agente[arr[i]] == 200 || this.visao_agente[arr[i]] == 210 || this.visao_agente[arr[i]] == 220 || this.visao_agente[arr[i]] == 230) {
//				System.out.println("Correr do ladrão");
				if (contem(DIRECAO_NORTE, arr[i])) {
//					aux.addAll(Arrays.asList( SUL, LESTE, OESTE ));
					return movimentoAleatorio(new ArrayList<Integer>(Arrays.asList( SUL, LESTE, OESTE )));
				} else if (contem(DIRECAO_SUL, arr[i])) {
//					aux.addAll(Arrays.asList( NORTE, LESTE, OESTE ));
					return movimentoAleatorio(new ArrayList<Integer>(Arrays.asList( NORTE, LESTE, OESTE )));
				} else if (contem(DIRECAO_LESTE, arr[i])) {
//					aux.addAll(Arrays.asList( OESTE, NORTE, SUL ));
					return movimentoAleatorio(new ArrayList<Integer>(Arrays.asList( OESTE, NORTE, SUL )));
				} else if (contem(DIRECAO_OESTE, arr[i])) {
//					aux.addAll(Arrays.asList( OESTE, NORTE, SUL ));
					return movimentoAleatorio(new ArrayList<Integer>(Arrays.asList( LESTE, NORTE, SUL )));
				} else if (contem(DIRECAO_NORDESTE, arr[i])) {
//					aux.addAll(Arrays.asList( SUL, OESTE ));
					return movimentoAleatorio(new ArrayList<Integer>(Arrays.asList( SUL, OESTE )));
				} else if (contem(DIRECAO_NOROESTE, arr[i])) {
//					aux.addAll(Arrays.asList( SUL, LESTE ));
					return movimentoAleatorio(new ArrayList<Integer>(Arrays.asList( SUL, LESTE )));
				} else if (contem(DIRECAO_SUDESTE, arr[i])) {
//					aux.addAll(Arrays.asList( NORTE, OESTE ));
					return movimentoAleatorio(new ArrayList<Integer>(Arrays.asList( NORTE, OESTE )));
				} else if (contem(DIRECAO_SUDOESTE, arr[i])) {
//					aux.addAll(Arrays.asList( NORTE, LESTE ));
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
	
	public int verificarOlfato(int indice) {
		if (indice == 0) {
			return movimentoAleatorio(new ArrayList<Integer>(Arrays.asList( SUL, LESTE )));
		} else if (indice == 1) {
			return movimentoAleatorio(new ArrayList<Integer>(Arrays.asList( SUL, LESTE, OESTE )));
		} else if (indice == 2) {
			return movimentoAleatorio(new ArrayList<Integer>(Arrays.asList( SUL, OESTE )));
		} else if (indice == 3) {
			return movimentoAleatorio(new ArrayList<Integer>(Arrays.asList( NORTE, SUL, LESTE )));
		} else if (indice == 4) {
			return movimentoAleatorio(new ArrayList<Integer>(Arrays.asList( NORTE, SUL, OESTE)));
		} else if (indice == 5) {
			return movimentoAleatorio(new ArrayList<Integer>(Arrays.asList( NORTE, LESTE )));
		} else if (indice == 6) {
			return movimentoAleatorio(new ArrayList<Integer>(Arrays.asList( NORTE, LESTE, OESTE )));
		} else if (indice == 7) {
			return movimentoAleatorio(new ArrayList<Integer>(Arrays.asList( NORTE, OESTE )));
		}
		return -1;
	}
	
	public int sentirOlfatoLadrao() {
		ArrayList<Integer> olfatoUm = new ArrayList<>();
		ArrayList<Integer> olfatoDois = new ArrayList<>();
		ArrayList<Integer> olfatoTres = new ArrayList<>();
		ArrayList<Integer> olfatoQuatro = new ArrayList<>();
		ArrayList<Integer> olfatoCinco = new ArrayList<>();
		
		for (int i = 0; i < olfato_ladrao.length; i++) {
			if (olfato_ladrao[i] == 1) {
				olfatoUm.add(i);
			} else if (olfato_ladrao[i] == 2) {
				olfatoDois.add(i);
			} else if (olfato_ladrao[i] == 3) {
				olfatoTres.add(i);
			} else if (olfato_ladrao[i] == 4) {
				olfatoQuatro.add(i);
			} else if (olfato_ladrao[i] == 5) {
				olfatoCinco.add(i);
			} 			
		}
		
		if (!olfatoUm.isEmpty()) {
			return verificarOlfato(selecionarAleatorio(olfatoUm));		
		}
		if (!olfatoDois.isEmpty()) {
			return verificarOlfato(selecionarAleatorio(olfatoDois));		
		}
		if (!olfatoTres.isEmpty()) {
			return verificarOlfato(selecionarAleatorio(olfatoTres));		
		}
		if (!olfatoQuatro.isEmpty()) {
			return verificarOlfato(selecionarAleatorio(olfatoQuatro));		
		}
		if (!olfatoCinco.isEmpty()) {
			return verificarOlfato(selecionarAleatorio(olfatoCinco));		
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
//		if (estaLooping()) {
//			return (int) (Math.random() * 4) + 1;
//		}
			
		int fugir = fugirLadrao();
		if (fugir != -1) {
			return fugir;
		}
			
		int sentirOlfato = sentirOlfatoLadrao();
		if (sentirOlfato != -1) {
	//		System.out.println("Sentindo...");
			return sentirOlfato;
		}
			
		if (estaLooping()) {
			return new Random().nextInt() * 4 + 1;
		}
		if (grafo.temBanco()) {
			
			int temMoedas = seAproximar(4);
			if (temMoedas != -1) {
				return temMoedas;
			}
			
			int depositar = depositar();
			if (depositar != -1) {
				return depositar;
			}
		}
		
		
		return conhecerMapa(); 
	}
	
	private int depositar() {
		
		int vendoBanco = seAproximar(3);
		
		if (vendoBanco != -1 && this.sensor.getNumeroDeMoedas() > 0) {
			return vendoBanco;
		}
		
		if (grafo.temBanco() && this.sensor.getNumeroDeMoedas() > 3) {
			LinkedList<Point> visitadosGrafo = new LinkedList<>();
			LinkedList<Coordenada> queue = new LinkedList<>();
			
			ArrayList<Point> movimentos = new ArrayList<>();
			
			
			Coordenada aux;
			Coordenada fim;
			
			queue.add(new Coordenada(posicaoAtual));
			
			while(!queue.isEmpty()) {
				aux = queue.pop();
				
				if (aux.coordenada.equals(Constantes.posicaoBanco)) {
					fim = aux;
					while (fim.parent != null) {
						movimentos.add(fim.coordenada);
						fim = fim.parent;
					}
					break;
				}
				
				for (Point atual : grafo.grafo.get(aux.coordenada)) {
					Coordenada m = new Coordenada(atual);
					m.parent = aux;
							
					if (!visitadosGrafo.contains(m.coordenada)) {
						visitadosGrafo.add(m.coordenada);
						queue.add(m);
					}
				}

			}
			if (!movimentos.isEmpty()) {
				int x = posicaoAtual.x;
				int y = posicaoAtual.y;
				Point movimentoSequente = movimentos.get(movimentos.size() - 1);
				int xSeq = movimentoSequente.x;
				int ySeq = movimentoSequente.y;
				
//				System.out.println(xSeq + " " + ySeq);
//				System.out.println(x + " " + y);
				ArrayList<Integer> escolhas = new ArrayList<Integer>();
				
				if (x - xSeq > 0) {
					escolhas.add(4);
				}
				
				if (x - xSeq < 0) {
					escolhas.add(3);
				}
				
				if (y - ySeq > 0) {
					escolhas.add(1);
				}
				
				if (y - ySeq < 0) {
					escolhas.add(2);
				}

				return movimentoAleatorio(escolhas);
				
			}
		}

		
		return -1;
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
//		for (int i = 0; i < 31; i++) {
//			for (int j = 0; j < 31; j++) {
//				System.out.print(this.visitados[i][j] + " ");
//			}
//			System.out.println();
//		}
//		System.out.println("\n");
		int comando = pensarMovimento();
		atualizarUltimosPassos(comando);
		return comando;
	}
}