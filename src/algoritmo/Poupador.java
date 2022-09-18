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

	int PARADO = 0, NORTE = 1, SUL = 2, LESTE = 3, OESTE = 4;

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

	final int PAREDE = 1;
	final int BANCO = 3;
	final int MOEDA = 4;

	int num_moedas_total = 0;
	int num_moedas;
	int num_moedas_banco;
	int num_imunes;
	int[] visao_agente;
	int[] olfato_ladrao;
	int[] olfato_poupador;
	int[][] visitados = new int[31][31];

	int quantVezesRoubadoAnterior = 0;
	int quantVezesRoubado = 0;
	int quantTempoSemSerRoubadoAnterior = 0;
	int quantTempoSemSerRoubado = 0;
	int quantTempoSemVerLadraoAnterior = 0;
	int quantTempoSemVerLadrao = 0;

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

	double PARAMETRO_LADRAO_UM = 500000;
	double PARAMETRO_LADRAO_DOIS = 150000;
	double PARAMETRO_LADRAO_TRES = 25000;
	double PARAMETRO_LADRAO_QUATRO = 12000;

	double PARAMETRO_POUPADOR_UM = 80000;
	double PARAMETRO_POUPADOR_DOIS = 20000;
	double PARAMETRO_POUPADOR_TRES = 10000;
	double PARAMETRO_POUPADOR_QUATRO = 5000;

	double PARAMETRO_MOEDA_UM = 3000;
	double PARAMETRO_MOEDA_DOIS = 1300;
	double PARAMETRO_MOEDA_TRES = 700;
	double PARAMETRO_MOEDA_QUATRO = 500;

	double PARAMETRO_PASTILHA_PODER_UM = 50000;
	double PARAMETRO_PASTILHA_PODER_DOIS = 20000;
	double PARAMETRO_PASTILHA_PODER_TRES = 10000;
	double PARAMETRO_PASTILHA_PODER_QUATRO = 2000;

	double PARAMETRO_PASTILHA_PODER_SEM_MONEY = 600;

	double PARAMETRO_OLFATO_LADRAO_UM = 80000;
	double PARAMETRO_OLFATO_LADRAO_DOIS = 40000;
	double PARAMETRO_OLFATO_LADRAO_TRES = 20000;
	double PARAMETRO_OLFATO_LADRAO_QUATRO = 15000;
	double PARAMETRO_OLFATO_LADRAO_CINCO = 10000;

	double PARAMETRO_OLFATO_POUPADOR_UM = 40;
	double PARAMETRO_OLFATO_POUPADOR_DOIS = 20;
	double PARAMETRO_OLFATO_POUPADOR_TRES = 10;
	double PARAMETRO_OLFATO_POUPADOR_QUATRO = 5;
	double PARAMETRO_OLFATO_POUPADOR_CINCO = 2;

	double PARAMETRO_IR_BANCO = 5;
	double PARAMETRO_ESPACO_VAZIO = 3;
	double PARAMETRO_ESPACO_VAZIO_NOVO = 300;

	void atualizarVariaveis() {
		this.num_moedas = this.sensor.getNumeroDeMoedas();
		this.num_moedas_banco = this.sensor.getNumeroDeMoedasBanco();
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

		this.quantTempoSemSerRoubadoAnterior = this.quantTempoSemSerRoubado;
		this.quantVezesRoubadoAnterior = this.quantVezesRoubado;

		if (this.num_moedas_total > (num_moedas + num_moedas_banco)) {
			this.quantVezesRoubado++;
			this.quantTempoSemSerRoubado = 0;
			System.out.println("ROUBADO");
		} else {
			this.quantTempoSemSerRoubado++;
		}

		this.num_moedas_total = num_moedas + num_moedas_banco;

	}

	void ajustarParametros() {
		if (quantVezesRoubado > quantVezesRoubadoAnterior) {
			PARAMETRO_LADRAO_UM *= 1.1;
			PARAMETRO_LADRAO_DOIS *= 1.1;
			PARAMETRO_LADRAO_TRES *= 1.1;
			PARAMETRO_LADRAO_QUATRO *= 1.1;

			PARAMETRO_OLFATO_LADRAO_UM *= 1.1;
			PARAMETRO_OLFATO_LADRAO_DOIS *= 1.1;
			PARAMETRO_OLFATO_LADRAO_TRES *= 1.1;
			PARAMETRO_OLFATO_LADRAO_QUATRO *= 1.1;
			PARAMETRO_OLFATO_LADRAO_CINCO *= 1.1;

			PARAMETRO_IR_BANCO *= 1.2;

			PARAMETRO_PASTILHA_PODER_UM *= 1.1;
			PARAMETRO_PASTILHA_PODER_DOIS *= 1.1;
			PARAMETRO_PASTILHA_PODER_TRES *= 1.1;
			PARAMETRO_PASTILHA_PODER_QUATRO *= 1.1;

			PARAMETRO_POUPADOR_UM *= 1.1;
			PARAMETRO_POUPADOR_DOIS *= 1.1;
			PARAMETRO_POUPADOR_TRES *= 1.1;
			PARAMETRO_POUPADOR_QUATRO *= 1.1;

		}

		if (quantTempoSemSerRoubado > quantTempoSemSerRoubadoAnterior) {
			PARAMETRO_ESPACO_VAZIO *= 1.002;
			PARAMETRO_ESPACO_VAZIO_NOVO *= 1.002;

			PARAMETRO_MOEDA_UM *= 1.002;
			PARAMETRO_MOEDA_DOIS *= 1.002;
			PARAMETRO_MOEDA_TRES *= 1.002;
			PARAMETRO_MOEDA_QUATRO *= 1.002;

			PARAMETRO_IR_BANCO *= 0.999;

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
		int x = (int) this.posicaoAtual.x;
		int y = (int) this.posicaoAtual.y;

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

		for (int i = 0; i < direcoes.length; i++) {
//			System.out.print(direcoes[i] +" ");
			if (direcoes[i] < menor && direcoes[i] != -1) {
				menor = direcoes[i];
			}
		}
//		System.out.println();
		for (int i = 0; i < direcoes.length; i++) {
			if (direcoes[i] == menor && direcoes[i] != -1) {
//				System.out.println("DIRECAO I: "+ i + " MENOR:" + direcoes[i]);
				pesoConhecerMapa[i + 1] = Math.pow(PARAMETRO_ESPACO_VAZIO_NOVO, 1);
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
		// int auxNumMoedas = this.num_moedas != 0 ? this.num_moedas : 1;

		ArrayList<Integer> poupador_distUm = poupadorNaVisao(DISTANCIA_UM);
		ArrayList<Integer> poupador_distDois = poupadorNaVisao(DISTANCIA_DOIS);
		ArrayList<Integer> poupador_distTres = poupadorNaVisao(DISTANCIA_TRES);
		ArrayList<Integer> poupador_distQuatro = poupadorNaVisao(DISTANCIA_QUATRO);

		for (int i = 0; i < poupador_distUm.size(); i++) {
			indiceAux = poupador_distUm.get(i);
			pesoIrLado = Math.pow(PARAMETRO_POUPADOR_UM, 1) * -1;

			auxPesosPoupador[indiceAux] = pesoIrLado + auxPesosPoupador[indiceAux];

		}

		for (int i = 0; i < poupador_distDois.size(); i++) {
			indiceAux = poupador_distDois.get(i);

			pesoIrLado = Math.pow(PARAMETRO_POUPADOR_DOIS, 1) * -1;

			auxPesosPoupador[indiceAux] = pesoIrLado + auxPesosPoupador[indiceAux];

		}

		for (int i = 0; i < poupador_distTres.size(); i++) {
			indiceAux = poupador_distTres.get(i);

			pesoIrLado = Math.pow(PARAMETRO_POUPADOR_TRES, 1) * -1;

			auxPesosPoupador[indiceAux] = pesoIrLado + auxPesosPoupador[indiceAux];

		}

		for (int i = 0; i < poupador_distQuatro.size(); i++) {
			indiceAux = poupador_distQuatro.get(i);

			pesoIrLado = Math.pow(PARAMETRO_POUPADOR_QUATRO, 1) * -1;

			auxPesosPoupador[indiceAux] = pesoIrLado + auxPesosPoupador[indiceAux];

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
		int indiceAux;
		double pesoIrLado;
		double[] auxPesosLadroes = { 0, 0, 0, 0, 0 };

		ArrayList<Integer> ladrao_distUm = ladroesNaVisao(DISTANCIA_UM);
		// se tiver do lado, já desconsidera ir para lá
		for (int i = 0; i < ladrao_distUm.size(); i++) {
			indiceAux = ladrao_distUm.get(i);
			this.probEscolhaLado[ladrao_distUm.get(i)] = 0;

			// auxiliar para quando for fazer o calculo da pastilha
			pesoIrLado = Math.pow(PARAMETRO_LADRAO_UM, 1) * -1;
			auxPesosLadroes[indiceAux] = pesoIrLado + auxPesosLadroes[indiceAux];
		}

		// int auxNumMoedas = this.num_moedas != 0 ? this.num_moedas : 1;

		ArrayList<Integer> ladrao_distDois = ladroesNaVisao(DISTANCIA_DOIS);
		ArrayList<Integer> ladrao_distTres = ladroesNaVisao(DISTANCIA_TRES);
		ArrayList<Integer> ladrao_distQuatro = ladroesNaVisao(DISTANCIA_QUATRO);

		// se tiver do lado, já desconsidera ir para lá
		for (int i = 0; i < ladrao_distDois.size(); i++) {
			indiceAux = ladrao_distDois.get(i);

			pesoIrLado = Math.pow(PARAMETRO_LADRAO_DOIS, 1) * -1;

			auxPesosLadroes[indiceAux] = pesoIrLado + auxPesosLadroes[indiceAux];

		}

		for (int i = 0; i < ladrao_distTres.size(); i++) {
			indiceAux = ladrao_distTres.get(i);

			pesoIrLado = Math.pow(PARAMETRO_LADRAO_TRES, 1) * -1;

			auxPesosLadroes[indiceAux] = pesoIrLado + auxPesosLadroes[indiceAux];

		}

		for (int i = 0; i < ladrao_distQuatro.size(); i++) {
			indiceAux = ladrao_distQuatro.get(i);

			pesoIrLado = Math.pow(PARAMETRO_LADRAO_QUATRO, 1) * -1;

			auxPesosLadroes[indiceAux] = pesoIrLado + auxPesosLadroes[indiceAux];

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
				pesoIrLado = Math.pow(PARAMETRO_MOEDA_UM, 1);

				auxPesosMoedas[indiceAux] = pesoIrLado + auxPesosMoedas[indiceAux];

			}

			for (int i = 0; i < moedas_distDois.size(); i++) {
				indiceAux = moedas_distDois.get(i);
				pesoIrLado = Math.pow(PARAMETRO_MOEDA_DOIS, 1);

				auxPesosMoedas[indiceAux] = pesoIrLado + auxPesosMoedas[indiceAux];
			}

			for (int i = 0; i < moedas_distTres.size(); i++) {
				indiceAux = moedas_distTres.get(i);
				pesoIrLado = Math.pow(PARAMETRO_MOEDA_TRES, 1);

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
		// int auxNumMoeda = this.num_moedas != 0 ? this.num_moedas : 1;

		if (!olfatoUm.isEmpty()) {
			for (int i = 0; i < olfatoUm.size(); i++) {
				indiceAux = olfatoUm.get(i);

				pesoLadoOlfatoLadrao[indiceAux] = Math.pow(PARAMETRO_OLFATO_LADRAO_UM, 1) * -1;
			}
//			return verificarOlfato(selecionarAleatorio(olfatoUm));
		}
		if (!olfatoDois.isEmpty()) {
			for (int i = 0; i < olfatoDois.size(); i++) {
				indiceAux = olfatoDois.get(i);

				pesoLadoOlfatoLadrao[indiceAux] = Math.pow(PARAMETRO_OLFATO_LADRAO_DOIS, 1) * -1;
			}
		}
		if (!olfatoTres.isEmpty()) {
			for (int i = 0; i < olfatoTres.size(); i++) {
				indiceAux = olfatoTres.get(i);

				pesoLadoOlfatoLadrao[indiceAux] = Math.pow(PARAMETRO_OLFATO_LADRAO_TRES, 1) * -1;
			}
		}
		if (!olfatoQuatro.isEmpty()) {
			for (int i = 0; i < olfatoQuatro.size(); i++) {
				indiceAux = olfatoQuatro.get(i);

				pesoLadoOlfatoLadrao[indiceAux] = Math.pow(PARAMETRO_OLFATO_LADRAO_QUATRO, 1) * -1;
			}
		}
		if (!olfatoCinco.isEmpty()) {
			for (int i = 0; i < olfatoCinco.size(); i++) {
				indiceAux = olfatoCinco.get(i);

				pesoLadoOlfatoLadrao[indiceAux] = Math.pow(PARAMETRO_OLFATO_LADRAO_CINCO, 1) * -1;
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
		// int auxNumMoeda = this.num_moedas != 0 ? this.num_moedas : 1;

		if (!olfatoUm.isEmpty()) {
			for (int i = 0; i < olfatoUm.size(); i++) {
				indiceAux = olfatoUm.get(i);

				pesoLadoOlfatoLadrao[indiceAux] = Math.pow(PARAMETRO_OLFATO_POUPADOR_UM, 1) * -1;
			}
//			return verificarOlfato(selecionarAleatorio(olfatoUm));
		}
		if (!olfatoDois.isEmpty()) {
			for (int i = 0; i < olfatoDois.size(); i++) {
				indiceAux = olfatoDois.get(i);

				pesoLadoOlfatoLadrao[indiceAux] = Math.pow(PARAMETRO_OLFATO_POUPADOR_DOIS, 1) * -1;
			}
		}
		if (!olfatoTres.isEmpty()) {
			for (int i = 0; i < olfatoTres.size(); i++) {
				indiceAux = olfatoTres.get(i);

				pesoLadoOlfatoLadrao[indiceAux] = Math.pow(PARAMETRO_OLFATO_POUPADOR_TRES, 1) * -1;
			}
		}
		if (!olfatoQuatro.isEmpty()) {
			for (int i = 0; i < olfatoQuatro.size(); i++) {
				indiceAux = olfatoQuatro.get(i);

				pesoLadoOlfatoLadrao[indiceAux] = Math.pow(PARAMETRO_OLFATO_POUPADOR_QUATRO, 1) * -1;
			}
		}
		if (!olfatoCinco.isEmpty()) {
			for (int i = 0; i < olfatoCinco.size(); i++) {
				indiceAux = olfatoCinco.get(i);

				pesoLadoOlfatoLadrao[indiceAux] = Math.pow(PARAMETRO_OLFATO_POUPADOR_CINCO, 1) * -1;
			}
		}

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

		if (this.num_moedas >= 5 && temLadroes && this.num_imunes <= 0) {

			for (int i = 0; i < pastilhaUm.size(); i++) {
				indiceAux = pastilhaUm.get(i);
				if (probEscolhaLado[indiceAux] != 0) {
					pesoPastilhaPoder[indiceAux] = Math.pow(PARAMETRO_PASTILHA_PODER_UM, 1);
				}
			}

			for (int i = 0; i < pastilhaDois.size(); i++) {
				indiceAux = pastilhaDois.get(i);
				if (probEscolhaLado[indiceAux] != 0) {
					pesoPastilhaPoder[indiceAux] = Math.pow(PARAMETRO_PASTILHA_PODER_DOIS, 1);
				}
			}

			for (int i = 0; i < pastilhaTres.size(); i++) {
				indiceAux = pastilhaTres.get(i);
				if (probEscolhaLado[indiceAux] != 0) {
					pesoPastilhaPoder[indiceAux] = Math.pow(PARAMETRO_PASTILHA_PODER_TRES, 1);

				}
			}

			for (int i = 0; i < pastilhaQuatro.size(); i++) {
				indiceAux = pastilhaQuatro.get(i);
				if (probEscolhaLado[indiceAux] != 0) {

					pesoPastilhaPoder[indiceAux] = Math.pow(PARAMETRO_PASTILHA_PODER_QUATRO, 1);
				}
			}

		} else {
			for (int i = 0; i < pastilhaUm.size(); i++) {
				indiceAux = pastilhaUm.get(i);
				if (probEscolhaLado[indiceAux] != 0) {
					probEscolhaLado[indiceAux] = 0;
				}
			}

//			for (int i = 0; i < pastilhaDois.size(); i++) {
//				indiceAux = pastilhaDois.get(i);
//				if (probEscolhaLado[indiceAux] != 0) {
//					pesoPastilhaPoder[indiceAux] = PARAMETRO_PASTILHA_PODER_SEM_MONEY * -1;
//				}
//			}
//			for (int i = 0; i < pastilhaTres.size(); i++) {
//				indiceAux = pastilhaTres.get(i);
//				if (probEscolhaLado[indiceAux] != 0) {
//					pesoPastilhaPoder[indiceAux] = Math.pow(PARAMETRO_PASTILHA_PODER_SEM_MONEY, 1.5) * -1;
//				}
//			}
//
//			for (int i = 0; i < pastilhaQuatro.size(); i++) {
//				indiceAux = pastilhaQuatro.get(i);
//				if (probEscolhaLado[indiceAux] != 0) {
//					pesoPastilhaPoder[indiceAux] = Math.pow(PARAMETRO_PASTILHA_PODER_SEM_MONEY, 1) * -1;
//				}
//			}
		}
	}

	// Essa função era usada pra outras coisas (como moedas, por isso permaneceu
	// assim)
	private ArrayList<Integer> irEmDirecao(int valor) {
		if (contem(DIRECAO_NORTE, valor)) {
			return new ArrayList<Integer>(Arrays.asList(NORTE));
		} else if (contem(DIRECAO_SUL, valor)) {
			return new ArrayList<Integer>(Arrays.asList(SUL));
		} else if (contem(DIRECAO_LESTE, valor)) {
			return new ArrayList<Integer>(Arrays.asList(LESTE));
		} else if (contem(DIRECAO_OESTE, valor)) {
			return new ArrayList<Integer>(Arrays.asList(OESTE));
		} else if (contem(DIRECAO_NORDESTE, valor)) {
			return new ArrayList<Integer>(Arrays.asList(NORTE, LESTE));
		} else if (contem(DIRECAO_NOROESTE, valor)) {
			return new ArrayList<Integer>(Arrays.asList(NORTE, OESTE));
		} else if (contem(DIRECAO_SUDESTE, valor)) {
			return new ArrayList<Integer>(Arrays.asList(SUL, LESTE));
		} else if (contem(DIRECAO_SUDOESTE, valor)) {
			return new ArrayList<Integer>(Arrays.asList(SUL, OESTE));
		}

		return null;
	}

	// Essa função era usada pra outras coisas (como moedas, por isso permaneceu
	// assim)
	ArrayList<Integer> seAproximar(int valor) {

		int posicaoMatriz = -1;
		for (int i = 0; i < this.visao_agente.length; i++) {
			if (this.visao_agente[i] == valor) {
				posicaoMatriz = i;

			}
		}

		if (posicaoMatriz != -1) {
			return irEmDirecao(posicaoMatriz);
		}
		return null;
	}

	public void calcularIrAoBanco() {

		if (grafo.temBanco()) {
//			System.out.println("TEM BANCO");
			ArrayList<Integer> vendoBanco = seAproximar(3);
			if (vendoBanco != null) {
				for (int i = 0; i < vendoBanco.size(); i++) {
					if (vendoBanco.get(i) == 4) {
						pesoIrAoBanco[4] = Math.pow(PARAMETRO_IR_BANCO, this.num_moedas);
					}

					if (vendoBanco.get(i) == 3) {
						pesoIrAoBanco[3] = Math.pow(PARAMETRO_IR_BANCO, this.num_moedas);
					}

					if (vendoBanco.get(i) == 1) {
						pesoIrAoBanco[1] = Math.pow(PARAMETRO_IR_BANCO, this.num_moedas);
					}

					if (vendoBanco.get(i) == 2) {
						pesoIrAoBanco[2] = Math.pow(PARAMETRO_IR_BANCO, this.num_moedas);
					}
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
				int x = this.posicaoAtual.x;
				int y = this.posicaoAtual.y;
				Point movimentoSequente = movimentos.get(movimentos.size() - 1);
				int xSeq = movimentoSequente.x;
				int ySeq = movimentoSequente.y;

//				System.out.println(x + " " + y);
//				System.out.println(xSeq + " " + ySeq);

				if (x > xSeq) {
					pesoIrAoBanco[4] = Math.pow(PARAMETRO_IR_BANCO, this.num_moedas);
				}

				if (x < xSeq) {
					pesoIrAoBanco[3] = Math.pow(PARAMETRO_IR_BANCO, this.num_moedas);
				}

				if (y > ySeq) {
					pesoIrAoBanco[1] = Math.pow(PARAMETRO_IR_BANCO, this.num_moedas);
				}

				if (y < ySeq) {
					pesoIrAoBanco[2] = Math.pow(PARAMETRO_IR_BANCO, this.num_moedas);
				}

			}
		}

	}
	
	void analisarFicarParado() {
		// QUANDO TIVER ENCURRALADO, É MELHOR FICAR PARADO
		int contNegativos = 0;
		int contParedesOuSemVisao = 0;
		double menor = Integer.MAX_VALUE;
		
		for (int i = 0; i < QUANTIDADE_MOVIMENTOS; i++) {
			if (pesoLadroes[i] < menor) {
				menor = pesoLadroes[i];
			}
		}	
		
		for (int i = 0; i < QUANTIDADE_MOVIMENTOS; i++) {
			if (pesoLadroes[i] == menor) {
				contNegativos++;
			}
		}
		
		for (int i = 0; i < DISTANCIA_UM.length; i++) {
			if (this.visao_agente[DISTANCIA_UM[i]] == 1 || this.visao_agente[DISTANCIA_UM[i]] == -1) {
				contParedesOuSemVisao++;
			}
		}
		
		if (contNegativos == 2 && contParedesOuSemVisao == 2) {
			probEscolhaLado[0] = 4;
		}
		
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
			if (this.probEscolhaLado[i] != 0) {
				auxPesos[i] += pesoLadroes[i] / (Math.pow(2, this.num_imunes));

			}
		}

		// moedas
		for (int i = 0; i < QUANTIDADE_MOVIMENTOS; i++) {
			if (this.probEscolhaLado[i] != 0) {
				auxPesos[i] += pesoMoedas[i] * (Math.pow(2, this.num_imunes));
			}
		}

		// conhecer mapa
		for (int i = 0; i < QUANTIDADE_MOVIMENTOS; i++) {
			if (this.probEscolhaLado[i] != 0) {
				auxPesos[i] += pesoConhecerMapa[i] * (Math.pow(2, this.num_imunes));
			}
		}

		// ir ao banco
		for (int i = 0; i < QUANTIDADE_MOVIMENTOS; i++) {
			if (this.probEscolhaLado[i] != 0) {
				auxPesos[i] += pesoIrAoBanco[i] * (Math.pow(2, this.num_imunes));
			}
		}

		// peso olfato do ladrao
		for (int i = 0; i < QUANTIDADE_MOVIMENTOS; i++) {
			if (this.probEscolhaLado[i] != 0) {

				auxPesos[i] += pesoLadoOlfatoLadrao[i] / (Math.pow(2, this.num_imunes));

			}
		}

		// peso olfato do poupador
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
		
		// peso poupador
		for (int i = 0; i < QUANTIDADE_MOVIMENTOS; i++) {
			if (this.probEscolhaLado[i] != 0) {
				auxPesos[i] += pesoPoupador[i];
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
			System.out.print(auxPesos[i] + " ");

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
		System.out.println();
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

		memorizar();
		ajustarParametros();

//		for (int i = 0; i < this.visao_agente.length; i++) {
//			System.out.print(this.visao_agente[i] + " ");
//		}
//		System.out.println();
//		for (int i = 0; i < visitados.length; i++) {
//			for (int j = 0; j < this.visitados[i].length; j++) {
//				System.out.print(this.visitados[j][i] + " ");
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
		
		analisarFicarParado();
		
		transformarEmProbabilidade();
		int comando = roletaRussa();
		// atualizarUltimosPassos(comando);
		return comando;
	}
}