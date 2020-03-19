package uk.ac.aston.jpd.coursework.officebuilding.building;

public class Building {
	//declaring fields
	private List <Floor> floors = new ArrayList <Floor>();;
	private Elevator elevator;
	
	//constuctor
	public Building (int floors){
	this.floors = floors; 
	}
	
	public void tick(){
	elevator.tick();
		for (Floor f : floors){
		f.tick();
		}
	}
	private int generateFloors(){
		for(int i = 0; i <8; i++){
			Floor f = new Floor(i);
			floor.add(f);
		}
	}
	public int getNumberOfFloor(){
	return floors.size();
	}
}
