package prod;

import java.util.ArrayList;
import java.util.Iterator;

public class TrainCollection {
	
	private ArrayList<Train> trains;
	
	public TrainCollection(){
		trains = new ArrayList<Train>();
	}

	public Train getTrainByName(String name){
		for (Train train : trains) {
			if(train.getName().equals(name)){
				return train;
			}
		}
		return null;
	}
	
	public void addTrain(Train train){
		// Bevor Zug hinzugefügt wird, muss überprüft werden, ob der Zug bereits mit dem Namen existiert
		if(this.trainIsAlreadyExisting(train.getName())){
			return;
		}else{
			this.trains.add(train);
		}
	}
	
	public ArrayList<Train> getTrains() {
		return trains;
	}

	public void setTrains(ArrayList<Train> trains) {
		this.trains = trains;
	}
	
	// Methode zum Überprüfen, ob Zug bereits existiert
	public boolean trainIsAlreadyExisting(String name){
		for (Train t : trains) {
			if(t.getName().equals(name)){
				return true;
			}
		}
		return false;
	}
	
	public void removeTrainFromCollection(Train train){
		if(train != null){
			this.trains.remove(train);
		}
	}
	
	// Methode zum Debuggen
	public void printAllTrain(){
		int i = 0;
		for (Train train : trains) {
			System.out.println("Zug Nr. " + i + ": " + train.getName());
			i++;
		}
	}
}
