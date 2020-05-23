package com.example.Lab3_1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.util.Log;

import com.example.Lab3_1.R;
import com.example.Lab3_1.FeedReaderContract.FeedReaderDbHelper;
import com.example.Lab3_1.FeedReaderContract.FeedEntry;

import java.util.ArrayList;
import java.util.List;

public class SecondActivity extends AppCompatActivity {

    FeedReaderDbHelper dbHelper;
    SQLiteDatabase db;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_activity);

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        Log.d("MyTag", "Opening DB");
        dbHelper = new FeedReaderDbHelper(getApplicationContext());
        db = dbHelper.getReadableDatabase();

        Log.d("MyTag", "Done");

        String[] projection = {
                BaseColumns._ID,
                FeedEntry.COLUMN_NAME_FIO,
                FeedEntry.COLUMN_NAME_TIME
        };

        String sortOrder =
                FeedEntry.COLUMN_NAME_FIO + " ASC";

        Log.d("MyTag", "Cursor");

        Cursor cursor = db.query(
                FeedEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );

        List itemIDs = new ArrayList<>();
        List itemFIOs = new ArrayList<>();
        List itemDates = new ArrayList<>();

        Log.d("MyTag", "Reading");

        while (cursor.moveToNext()) {
            long itemId = cursor.getLong(
                    cursor.getColumnIndexOrThrow(BaseColumns._ID));
            String itemFIO = cursor.getString(
                    cursor.getColumnIndexOrThrow(FeedEntry.COLUMN_NAME_FIO));
            String itemDate = cursor.getString(
                    cursor.getColumnIndexOrThrow(FeedEntry.COLUMN_NAME_TIME));

            itemIDs.add(itemId);
            itemFIOs.add(itemFIO);
            itemDates.add(itemDate);

            Log.d("MyTag", "Id:" + itemId + "; FIO:" + itemFIO + "; Time:" + itemDate);
        }

        cursor.close();

        mAdapter = new MyAdapter(itemIDs, itemFIOs, itemDates);
        recyclerView.setAdapter(mAdapter);
    }


}
