
public class SariTop {

	private int konumX;
	private int konumY;
	private int boyut;
	private int cizilecekBoyut;
	
	public SariTop(int boyut, int konumX, int konumY) {
		this.boyut = boyut;
		this.konumX = konumX;
		this.konumY = konumY;
		
		cizilecekBoyut = 300/boyut;
		
		if(konumY + cizilecekBoyut > 540) {
			this.konumY -= cizilecekBoyut;
		}
	}
	
	public int getKonumX() {
		return konumX;
	}
	
	public int getKonumY() {
		return konumY;
	}
	
	public int getBoyut() {
		return boyut;
	}
	
	public int getCizilecekBoyut() {
		return cizilecekBoyut;
	}
	
	public void setKonumX(int konumX) {
		this.konumX = konumX;
	}
	
	public void setKonumY(int konumY) {
		this.konumY = konumY;
	}
	
}
