import java.awt.image.BufferedImage;

public class BoyutOku {

	private BufferedImage okResmi;
	private int konumX;
	private int konumY;
	private boolean buyut;
	
	public BoyutOku(BufferedImage okResmi, int konumX, int konumY, boolean buyut) {
		this.okResmi = okResmi;
		this.konumX = konumX;
		this.konumY = konumY;
		this.buyut = buyut;
		
		if(konumY + okResmi.getWidth() > 540) {
			konumY = 540 - okResmi.getWidth();
		}
	}
	
	public BufferedImage getOkResmi() {
		return okResmi;
	}
	
	public int getKonumX() {
		return konumX;
	}
	
	public int getKonumY() {
		return konumY;
	}
	
	public void setKonumX(int konumX) {
		this.konumX = konumX;
	}
	
	public void setKonumY(int konumY) {
		this.konumY = konumY;
	}
	
	public boolean isBuyut() {
		return buyut;
	}
	
}
