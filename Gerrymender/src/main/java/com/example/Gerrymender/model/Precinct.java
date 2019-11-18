package com.example.Gerrymender.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

@Table(name="Precinct")
@Entity
public class Precinct implements Serializable {

    private static final long SerializeID = 2L; //or some other ID

    @Id //ID Annotation for the subclasses
    @Column(name="precinctId")
    private String nameID; //replaces name and ID in state, precinct, district

    @Column(name="totalPop")
    private long totalPop;
    @Column(name="Pol_Part")
    private Pol_part party;

    @Column(name="countyName")
    private String countyName;


    @Column(name="whitePop")
    private long white_pop;
    @Column(name="africanAmericanPop")
    private long africanAmerican_pop;
    @Column(name="hispanicPop")
    private long hispanic_pop;
    @Column(name="asianPop")
    private long asian_pop;
    @Column(name="nativePop")
    private long nativeAmerican_pop;

    public long getNativeAmerican_pop() {
        return nativeAmerican_pop;
    }

    public void setNativeAmerican_pop(long nativeAmerican_pop) {
        this.nativeAmerican_pop = nativeAmerican_pop;
    }

    public long getAsian_pop() {
        return asian_pop;
    }

    public void setAsian_pop(long asian_pop) {
        this.asian_pop = asian_pop;
    }

    public long getHispanic_pop() {
        return hispanic_pop;
    }

    public void setHispanic_pop(long hispanic_pop) {
        this.hispanic_pop = hispanic_pop;
    }

    public long getAfricanAmerican_pop() {
        return africanAmerican_pop;
    }

    public void setAfricanAmerican_pop(long africanAmerican_pop) {
        this.africanAmerican_pop = africanAmerican_pop;
    }

    public long getWhite_pop() {
        return white_pop;
    }

    public void setWhite_pop(long white_pop) {
        this.white_pop = white_pop;
    }


    public String getNameID() {
        return nameID;
    }

    public void setNameID(String nameID) {
        assertNotEquals(nameID, "");
        this.nameID = nameID;
    }

    public long getTotalPop() {
        return totalPop;
    }

    public void setTotalPop(long totalPop) {
        assert(totalPop>0);
        this.totalPop = totalPop;
    }


    public Pol_part getParty() {
        return party;
    }

    public void setParty(Pol_part party) {
        this.party = party;
    }



    @Id //Composite Key for Precincts to know what district they're part of
    @Column(name="districtID")
    private String districtID; //Can't have a state object, as that would bring in circular dependencies.
    private Race votingBlockRace;

    public String getDistrictID(){return districtID;}
    public void setDistrictID(String districtID ){this.districtID=districtID;}


    public Race getVotingBlockRace() {
        return votingBlockRace;
    }

    public void setVotingBlockRace(Race votingBlockRace) {
        this.votingBlockRace = votingBlockRace;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }
}
