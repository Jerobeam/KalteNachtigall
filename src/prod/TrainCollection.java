package prod;

import java.util.ArrayList;
import java.util.Iterator;

public class TrainCollection {
	
	private ArrayList<Train> trains;
	
	public TrainCollection(){
		
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
		for (Train t : trains) {
			if(t.getName().equals(train.getName())){
				return;
			}
		}
		this.trains.add(train);
	}
	
	public ArrayList<Train> getTrains() {
		return trains;
	}

	public void setTrains(ArrayList<Train> trains) {
		this.trains = trains;
	}
	
	
	
}
