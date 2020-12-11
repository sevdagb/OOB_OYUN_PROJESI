
public class KuruKafa {

	private int konumX;
	private int konumY;
	
	public KuruKafa(int konumX, int konumY, int resimYukseklik) {
		this.konumX = konumX;
		this.konumY = konumY;
		
		if(konumY + resimYukseklik > 540) {
			konumY = 540 - resimYukseklik;
		}
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
	
}
