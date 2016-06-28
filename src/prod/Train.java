package prod;

public class Train {
	
	private String name;
	private String modelDesc;
	private String imagePath;
	// Geschwindigkeit in Prozent von 0 bis 100
	private int speed;
	private boolean lightActive;
	private boolean directionRight;
	
	public Train(String name, String modelDesc, String imagePath){
		this.name = name;
		this.modelDesc = modelDesc;
		this.imagePath = imagePath;
		this.speed = 0;
		this.lightActive = false;
		this.directionRight = true;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getModelDesc() {
		return modelDesc;
	}
	public void setModelDesc(String modelDesc) {
		this.modelDesc = modelDesc;
	}
	public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	public int getSpeed() {
		return speed;
	}
	public void setSpeed(int speed) {
		// Erlaube nur Werte von 0 bis 100
		if(speed > 100){
			this.speed = 100;
		}else if(speed < 0){
			this.speed = 0;
		}else{
			this.speed = speed;
		}
	}
	public boolean isLightActive() {
		return lightActive;
	}
	public void setLightActive(boolean lightActive) {
		this.lightActive = lightActive;
	}
	public boolean isDirectionRight() {
		return directionRight;
	}
	public void setDirectionRight(boolean directionRight) {
		this.directionRight = directionRight;
	}
}
