package student.gettysburg.engine.common;

import gettysburg.common.BattleResolution;
import gettysburg.common.BattleResult;
import gettysburg.common.GbgUnit;

import java.util.Collection;

public class Resolution implements BattleResolution {
    BattleResult result;

    Collection<GbgUnit> eliminatedUnionUnits, eliminatedConfederateUnits,
    activeUnionUnits, activeConfederateUnits;

    public Resolution(BattleResult result, Collection<GbgUnit> eliminatedUnionUnits,
                      Collection<GbgUnit> eliminatedConfederateUnits, Collection<GbgUnit> activeUnionUnits,
                      Collection<GbgUnit> activeConfederateUnits) {
        this.result = result;
        this.eliminatedUnionUnits = eliminatedUnionUnits;
        this.eliminatedConfederateUnits = eliminatedConfederateUnits;
        this.activeUnionUnits = activeUnionUnits;
        this.activeConfederateUnits = activeConfederateUnits;
    }

    @Override
    public BattleResult getBattleResult() {
        return result;
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
