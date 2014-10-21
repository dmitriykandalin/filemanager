package com.app.kandalin.filemanager;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {

    private List<String> directoryEntries = new ArrayList<String>();
    private File currentDirectory = new File("/");
    private TextView headerTextView;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_actrivity);

        headerTextView = (TextView) findViewById(R.id.headerTextView);
        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                invokeOnItemClickHandler(position);
            }
        });

        //browse to root directory
        browseTo(new File("/"));
    }

    private void browseTo(final File directory){
        if (directory.isDirectory()){
            this.currentDirectory = directory;
            fill(directory.listFiles());

            headerTextView.setText(directory.getAbsolutePath());//set header text
        } else {
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("file://" + directory.getAbsolutePath()));
            startActivity(intent);
        }
    }

    private void fill(File[] files){
        this.directoryEntries.clear();

        if (this.currentDirectory.getParent() != null)
            this.directoryEntries.add("..");

        //add every file into list
        for (File file : files) {
            this.directoryEntries.add(file.getAbsolutePath());
        }
        //create array adapter to show everything
        ArrayAdapter<String> directoryList = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, this.directoryEntries);
        this.listView.setAdapter(directoryList);
    }


    private void invokeOnItemClickHandler(int position) {
        //get selected file name
        int selectionRowID = position;
        String selectedFileString = this.directoryEntries.get(selectionRowID);

        //if we select ".." then go upper
        if(selectedFileString.equals("..")){
            this.upOneLevel();
        } else {
            //browse to clicked file or directory using browseTo()
            File clickedFile = null;
            clickedFile = new File(selectedFileString);
            if (clickedFile != null)
                this.browseTo(clickedFile);
        }
    }

    //browse to parent directory
    private void upOneLevel(){
        if(this.currentDirectory.getParent() != null) {
            this.browseTo(this.currentDirectory.getParentFile());
        }
    }
}
