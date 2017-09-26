package ddd;

public class RemoteWithId implements Remote {
	private int id;
	private DogDoor door;
	
	public RemoteWithId(DogDoor door, int id) {
		this.door = door;
		this.id = id;
	}

	@Override
	public void press() {
		door.press(id);
	}
	
	public int getId() {
		return id;
	}
	
	public DogDoor getDoor() {
		return door;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((door == null) ? 0 : door.hashCode());
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof RemoteWithId)
			return door.equals(((RemoteWithId) obj).door) && id == ((RemoteWithId) obj).id;
		
		return false;
	}
	
	

}
