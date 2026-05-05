package com.example.databasehandling;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    SQLiteDatabase db;
    Button b1, b2;
    TextView t1;
    String str = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // make sure file name same

        b1 = findViewById(R.id.button1);
        b2 = findViewById(R.id.button2);
        t1 = findViewById(R.id.textView3);

        try {
            db = openOrCreateDatabase("StudentDB", MODE_PRIVATE, null);
            db.execSQL("CREATE TABLE IF NOT EXISTS Temp(id INTEGER, name TEXT)");
        } catch (SQLException e) {
            Toast.makeText(this, "DB Error", Toast.LENGTH_SHORT).show();
        }

        // INSERT
        b1.setOnClickListener(v -> {
            EditText eid = findViewById(R.id.editText1);
            EditText ename = findViewById(R.id.editText2);

            ContentValues values = new ContentValues();
            values.put("id", eid.getText().toString());
            values.put("name", ename.getText().toString());

            if (db.insert("Temp", null, values) != -1) {
                Toast.makeText(MainActivity.this, "Record Inserted ✅", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Insert Error ❌", Toast.LENGTH_SHORT).show();
            }
        });

        // DISPLAY
        b2.setOnClickListener(v -> {
            Cursor c = db.rawQuery("SELECT * FROM Temp", null);

            str = "";

            if (c.moveToFirst()) {
                do {
                    str += "ID: " + c.getString(0) + "\n";
                    str += "Name: " + c.getString(1) + "\n\n";
                } while (c.moveToNext());
            }

            t1.setText(str);
            c.close();
        });
    }
}