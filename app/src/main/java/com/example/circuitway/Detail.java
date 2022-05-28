package com.example.circuitway;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.res.Resources;
import android.graphics.Color;
import android.telecom.Call;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableRow;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import java.util.ArrayList;
import java.util.Objects;

public class Detail {

    public static int lastID = 0;
    public int ID;
    public boolean isEditable = true;

    public float Resistance;
    public Pin[] Pins;
    public final int NominalContactCount = 0;

    public Branch Branch;
    public Detail LastBranchCheckSource;

    public Button GraphicButton;
    public ImageView Graphic;
    public ImageView SelectionGraphic;

    public static DetailType SelectedDetailType = DetailType.WIRE;

    CircuitActivity circuitActivity;

    public Detail(CircuitActivity c, Pin[] pins){
        circuitActivity = c;

        ID = lastID++;
        Pins = pins;
        for (int i = 0; i < pins.length; i++){
            pins[i].Details.add(this);
        }

        GraphicButton = new Button(c);
        GraphicButton.setBackground(AppCompatResources.getDrawable(c,
                R.drawable.d_transparent));
        GraphicButton.getBackground().setTint(
                circuitActivity.getResources().getColor(R.color.background));

        Graphic = new ImageView(c);
        Graphic.setBackground(AppCompatResources.getDrawable(c,
                R.drawable.d_unknown));
        Graphic.getBackground().setTint(
                circuitActivity.getResources().getColor(R.color.mainTheme));

        SelectionGraphic = new ImageView(c);
        SelectionGraphic.setBackground(AppCompatResources.getDrawable(c,
                R.drawable.selectionbox));
        SelectionGraphic.getBackground().setTint(
                circuitActivity.getResources().getColor(R.color.mainTheme));
        SelectionGraphic.setVisibility(GONE);

        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                64, 64);
        ConstraintLayout.LayoutParams params2 = new ConstraintLayout.LayoutParams(
                128, 64);
        ConstraintLayout.LayoutParams params3 = new ConstraintLayout.LayoutParams(
                128, 64);

        params.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
        params.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID;
        params2.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
        params2.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID;
        params3.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
        params3.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID;

        if (Pins[0].x < Pins[1].x){
            params.setMargins(64 * Pins[0].x + 64, 64 * Pins[0].y, 0, 0);
            params2.setMargins(64 * Pins[0].x + 32, 64 * Pins[0].y, 0, 0);
            params3.setMargins(64 * Pins[0].x + 32, 64 * Pins[0].y, 0, 0);
        }
        else if (Pins[0].x > Pins[1].x){
            params.setMargins(64 * Pins[1].x + 64, 64 * Pins[0].y, 0, 0);
            params2.setMargins(64 * Pins[1].x + 32, 64 * Pins[0].y, 0, 0);
            params3.setMargins(64 * Pins[1].x + 32, 64 * Pins[0].y, 0, 0);
        }
        else if (Pins[0].y < Pins[1].y){
            params.setMargins(64 * Pins[0].x, 64 * Pins[0].y + 64, 0, 0);
            params2.setMargins(64 * Pins[0].x - 32, 64 * Pins[0].y + 64, 0, 0);
            params3.setMargins(64 * Pins[0].x - 32, 64 * Pins[0].y + 64, 0, 0);
            Graphic.setRotation(90);
            SelectionGraphic.setRotation(90);
        }
        else if (Pins[0].y > Pins[1].y){
            params.setMargins(64 * Pins[0].x, 64 * Pins[1].y + 64, 0, 0);
            params2.setMargins(64 * Pins[0].x - 32, 64 * Pins[1].y + 64, 0, 0);
            params3.setMargins(64 * Pins[0].x - 32, 64 * Pins[1].y + 64, 0, 0);
            Graphic.setRotation(90);
            SelectionGraphic.setRotation(90);
        }

        GraphicButton.setLayoutParams(params);
        Graphic.setLayoutParams(params2);
        SelectionGraphic.setLayoutParams(params3);
        c.DetailField.addView(GraphicButton);
        c.DetailField.addView(Graphic);
        c.DetailField.addView(SelectionGraphic);

        GraphicButton.setOnClickListener(v ->
        {
            if (circuitActivity.SelectedDetail != this) {
                circuitActivity.SetSelectedDetail(this);
                SelectionGraphic.setVisibility(VISIBLE);
            }
            else {
                circuitActivity.SetSelectedDetail(null);
            }

        });

    }



    public Branch getBranch(Detail d) {
        if (Branch == null){
            for (Pin v : Pins){
                if (v.Details.size() == 2){
                    for (Detail b : v.Details){
                        if (b != this && b.LastBranchCheckSource != d){
                            b.LastBranchCheckSource = d;
                            Branch = b.getBranch(d);
                            Branch.Details.add(this);
                            return Branch;
                        }
                    }
                }
            }
        }
        else return Branch;

        Branch = new Branch(circuitActivity);
        return Branch;
    }

    public float getResistance() { return Resistance; }
    public float getBranchResistance(){
        return Branch.Resistance;
    }

    public void balancePotentials(){

    }

    public float getCurrent(){
        return Float.NaN;
    }

    public float getVoltage(){ return Math.abs(Pins[0].Potential - Pins[1].Potential); }

    public void step(){

    }
    public void reset(){

    }

    public String getEditorInfo(){
        String res = "The detail is broken.";
        if (!isEditable) res += "\nLocked";
        return res;
    }

    public String getRunningInfo(){
        String res = getEditorInfo();
        res += "\nVoltage: " + getVoltage();
        res += "\nCurrent: " + getCurrent();
        return res;
    }

    public final void detach(){
        for (int i = 0; i < Pins.length; i++){
            Pins[i].Details.remove(this);
        }
        circuitActivity.Details.remove(this);
        circuitActivity.DetailField.removeView(GraphicButton);
        circuitActivity.DetailField.removeView(Graphic);
        circuitActivity.DetailField.removeView(SelectionGraphic);
    }

    public void specialAction(){

    }

    @Override
    public final boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Detail detail = (Detail) o;
        return ID == detail.ID;
    }

    @Override
    public final int hashCode() {
        return Objects.hash(ID);
    }



    public enum DetailType {
        INVALID,
        WIRE,
        BATTERY,
        SWITCH,
        RESISTOR,
        DISISTOR,
        TRIGGER,
        CAPACITOR
    }

    public static Detail CreateDetail(DetailType t, CircuitActivity c, Pin ... pins) {
        switch (t){
            case INVALID: return new Detail(c, pins);
            case WIRE: return new Wire(c, pins);
            case BATTERY: return new Battery(c, pins);
            case SWITCH: return new Switch(c, pins);
            case RESISTOR: return new Resistor(c, pins);
            case DISISTOR: return new Disistor(c, pins);
            case TRIGGER: return new Trigger(c, pins);
            default: return null;
        }
    }
}
