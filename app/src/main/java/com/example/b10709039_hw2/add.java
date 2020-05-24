package com.example.b10709039_hw2;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class add extends AppCompatActivity {
    EditText tb ;
    EditText nb ;
    SQLiteDatabase db=null;
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add);
        tb = findViewById(R.id.tb);
        nb = findViewById(R.id.nb);
        Button ok = findViewById(R.id.ok);
        ok.setOnClickListener(okbt);
        Button cannel = findViewById(R.id.cannel);
        cannel.setOnClickListener(cannelbt);


        db=openOrCreateDatabase("db1.db",MODE_PRIVATE,null);






    }

    private View.OnClickListener okbt = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            ContentValues cv=new ContentValues();

            cv.put("t1",nb.getText().toString());
            cv.put("t2",tb.getText().toString());
            db.insert("table01",null,cv);
db.close();
finish();

        }
    };
    private View.OnClickListener cannelbt = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

}
