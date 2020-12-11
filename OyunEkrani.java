
import java.awt.Color;
import java.awt.HeadlessException;
import java.io.IOException;

import javax.swing.JFrame;

public class OyunEkrani extends JFrame {

	public OyunEkrani(String title) throws HeadlessException {
		super(title);
	}
	
	@Override
    public void addNotify() {
        super.addNotify();
        createBufferStrategy(2);           
    }


	public static void main(String[] args) throws IOException {

		OyunEkrani ekran = new OyunEkrani("oyun");

		ekran.setResizable(false);
		ekran.setFocusable(false);

		ekran.setSize(800, 600);
		ekran.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ekran.setBackground(Color.BLACK);
		ekran.setEnabled(true);
		Oyun oyun = new Oyun();

		oyun.requestFocus();
		oyun.setFocusable(true);
		oyun.setFocusTraversalKeysEnabled(false);

		ekran.add(oyun);
		ekran.setLocationRelativeTo(null);
		ekran.setVisible(true);
		
		oyun.Baslat();
	}

}
