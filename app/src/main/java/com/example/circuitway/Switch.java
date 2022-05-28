package com.example.circuitway;

import androidx.appcompat.content.res.AppCompatResources;

public class Switch extends Wire {

    public float ClosedResistance = Float.POSITIVE_INFINITY;

    public boolean opened = false;

    public Switch(CircuitActivity c, Pin ... pins){
        super(c, pins);

        Graphic.setBackground(AppCompatResources.getDrawable(c,
                R.drawable.d_switch_off));
        Graphic.getBackground().setTint(
                circuitActivity.getResources().getColor(R.color.mainTheme));
    }

    @Override
    public float getCurrent() {
        super.getCurrent();
        return getVoltage() / getBranchResistance();
    }

    @Override
    public void specialAction() {
        super.specialAction();
        opened = !opened;
        if (opened) Graphic.setBackground(AppCompatResources.getDrawable(circuitActivity,
                R.drawable.d_switch_on));
        else Graphic.setBackground(AppCompatResources.getDrawable(circuitActivity,
                R.drawable.d_switch_off));
        Graphic.getBackground().setTint(
                circuitActivity.getResources().getColor(R.color.mainTheme));
    }

    @Override
    public float getResistance() {
        if (opened) return Resistance;
        else return ClosedResistance;
    }

    @Override
    public String getEditorInfo() {
        String res = "Switch\nOpened: " + opened;
        if (!isEditable) res += "\nLocked";
        return res;
    }
}
