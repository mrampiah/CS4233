package model;

import gettysburg.common.BattleDescriptor;
import gettysburg.common.GbgUnit;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;

public class Battle implements BattleDescriptor {

    private Collection<GbgUnit> attackers, defenders;

    private Battle() {
        attackers = new HashSet<>(); //sets to prevent duplication
        defenders = new HashSet<>();
    }

    private Battle(Collection<GbgUnit> attackers, Collection<GbgUnit> defenders) {
        this.attackers = attackers;
        this.defenders = defenders;
    }

    public static Battle makeEmptyBattle(){
        return new Battle();
    }

    public static Battle makeBattle(Collection<GbgUnit> attackers, Collection<GbgUnit> defenders){
        return new Battle(attackers, defenders);
    }

    @Override
    public Collection<GbgUnit> getAttackers() {
        return attackers;
    }

    @Override
    public Collection<GbgUnit> getDefenders() {
        return defenders;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof Battle)) return false;

        Battle battle = (Battle) o;

        return attackers.containsAll(((Battle) o).attackers) && defenders.containsAll(((Battle) o).defenders);

    }

    @Override
    public int hashCode() {
        int result = attackers != null ? attackers.hashCode() : 0;
        result = 31 * result + (defenders != null ? defenders.hashCode() : 0);
        return result;
    }
}
