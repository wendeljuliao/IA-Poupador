package algoritmo;

public class Ladrao extends ProgramaLadrao {
	
	@Override
	public int acao() {
		return (int) (Math.random() * 5);
	}

}