package prod;

/**
 * Train ist die Klasse f�r ein einzelnes Zugobjekt. Sie h�lt Zugeigenschaften
 * und bietet Methoden zu deren Manipulation an.
 * 
 * @author Sebastian R�hling
 *
 * @version 1.0
 */
public class Train {

	/**
	 * Name des Zuges
	 */
	private String name;
	/**
	 * Optionale Modellbeschreibung des Zuges
	 */
	private String modelDesc;
	/**
	 * Optionaler Bildpfad des Zuges
	 */
	private String imagePath;

	/**
	 * Geschwindigkeit von 0 bis 100 des Zuges in Prozent
	 */
	private int speed;
	/**
	 * Flag f�r das Licht des Zuges
	 */
	private boolean lightActive;
	/**
	 * Flag f�r die Fahrtrichtung des Zuges
	 */
	private boolean directionRight;

	/**
	 * Konstruktor
	 * 
	 * @param name
	 *            Zugname
	 * @param modelDesc
	 *            Modellbeschreibung des Zuges
	 * @param imagePath
	 *            Bildpfad des Zuges
	 */
	public Train(String name, String modelDesc, String imagePath) {
		this.name = name;
		this.modelDesc = modelDesc;
		this.imagePath = imagePath;
		this.speed = 0;
		this.lightActive = false;
		this.directionRight = true;
	}

	/**
	 * Gibt den Zugnamen zur�ck
	 * 
	 * @return Zugname
	 */
	public String getName() {
		return name;
	}

	/**
	 * Setzt den Zugnamen und loggt die vorgenommene �nderung
	 * 
	 * @param name
	 *            Zugname
	 */
	public void setName(String name) {
		System.out.println("Name des Zuges '" + this.name + "' ge�ndert zu: '" + name + "'");
		this.name = name;
	}

	/**
	 * Gibt die Modellbeschreibung des Zuges zur�ck
	 * 
	 * @return Modellbeschreibung des Zuges
	 */
	public String getModelDesc() {
		return modelDesc;
	}

	/**
	 * Setzt die Modellbeschreiung des Zuges und loggt die vorgenommene �nderung
	 * 
	 * @param modelDesc
	 *            Modellbeschreiung des Zuges
	 */
	public void setModelDesc(String modelDesc) {
		System.out.println("Beschreibung des Zuges '" + this.name + "' von '" + this.modelDesc + "' zu '" + modelDesc
				+ "' ge�ndert");
		this.modelDesc = modelDesc;
	}

	/**
	 * Gibt den Bildpfad des Zuges zur�ck
	 * 
	 * @return Bildpfad des Zuges
	 */
	public String getImagePath() {
		return imagePath;
	}

	/**
	 * Setzt den Bildpfad des Zuges
	 * 
	 * @param imagePath
	 *            Bildpfad des Zuges
	 */
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	/**
	 * Gibt die aktuelle Geschwindigkeit des Zuges zur�ck
	 * 
	 * @return Geschwindigkeit des Zuges
	 */
	public int getSpeed() {
		return speed;
	}

	/**
	 * Setzt die Geschwindigkeit des Zuges und loggt die vorgenommene �nderung
	 * in 10er Schritten
	 * 
	 * @param speed
	 *            Geschwindigkeit des Zuges
	 */
	public void setSpeed(int speed) {
		// Erlaube nur Werte von 0 bis 100
		if (speed > 100) {
			this.speed = 100;
		} else if (speed < 0) {
			this.speed = 0;
		} else {
			this.speed = speed;
		}

		// Gebe Geschwindigkeitsver�nderungen nur in 10er Schritten aus
		if (this.speed % 10 == 0) {
			System.out.println("Geschwindigkeit des Zuges '" + this.name + "' auf " + this.speed + " ge�ndert");
		}

	}

	/**
	 * Gibt das Lichtflag des Zuges zur�ck
	 * 
	 * @return Flag Licht an
	 */
	public boolean isLightActive() {
		return lightActive;
	}

	/**
	 * Setzt das Lichtflag des Zuges und loggt die vorgenommene �nderung
	 * 
	 * @param lightActive
	 *            Flag Licht an
	 */
	public void setLightActive(boolean lightActive) {
		this.lightActive = lightActive;
		if (lightActive) {
			System.out.println("Licht des Zuges '" + this.name + "' angeschaltet");
		} else {
			System.out.println("Licht des Zuges '" + this.name + "' ausgeschaltet");
		}
	}

	/**
	 * Gibt das Richtungsflag des Zuges zur�ck
	 * 
	 * @return Flag Richtung rechts
	 */
	public boolean isDirectionRight() {
		return directionRight;
	}

	/**
	 * Setzt das Richtungsflag des Zuges und loggt die vorgenommene �nderung
	 * 
	 * @param directionRight
	 *            Flag Richtung rechts
	 */
	public void setDirectionRight(boolean directionRight) {
		this.directionRight = directionRight;
		if (directionRight) {
			System.out.println("Zug '" + this.name + "' f�hrt nun rechts herum");
		} else {
			System.out.println("Zug '" + this.name + "' f�hrt nun links herum");
		}
	}
}
