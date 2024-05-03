package com.example.remainderalarm;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

public class TableActivity extends AppCompatActivity {
    private ListView listView;
    private TableCustomAdapter customAdapter;
    private DatabaseHelper databaseHelper;
    private static final String TAG = "TableActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_table);
        Log.i(TAG, "onCreate: table activity");
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        listView = findViewById(R.id.listView);
        databaseHelper = new DatabaseHelper(this);

        // Retrieve data from SQLite database
        List<TableDataModel> dataList = databaseHelper.getAllReminders();
        Log.i("TableActvity", "dataList: "+ dataList);

        customAdapter = new TableCustomAdapter(this, R.layout.list_item, dataList);
        listView.setAdapter(customAdapter);

    }
}