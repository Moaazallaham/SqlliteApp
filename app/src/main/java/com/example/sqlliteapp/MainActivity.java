package com.example.sqlliteapp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.app.DialogFragment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

public class MainActivity extends AppCompatActivity   {

    DatabaseHelper myDB;
    EditText editName,editTextID;
    Button btnAddData;
    Button btnView;
    Button btnUpdate;
    Button btnDelete;



    Button b_time;
    Button b_date;
    int day_x,month_x,year_x,hour_x,minute_x;
    static final int DLG_time=1;
    static final int DLG_date=0;


    Button btnpicture;
    ImageView image_view;
    private static final int requestCode=20;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDB=new DatabaseHelper(this);

        editName= (EditText)findViewById(R.id.editText_name);

        editTextID= (EditText)findViewById(R.id.editTextID);
        btnAddData= (Button) findViewById(R.id.button_add);
        btnView= (Button) findViewById(R.id.button_view);
        btnUpdate=(Button)findViewById(R.id.button_update);
        btnDelete=(Button)findViewById(R.id.button_delete);







        b_time=(Button)findViewById(R.id.b_time);
        b_date=(Button)findViewById(R.id.b_date);

        final Calendar cal=Calendar.getInstance();
        year_x=cal.get(Calendar.YEAR);
        month_x=cal.get(Calendar.MONTH);
        day_x=cal.get(Calendar.DAY_OF_MONTH);
        hour_x=cal.get(Calendar.HOUR_OF_DAY);
        minute_x=cal.get(Calendar.MINUTE);



        AddData();
        viewAll();
        updateData();
        deleteDate();
        DateDialogDate();
        DateDialogTime();
        camaraaction();




    }


    public void DateDialogDate()
    {
        b_date=(Button)findViewById(R.id.b_date);
        b_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DLG_date);
            }
        });
    }

    public void DateDialogTime()
    {
        b_time=(Button)findViewById(R.id.b_time);
        b_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DLG_time);
            }
        });
    }


    @Override
    protected Dialog onCreateDialog(int id)
    {
        switch(id) {
            case DLG_date:
               return new DatePickerDialog(MainActivity.this,dpickerListner,year_x,month_x,day_x);

            case DLG_time:
               return new TimePickerDialog(MainActivity.this,tpickerListner,hour_x,minute_x,false);

            default:
               return null;
        }
    }


    private DatePickerDialog.OnDateSetListener dpickerListner= new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            year_x=year;
            month_x=month;
            day_x=dayOfMonth;


        }
    };
    private TimePickerDialog.OnTimeSetListener tpickerListner= new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            hour_x=hourOfDay;
            minute_x=minute;

        }


    };
    public void deleteDate()
    {
        btnDelete.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Integer deletedRows=myDB.deleteData(editTextID.getText().toString());
                        if(deletedRows>0)
                        {
                            Toast.makeText(MainActivity.this,"Data Deleted",Toast.LENGTH_LONG).show();
                        }
                        else
                            Toast.makeText(MainActivity.this,"Data is not Deleted",Toast.LENGTH_LONG).show();

                    }
                }
        );
    }
    public void updateData()
    {
        btnUpdate.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String temp_date= String.valueOf(year_x)+'/'+ String.valueOf(month_x)+'/'+String.valueOf(day_x);
                        String temp_time= String.valueOf(hour_x)+':'+String.valueOf(minute_x);
                        boolean isUpdate=myDB.updateData(editTextID.getText().toString(),editName.getText().toString(),temp_date,temp_time);
                        if(isUpdate==true)
                        {
                            Toast.makeText(MainActivity.this,"Data Updated",Toast.LENGTH_LONG).show();
                        }
                        else
                            Toast.makeText(MainActivity.this,"Data is not Updated",Toast.LENGTH_LONG).show();
                    }
                }
        );
    }


    public void camaraaction() {
        btnpicture =(Button) findViewById(R.id.btn_picture);
        image_view = (ImageView) findViewById(R.id.imageView);

        btnpicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent picture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                startActivityForResult(picture,requestCode);
            }
        });
    }


    public void AddData()
    {
        btnAddData.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String temp_date= String.valueOf(year_x)+'/'+ String.valueOf(month_x+1)+'/'+String.valueOf(day_x);
                        String temp_time= String.valueOf(hour_x)+':'+String.valueOf(minute_x);
                        boolean isInserted= myDB.insertData(editName.getText().toString(),temp_date,temp_time);

                        editName.setText("");
                        if(isInserted==true)
                            Toast.makeText(MainActivity.this,"Data Inserted",Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(MainActivity.this,"Data is not Inserted",Toast.LENGTH_LONG).show();


                    }
                }
        );
    }
    public void viewAll()
    {
        btnView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Cursor res= myDB.getAllData();
                        if(res.getCount()==0)
                        {
                            showMessage("Error","Nothing found");
                            return;
                        }
                        StringBuffer buffer= new StringBuffer();

                        while(res.moveToNext())
                        {
                            buffer.append("Id :"+res.getString(0)+"\n");
                            buffer.append("Name :"+res.getString(1)+"\n");
                            buffer.append("Date :"+res.getString(2)+"\n");
                            buffer.append("Time :"+res.getString(3)+"\n\n");


                        }
                        showMessage("Data",buffer.toString());
                    }
                }
        );
    }
    public void showMessage(String title, String message)
    {

        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(this.requestCode == requestCode && resultCode == RESULT_OK){
            Bitmap bitmap = (Bitmap)data.getExtras().get("data");

            String partFilename = currentDateFormat();
            storeCameraPhotoInSDCard(bitmap, partFilename);

            String storeFilename = "photo_" + partFilename + ".jpg";
            Bitmap mBitmap = getImageFileFromSDCard(storeFilename);
            image_view.setImageBitmap(mBitmap);
            image_view.setImageBitmap(bitmap);
        }

    }
    private void storeCameraPhotoInSDCard(Bitmap bitmap, String currentDate){
        File outputFile = new File(Environment.getExternalStorageDirectory(), "photo_" + currentDate + ".jpg");
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private String currentDateFormat(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HH_mm_ss");
        String  currentTimeStamp = dateFormat.format(new Date());
        return currentTimeStamp;
    }
    private Bitmap getImageFileFromSDCard(String filename){
        Bitmap bitmap = null;
        File imageFile = new File(Environment.getExternalStorageDirectory() + filename);
        try {
            FileInputStream fis = new FileInputStream(imageFile);
            bitmap = BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bitmap;
    }


}
