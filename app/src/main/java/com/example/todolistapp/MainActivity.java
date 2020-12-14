package com.example.todolistapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import com.example.todolistapp.Adapter.ToDoAdapter;
import com.example.todolistapp.Model.ToDoModel;
import com.example.todolistapp.Utils.DatabaseHandler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements DialogCloseListener{

    private DatabaseHandler db;
    private RecyclerView taskRecyclerView;
    private ToDoAdapter tasksAdapter;

    private FloatingActionButton fab;
    private List<ToDoModel>taskList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).hide();

        db=new DatabaseHandler(this);
        db.openDatabase();

        taskRecyclerView=findViewById(R.id.tasksRecyclerView);
        taskRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        tasksAdapter=new ToDoAdapter(db,MainActivity.this);
        taskRecyclerView.setAdapter(tasksAdapter);


        ItemTouchHelper itemTouchHelper=new ItemTouchHelper(new RecyclerItemTouchHelper(tasksAdapter));
        itemTouchHelper.attachToRecyclerView(taskRecyclerView);

        fab=findViewById(R.id.fab);

       taskList=db.getAllTasks();
       Collections.reverse(taskList);
       tasksAdapter.setTasks(taskList);

       fab.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               AddNewTask.newInstance().show(getSupportFragmentManager(),AddNewTask.TAG);
           }
       });
    }

    @Override
    public void handleDialogClose(DialogInterface dialog){
        taskList=db.getAllTasks();
        Collections.reverse(taskList);
        tasksAdapter.setTasks(taskList);
        tasksAdapter.notifyDataSetChanged();
    }
}