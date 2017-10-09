package model;

import gettysburg.common.BattleResolution;
import gettysburg.common.BattleResult;
import gettysburg.common.GbgUnit;

import java.util.Collection;
import java.util.HashSet;

public class Resolution implements BattleResolution {
    private BattleResult result;
    private Collection<GbgUnit> eliminatedUnionUnits, eliminatedConfederateUnits,
    activeUnionUnits, activeConfederateUnits;

    public Resolution(){
        eliminatedUnionUnits = new HashSet<>();
        eliminatedConfederateUnits = new HashSet<>();
        activeUnionUnits = new HashSet<>();
        activeConfederateUnits = new HashSet<>();
    }

    private Resolution(BattleResult result, Collection<GbgUnit> eliminatedUnionUnits, Collection<GbgUnit> eliminatedConfederateUnits,
                      Collection<GbgUnit> activeUnionUnits, Collection<GbgUnit> activeConfederateUnits) {
        this.result = result;
        this.eliminatedUnionUnits = eliminatedUnionUnits;
        this.eliminatedConfederateUnits = eliminatedConfederateUnits;
        this.activeUnionUnits = activeUnionUnits;
        this.activeConfederateUnits = activeConfederateUnits;
    }

    public static Resolution makeResolution(BattleResult result, Collection<GbgUnit> eliminatedUnionUnits,
                                            Collection<GbgUnit> eliminatedConfederateUnits,
                                            Collection<GbgUnit> activeUnionUnits, Collection<GbgUnit> activeConfederateUnits) {
        return new Resolution(result, eliminatedUnionUnits, eliminatedConfederateUnits, activeUnionUnits, activeConfederateUnits);
    }

    public static Resolution makeEmptyResolution(){
        return new Resolution();
    }

        @Override
    public BattleResult getBattleResult() {
        return result;
    }

    public void setBattleResult(BattleResult result){
        this.result = result;
    }

    @Override
    public Collection<GbgUnit> getEliminatedUnionUnits() {
        return eliminatedUnionUnits;
    }

    @Override
    public Collection<GbgUnit> getEliminatedConfederateUnits() {
        return eliminatedConfederateUnits;
    }

    @Override
    public Collection<GbgUnit> getActiveUnionUnits() {
        return activeUnionUnits;
    }

    @Override
    public Collection<GbgUnit> getActiveConfederateUnits() {
        return activeConfederateUnits;
    }
}
