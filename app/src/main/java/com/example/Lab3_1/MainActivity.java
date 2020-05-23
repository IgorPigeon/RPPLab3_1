package com.example.Lab3_1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.Lab3_1.R;
import com.example.Lab3_1.FeedReaderContract.FeedReaderDbHelper;
import com.example.Lab3_1.FeedReaderContract.FeedEntry;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends AppCompatActivity {

    Button but1;
    Button but2;
    Button but3;
    Intent intent;
    FeedReaderDbHelper dbHelper;
    SQLiteDatabase db;
    long lastRow;

    int currentFIO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentFIO = 0;
        lastRow = 0;

        intent = new Intent(this, SecondActivity.class);

        but1 = (Button) findViewById(R.id.button1);
        but2 = (Button) findViewById(R.id.button2);
        but3 = (Button) findViewById(R.id.button3);

        dbHelper = new FeedReaderDbHelper(getApplicationContext());
        db = dbHelper.getWritableDatabase();
        dbHelper.deleteData(db);

        for (int i=0; i<5; i++) {
            lastRow = writeToBD();
        }

        but1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent);
            }
        });

        but2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastRow = writeToBD();
            }
        });

        but3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues values = new ContentValues();
                values.put(BaseColumns._ID, lastRow);
                values.put(FeedEntry.COLUMN_NAME_FIO, "Pupkin Vasiliy Ivanovich");
                values.put(FeedEntry.COLUMN_NAME_TIME, getCurrentTime());

                db.replaceOrThrow(FeedEntry.TABLE_NAME, null, values);

                Toast toast = Toast.makeText(MainActivity.this, lastRow+" record was changed", Toast.LENGTH_SHORT);
                toast.show();

            }
        });

    }

    @Override
    protected void onDestroy() {
        dbHelper.deleteData(db);
        dbHelper.close();
        super.onDestroy();
    }

    private String readFromRaw(int numFIO)throws ClassCastException, IOException {
        InputStream buffer  = getResources().openRawResource(R.raw.students);

        int findEn = 0;
        int c;
        StringBuilder strFIO = new StringBuilder("");
        while((c=buffer.read())!=-1){
            if((char)c == '\n'){
                findEn++;
            }
            else if(findEn == numFIO) {
                strFIO.append((char)c);
            }
        }

        Log.d("MyTag", strFIO.toString());
        return strFIO.toString();
    }

    private String getCurrentTime(){
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    private long writeToBD(){

        Toast toast;

        if (currentFIO<30) {
            ContentValues values = new ContentValues();
            String full_name = "";
            try {
                full_name = readFromRaw(currentFIO);
            } catch (IOException e) {
                e.printStackTrace();
                full_name = "Reading error.";
            }

            values.put(FeedEntry.COLUMN_NAME_FIO, full_name);
            values.put(FeedEntry.COLUMN_NAME_TIME, getCurrentTime());

            long keyRow = db.insert(FeedEntry.TABLE_NAME, null, values);

            values.clear();

            currentFIO++;

            toast = Toast.makeText(this, "The record was added", Toast.LENGTH_SHORT);
            toast.show();

            return keyRow;
        }

        toast = Toast.makeText(this, "Out of students", Toast.LENGTH_SHORT);
        toast.show();

        return lastRow;
    }
}
