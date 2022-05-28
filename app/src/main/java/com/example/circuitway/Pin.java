package com.example.circuitway;


import android.graphics.ColorSpace;
import android.view.View;
import android.widget.Button;
import android.widget.TableRow;

import androidx.appcompat.content.res.AppCompatResources;

import java.util.ArrayList;
import java.util.Objects;

public class Pin {

    private static int lastID = 0;
    public int ID;

    public float Potential = 0;
    public float balancePotential = 0;

    public ArrayList<Detail> Details = new ArrayList<>();

    Button Graphic;
    static ArrayList<Pin> selectedPins = new ArrayList<>();
    static int maxToSelect = 2;
    boolean isSelected = false;

    public int x;
    public int y;

    public CircuitActivity CircuitActivity;

    public Pin(CircuitActivity context, int x, int y){

        CircuitActivity = context;
        ID = lastID++;

        Graphic = new Button(context);
        Graphic.setBackground(AppCompatResources.getDrawable(context,
                R.drawable.circuit_dot));
        Graphic.getBackground().setTint(
                context.getResources().getColor(R.color.mainTheme));

        TableRow.LayoutParams params = new TableRow.LayoutParams(
                64, 64);
        Graphic.setLayoutParams(params);

        this.x = x;
        this.y = y;

        Graphic.setOnClickListener(v -> {
            if (!isSelected){
                if (checkCorrectnessFor(selectedPins.size() + 1,
                        Pin.this)) {
                    select();
                    if (selectedPins.size() == maxToSelect) {
                        Detail d = Detail.CreateDetail(
                                Detail.SelectedDetailType,
                                CircuitActivity,
                                selectedPins.toArray(new Pin[0]));
                        CircuitActivity.Details.add(d);

                        while (selectedPins.size() > 0) {
                            selectedPins.get(0).deselect();
                        }
                    }
                }
                else {
                    while (selectedPins.size() > 0) {
                        selectedPins.get(0).deselect();
                    }
                }
            }
            else {
                deselect();
            }
        });
    }

    void select(){
        selectedPins.add(this);
        Graphic.setAlpha(0.5f);
        isSelected = true;
        System.out.println("Selected pin " + ID);
    }

    void deselect(){
        selectedPins.remove(this);
        Graphic.setAlpha(1f);
        isSelected = false;
    }

    boolean checkCorrectnessFor(int n, Pin p){
        switch (n){
            case 1: return true;
            case 2:
                Pin sp = selectedPins.get(0);
                boolean res = (sp.x == p.x && Math.abs(sp.y - p.y) == 2)
                        || (sp.y == p.y && Math.abs(sp.x - p.x) == 2);
                System.out.println("The pattern is correct: " + res);
                return res;
            default: return false;
        }
    }

    public void postPotentialBalance() {
        Potential = balancePotential / Details.size();
        balancePotential = 0;
        //Graphic.setAlpha(Math.abs(Potential / 128f));
        //if (!Float.isNaN(Potential)) System.out.println("Potential on " + ID + " is " + Potential);
    }

    @Override
    public final boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Pin pin = (Pin) o;
        return ID == pin.ID;
    }

    @Override
    public final int hashCode() {
        return Objects.hash(ID);
    }

}
