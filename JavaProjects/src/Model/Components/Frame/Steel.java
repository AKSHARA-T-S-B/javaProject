package Model.Components.Frame;

import java.util.HashMap;
import java.util.Map;

public class Steel {
	private int year;
	private int jan;
	private int feb;
	private int mar;
	private int apr;
	private int may;
	private int jun;
	private int jul;
	private int aug;
	private int sep;
	private int oct;
	private int nov;
	private int dec;
	private Map<String, Integer> costMap = new HashMap<>();
	
	public synchronized Map<String, Integer> getCostMap(){
		costMap.put("year", year);
		costMap.put("jan", jan);
		costMap.put("feb", feb);
		costMap.put("mar", mar);
		costMap.put("apr", apr);
		costMap.put("may", may);
		costMap.put("jun", jun);
		costMap.put("jul", jul);
		costMap.put("aug", aug);
		costMap.put("sep", sep);
		costMap.put("oct", oct);
		costMap.put("nov", nov);
		costMap.put("dec", dec);
		return costMap;
	}
	public synchronized void setCostMap(Map<String, Integer> costMap) {
		this.costMap = costMap;
	}
	public synchronized int getYear() {
		return year;
	}
	public synchronized void setYear(int year) {
		this.year = year;
	}
	public synchronized int getJan() {
		return jan;
	}
	public synchronized void setJan(int jan) {
		this.jan = jan;
	}
	public synchronized int getFeb() {
		return feb;
	}
	public synchronized void setFeb(int feb) {
		this.feb = feb;
	}
	public synchronized int getMar() {
		return mar;
	}
	public synchronized void setMar(int mar) {
		this.mar = mar;
	}
	public synchronized int getApr() {
		return apr;
	}
	public synchronized void setApr(int apr) {
		this.apr = apr;
	}
	public synchronized int getMay() {
		return may;
	}
	public synchronized void setMay(int may) {
		this.may = may;
	}
	public synchronized int getJun() {
		return jun;
	}
	public synchronized void setJun(int jun) {
		this.jun = jun;
	}
	public synchronized int getJul() {
		return jul;
	}
	public synchronized void setJul(int jul) {
		this.jul = jul;
	}
	public synchronized int getAug() {
		return aug;
	}
	public synchronized void setAug(int aug) {
		this.aug = aug;
	}
	public synchronized int getSep() {
		return sep;
	}
	public synchronized void setSep(int sep) {
		this.sep = sep;
	}
	public synchronized int getOct() {
		return oct;
	}
	public synchronized void setOct(int oct) {
		this.oct = oct;
	}
	public synchronized int getNov() {
		return nov;
	}
	public synchronized void setNov(int nov) {
		this.nov = nov;
	}
	public synchronized int getDec() {
		return dec;
	}
	public synchronized void setDec(int dec) {
		this.dec = dec;
	}
	
}
