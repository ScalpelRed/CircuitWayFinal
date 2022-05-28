package com.example.circuitway;

import java.util.ArrayList;
import java.util.Objects;

public class Branch {

    private static int lastID = 0;
    public int ID;

    public ArrayList<Detail> Details = new ArrayList<>();
    public Pin EdgePin1;
    public Pin EdgePin2;

    public CircuitActivity circuitActivity;

    public float Resistance;

    public Branch(CircuitActivity c) {
        circuitActivity = c;
        c.Branches.add(this);
        ID = lastID++;
    }

    public void CalculateResistance(){
        Resistance = 0;
        for (Detail v : Details) Resistance += v.Resistance;
    }

    public void FindEdges() {
        for (Detail v : Details){
            if (v.Pins[0].Details.size() != 2) {
                if (EdgePin1 == null) EdgePin1 = v.Pins[0];
                else EdgePin2 = v.Pins[0];
            }
            if (v.Pins[1].Details.size() != 2) {
                if (EdgePin1 == null) EdgePin1 = v.Pins[1];
                else EdgePin2 = v.Pins[1];
            }
        }
    }

    public float GetRelativeVoltageFor(Pin pin){
        if (EdgePin1 == pin) return pin.Potential - EdgePin2.Potential;
        else if (EdgePin2 == pin) return pin.Potential - EdgePin1.Potential;

        System.out.println("Pin " + pin.ID + " is not an edge pin of " + ID);
        return Float.NaN;
    }

    @Override
    public final boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Branch branch = (Branch) o;
        return ID == branch.ID;
    }

    @Override
    public final int hashCode() {
        return Objects.hash(ID);
    }

}
