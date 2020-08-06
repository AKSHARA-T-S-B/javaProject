package Model.Components.Frame;

public class Frame {

	private Diamond[] diamond;
	private StepThrough[] stepThrough;
	private Contilever[] contilever;
	private Recumbent[] recumbent;
	private Steel[] steel;
	private Aluminium[] aluminium;
	private Titanium[] titanium;

	public synchronized Diamond[] getDiamond() {
		return diamond;
	}

	public synchronized void setDiamond(Diamond[] diamond) {
		this.diamond = diamond;
	}

	public synchronized StepThrough[] getStepThrough() {
		return stepThrough;
	}

	public synchronized void setStepThrough(StepThrough[] stepThrough) {
		this.stepThrough = stepThrough;
	}

	public synchronized Contilever[] getContilever() {
		return contilever;
	}

	public synchronized void setContilever(Contilever[] contilever) {
		this.contilever = contilever;
	}

	public synchronized Recumbent[] getRecumbent() {
		return recumbent;
	}

	public synchronized void setRecumbent(Recumbent[] recumbent) {
		this.recumbent = recumbent;
	}

	public synchronized Steel[] getSteel() {
		return steel;
	}

	public synchronized void setSteel(Steel[] steel) {
		this.steel = steel;
	}

	public synchronized Aluminium[] getAluminium() {
		return aluminium;
	}

	public synchronized void setAluminium(Aluminium[] aluminium) {
		this.aluminium = aluminium;
	}

	public synchronized Titanium[] getTitanium() {
		return titanium;
	}

	public synchronized void setTitanium(Titanium[] titanium) {
		this.titanium = titanium;
	}
}
