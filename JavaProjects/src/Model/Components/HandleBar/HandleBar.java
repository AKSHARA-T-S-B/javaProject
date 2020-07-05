package Model.Components.HandleBar;

import java.util.HashMap;
import java.util.Map;

public class HandleBar {
	private PursuitBar[] pursuitBar;
	private DropBar[] dropBar;
	private RiserBar[] riserBar;
	
	public synchronized PursuitBar[] getPursuitBar() {
		return pursuitBar;
	}
	public synchronized void setPursuitBar(PursuitBar[] pursuitBar) {
		this.pursuitBar = pursuitBar;
	}
	public synchronized DropBar[] getDropBar() {
		return dropBar;
	}
	public synchronized void setDropBar(DropBar[] dropBar) {
		this.dropBar = dropBar;
	}
	public synchronized RiserBar[] getRiserBar() {
		return riserBar;
	}
	public synchronized void setRiserBar(RiserBar[] riserBar) {
		this.riserBar = riserBar;
	}
}
