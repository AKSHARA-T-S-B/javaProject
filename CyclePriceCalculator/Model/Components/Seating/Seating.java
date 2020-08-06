package Model.Components.Seating;

public class Seating {
	private RacingSaddle[] racingSaddle;
	private ComfortSaddle[] comfortSaddle;
	private CruiserSaddle[] cruiserSaddle;

	public synchronized RacingSaddle[] getRacingSaddle() {
		return racingSaddle;
	}

	public synchronized void setRacingSaddle(RacingSaddle[] racingSaddle) {
		this.racingSaddle = racingSaddle;
	}

	public synchronized ComfortSaddle[] getComfortSaddle() {
		return comfortSaddle;
	}

	public synchronized void setComfortSaddle(ComfortSaddle[] comfortSaddle) {
		this.comfortSaddle = comfortSaddle;
	}

	public synchronized CruiserSaddle[] getCruiserSaddle() {
		return cruiserSaddle;
	}

	public synchronized void setCruiserSaddle(CruiserSaddle[] cruiserSaddle) {
		this.cruiserSaddle = cruiserSaddle;
	}
}
