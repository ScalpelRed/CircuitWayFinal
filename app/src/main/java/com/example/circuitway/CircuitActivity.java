package com.example.circuitway;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Layout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.sql.Time;
import java.util.ArrayList;
import java.util.GregorianCalendar;

public class CircuitActivity extends AppCompatActivity {

    Resources resources;

    public ArrayList<Branch> Branches = new ArrayList<>();
    public ArrayList<Detail> Details = new ArrayList<>();
    public ArrayList<Pin> Pins = new ArrayList<>();
    public ArrayList<Task> Tasks = new ArrayList<>();
    
    public Detail SelectedDetail = null;

    public TableLayout PinField;
    public ConstraintLayout DetailField;
    public RelativeLayout CircuitField;

    public ConstraintLayout EditorButtons; //
    public Button RemoveDetailButton;
    public Button SpecialActionButton;

    public ConstraintLayout RunningControls; //
    public Button PauseButton;
    public Button StopButton;
    public Button ResumeButton;

    public ConstraintLayout EditorControls; //
    public Button StartButton;

    public ConstraintLayout SelectionView; //
    public Button SelectWireButton;
    public Button SelectBatteryButton;
    public Button SelectSwitchButton;
    public Button SelectResistorButton;
    public Button SelectDisistorButton;
    public ImageView DetailSelection;
    public Button OpenSelection;

    public TextView InfoText;

    public ConstraintLayout CircuitView;
    public ConstraintLayout TaskView;
    public TextView TaskList;
    public ConstraintLayout LowerTask = null;
    public Button CloseTasksButton;
    public Button OpenTasksButton;

    public TextView MessengerText;

    public int timeStep = 100;
    public int taskStep = 1000;
    public boolean isRunning = false;
    public boolean isPaused = false;

    public CountDownTimer SimulationTimer;
    public CountDownTimer TaskTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circuit);

        resources = getResources();

        PinField = findViewById(R.id.PinField);
        DetailField = findViewById(R.id.DetailField);
        CircuitField = findViewById(R.id.CircuitField);

        EditorButtons = findViewById(R.id.editorButtons);
        RemoveDetailButton = findViewById(R.id.RemoveButton);
        SpecialActionButton = findViewById(R.id.ActionButton);

        RunningControls = findViewById(R.id.runningControls);
        PauseButton = findViewById(R.id.PauseButton);
        StopButton = findViewById(R.id.StopButton);
        ResumeButton = findViewById(R.id.ResumeButton);

        EditorControls = findViewById(R.id.editorControls);
        StartButton = findViewById(R.id.StartButton);

        SelectionView = findViewById(R.id.selectionView);
        SelectWireButton = findViewById(R.id.DSB_wire);
        SelectBatteryButton = findViewById(R.id.DSB_battery);
        SelectSwitchButton = findViewById(R.id.DSB_switch);
        SelectResistorButton = findViewById(R.id.DSB_resistor);
        SelectDisistorButton = findViewById(R.id.DSB_disistor);
        DetailSelection = findViewById(R.id.detailSelection);
        OpenSelection = findViewById(R.id.OpenSelectionButton);

        InfoText = findViewById(R.id.InfoText);

        CircuitView = findViewById(R.id.circuitView);
        TaskView = findViewById(R.id.taskView);
        TaskList = findViewById(R.id.taskList);
        CloseTasksButton = findViewById(R.id.closeTasks);
        OpenTasksButton = findViewById(R.id.openTasks);

        MessengerText = findViewById(R.id.MessengerText);


        SimulationTimer = new CountDownTimer(Long.MAX_VALUE, timeStep) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (!isPaused) simulationStep();
            }

            @Override
            public void onFinish() {
                MessengerText.setText("Internal timer expired.");
            }
        };
        TaskTimer = new CountDownTimer(Long.MAX_VALUE, taskStep) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (!isPaused) checkLevelCompleteness();
            }

            @Override
            public void onFinish() {
                MessengerText.setText("Internal timer expired.");
            }
        };


        StartButton.setOnClickListener(v -> runSimulation());

        PauseButton.setOnClickListener(v -> pauseSimulation());
        StopButton.setOnClickListener(v -> StopSimulation());
        ResumeButton.setOnClickListener(v -> resumeSimulation());

        SelectWireButton.setOnClickListener(v
                -> SetSelectedDetailType(Detail.DetailType.WIRE));
        SelectBatteryButton.setOnClickListener(v
                -> SetSelectedDetailType(Detail.DetailType.BATTERY));
        SelectSwitchButton.setOnClickListener(v
                -> SetSelectedDetailType(Detail.DetailType.SWITCH));
        SelectResistorButton.setOnClickListener(v
                -> SetSelectedDetailType(Detail.DetailType.RESISTOR));
        SelectDisistorButton.setOnClickListener(v
                -> SetSelectedDetailType(Detail.DetailType.DISISTOR));

        RemoveDetailButton.setOnClickListener(v -> {
                        SelectedDetail.detach();
                        SetSelectedDetail(null);
                    });
        SpecialActionButton.setOnClickListener(v -> {
            SelectedDetail.specialAction();
            SetInfoText(SelectedDetail.getEditorInfo());
        });

        OpenTasksButton.setOnClickListener(v -> {
            CircuitView.setVisibility(GONE);
            TaskView.setVisibility(VISIBLE);
        });
        CloseTasksButton.setOnClickListener(v -> {
            CircuitView.setVisibility(VISIBLE);
            TaskView.setVisibility(GONE);
        });
        TaskView.setVisibility(GONE);

        SelectionView.setVisibility(GONE);
        OpenSelection.setOnClickListener(v -> {
            SelectionView.setVisibility(VISIBLE);
            CircuitView.setVisibility(GONE);
        });

        RunningControls.setVisibility(GONE);

        Bundle intent = getIntent().getExtras();
        int level = intent.getInt("level");
        LevelLoader.Open(level, this);
    }

    public void runSimulation(){

        EditorButtons.setVisibility(GONE);
        EditorControls.setVisibility(GONE);
        OpenSelection.setVisibility(GONE);
        RunningControls.setVisibility(VISIBLE);

        ResumeButton.setVisibility(GONE);
        PauseButton.setVisibility(VISIBLE);

        OpenSelection.setVisibility(GONE);

        isRunning = true;

        for (Detail d : Details){
            d.getBranch(d);
            //System.out.println("The branch of " + d.ID + " is " + d.Branch.ID);
        }
        for (Branch b : Branches){
            b.FindEdges();
        }

        SimulationTimer.start();
        TaskTimer.start();

    }

    public void pauseSimulation(){
        isPaused = true;
        PauseButton.setVisibility(GONE);
        ResumeButton.setVisibility(VISIBLE);
    }

    public void StopSimulation(){
        isRunning = false;
        SimulationTimer.cancel();
        TaskTimer.cancel();

        EditorButtons.setVisibility(VISIBLE);
        EditorControls.setVisibility(VISIBLE);
        OpenSelection.setVisibility(VISIBLE);
        RunningControls.setVisibility(GONE);

        OpenSelection.setVisibility(VISIBLE);

        for (Pin p : Pins) p.Potential = 0;
        for (Detail d : Details) d.reset();
    }

    public void resumeSimulation(){
        isPaused = false;
        PauseButton.setVisibility(VISIBLE);
        ResumeButton.setVisibility(GONE);
    }

    private void simulationStep(){
        for (Detail d : Details){
            d.balancePotentials();
        }
        for (Pin p : Pins){
            p.postPotentialBalance();
        }
        for (Branch b : Branches){
            b.CalculateResistance();
        }
        for (Detail d : Details){
            d.step();
        }
        if (SelectedDetail != null){
            SetInfoText(SelectedDetail.getRunningInfo());
        }
    }

    public boolean checkLevelCompleteness(){
        for (Task t : Tasks)
        {
            if (!t.checkCompleteness()) { return false; }
        }

        MessengerText.setText("Level completed.");
        return true;
    }

    public void SetInfoText(String text){
        InfoText.setText(text);
    }

    public void SetSelectedDetailType(Detail.DetailType t) {
        Button t2 = null;
        switch (t){
            case WIRE:
                t2 = SelectWireButton;
                Detail.SelectedDetailType = Detail.DetailType.WIRE;
                break;
            case BATTERY:
                t2 = SelectBatteryButton;
                Detail.SelectedDetailType = Detail.DetailType.BATTERY;
                break;
            case SWITCH:
                t2 = SelectSwitchButton;
                Detail.SelectedDetailType = Detail.DetailType.SWITCH;
                break;
            case RESISTOR:
                t2 = SelectResistorButton;
                Detail.SelectedDetailType = Detail.DetailType.RESISTOR;
                break;
            case DISISTOR:
                t2 = SelectDisistorButton;
                Detail.SelectedDetailType = Detail.DetailType.DISISTOR;
                break;
        }
        ConstraintLayout.LayoutParams p
                = (ConstraintLayout.LayoutParams)DetailSelection.getLayoutParams();
        p.topToTop = t2.getId();
        p.leftToLeft = t2.getId();
        DetailSelection.setLayoutParams(p);

        SelectionView.setVisibility(GONE);
        CircuitView.setVisibility(VISIBLE);
    }

    public void SetSelectedDetail(Detail d){
        SelectedDetail = d;
        for (Detail d2 : Details)
            d2.SelectionGraphic.setVisibility(GONE);
        if (d == null) {
            SetInfoText("Select a detail to view information about it.");
            EditorButtons.setVisibility(GONE);
        }
        else if (!isRunning) {
            SetInfoText(d.getEditorInfo());
            if (d.isEditable) EditorButtons.setVisibility(VISIBLE);
            else EditorButtons.setVisibility(GONE);
        }
        else {
            SetInfoText(d.getRunningInfo());
        }
    }

    public Detail GetDetailByID(int id){
        for (Detail d : Details) if (d.ID == id) return d;
        return null;
    }



}