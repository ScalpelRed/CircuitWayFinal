package com.example.circuitway;

import androidx.appcompat.content.res.AppCompatResources;

public class Wire extends Detail {

    public final int NominalContactCount = 2;

    public Wire(CircuitActivity c, Pin ... pins) {
        super(c, pins);

        Graphic.setBackground(AppCompatResources.getDrawable(c,
                R.drawable.d_wire));
        Graphic.getBackground().setTint(
                circuitActivity.getResources().getColor(R.color.mainTheme));
    }

    @Override
    public void step() {
        super.step();

    }

    @Override
    public float getCurrent() {
        super.getCurrent();
        return getVoltage() / getBranchResistance();
    }

    @Override
    public float getResistance(){
        return 0.1f;
    }

    @Override
    public void balancePotentials(){
        Pins[0].balancePotential += Pins[1].Potential;
        Pins[1].balancePotential += Pins[0].Potential;
    }

    @Override
    public String getEditorInfo() {
        String res = "Wire\nResistance: 0.1";
        if (!isEditable) res += "\nLocked";
        return res;

    }
}
