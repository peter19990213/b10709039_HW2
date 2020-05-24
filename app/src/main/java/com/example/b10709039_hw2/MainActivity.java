package com.example.b10709039_hw2;

import android.app.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private MyAdapter myAdapter;
    private RecyclerView mList;
    private ArrayList myDataset;
    public Drawable drawable;
    SQLiteDatabase db=null;
    String s_list[];
    String s2_list[];
    String[] str_list = {"red", "blue", "green"};
    final static String CREATE_TABLE="create table table01(_id INTEGER PRIMARY KEY,t1 TEXT,t2 TEXT) ";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        SharedPreferences sharedPreferences= getSharedPreferences("test",
                Activity.MODE_PRIVATE);
        String color =sharedPreferences.getString("color", "");
        boolean r=color.equals("red");
        boolean b=color.equals("blue");
        boolean g=color.equals("green");
        if (r){
            drawable=getResources().getDrawable(R.drawable.r);
        }
        if (b)
        {
            drawable=getResources().getDrawable(R.drawable.b);
        }
        if (g)
        {
            drawable=getResources().getDrawable(R.drawable.g);
        }

        myDataset = new ArrayList<Item>();
        db=openOrCreateDatabase("db1.db",MODE_PRIVATE,null);
            String sql="SELECT * FROM table01" ;
            Cursor c= db.rawQuery(sql,null);

        if (c!=null && c.getCount()>=0)
        {
            s_list=new String[c.getCount()];
            s2_list=new String[c.getCount()];
            myDataset.add(new Item());
            for(int i = 0; i < c.getCount(); i++){

                c.moveToNext();
                Item item = new Item();
                item.setText(c.getString(1));
                s_list[i]=c.getString(1);
                item.setText2(c.getString(2));
                s2_list[i]=c.getString(2);
                myDataset.add(item);
            }


        }
        db.close();



        myAdapter = new MyAdapter(myDataset);
        mList = (RecyclerView) findViewById(R.id.my_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mList.setLayoutManager(layoutManager);
        mList.setAdapter(myAdapter);
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP | ItemTouchHelper.DOWN,
                ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                //  上下拖移callback
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                // 左右滑動callback
                AlertDialog.Builder alert= new AlertDialog.Builder(MainActivity.this);
                alert.setMessage("確定使否要刪除");
                alert.setTitle("刪除");
                final int w=viewHolder.getLayoutPosition()-1;


                alert.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db=openOrCreateDatabase("db1.db",MODE_PRIVATE,null);
                        AlertDialog.Builder a= new AlertDialog.Builder(MainActivity.this);
                        String sql="t1='"+s_list[w]+"' and t2='"+s2_list[w]+"'";
                        a.setMessage(sql);
                        a.show();
                        db.delete("table01",sql,null);
                        db.close();
                        resetdb();

                    }
                });

                alert.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        resetdb();
                    }
                });
                alert.show();
            }
        }).attachToRecyclerView(mList);
    }



    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        private List<Item> mData;

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView mTextView;
            public TextView mTextView2;
            public ViewHolder(View v) {
                super(v);
                mTextView = (TextView) v.findViewById(R.id.info_text);
                mTextView2 = (TextView) v.findViewById(R.id.info_text2);
            }
        }

        public MyAdapter(List<Item> data) {
            mData = data;
        }

        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item, parent, false);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {

            Item item = mData.get(position);
            holder.mTextView.setText(item.getText());
            holder.mTextView2.setText(item.getText2());
            holder.mTextView.setBackground(drawable);
        }
        public int getItemCount(){return mData.size();}
    }

    private static class Item{
        String text;
        String text2;
        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getText2() {
            return text2;
        }

        public void setText2(String text) {
            this.text2 = text;
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i =new Intent();
            i.setClass(MainActivity.this,add.class);
            startActivityForResult(i,1);
            return true;

        }
        else if(id == R.id.color) {

            SharedPreferences sharedPreferences = getSharedPreferences("test", Context.MODE_PRIVATE);

            final SharedPreferences.Editor editor = sharedPreferences.edit();

            AlertDialog.Builder a=new AlertDialog.Builder(MainActivity.this);
            a.setSingleChoiceItems(str_list, -1, new DialogInterface
                    .OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {
                    Toast.makeText(getApplicationContext(),
                            str_list[item], Toast.LENGTH_SHORT).show();
                    if(str_list[item]=="red")
                    {
                        editor.putString("color","red");
                        editor.commit();
                        drawable=getResources().getDrawable(R.drawable.r);
                        resetdb();

                    }else if(str_list[item]=="blue")
                    {
                        editor.putString("color","blue");
                        editor.commit();
                        drawable=getResources().getDrawable(R.drawable.b);
                        resetdb();
                    }else if ((str_list[item]=="green"))
                    {
                        editor.putString("color","green");
                        editor.commit();
                        drawable=getResources().getDrawable(R.drawable.g);
                        resetdb();
                    }
                    dialog.dismiss();// dismiss the alertbox after chose option

                }
            });
            AlertDialog alert = a.create();
            alert.show();
        }

        return super.onOptionsItemSelected(item);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        resetdb();
    }
   public void resetdb(){
       myDataset = new ArrayList<>();
       db=openOrCreateDatabase("db1.db",MODE_PRIVATE,null);
       String sql="SELECT * FROM table01" ;
       Cursor c= db.rawQuery(sql,null);

       if (c!=null && c.getCount()>=0)
       {
           s_list=new String[c.getCount()];
           s2_list=new String[c.getCount()];
           myDataset.add(new Item());
           for(int i = 0; i < c.getCount(); i++){

               c.moveToNext();
               Item item = new Item();
               item.setText(c.getString(1));
               s_list[i]=c.getString(1);
               item.setText2(c.getString(2));
               s2_list[i]=c.getString(2);
               myDataset.add(item);
           }


       }
       db.close();
       myAdapter = new MyAdapter(myDataset);
       mList.setAdapter(myAdapter);

   }
}


