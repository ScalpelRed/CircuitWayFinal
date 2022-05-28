package com.example.circuitway;

import androidx.appcompat.content.res.AppCompatResources;

public class Disistor extends Resistor {
    public final int NominalContactCount = 2;

    public Disistor(CircuitActivity c, Pin ... pins) {

        super(c, pins);

        Graphic.setBackground(AppCompatResources.getDrawable(c,
                R.drawable.d_disistor));
        Graphic.getBackground().setTint(
                circuitActivity.getResources().getColor(R.color.mainTheme));
        Resistance = 1;
    }

    @Override
    public String getEditorInfo() {
        String res = "Disistor\nResistance: " + getResistance();
        if (!isEditable) res += "\nLocked";
        return res;
    }

    @Override
    public void specialAction() {
        if (Resistance < 10) Resistance += 1;
        else if (Resistance < 100) Resistance += 10;
        else Resistance = 1;
    }

    @Override
    public float getResistance() {
        return -Resistance;
    }
}
