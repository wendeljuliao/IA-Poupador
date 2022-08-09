package algoritmo;

import java.util.ArrayList;
import java.util.Random;

public class Poupador extends ProgramaPoupador {
	
	final int NORTE = 1;
	final int SUL = 2;
	final int LESTE = 3;
	final int OESTE = 4;
	final int NORDESTE = new Random().nextBoolean() ? NORTE : LESTE;
	final int NOROESTE = new Random().nextBoolean() ? NORTE : OESTE;
	final int SUDESTE = new Random().nextBoolean() ? SUL : LESTE;
	final int SUDOESTE = new Random().nextBoolean() ? SUL : OESTE;
	
	final int[] DIRECAO_NORTE = {2, 7};
	final int[] DIRECAO_SUL = {16, 21};
	final int[] DIRECAO_LESTE = {12, 13};
	final int[] DIRECAO_OESTE = {10, 11};
	final int[] DIRECAO_NORDESTE = {3, 4, 8, 9};
	final int[] DIRECAO_NOROESTE = {0, 1, 5, 6};
	final int[] DIRECAO_SUDESTE = {17, 18, 22, 23};
	final int[] DIRECAO_SUDOESTE = {14, 15, 20, 19};
	
	final int[] DISTANCIA_UM = {7, 11, 12, 16};
	final int[] DISTANCIA_DOIS = {2, 6, 8, 10, 13, 15, 17, 21};
	final int[] DISTANCIA_TRES = {1, 3, 5, 9, 14, 18, 20, 22};
	final int[] DISTANCIA_QUATRO = {0, 4, 19, 23};
	
	boolean hasValue(ArrayList<Integer> arr, int value) {
		for (int s : arr) {
			if (s == value) {
				return true;
			}
		}
		return false;
	}
	
	boolean inDirection(int[] arr, int value) {
		for (int s : arr) {
			if (s == value) {
				return true;
			}
		}
		return false;
	}
	
	private int move(int command) {
		int[] visaoIdentificacao = this.sensor.getVisaoIdentificacao();
		if (visaoIdentificacao[command] == 1 || visaoIdentificacao[command] == -2) {
			return (int) (Math.random() * 5);
		}
		return command;
	}
	
	private int moveForCoin() {
		int[] visaoIdentificacao = this.sensor.getVisaoIdentificacao();
		
		ArrayList<Integer>positionCoins = new ArrayList<>();
		
		for (int i = 0; i < visaoIdentificacao.length; i++) {
			if (visaoIdentificacao[i] == 4) {
				positionCoins.add(i);
			}
		}
		
		if (positionCoins.size() <= 0) {
			return (int) (Math.random() * 5);
		}
		
		for (int i = 0; i < DISTANCIA_DOIS.length; i++) {
			if (hasValue(positionCoins, DISTANCIA_DOIS[i])) {
				if (inDirection(DIRECAO_NORTE, DISTANCIA_DOIS[i])) {
					return move(NORTE);
				} else if (inDirection(DIRECAO_SUL, DISTANCIA_DOIS[i])) {
					return move(SUL);
				} else if (inDirection(DIRECAO_LESTE, DISTANCIA_DOIS[i])) {
					return move(LESTE);
				} else if (inDirection(DIRECAO_OESTE, DISTANCIA_DOIS[i])) {
					return move(OESTE);
				} else if (inDirection(DIRECAO_NORDESTE, DISTANCIA_DOIS[i])) {
					return move(NORDESTE);
				} else if (inDirection(DIRECAO_NOROESTE, DISTANCIA_DOIS[i])) {
					return move(NOROESTE);
				} else if (inDirection(DIRECAO_SUDESTE, DISTANCIA_DOIS[i])) {
					return move(SUDESTE);
				} else if (inDirection(DIRECAO_SUDOESTE, DISTANCIA_DOIS[i])) {
					return move(SUDOESTE);
				}
			}
		}
		
		for (int i = 0; i < DISTANCIA_TRES.length; i++) {
			if (hasValue(positionCoins, DISTANCIA_TRES[i])) {
				if (inDirection(DIRECAO_NORTE, DISTANCIA_TRES[i])) {
					return move(NORTE);
				} else if (inDirection(DIRECAO_SUL, DISTANCIA_TRES[i])) {
					return move(SUL);
				} else if (inDirection(DIRECAO_LESTE, DISTANCIA_TRES[i])) {
					return move(LESTE);
				} else if (inDirection(DIRECAO_OESTE, DISTANCIA_TRES[i])) {
					return move(OESTE);
				} else if (inDirection(DIRECAO_NORDESTE, DISTANCIA_TRES[i])) {
					return move(NORDESTE);
				} else if (inDirection(DIRECAO_NOROESTE, DISTANCIA_TRES[i])) {
					return move(NOROESTE);
				} else if (inDirection(DIRECAO_SUDESTE, DISTANCIA_TRES[i])) {
					return move(SUDESTE);
				} else if (inDirection(DIRECAO_SUDOESTE, DISTANCIA_TRES[i])) {
					return move(SUDOESTE);
				}
			}
		}
		
		for (int i = 0; i < DISTANCIA_QUATRO.length; i++) {
			if (hasValue(positionCoins, DISTANCIA_QUATRO[i])) {
				if (inDirection(DIRECAO_NORTE, DISTANCIA_QUATRO[i])) {
					return NORTE;
				} else if (inDirection(DIRECAO_SUL, DISTANCIA_QUATRO[i])) {
					return SUL;
				} else if (inDirection(DIRECAO_LESTE, DISTANCIA_QUATRO[i])) {
					return LESTE;
				} else if (inDirection(DIRECAO_OESTE, DISTANCIA_QUATRO[i])) {
					return OESTE;
				} else if (inDirection(DIRECAO_NORDESTE, DISTANCIA_QUATRO[i])) {
					return NORDESTE;
				} else if (inDirection(DIRECAO_NOROESTE, DISTANCIA_QUATRO[i])) {
					return NOROESTE;
				} else if (inDirection(DIRECAO_SUDESTE, DISTANCIA_QUATRO[i])) {
					return SUDESTE;
				} else if (inDirection(DIRECAO_SUDOESTE, DISTANCIA_QUATRO[i])) {
					return SUDOESTE;
				}
			}
		}

		return (int) (Math.random() * 5);
	}	
	
	private int getCoin() {
		int[] visaoIdentificacao = this.sensor.getVisaoIdentificacao();		
		
		if(visaoIdentificacao[7] == 4 ) { 
			return 1;
		} else if(visaoIdentificacao[11] == 4) {
			return 4;
		} else if(visaoIdentificacao[12] == 4) {
			return 3;
		} else if(visaoIdentificacao[16] == 4) {
			return 2;
		}
		
		return moveForCoin();
	}
		
	@Override
	public int acao() {		
		return getCoin();
	}
}