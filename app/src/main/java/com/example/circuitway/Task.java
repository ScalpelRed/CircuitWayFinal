package com.example.circuitway;

import android.Manifest;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;

import org.w3c.dom.Text;

import java.util.concurrent.Callable;

public class Task {

    TaskObjective objective;
    TaskCondition condition;
    float value;
    Detail detail;

    Callable<Boolean> checkFunc;

    CircuitActivity circuitActivity;

    public Task(Detail d, TaskObjective o, TaskCondition c, float v,
                CircuitActivity c2){
        circuitActivity = c2;
        value = v;
        objective = o;
        condition = c;
        detail = d;
        autoConfigure(o, c);
    }

    private void autoConfigure(TaskObjective o, TaskCondition c) {
        byte t = 0;

        switch (o){
            case CURRENT: t += 10; break;
            case VOLTAGE: t += 20; break;
            case RESISTANCE: t += 30; break;
        }

        switch (c){
            case MINIMUM: t += 1; break;
            case MAXIMUM: t += 2; break;
        }

        switch (t){
            case 11:
                checkFunc = () -> {
                    System.out.println(detail.getCurrent() + " >= " + value);
                    return detail.getCurrent() >= value;
                };
                break;
            case 12:
                checkFunc = () -> {
                    System.out.println(detail.getCurrent() + " <= " + value);
                    return detail.getCurrent() <= value;
                };
                break;
            case 21:
                checkFunc = () -> {
                    return detail.getVoltage() >= value;
                };
                break;
            case 22:
                checkFunc = () -> {
                    return detail.getVoltage() <= value;
                };
                break;
            case 31:
                checkFunc = () -> {
                    return detail.getResistance() >= value;
                };
                break;
            case 32:
                checkFunc = () -> {
                    return detail.getResistance() <= value;
                };
                break;
        }
        System.out.println("aaaaa " + toString() + " " + t);
    }

    public boolean checkCompleteness() {
        try{
            return checkFunc.call();
        }
        catch (Exception e) { return false; }
    }


    public enum TaskObjective {
        CURRENT,
        VOLTAGE,
        RESISTANCE,
    }

    public enum TaskCondition {
        MINIMUM,
        MAXIMUM,
    }

    @Override
    public String toString(){
        String res = "> ";
        switch (condition){
            case MINIMUM: res += "Reach "; break;
            case MAXIMUM: res += "Do not exceed "; break;
        }
        res += value;
        switch (objective){
            case CURRENT: res += " current "; break;
            case VOLTAGE: res += " voltage "; break;
            case RESISTANCE: res += " resistance "; break;
        }
        res += "on trigger № " + detail.ID + "\n";
        return res;
    }

}
