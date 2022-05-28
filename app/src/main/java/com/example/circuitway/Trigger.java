package com.example.circuitway;

import androidx.appcompat.content.res.AppCompatResources;

public class Trigger extends Wire {
    public Trigger(CircuitActivity c, Pin ... pins){
        super(c, pins);

        Graphic.setBackground(AppCompatResources.getDrawable(c,
                R.drawable.d_trigger));
        Graphic.getBackground().setTint(
                circuitActivity.getResources().getColor(R.color.mainTheme));
    }

    @Override
    public String getEditorInfo() {
        String res = "Trigger\nNumber: " + ID;
        if (!isEditable) res += "\nLocked";
        return res;

    }
}
