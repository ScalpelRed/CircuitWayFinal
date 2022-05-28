package com.example.circuitway;

import androidx.appcompat.content.res.AppCompatResources;

public class Resistor extends Wire {

    public Resistor(CircuitActivity c, Pin ... pins) {

        super(c, pins);

        Graphic.setBackground(AppCompatResources.getDrawable(c,
                R.drawable.d_resistor));
        Graphic.getBackground().setTint(
                circuitActivity.getResources().getColor(R.color.mainTheme));
        Resistance = 1;
    }

    @Override
    public String getEditorInfo() {
        String res = "Resistor\nResistance: " + getResistance();
        if (!isEditable) res += "\nLocked";
        return res;
    }

    @Override
    public void specialAction() {
        if (Resistance < 10) Resistance += 1;
        else if (Resistance < 100) Resistance += 10;
        else Resistance = 1;
    }
}
