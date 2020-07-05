package Model.Components.Wheels;

public class Wheels {
	private Spokes[] spokes;
	private Rim[] rim;
	private Tube[] tube;
	private Tyre[] tyre;
	public synchronized Spokes[] getSpokes() {
		return spokes;
	}
	public synchronized void setSpokes(Spokes[] spokes) {
		this.spokes = spokes;
	}
	public synchronized Rim[] getRim() {
		return rim;
	}
	public synchronized void setRim(Rim[] rim) {
		this.rim = rim;
	}
	public synchronized Tube[] getTube() {
		return tube;
	}
	public synchronized void setTube(Tube[] tube) {
		this.tube = tube;
	}
	public synchronized Tyre[] getTyre() {
		return tyre;
	}
	public synchronized void setTyre(Tyre[] tyre) {
		this.tyre = tyre;
	}
}
