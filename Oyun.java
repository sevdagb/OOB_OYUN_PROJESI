
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class Oyun extends JPanel {

	private int topX = 0;
	private int topdirX = 2;
	private int pandaX = 0;
	private int dirPandaX = 20;

	// yumagin resmi
	private BufferedImage yumakResim;
	// yesil ok resmi
	private BufferedImage yesilOkResim;
	// mavi ok resmi
	private BufferedImage maviOkResim;
	// kurukafa resmi
	private BufferedImage kuruKafaResim;

	// yumagin konumu
	private int yumakX, yumakY;
	// yumakin boyutu
	private int yumakBoyutX, yumakBoyutY;
	// yumagin omru
	private long yumaginOmru = 10000; // milisaniye
	// gecen sure
	private int gecenSure;
	// yumagin boyutu
	private int yumaginBoyutu = 1;

	private int konumDegisimi = 7;

	private Thread oyunThread;
	private boolean oyunDevamEdiyor = false;

	// Sari toplar
	private List<SariTop> sariToplar;
	private List<BoyutOku> boyutOklari;
	private List<KuruKafa> kuruKafalar;

	private boolean sariTopEkle;
	private boolean okEkle;
	private boolean kuruKafaEkle;

	// oyun bitisi
	private boolean oyunBittiMi = false;
	private boolean oyunSuredenBitti = false;
	private boolean oyunKuruKafadanBitti = false;
	
	// Rastgele
	private Random random;

	public Oyun() {

		try {
			yumakResim = ImageIO.read(this.getClass().getResource("/panda.png"));
			yesilOkResim = ImageIO.read(this.getClass().getResource("/yesilOk.png"));
			maviOkResim = ImageIO.read(this.getClass().getResource("/maviOk.png"));
			kuruKafaResim = ImageIO.read(this.getClass().getResource("/kurukafa.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		setBackground(Color.black);

		setFocusable(true);
		System.out.println("*GameEngine is created");
		addKeyListener(new KeyInputHandler());
		requestFocus();

		yumakY = (540 - yumakResim.getHeight()) / 2;
		yumakBoyutX = yumakResim.getWidth();
		yumakBoyutY = yumakResim.getHeight();

		random = new Random();

		sariToplar = new CopyOnWriteArrayList<SariTop>();
		boyutOklari = new CopyOnWriteArrayList<BoyutOku>();
		kuruKafalar = new CopyOnWriteArrayList<KuruKafa>();
	}

	@Override
	protected void paintComponent(Graphics g) {
		setSize(800, 600);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.clearRect(0, 0, getWidth(), getHeight());

		g2d.drawImage(yumakResim, yumakX, yumakY, yumakBoyutX, yumakBoyutY, null);

		/*
		 * 
		 * setBackground(Color.red); g.setColor(Color.yellow);
		 * 
		 * g.fillOval(topX, 0, 20, 20); g.drawImage(yumak, pandaX, 490, yumak.getWidth()
		 * / 10, yumak.getHeight() / 10, this);
		 * 
		 */
		
		// sari toplari ciz
		for (SariTop sariTop : sariToplar) {
			g2d.setColor(Color.YELLOW);
			g2d.fillOval(sariTop.getKonumX(), sariTop.getKonumY(), sariTop.getCizilecekBoyut(),
					sariTop.getCizilecekBoyut());
			g2d.setColor(Color.BLACK);
			g2d.drawString("" + sariTop.getBoyut(), sariTop.getKonumX() + sariTop.getCizilecekBoyut() / 2,
					sariTop.getKonumY() + sariTop.getCizilecekBoyut() / 2);
		}

		for (BoyutOku boyutOku : boyutOklari) {
			g2d.drawImage(boyutOku.getOkResmi(), boyutOku.getKonumX(), boyutOku.getKonumY(), null);
		}

		for (KuruKafa kuruKafa : kuruKafalar) {
			g2d.drawImage(kuruKafaResim, kuruKafa.getKonumX(), kuruKafa.getKonumY(), null);
		}

		g2d.setColor(Color.BLACK);
		g2d.fillRect(0, 540, 800, 10);
		g2d.drawString("Yumağın ömrü : " + yumaginOmru / 1000, 5, 565);
		g2d.drawString("Geçen Süre : " + gecenSure / 1000, 670, 565);
		
		if(oyunBittiMi) {
			g2d.setColor(Color.RED);
			if(oyunSuredenBitti) {
				g2d.drawString("YUMAK'IN ÖMRÜ TÜKENDİ :(", 300, 280);
			} else if(oyunKuruKafadanBitti) {
				g2d.drawString("KURU KAFAYA ÇARPTIN :(", 300, 280);
			}
		}
	}

	public void Baslat() {
		Runnable r = new Runnable() {

			@Override
			public void run() {
				try {
					long sonDonguZamani = System.currentTimeMillis();
					while (oyunDevamEdiyor) {
						long time = System.currentTimeMillis() - sonDonguZamani;
						sonDonguZamani = System.currentTimeMillis();

						mantikDongusu(time);

						Thread.sleep(1);

						Oyun.this.repaint();

						Thread.sleep(10);
						
						if(oyunBittiMi) {
							Oyun.this.repaint();
							oyunDevamEdiyor = false;
						}
					}
				} catch (InterruptedException iex) {
					System.err.println("*Game is interrupted unexpectedly!");
				}
			}
		};
		oyunThread = new Thread(r);
		oyunThread.start();
		oyunDevamEdiyor = true;
	}

	private void mantikDongusu(long time) {
		yumaginOmru -= time;
		gecenSure += time;

		if (((gecenSure / 1500) % 2) == 0) {
			if (!sariTopEkle) {
				// sari top ekle
				sariToplar.add(new SariTop(random.nextInt(10) + 1, 800, random.nextInt(541)));
				sariTopEkle = true;
			}
		} else {
			sariTopEkle = false;
		}

		if (((gecenSure / 2500) % 3) == 0) {
			if (!okEkle) {
				// sari top ekle
				if (random.nextBoolean()) {
					boyutOklari.add(new BoyutOku(yesilOkResim, 800, random.nextInt(541), true));
				} else {
					boyutOklari.add(new BoyutOku(maviOkResim, 800, random.nextInt(541), false));
				}

				okEkle = true;
			}
		} else {
			okEkle = false;
		}

		if ((gecenSure / 3500) % 2 == 0) {
			if (!kuruKafaEkle) {
				kuruKafalar.add(new KuruKafa(800, random.nextInt(541), kuruKafaResim.getHeight()));
				kuruKafaEkle = true;
			}
		} else {
			kuruKafaEkle = false;
		}

		for (SariTop sariTop : sariToplar) {
			sariTop.setKonumX(sariTop.getKonumX() - 2);
		}

		for (BoyutOku boyutOku : boyutOklari) {
			boyutOku.setKonumX(boyutOku.getKonumX() - 3);
		}

		for (KuruKafa kurukafa : kuruKafalar) {
			kurukafa.setKonumX(kurukafa.getKonumX() - 4);
		}

		carpismaControlu();
		
		if(yumaginOmru < 0) {
			oyunBittiMi = true;
			oyunSuredenBitti = true;
		} else if(oyunKuruKafadanBitti) {
			oyunBittiMi = true;
		}
	}

	private void carpismaControlu() {
		Rectangle yumakRectangle = new Rectangle(yumakX, yumakY, yumakBoyutX, yumakBoyutY);

		List<SariTop> carpisanToplar = new CopyOnWriteArrayList<SariTop>();

		for (SariTop sariTop : sariToplar) {
			if (new Rectangle(sariTop.getKonumX(), sariTop.getKonumY(), sariTop.getCizilecekBoyut(),
					sariTop.getCizilecekBoyut()).intersects(yumakRectangle)) {
				// yumak topa carpti
				yumaginOmru += sariTop.getBoyut() * 1000 * yumaginBoyutu;
				carpisanToplar.add(sariTop);
			} else if (sariTop.getKonumX() + sariTop.getCizilecekBoyut() < 0) {
				carpisanToplar.add(sariTop);
			}
		}

		List<BoyutOku> carpisanOklar = new CopyOnWriteArrayList<BoyutOku>();
		for (BoyutOku boyutOku : boyutOklari) {
			if (new Rectangle(boyutOku.getKonumX(), boyutOku.getKonumY(), maviOkResim.getHeight(),
					maviOkResim.getWidth()).intersects(yumakRectangle)) {
				// yumak oka carpti
				if (boyutOku.isBuyut()) {
					yumaginBoyutu++;
					if (yumaginBoyutu > 5) {
						yumaginBoyutu = 5;
					}
				} else {
					yumaginBoyutu--;
					if (yumaginBoyutu < 1) {
						yumaginBoyutu = 1;
					}
				}
				carpisanOklar.add(boyutOku);
			} else if (boyutOku.getKonumX() + maviOkResim.getWidth() < 0) {
				carpisanOklar.add(boyutOku);
			}
		}

		List<KuruKafa> carpisanKuruKafalar = new CopyOnWriteArrayList<KuruKafa>();
		for (KuruKafa kuruKafa : kuruKafalar) {
			if (new Rectangle(kuruKafa.getKonumX(), kuruKafa.getKonumY(), kuruKafaResim.getHeight(),
					kuruKafaResim.getWidth()).intersects(yumakRectangle)) {
				carpisanKuruKafalar.add(kuruKafa);
				oyunKuruKafadanBitti = true;
			} else if (kuruKafa.getKonumX() + kuruKafaResim.getWidth() < 0) {
				carpisanKuruKafalar.add(kuruKafa);
			}
		}

		yumakBoyutX = (int) (yumakResim.getWidth() * ((float) yumaginBoyutu / 2));
		yumakBoyutY = (int) (yumakResim.getHeight() * ((float) yumaginBoyutu / 2));

		sariToplar.removeAll(carpisanToplar);
		boyutOklari.removeAll(carpisanOklar);
		kuruKafalar.removeAll(carpisanKuruKafalar);
	}

	private class KeyInputHandler extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
				yumakY -= konumDegisimi;
				if (yumakY < 0) {
					yumakY = 0;
				}
			} else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
				yumakY += konumDegisimi;
				if (yumakY + yumakBoyutY > 540) {
					yumakY = 540 - yumakBoyutY;
				}
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
			super.keyReleased(e);
		}
	}

}
