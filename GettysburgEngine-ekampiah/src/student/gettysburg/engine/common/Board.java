package student.gettysburg.engine.common;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import gettysburg.common.Coordinate;
import gettysburg.common.GbgBoard;
import gettysburg.common.GbgUnit;
import student.gettysburg.engine.utility.configure.UnitInitializer;

public class Board implements GbgBoard{
	private Map<Coordinate, Collection<GbgUnit>> board;
	
	public Board(Map<Coordinate, Collection<GbgUnit>> units) {
		this.board = units;
	}
	
	public Board(List<UnitInitializer> config) {
		board = new HashMap<Coordinate, Collection<GbgUnit>>();
		config.forEach(unit -> {
			addUnit(unit.where, unit.unit);
		});
	}
	
	public void move(GbgUnit unit, Coordinate from, Coordinate to) {
		removeUnit(from, unit);
		addUnit(to, unit);
	}
	
	public void addUnit(Coordinate where, GbgUnit unit) {
		Collection<GbgUnit> previous = board.get(where);
		if(previous == null)
			previous = new HashSet<GbgUnit>();
		
		if(previous.contains(unit))
			previous.remove(unit);
		
		previous.add(unit);
		board.put(where, previous);
	}
	
	private void removeUnit(Coordinate where, GbgUnit unit) {
		Collection<GbgUnit> units = board.get(where);
		if(units != null && units.contains(unit))
			units.remove(unit);
	}
		
	public Collection<GbgUnit> getUnitsAt(Coordinate where){
		Collection<GbgUnit> units = board.get(where);
		return units.isEmpty()? null : units ;
	}
	
	public Coordinate getUnitLocation(GbgUnit unit) {
		for(Coordinate coord : board.keySet()) {
			if(board.get(coord).contains(unit))
				return coord;
		}
		
		return null;
	}
	
	public GbgUnit findUnit(GbgUnit unit) {
		do {
			Collection<GbgUnit> set = board.values().iterator().next();
			if(set.contains(unit)) {
				List<GbgUnit> helper = set.stream().collect(Collectors.toList());
				return helper.get(helper.indexOf(unit));
			}			
		}while(board.values().iterator().hasNext()) ;
		
		return null;
	}
	
}
