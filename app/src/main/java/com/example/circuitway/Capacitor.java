package com.example.circuitway;

import androidx.appcompat.content.res.AppCompatResources;

public class Capacitor extends Detail{

    public final int NominalContactCount = 2;

    public float Capacity = 0.0001f;

    public float Charge = 0;

    public Capacitor(CircuitActivity c, Pin ... pins){
        super(c, pins);

        Graphic.setBackground(AppCompatResources.getDrawable(c,
                R.drawable.d_capacitor));
        Graphic.getBackground().setTint(
                circuitActivity.getResources().getColor(R.color.mainTheme));
    }

    @Override
    public void step() {
        super.step();

    }

    @Override
    public void reset() {
        super.reset();
        Charge = 0;
    }

    @Override
    public float getResistance(){
        return 0;
    }
}
