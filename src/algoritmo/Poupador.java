package algoritmo;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
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
				grafo.get(destino).add(origem);
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

		Coordenada(Point coordenada) {
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

	int[][] baseCoordenadaVisao = { { -2, -2 }, { -1, -2 }, { 0, -2 }, { 1, -2 }, { 2, -2 }, { -2, -1 }, { -1, -1 },
			{ 0, -1 }, { 1, -1 }, { 2, -1 }, { -2, 0 }, { -1, 0 }, { 1, 0 }, { 2, 0 }, { -2, 1 }, { -1, 1 }, { 0, 1 },
			{ 1, 1 }, { 2, 1 }, { -2, 2 }, { -1, 2 }, { 0, 2 }, { 1, 2 }, { 2, 2 },

	};

	int[][] baseCoordenadaMovimentos = { { 0, 0 }, { 0, -1 }, { 0, 1 }, { 1, 0 }, { -1, 0 } };

	int[] baseDirecaoMovimentos = { 7, 16, 12, 11 };

	int PARADO = 0, NORTE = 1, SUL = 2, LESTE = 3, OESTE = 4;
	int NORDESTE = new Random().nextBoolean() ? NORTE : LESTE;
	int NOROESTE = new Random().nextBoolean() ? NORTE : OESTE;
	int SUDESTE = new Random().nextBoolean() ? SUL : LESTE;
	int SUDOESTE = new Random().nextBoolean() ? SUL : OESTE;

	final int[] DIRECAO_NORTE = { 2, 7 };
	final int[] DIRECAO_SUL = { 16, 21 };
	final int[] DIRECAO_LESTE = { 12, 13 };
	final int[] DIRECAO_OESTE = { 10, 11 };
	final int[] DIRECAO_NORDESTE = { 3, 4, 8, 9 };
	final int[] DIRECAO_NOROESTE = { 0, 1, 5, 6 };
	final int[] DIRECAO_SUDESTE = { 17, 18, 22, 23 };
	final int[] DIRECAO_SUDOESTE = { 14, 15, 20, 19 };

	final int[] DISTANCIA_UM = { 7, 16, 12, 11 };
	final int[] DISTANCIA_DOIS = { 2, 6, 8, 10, 13, 15, 17, 21 };
	final int[] DISTANCIA_TRES = { 1, 3, 5, 9, 14, 18, 20, 22 };
	final int[] DISTANCIA_QUATRO = { 0, 4, 19, 23 };

	final int OLFATO_DIRECAO_NORTE = 1;
	final int OLFATO_DIRECAO_SUL = 6;
	final int OLFATO_DIRECAO_LESTE = 4;
	final int OLFATO_DIRECAO_OESTE = 3;
	final int OLFATO_DIRECAO_NORDESTE = 2;
	final int OLFATO_DIRECAO_NOROESTE = 0;
	final int OLFATO_DIRECAO_SUDESTE = 7;
	final int OLFATO_DIRECAO_SUDOESTE = 5;

	final int[] OLFATO_DISTANCIA_UM = { 1, 6, 4, 3 };
	final int[] OLFATO_DISTANCIA_DOIS = { 0, 2, 5, 7 };

	final int PAREDE = 1;
	final int BANCO = 3;
	final int MOEDA = 4;

	int num_moedas;
	int num_imunes;
	int[] visao_agente;
	int[] olfato_ladrao;
	int[] olfato_poupador;
	int[][] visitados = new int[31][31];
//	int[][] memoria = new int[30][30];
//	ArrayList<Point> memoriaMoedas = new ArrayList<>();

	Point posicaoAtual;

	ArrayList<Integer> ultimosPassos = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0));

	Grafo grafo = new Grafo();

	final int QUANTIDADE_MOVIMENTOS = 5;

	double[] pesoLadroes = new double[5];
	double[] pesoPoupador = new double[5];
	double[] pesoMoedas = new double[5];
	double[] pesoConhecerMapa = new double[5];
	double[] pesoIrAoBanco = new double[5];
	double[] pesoPastilhaPoder = new double[5];
	double[] pesoLadoOlfatoLadrao = new double[5];
	double[] pesoLadoOlfatoPoupador = new double[5];

	double[] probEscolhaLado = new double[5];

	// desconsidera de ir pra lá
	final double PARAMETRO_LADRAO_UM = 0;
	final double PARAMETRO_LADRAO_DOIS = 1200;
	final double PARAMETRO_LADRAO_TRES = 1000;
	final double PARAMETRO_LADRAO_QUATRO = 900;

	final double PARAMETRO_POUPADOR_UM = 400;
	final double PARAMETRO_POUPADOR_DOIS = 350;
	final double PARAMETRO_POUPADOR_TRES = 325;
	final double PARAMETRO_POUPADOR_QUATRO = 300;
	final double PARAMETRO_POUPADOR_CINCO = 280;
	
	final double PARAMETRO_MOEDA_UM = 300;
	final double PARAMETRO_MOEDA_DOIS = 250;
	final double PARAMETRO_MOEDA_TRES = 225;
	final double PARAMETRO_MOEDA_QUATRO = 200;
	
	final double PARAMETRO_PASTILHA_PODER_UM = 1500;
	final double PARAMETRO_PASTILHA_PODER_DOIS = 600;
	final double PARAMETRO_PASTILHA_PODER_TRES = 500;
	final double PARAMETRO_PASTILHA_PODER_QUATRO = 400;
	
	final double PARAMETRO_PASTILHA_PODER_SEM_MONEY = 300;

	final double PARAMETRO_OLFATO_LADRAO_UM = 500;
	final double PARAMETRO_OLFATO_LADRAO_DOIS = 450;
	final double PARAMETRO_OLFATO_LADRAO_TRES = 400;
	final double PARAMETRO_OLFATO_LADRAO_QUATRO = 350;
	final double PARAMETRO_OLFATO_LADRAO_CINCO = 300;

	final double PARAMETRO_OLFATO_POUPADOR_UM = 250;
	final double PARAMETRO_OLFATO_POUPADOR_DOIS = 200;
	final double PARAMETRO_OLFATO_POUPADOR_TRES = 180;
	final double PARAMETRO_OLFATO_POUPADOR_QUATRO = 160;
	final double PARAMETRO_OLFATO_POUPADOR_CINCO = 130;
	
	final double PARAMETRO_IR_BANCO = 900;
	final double PARAMETRO_ESPACO_VAZIO = 150;

	void atualizarVariaveis() {
		this.num_moedas = this.sensor.getNumeroDeMoedas();
		this.num_imunes = this.sensor.getNumeroJogadasImunes();
		this.posicaoAtual = this.sensor.getPosicao();
		this.visitados[posicaoAtual.x][posicaoAtual.y]++;
		this.visao_agente = this.sensor.getVisaoIdentificacao();
		this.olfato_ladrao = this.sensor.getAmbienteOlfatoLadrao();
		this.olfato_poupador = this.sensor.getAmbienteOlfatoPoupador();

		// resetando probabilidades
		for (int i = 0; i < QUANTIDADE_MOVIMENTOS; i++) {
			pesoLadroes[i] = 0;
			pesoMoedas[i] = 0;
			pesoConhecerMapa[i] = 0;
			pesoIrAoBanco[i] = 0;
			pesoPastilhaPoder[i] = 0;
			pesoLadoOlfatoLadrao[i] = 0;
			pesoLadoOlfatoPoupador[i] = 0;
			pesoPoupador[i] = 0;

			probEscolhaLado[i] = 0.2;
		}

	}

	boolean contem(int[] arr, int value) {
		for (int s : arr) {
			if (s == value) {
				return true;
			}
		}
		return false;
	}

	private int movimentoAleatorio(ArrayList<Integer> opcoes) {
		if (opcoes.size() <= 0) {
//			System.out.println("Sem Opï¿½ï¿½o");
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

	void memorizar() {
		int x = this.posicaoAtual.x;
		int y = this.posicaoAtual.y;

		int xNovo, yNovo;
		Point pontoNovo;

		int bloco;

		for (int i = 0; i < visao_agente.length; i++) {
			bloco = this.visao_agente[i];
			if (bloco == 0 || bloco == 3 || bloco == 4 || bloco == 5 || bloco == 100 || bloco == 110 || bloco == 200
					|| bloco == 210 || bloco == 220 || bloco == 230) {
				xNovo = x + baseCoordenadaVisao[i][0];
				yNovo = y + baseCoordenadaVisao[i][1];
				pontoNovo = new Point(xNovo, yNovo);

				grafo.addEdge(this.posicaoAtual, pontoNovo);

			}
		}
	}

	public void calcularExplorarMapa() {
		int x = this.posicaoAtual.x;
		int y = this.posicaoAtual.y;

		int[] direcoes = new int[4];

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
				pesoConhecerMapa[i + 1] = Math.pow(PARAMETRO_ESPACO_VAZIO, 1) * 3;
			} else if (direcoes[i] != -1) {
				pesoConhecerMapa[i + 1] = Math.pow(PARAMETRO_ESPACO_VAZIO, 1);
			}
		}

//		return indicesMenores.get(aleatorio) + 1;
	}

	private ArrayList<Integer> poupadorNaVisao(int[] arr) {
		ArrayList<Integer> aux = new ArrayList<>();

		for (int i = 0; i < arr.length; i++) {
			if (this.visao_agente[arr[i]] == 100 || this.visao_agente[arr[i]] == 110) {
				if (contem(DIRECAO_NORTE, arr[i])) {
//					aux.addAll(Arrays.asList( SUL, LESTE, OESTE ));
					aux.add(NORTE);
				} else if (contem(DIRECAO_SUL, arr[i])) {
//					aux.addAll(Arrays.asList( NORTE, LESTE, OESTE ));
					aux.add(SUL);

				} else if (contem(DIRECAO_LESTE, arr[i])) {
//					aux.addAll(Arrays.asList( OESTE, NORTE, SUL ));
					aux.add(LESTE);

				} else if (contem(DIRECAO_OESTE, arr[i])) {
//					aux.addAll(Arrays.asList( OESTE, NORTE, SUL ));
					aux.add(OESTE);

				} else if (contem(DIRECAO_NORDESTE, arr[i])) {
//					aux.addAll(Arrays.asList( SUL, OESTE ));
					aux.add(NORTE);
					aux.add(LESTE);

				} else if (contem(DIRECAO_NOROESTE, arr[i])) {
//					aux.addAll(Arrays.asList( SUL, LESTE ));
					aux.add(NORTE);
					aux.add(OESTE);

				} else if (contem(DIRECAO_SUDESTE, arr[i])) {
//					aux.addAll(Arrays.asList( NORTE, OESTE ));
					aux.add(SUL);
					aux.add(LESTE);

				} else if (contem(DIRECAO_SUDOESTE, arr[i])) {
//					aux.addAll(Arrays.asList( NORTE, LESTE ));
					aux.add(SUL);
					aux.add(OESTE);

				}
			}

		}

		return aux;

	}

	public void calcularPoupadorVisao() {
		int indiceAux;
		double pesoIrLado;
		double[] auxPesosPoupador = { 0, 0, 0, 0, 0 };
		int auxNumMoedas = this.num_moedas != 0 ? this.num_moedas : 1;

		ArrayList<Integer> poupador_distUm = poupadorNaVisao(DISTANCIA_UM);
		ArrayList<Integer> poupador_distDois = poupadorNaVisao(DISTANCIA_DOIS);
		ArrayList<Integer> poupador_distTres = poupadorNaVisao(DISTANCIA_TRES);
		ArrayList<Integer> poupador_distQuatro = poupadorNaVisao(DISTANCIA_QUATRO);

		for (int i = 0; i < poupador_distUm.size(); i++) {
			indiceAux = poupador_distUm.get(i);
			if (probEscolhaLado[indiceAux] != 0) {
				pesoIrLado = Math.pow(PARAMETRO_POUPADOR_UM, auxNumMoedas) * -1;

				auxPesosPoupador[indiceAux] = pesoIrLado + auxPesosPoupador[indiceAux];
			}
		}

		for (int i = 0; i < poupador_distDois.size(); i++) {
			indiceAux = poupador_distDois.get(i);
			if (probEscolhaLado[indiceAux] != 0) {

				pesoIrLado = Math.pow(PARAMETRO_POUPADOR_DOIS, auxNumMoedas) * -1;

				auxPesosPoupador[indiceAux] = pesoIrLado + auxPesosPoupador[indiceAux];

			}
		}

		for (int i = 0; i < poupador_distTres.size(); i++) {
			indiceAux = poupador_distTres.get(i);
			if (probEscolhaLado[indiceAux] != 0) {

				pesoIrLado = Math.pow(PARAMETRO_POUPADOR_TRES, auxNumMoedas) * -1;

				auxPesosPoupador[indiceAux] = pesoIrLado + auxPesosPoupador[indiceAux];

			}
		}

		for (int i = 0; i < poupador_distQuatro.size(); i++) {
			indiceAux = poupador_distQuatro.get(i);
			if (probEscolhaLado[indiceAux] != 0) {

				pesoIrLado = Math.pow(PARAMETRO_POUPADOR_QUATRO, auxNumMoedas) * -1;

				auxPesosPoupador[indiceAux] = pesoIrLado + auxPesosPoupador[indiceAux];

			}
		}

		for (int i = 0; i < auxPesosPoupador.length; i++) {
			this.pesoPoupador[i] = auxPesosPoupador[i];
//			System.out.print(pesoLadroes[i] + " ");
		}
//		System.out.println();
//		System.out.println();
	}

	private ArrayList<Integer> ladroesNaVisao(int[] arr) {
		ArrayList<Integer> aux = new ArrayList<>();

		for (int i = 0; i < arr.length; i++) {
			if (this.visao_agente[arr[i]] == 200 || this.visao_agente[arr[i]] == 210 || this.visao_agente[arr[i]] == 220
					|| this.visao_agente[arr[i]] == 230) {
				if (contem(DIRECAO_NORTE, arr[i])) {
//					aux.addAll(Arrays.asList( SUL, LESTE, OESTE ));
					aux.add(NORTE);
				} else if (contem(DIRECAO_SUL, arr[i])) {
//					aux.addAll(Arrays.asList( NORTE, LESTE, OESTE ));
					aux.add(SUL);

				} else if (contem(DIRECAO_LESTE, arr[i])) {
//					aux.addAll(Arrays.asList( OESTE, NORTE, SUL ));
					aux.add(LESTE);

				} else if (contem(DIRECAO_OESTE, arr[i])) {
//					aux.addAll(Arrays.asList( OESTE, NORTE, SUL ));
					aux.add(OESTE);

				} else if (contem(DIRECAO_NORDESTE, arr[i])) {
//					aux.addAll(Arrays.asList( SUL, OESTE ));
					aux.add(NORTE);
					aux.add(LESTE);

				} else if (contem(DIRECAO_NOROESTE, arr[i])) {
//					aux.addAll(Arrays.asList( SUL, LESTE ));
					aux.add(NORTE);
					aux.add(OESTE);

				} else if (contem(DIRECAO_SUDESTE, arr[i])) {
//					aux.addAll(Arrays.asList( NORTE, OESTE ));
					aux.add(SUL);
					aux.add(LESTE);

				} else if (contem(DIRECAO_SUDOESTE, arr[i])) {
//					aux.addAll(Arrays.asList( NORTE, LESTE ));
					aux.add(SUL);
					aux.add(OESTE);

				}
			}

		}

		return aux;

	}

	public void calcularFugaLadroes() {
		ArrayList<Integer> ladrao_distUm = ladroesNaVisao(DISTANCIA_UM);
		// se tiver do lado, já desconsidera ir para lá
		for (int i = 0; i < ladrao_distUm.size(); i++) {
			this.probEscolhaLado[ladrao_distUm.get(i)] = 0;
		}

		int indiceAux;
		double pesoIrLado;
		double[] auxPesosLadroes = { 0, 0, 0, 0, 0 };
		int auxNumMoedas = this.num_moedas != 0 ? this.num_moedas : 1;

		ArrayList<Integer> ladrao_distDois = ladroesNaVisao(DISTANCIA_DOIS);
		ArrayList<Integer> ladrao_distTres = ladroesNaVisao(DISTANCIA_TRES);
		ArrayList<Integer> ladrao_distQuatro = ladroesNaVisao(DISTANCIA_QUATRO);

		// se tiver do lado, já desconsidera ir para lá
		for (int i = 0; i < ladrao_distDois.size(); i++) {
			indiceAux = ladrao_distDois.get(i);
			if (probEscolhaLado[indiceAux] != 0) {

				pesoIrLado = Math.pow(PARAMETRO_LADRAO_DOIS, auxNumMoedas) * -1;

				auxPesosLadroes[indiceAux] = pesoIrLado + auxPesosLadroes[indiceAux];

			}
		}

		for (int i = 0; i < ladrao_distTres.size(); i++) {
			indiceAux = ladrao_distTres.get(i);
			if (probEscolhaLado[indiceAux] != 0) {

				pesoIrLado = Math.pow(PARAMETRO_LADRAO_TRES, auxNumMoedas) * -1;

				auxPesosLadroes[indiceAux] = pesoIrLado + auxPesosLadroes[indiceAux];

			}
		}

		for (int i = 0; i < ladrao_distQuatro.size(); i++) {
			indiceAux = ladrao_distQuatro.get(i);
			if (probEscolhaLado[indiceAux] != 0) {

				pesoIrLado = Math.pow(PARAMETRO_LADRAO_QUATRO, auxNumMoedas) * -1;

				auxPesosLadroes[indiceAux] = pesoIrLado + auxPesosLadroes[indiceAux];

			}
		}

		for (int i = 0; i < auxPesosLadroes.length; i++) {
			this.pesoLadroes[i] = auxPesosLadroes[i];
//			System.out.print(pesoLadroes[i] + " ");
		}
//		System.out.println();
//		System.out.println();
	}

	private ArrayList<Integer> moedasNaVisao(int[] arr) {
		ArrayList<Integer> aux = new ArrayList<>();

		for (int i = 0; i < arr.length; i++) {
			if (this.visao_agente[arr[i]] == 4) {
				if (contem(DIRECAO_NORTE, arr[i])) {
					aux.add(NORTE);
				} else if (contem(DIRECAO_SUL, arr[i])) {
					aux.add(SUL);

				} else if (contem(DIRECAO_LESTE, arr[i])) {
					aux.add(LESTE);

				} else if (contem(DIRECAO_OESTE, arr[i])) {
					aux.add(OESTE);

				} else if (contem(DIRECAO_NORDESTE, arr[i])) {
					aux.add(NORTE);
					aux.add(LESTE);

				} else if (contem(DIRECAO_NOROESTE, arr[i])) {
					aux.add(NORTE);
					aux.add(OESTE);

				} else if (contem(DIRECAO_SUDESTE, arr[i])) {
					aux.add(SUL);
					aux.add(LESTE);

				} else if (contem(DIRECAO_SUDOESTE, arr[i])) {
					aux.add(SUL);
					aux.add(OESTE);
				}
			}
		}

		return aux;

	}

	public void calcularPegarMoedas() {
		if (grafo.temBanco()) {

			ArrayList<Integer> moedas_distUm = moedasNaVisao(DISTANCIA_UM);
			ArrayList<Integer> moedas_distDois = moedasNaVisao(DISTANCIA_DOIS);
			ArrayList<Integer> moedas_distTres = moedasNaVisao(DISTANCIA_TRES);
			ArrayList<Integer> moedas_distQuatro = moedasNaVisao(DISTANCIA_QUATRO);

			int indiceAux;
			double pesoIrLado;
			double[] auxPesosMoedas = { 0, 0, 0, 0, 0 };

			for (int i = 0; i < moedas_distUm.size(); i++) {
				indiceAux = moedas_distUm.get(i);
				pesoIrLado = Math.pow(PARAMETRO_MOEDA_UM, 4);

				auxPesosMoedas[indiceAux] = pesoIrLado + auxPesosMoedas[indiceAux];

			}

			for (int i = 0; i < moedas_distDois.size(); i++) {
				indiceAux = moedas_distDois.get(i);
				pesoIrLado = Math.pow(PARAMETRO_MOEDA_DOIS, 3);

				auxPesosMoedas[indiceAux] = pesoIrLado + auxPesosMoedas[indiceAux];
			}

			for (int i = 0; i < moedas_distTres.size(); i++) {
				indiceAux = moedas_distTres.get(i);
				pesoIrLado = Math.pow(PARAMETRO_MOEDA_TRES, 2);

				auxPesosMoedas[indiceAux] = pesoIrLado + auxPesosMoedas[indiceAux];
			}

			for (int i = 0; i < moedas_distQuatro.size(); i++) {
				indiceAux = moedas_distQuatro.get(i);
				pesoIrLado = Math.pow(PARAMETRO_MOEDA_QUATRO, 1);

				auxPesosMoedas[indiceAux] = pesoIrLado + auxPesosMoedas[indiceAux];
			}

			for (int i = 0; i < auxPesosMoedas.length; i++) {
				this.pesoMoedas[i] = auxPesosMoedas[i];
			}
		}

	}

	public int verificarOlfato(int indice) {
		if (indice == 0) {
			return movimentoAleatorio(new ArrayList<Integer>(Arrays.asList(SUL, LESTE)));
		} else if (indice == 1) {
			return movimentoAleatorio(new ArrayList<Integer>(Arrays.asList(SUL, LESTE, OESTE)));
		} else if (indice == 2) {
			return movimentoAleatorio(new ArrayList<Integer>(Arrays.asList(SUL, OESTE)));
		} else if (indice == 3) {
			return movimentoAleatorio(new ArrayList<Integer>(Arrays.asList(NORTE, SUL, LESTE)));
		} else if (indice == 4) {
			return movimentoAleatorio(new ArrayList<Integer>(Arrays.asList(NORTE, SUL, OESTE)));
		} else if (indice == 5) {
			return movimentoAleatorio(new ArrayList<Integer>(Arrays.asList(NORTE, LESTE)));
		} else if (indice == 6) {
			return movimentoAleatorio(new ArrayList<Integer>(Arrays.asList(NORTE, LESTE, OESTE)));
		} else if (indice == 7) {
			return movimentoAleatorio(new ArrayList<Integer>(Arrays.asList(NORTE, OESTE)));
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

	public ArrayList<Integer> olfatoNaVisao(int[] olfato_agente, int indice) {
		ArrayList<Integer> aux = new ArrayList<Integer>();
		for (int i = 0; i < olfato_agente.length; i++) {
			if (indice == olfato_agente[i] && i == 0) {
				aux = new ArrayList<Integer>(Arrays.asList(NORTE, OESTE));
			} else if (indice == olfato_agente[i] && i == 1) {
				aux = new ArrayList<Integer>(Arrays.asList(NORTE));
			} else if (indice == olfato_agente[i] && i == 2) {
				aux = new ArrayList<Integer>(Arrays.asList(NORTE, LESTE));
			} else if (indice == olfato_agente[i] && i == 3) {
				aux = new ArrayList<Integer>(Arrays.asList(OESTE));
			} else if (indice == olfato_agente[i] && i == 4) {
				aux = new ArrayList<Integer>(Arrays.asList(LESTE));
			} else if (indice == olfato_agente[i] && i == 5) {
				aux = new ArrayList<Integer>(Arrays.asList(SUL, OESTE));
			} else if (indice == olfato_agente[i] && i == 6) {
				aux = new ArrayList<Integer>(Arrays.asList(SUL));
			} else if (indice == olfato_agente[i] && i == 7) {
				aux = new ArrayList<Integer>(Arrays.asList(SUL, LESTE));
			}
		}
		return aux;
	}

	public void calcularOlfatoLadroes() {
		ArrayList<Integer> olfatoUm = olfatoNaVisao(olfato_ladrao, 1);
		ArrayList<Integer> olfatoDois = olfatoNaVisao(olfato_ladrao, 2);
		ArrayList<Integer> olfatoTres = olfatoNaVisao(olfato_ladrao, 3);
		ArrayList<Integer> olfatoQuatro = olfatoNaVisao(olfato_ladrao, 4);
		ArrayList<Integer> olfatoCinco = olfatoNaVisao(olfato_ladrao, 5);

		int indiceAux;
		int auxNumMoeda = this.num_moedas != 0 ? this.num_moedas : 1;

		if (!olfatoUm.isEmpty()) {
			for (int i = 0; i < olfatoUm.size(); i++) {
				indiceAux = olfatoUm.get(i);

				pesoLadoOlfatoLadrao[indiceAux] = Math.pow(PARAMETRO_OLFATO_LADRAO_UM, auxNumMoeda) * -1;
			}
//			return verificarOlfato(selecionarAleatorio(olfatoUm));
		}
		if (!olfatoDois.isEmpty()) {
			for (int i = 0; i < olfatoDois.size(); i++) {
				indiceAux = olfatoDois.get(i);

				pesoLadoOlfatoLadrao[indiceAux] = Math.pow(PARAMETRO_OLFATO_LADRAO_DOIS, auxNumMoeda) * -1;
			}
		}
		if (!olfatoTres.isEmpty()) {
			for (int i = 0; i < olfatoTres.size(); i++) {
				indiceAux = olfatoTres.get(i);

				pesoLadoOlfatoLadrao[indiceAux] = Math.pow(PARAMETRO_OLFATO_LADRAO_TRES, auxNumMoeda) * -1;
			}
		}
		if (!olfatoQuatro.isEmpty()) {
			for (int i = 0; i < olfatoQuatro.size(); i++) {
				indiceAux = olfatoQuatro.get(i);

				pesoLadoOlfatoLadrao[indiceAux] = Math.pow(PARAMETRO_OLFATO_LADRAO_QUATRO, auxNumMoeda) * -1;
			}
		}
		if (!olfatoCinco.isEmpty()) {
			for (int i = 0; i < olfatoCinco.size(); i++) {
				indiceAux = olfatoCinco.get(i);

				pesoLadoOlfatoLadrao[indiceAux] = Math.pow(PARAMETRO_OLFATO_LADRAO_CINCO, auxNumMoeda) * -1;
			}
		}

	}

	public void calcularOlfatoPoupador() {
		ArrayList<Integer> olfatoUm = olfatoNaVisao(olfato_poupador, 1);
		ArrayList<Integer> olfatoDois = olfatoNaVisao(olfato_poupador, 2);
		ArrayList<Integer> olfatoTres = olfatoNaVisao(olfato_poupador, 3);
		ArrayList<Integer> olfatoQuatro = olfatoNaVisao(olfato_poupador, 4);
		ArrayList<Integer> olfatoCinco = olfatoNaVisao(olfato_poupador, 5);

		int indiceAux;
		int auxNumMoeda = this.num_moedas != 0 ? this.num_moedas : 1;

		if (!olfatoUm.isEmpty()) {
			for (int i = 0; i < olfatoUm.size(); i++) {
				indiceAux = olfatoUm.get(i);

				pesoLadoOlfatoLadrao[indiceAux] = Math.pow(PARAMETRO_OLFATO_POUPADOR_UM, auxNumMoeda) * -1;
			}
//			return verificarOlfato(selecionarAleatorio(olfatoUm));
		}
		if (!olfatoDois.isEmpty()) {
			for (int i = 0; i < olfatoDois.size(); i++) {
				indiceAux = olfatoDois.get(i);

				pesoLadoOlfatoLadrao[indiceAux] = Math.pow(PARAMETRO_OLFATO_POUPADOR_DOIS, auxNumMoeda) * -1;
			}
		}
		if (!olfatoTres.isEmpty()) {
			for (int i = 0; i < olfatoTres.size(); i++) {
				indiceAux = olfatoTres.get(i);

				pesoLadoOlfatoLadrao[indiceAux] = Math.pow(PARAMETRO_OLFATO_POUPADOR_TRES, auxNumMoeda) * -1;
			}
		}
		if (!olfatoQuatro.isEmpty()) {
			for (int i = 0; i < olfatoQuatro.size(); i++) {
				indiceAux = olfatoQuatro.get(i);

				pesoLadoOlfatoLadrao[indiceAux] = Math.pow(PARAMETRO_OLFATO_POUPADOR_QUATRO, auxNumMoeda) * -1;
			}
		}
		if (!olfatoCinco.isEmpty()) {
			for (int i = 0; i < olfatoCinco.size(); i++) {
				indiceAux = olfatoCinco.get(i);

				pesoLadoOlfatoLadrao[indiceAux] = Math.pow(PARAMETRO_OLFATO_POUPADOR_CINCO, auxNumMoeda) * -1;
			}
		}

	}

	private int irEmDirecao(int valor, String restricao) {
		if (contem(DIRECAO_NORTE, valor)) {
			if (restricao.equals("moeda")) {
				if (this.visao_agente[7] == 5) {
					return movimentoAleatorio(new ArrayList<Integer>(Arrays.asList(LESTE, OESTE)));
				}
			}
			return NORTE;
		} else if (contem(DIRECAO_SUL, valor)) {
			if (restricao.equals("moeda")) {
				if (this.visao_agente[7] == 16) {
					return movimentoAleatorio(new ArrayList<Integer>(Arrays.asList(LESTE, OESTE)));
				}
			}
			return SUL;
		} else if (contem(DIRECAO_LESTE, valor)) {
			if (restricao.equals("moeda")) {
				if (this.visao_agente[7] == 12) {
					return movimentoAleatorio(new ArrayList<Integer>(Arrays.asList(NORTE, SUL)));
				}
			}
			return LESTE;
		} else if (contem(DIRECAO_OESTE, valor)) {
			if (restricao.equals("moeda")) {
				if (this.visao_agente[7] == 11) {
					return movimentoAleatorio(new ArrayList<Integer>(Arrays.asList(NORTE, SUL)));
				}
			}
			return OESTE;
		} else if (contem(DIRECAO_NORDESTE, valor)) {
			return movimentoAleatorio(new ArrayList<Integer>(Arrays.asList(NORTE, LESTE)));
		} else if (contem(DIRECAO_NOROESTE, valor)) {
			return movimentoAleatorio(new ArrayList<Integer>(Arrays.asList(NORTE, OESTE)));
		} else if (contem(DIRECAO_SUDESTE, valor)) {
			return movimentoAleatorio(new ArrayList<Integer>(Arrays.asList(SUL, LESTE)));
		} else if (contem(DIRECAO_SUDOESTE, valor)) {
			return movimentoAleatorio(new ArrayList<Integer>(Arrays.asList(SUL, OESTE)));
		}

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

			if (valor == 4) {
				return irEmDirecao(escolhidoValor, "moeda");
			}
			return irEmDirecao(escolhidoValor, "");

		}

		return -1;
	}

	public ArrayList<Integer> pastilharPoderNaVisao(int[] arr) {
		ArrayList<Integer> aux = new ArrayList<>();

		for (int i = 0; i < arr.length; i++) {
			if (this.visao_agente[arr[i]] == 5) {
				if (contem(DIRECAO_NORTE, arr[i])) {
					aux.add(NORTE);
				} else if (contem(DIRECAO_SUL, arr[i])) {
					aux.add(SUL);

				} else if (contem(DIRECAO_LESTE, arr[i])) {
					aux.add(LESTE);

				} else if (contem(DIRECAO_OESTE, arr[i])) {
					aux.add(OESTE);

				} else if (contem(DIRECAO_NORDESTE, arr[i])) {
					aux.add(NORTE);
					aux.add(LESTE);

				} else if (contem(DIRECAO_NOROESTE, arr[i])) {
					aux.add(NORTE);
					aux.add(OESTE);

				} else if (contem(DIRECAO_SUDESTE, arr[i])) {
					aux.add(SUL);
					aux.add(LESTE);

				} else if (contem(DIRECAO_SUDOESTE, arr[i])) {
					aux.add(SUL);
					aux.add(OESTE);
				}
			}
		}

		return aux;
	}

	public void calcularPastilhaDoPoder() {
		ArrayList<Integer> pastilhaUm = pastilharPoderNaVisao(DISTANCIA_UM);
		ArrayList<Integer> pastilhaDois = pastilharPoderNaVisao(DISTANCIA_DOIS);
		ArrayList<Integer> pastilhaTres = pastilharPoderNaVisao(DISTANCIA_TRES);
		ArrayList<Integer> pastilhaQuatro = pastilharPoderNaVisao(DISTANCIA_QUATRO);

		boolean temLadroes = false;
		int indiceAux;

		for (int i = 0; i < this.pesoLadroes.length; i++) {
			if (pesoLadroes[i] < 0) {
				temLadroes = true;
			}
		}

		if (this.num_moedas >= 5 && temLadroes) {

			for (int i = 0; i < pastilhaUm.size(); i++) {
				indiceAux = pastilhaUm.get(i);
				if (probEscolhaLado[indiceAux] != 0) {
					pesoPastilhaPoder[indiceAux] = Math.pow(PARAMETRO_PASTILHA_PODER_UM, this.num_moedas);
				}
			}

			for (int i = 0; i < pastilhaDois.size(); i++) {
				indiceAux = pastilhaDois.get(i);
				if (probEscolhaLado[indiceAux] != 0) {
					pesoPastilhaPoder[indiceAux] = Math.pow(PARAMETRO_PASTILHA_PODER_DOIS, this.num_moedas);
				}
			}

			for (int i = 0; i < pastilhaTres.size(); i++) {
				indiceAux = pastilhaTres.get(i);
				if (probEscolhaLado[indiceAux] != 0) {
					pesoPastilhaPoder[indiceAux] = Math.pow(PARAMETRO_PASTILHA_PODER_TRES, this.num_moedas);

				}
			}

			for (int i = 0; i < pastilhaQuatro.size(); i++) {
				indiceAux = pastilhaQuatro.get(i);
				if (probEscolhaLado[indiceAux] != 0) {

					pesoPastilhaPoder[indiceAux] = Math.pow(PARAMETRO_PASTILHA_PODER_QUATRO, this.num_moedas);
				}
			}

		} else {
			for (int i = 0; i < pastilhaUm.size(); i++) {
				indiceAux = pastilhaUm.get(i);
				if (probEscolhaLado[indiceAux] != 0) {
					probEscolhaLado[indiceAux] = 0;
				}
			}

			for (int i = 0; i < pastilhaDois.size(); i++) {
				indiceAux = pastilhaDois.get(i);
				if (probEscolhaLado[indiceAux] != 0) {
					pesoPastilhaPoder[indiceAux] = Math.pow(PARAMETRO_PASTILHA_PODER_SEM_MONEY, 2) * -1;
				}
			}

			for (int i = 0; i < pastilhaTres.size(); i++) {
				indiceAux = pastilhaTres.get(i);
				if (probEscolhaLado[indiceAux] != 0) {
					pesoPastilhaPoder[indiceAux] = Math.pow(PARAMETRO_PASTILHA_PODER_SEM_MONEY, 1.5) * -1;
				}
			}

			for (int i = 0; i < pastilhaQuatro.size(); i++) {
				indiceAux = pastilhaQuatro.get(i);
				if (probEscolhaLado[indiceAux] != 0) {
					pesoPastilhaPoder[indiceAux] = Math.pow(PARAMETRO_PASTILHA_PODER_SEM_MONEY, 1) * -1;
				}
			}
		}
	}

	private boolean medo() {
		double fear = Math.pow(1.2, this.sensor.getNumeroDeMoedas());
		Random r = new Random();
		int randomInt = r.nextInt(100) + 1;
		return (randomInt < fear);
	}

	public void calcularIrAoBanco() {

		if (grafo.temBanco()) {
			int vendoBanco = seAproximar(3);

			if (vendoBanco != -1) {
				if (vendoBanco == 4) {
					pesoIrAoBanco[4] = Math.pow(PARAMETRO_IR_BANCO, this.num_moedas);
				}

				if (vendoBanco == 3) {
					pesoIrAoBanco[3] = Math.pow(PARAMETRO_IR_BANCO, this.num_moedas);
				}

				if (vendoBanco == 1) {
					pesoIrAoBanco[1] = Math.pow(PARAMETRO_IR_BANCO, this.num_moedas);
				}

				if (vendoBanco == 2) {
					pesoIrAoBanco[2] = Math.pow(PARAMETRO_IR_BANCO, this.num_moedas);
				}
				return;
			}
			LinkedList<Point> visitadosGrafo = new LinkedList<>();
			LinkedList<Coordenada> queue = new LinkedList<>();

			ArrayList<Point> movimentos = new ArrayList<>();

			Coordenada aux;
			Coordenada fim;

			queue.add(new Coordenada(posicaoAtual));
			visitadosGrafo.add(posicaoAtual);

			while (!queue.isEmpty()) {
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

				if (x - xSeq > 0) {
					pesoIrAoBanco[4] = Math.pow(PARAMETRO_IR_BANCO, this.num_moedas);
				}

				if (x - xSeq < 0) {
					pesoIrAoBanco[3] = Math.pow(PARAMETRO_IR_BANCO, this.num_moedas);
				}

				if (y - ySeq > 0) {
					pesoIrAoBanco[1] = Math.pow(PARAMETRO_IR_BANCO, this.num_moedas);
				}

				if (y - ySeq < 0) {
					pesoIrAoBanco[2] = Math.pow(PARAMETRO_IR_BANCO, this.num_moedas);
				}

			}
		}

	}

	private int distanciaManhattan(int x1, int y1, int x0, int y0) {
		return Math.abs(x1 - x0) + Math.abs(y1 - y0);
	}

	private boolean estaLooping() {
		return (ultimosPassos.get(0).equals(ultimosPassos.get(2)) && ultimosPassos.get(1).equals(ultimosPassos.get(3))
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

	public void transformarEmProbabilidade() {
		double[] probAux = { 0, 0, 0, 0, 0 };

		double[] auxPesos = { 0, 0, 0, 0, 0 };
		double somaAuxPesos = 0;
		double menorValorPesos = 0;

		// Tem paredes ou fora do ambiente
		for (int i = 0; i < DISTANCIA_UM.length; i++) {
			if (this.visao_agente[DISTANCIA_UM[i]] == 1 || this.visao_agente[DISTANCIA_UM[i]] == -1) {
				// +1 para ficar norte, sul, leste, oeste já que dist tem 4 de tamanho
				this.probEscolhaLado[i + 1] = 0;
			}
		}

		// ladroes
		for (int i = 0; i < QUANTIDADE_MOVIMENTOS; i++) {
			if (this.probEscolhaLado[i] != 0 && this.num_imunes <= 2) {
				auxPesos[i] += pesoLadroes[i];

			}
		}

		// moedas
		for (int i = 0; i < QUANTIDADE_MOVIMENTOS; i++) {
			if (this.probEscolhaLado[i] != 0) {
				auxPesos[i] += pesoMoedas[i];
			}
		}

		// conhecer mapa
		for (int i = 0; i < QUANTIDADE_MOVIMENTOS; i++) {
			if (this.probEscolhaLado[i] != 0) {
				auxPesos[i] += pesoConhecerMapa[i];
			}
		}

		// ir ao banco
		for (int i = 0; i < QUANTIDADE_MOVIMENTOS; i++) {
			if (this.probEscolhaLado[i] != 0) {
				auxPesos[i] += pesoIrAoBanco[i];
			}
		}

		// peso olfato do ladrao
		for (int i = 0; i < QUANTIDADE_MOVIMENTOS; i++) {
			if (this.probEscolhaLado[i] != 0 && this.num_imunes <= 2) {
				auxPesos[i] += pesoLadoOlfatoLadrao[i];
			}
		}

		// peso olfato do outro poupador
		for (int i = 0; i < QUANTIDADE_MOVIMENTOS; i++) {
			if (this.probEscolhaLado[i] != 0) {
				auxPesos[i] += pesoLadoOlfatoPoupador[i];
			}
		}
		// pastilha do poder
		for (int i = 0; i < QUANTIDADE_MOVIMENTOS; i++) {
			if (this.probEscolhaLado[i] != 0) {
				auxPesos[i] += pesoPastilhaPoder[i];
			}
		}

		// somando todos os pesos das direções
		for (int i = 0; i < QUANTIDADE_MOVIMENTOS; i++) {
			if (this.probEscolhaLado[i] != 0) {
				somaAuxPesos += Math.abs(auxPesos[i]);
				if (auxPesos[i] < menorValorPesos) {
					menorValorPesos = pesoLadroes[i] + pesoPoupador[i] + pesoLadoOlfatoLadrao[i]
							+ pesoLadoOlfatoPoupador[i];
					if (pesoPastilhaPoder[i] < 0) {
						menorValorPesos += pesoPastilhaPoder[i];
					}
				}
			}
		}

		double somaProbAux = 0;
		// transformando tudo em probabilidade
		for (int i = 1; i < QUANTIDADE_MOVIMENTOS; i++) {
			if (this.probEscolhaLado[i] != 0) {
//				if (auxPesos[i] >= 0) {
//					probAux[i] = (auxPesos[i] / somaAuxPesos);
//				} else {
//					probAux[i] = ((auxPesos[i] * -1) / somaAuxPesos);
//				}
				probAux[i] = (auxPesos[i] + menorValorPesos * -1) / somaAuxPesos;
			}
//			System.out.print(probAux[i] + " ");
			somaProbAux += probAux[i];
		}
//		System.out.println();
//		System.out.println();

		// formatando probs para quando tudo somado, dê 1
		for (int i = 0; i < QUANTIDADE_MOVIMENTOS; i++) {
			if (somaProbAux != 0) {
				this.probEscolhaLado[i] = probAux[i] / somaProbAux;
			}
			System.out.print(probEscolhaLado[i] + " ");
		}
		System.out.println();
		System.out.println();

	}

	public int roletaRussa() {
		double probParado = this.probEscolhaLado[0];
		double probNorte = this.probEscolhaLado[1];
		double probSul = this.probEscolhaLado[2];
		double probLeste = this.probEscolhaLado[3];
		double probOeste = this.probEscolhaLado[4];

		double numRandomico = new Random().nextDouble();
//		System.out.println(numRandomico);
//		System.out.println();
		int escolha = 0;

		if (numRandomico <= probParado) {
			escolha = 0;
		} else if (numRandomico <= (probParado + probNorte)) {
			escolha = 1;
		} else if (numRandomico <= (probParado + probNorte + probSul)) {
			escolha = 2;
		} else if (numRandomico <= (probParado + probNorte + probSul + probLeste)) {
			escolha = 3;
		} else if (numRandomico <= (probParado + probNorte + probSul + probLeste + probOeste)) {
			escolha = 4;
		}

		return escolha;

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
		calcularFugaLadroes();
		calcularPoupadorVisao();
		calcularOlfatoLadroes();
		calcularOlfatoPoupador();
		calcularPegarMoedas();
		calcularExplorarMapa();
		calcularIrAoBanco();
		// calcularPastilhaDoPoder tem que estar depois de fugaLadroes e olfato, pois é
		// baseado neles
		calcularPastilhaDoPoder();
		transformarEmProbabilidade();
//		int comando = pensarMovimento();
		int comando = roletaRussa();
		atualizarUltimosPassos(comando);
		return comando;
	}
}