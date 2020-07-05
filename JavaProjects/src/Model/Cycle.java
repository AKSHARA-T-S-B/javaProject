package Model;

import Model.Components.ChainAssembly.ChainAssembly;
import Model.Components.Frame.Frame;
import Model.Components.HandleBar.HandleBar;
import Model.Components.Seating.Seating;
import Model.Components.Wheels.Wheels;

public class Cycle {
	private Frame frame;
	private HandleBar handleBar;
	private Seating seating;
	private Wheels wheels;
	private ChainAssembly chainAssembly;
	
	public Frame getFrame() {
		return frame;
	}
	public void setFrame(Frame frame) {
		this.frame = frame;
	}
	
	public HandleBar getHandleBar() {
		return handleBar;
	}
	public void setHandleBar(HandleBar handleBar) {
		this.handleBar = handleBar;
	}
	
	public Seating getSeating() {
		return seating;
	}
	public void setSeating(Seating seating) {
		this.seating = seating;
	}
	
	public Wheels getWheels() {
		return wheels;
	}
	public void setWheels(Wheels wheels) {
		this.wheels = wheels;
	}
	
	public ChainAssembly getChainAssembly() {
		return chainAssembly;
	}
	public void setChainAssembly(ChainAssembly chainAssembly) {
		this.chainAssembly = chainAssembly;
	}
}
