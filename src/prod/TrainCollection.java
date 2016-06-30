package prod;

import java.util.ArrayList;

/**
 * Klasse f�r eine Ansammlung an Z�gen
 * 
 * @author Sebastian R�hling
 *
 * @version 1.0
 */
public class TrainCollection {
	
	/**
	 * Liste der Z�ge
	 */
	private ArrayList<Train> trains;
	
	/**
	 * Konstruktor, initialisiert die Zugliste
	 */
	public TrainCollection(){
		trains = new ArrayList<Train>();
	}

	/**
	 * Sucht in der Zugliste nach einen Zug mit entsprechenden Zugnamen und gibt diesen zur�ck, wenn er gefunden wurde
	 * @param name Zugname
	 * @return Zugobjekt oder null
	 */
	public Train getTrainByName(String name){
		for (Train train : trains) {
			if(train.getName().equals(name)){
				return train;
			}
		}
		return null;
	}
	
	/**
	 * F�gt einen Zug der Zugliste hinzu, wenn der Zugname noch nicht belegt ist
	 * @param train Zugobjekt
	 */
	public void addTrain(Train train){
		// Bevor Zug hinzugef�gt wird, muss �berpr�ft werden, ob der Zug bereits mit dem Namen existiert
		if(this.trainIsAlreadyExisting(train.getName())){
			return;
		}else{
			this.trains.add(train);
		}
	}
	
	/**
	 * Gibt die Zugliste zur�ck
	 * @return Zugliste
	 */
	public ArrayList<Train> getTrains() {
		return trains;
	}

	/**
	 * �berpr�ft, ob ein Zugname bereits verwendet wurde
	 * @param name Zugname
	 * @return true oder false
	 */
	public boolean trainIsAlreadyExisting(String name){
		for (Train t : this.trains) {
			if(t.getName().equals(name)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Entfernt einen Zug aus der Zugliste und loggt die vorgenommene �nderung
	 * @param train Zugobjekt
	 */
	public void removeTrainFromCollection(Train train){
		if(train != null){
			this.trains.remove(train);
		}
		System.out.println("Zug '" + train.getName()+ "' gel�scht");
		
		// Gebe Speicherplatz des Objektes durch Garbage Collector frei
		train = null;
	}
	
	/**
	 * Listet die Namen aller Zugobjekte in der Zugliste f�r Debug-Zwecke auf
	 */
	public void printAllTrains(){
		int i = 0;
		for (Train train : this.trains) {
			System.out.println("Zug Nr. " + i + ": " + train.getName());
			i++;
		}
	}
	
	/**
	 * Setzt die Geschwindigkeit aller Zugobjekte in der Zugliste auf 0
	 */
	public void stopAllTrains(){
		for (Train t : this.trains) {
			if(t.getSpeed() != 0){
				t.setSpeed(0);
			}
		}
	}
	
}
