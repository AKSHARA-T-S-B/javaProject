package Model.Components.ChainAssembly;

public class ChainAssembly {
	private FixedGear[] fixedGear;
	private Gear3[] gear3;
	private Gear4[] gear4;
	private Gear5[] gear5;
	private Gear6[] gear6;
	private Gear7[] gear7;
	private Gear8[] gear8;

	public synchronized FixedGear[] getFixedGear() {
		return fixedGear;
	}

	public synchronized void setFixedGear(FixedGear[] fixedGear) {
		this.fixedGear = fixedGear;
	}

	public synchronized Gear3[] getGear3() {
		return gear3;
	}

	public synchronized void setGear3(Gear3[] gear3) {
		this.gear3 = gear3;
	}

	public synchronized Gear4[] getGear4() {
		return gear4;
	}

	public synchronized void setGear4(Gear4[] gear4) {
		this.gear4 = gear4;
	}

	public synchronized Gear5[] getGear5() {
		return gear5;
	}

	public synchronized void setGear5(Gear5[] gear5) {
		this.gear5 = gear5;
	}

	public synchronized Gear6[] getGear6() {
		return gear6;
	}

	public synchronized void setGear6(Gear6[] gear6) {
		this.gear6 = gear6;
	}

	public synchronized Gear7[] getGear7() {
		return gear7;
	}

	public synchronized void setGear7(Gear7[] gear7) {
		this.gear7 = gear7;
	}

	public synchronized Gear8[] getGear8() {
		return gear8;
	}

	public synchronized void setGear8(Gear8[] gear8) {
		this.gear8 = gear8;
	}
}
