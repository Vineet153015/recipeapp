package com.example.recipeapp;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends Activity {

    private TextView timerText;
    private Button startTimerButton, resetTimerButton;
    private EditText toDoInput;
    private Button addTaskButton;
    private ListView toDoListView;
    private ArrayList<String> toDoList;
    private ArrayAdapter<String> toDoAdapter;
    private ArrayList<Long> taskStartTime; // Track the start time of each task
    private static final long POMODORO_TIME = 1500000;  // 25 minutes
    private CountDownTimer pomodoroTimer;
    private boolean isTimerRunning = false;
    private long timeRemaining = POMODORO_TIME;  // Track remaining time for resetting the timer

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timerText = findViewById(R.id.timerText);
        startTimerButton = findViewById(R.id.startTimerButton);
        resetTimerButton = findViewById(R.id.resetTimerButton);
        toDoInput = findViewById(R.id.toDoInput);
        addTaskButton = findViewById(R.id.addTaskButton);
        toDoListView = findViewById(R.id.toDoListView);

        toDoList = new ArrayList<>();
        taskStartTime = new ArrayList<>();
        toDoAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, toDoList);
        toDoListView.setAdapter(toDoAdapter);

        // Add task to the to-do list with the time
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String task = toDoInput.getText().toString();
                if (!task.isEmpty()) {
                    // Capture the current time and add to task list
                    String currentTime = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());
                    String taskWithTime = task + " (Added at: " + currentTime + ")";
                    toDoList.add(taskWithTime);
                    taskStartTime.add(System.currentTimeMillis());  // Track start time
                    toDoAdapter.notifyDataSetChanged();
                    toDoInput.setText("");
                } else {
                    Toast.makeText(MainActivity.this, "Task cannot be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Delete a task on long press
        toDoListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                toDoList.remove(position);
                taskStartTime.remove(position);  // Remove the associated start time
                toDoAdapter.notifyDataSetChanged();
                return true;
            }
        });

        // Mark task as completed on click and show completion duration
        toDoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                long endTime = System.currentTimeMillis();
                long startTime = taskStartTime.get(position);
                long durationMillis = endTime - startTime;

                // Calculate the duration in minutes
                long durationMinutes = (durationMillis / 1000) / 60;
                String durationMessage = "Task completed in " + durationMinutes + " minutes.";

                Toast.makeText(MainActivity.this, durationMessage, Toast.LENGTH_LONG).show();

                // Optionally, remove the task after marking it as completed
                toDoList.remove(position);
                taskStartTime.remove(position);
                toDoAdapter.notifyDataSetChanged();
            }
        });

        startTimerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isTimerRunning) {
                    stopPomodoro();  // Stop the timer if it's running
                } else {
                    startPomodoro();  // Start the timer if it's not running
                }
            }
        });

        resetTimerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPomodoro();  // Reset the Pomodoro timer
            }
        });
    }

    private void startPomodoro() {
        isTimerRunning = true;
        startTimerButton.setText("Stop Pomodoro");

        pomodoroTimer = new CountDownTimer(timeRemaining, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeRemaining = millisUntilFinished;
                long minutes = (millisUntilFinished / 1000) / 60;
                long seconds = (millisUntilFinished / 1000) % 60;
                timerText.setText(String.format("%02d:%02d", minutes, seconds));
            }

            @Override
            public void onFinish() {
                timerText.setText("25:00");
                startTimerButton.setText("Start Pomodoro");
                isTimerRunning = false;
                timeRemaining = POMODORO_TIME;
                Toast.makeText(MainActivity.this, "Pomodoro session completed!", Toast.LENGTH_SHORT).show();
            }
        }.start();
    }

    private void stopPomodoro() {
        isTimerRunning = false;
        pomodoroTimer.cancel();
        startTimerButton.setText("Start Pomodoro");
        timerText.setText(String.format("%02d:%02d", (timeRemaining / 1000) / 60, (timeRemaining / 1000) % 60));
    }

    private void resetPomodoro() {
        if (isTimerRunning && pomodoroTimer != null) {
            pomodoroTimer.cancel();
        }

        isTimerRunning = false;
        timeRemaining = POMODORO_TIME;
        startTimerButton.setText("Start Pomodoro");
        timerText.setText("25:00");
        Toast.makeText(MainActivity.this, "Pomodoro timer reset!", Toast.LENGTH_SHORT).show();
    }
}
