package com.example.contentprovider;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    TextView contacts, calendar,calllog;
    Button insert_contact;
    final int REQUEST_PERMISSIONS = 100;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        contacts = findViewById(R.id.contacts);
        calendar = findViewById(R.id.calendar);
        calllog = findViewById(R.id.calllog);
        insert_contact = findViewById(R.id.insert_contact);
        validatePermissions();
        accessBank();
        insertClientBank();

        insert_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InsertContact();
            }
        });


    }



    private void InsertContact(){

        Intent intent = new Intent(ContactsContract.Intents.Insert.ACTION);
        intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
        startActivity(intent);


        /*Uri newUri;
        ContentValues newValues = new ContentValues();
        newValues.put(ContactsContract.Data.DISPLAY_NAME, "Prueba");
        newValues.put(ContactsContract.CommonDataKinds.Phone.NUMBER, "31457397372");

        newUri= getContentResolver().insert(
                ContactsContract.Data.CONTENT_URI,
                newValues
        );

        System.out.println(newUri);*/

    }


    private void ListContacts(){

        String fields[] = {ContactsContract.Data.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER};
        Cursor cursor = getContentResolver().query(ContactsContract.Data.CONTENT_URI,fields,null,null,null);

        contacts.setText("");

        while(cursor.moveToNext()){
            this.contacts.append(cursor.getString(0) + " " + cursor.getString(1));

        }
    }

    private void ListCalendar(){

        String fields[] = {CalendarContract.EventsEntity.DTSTART};
        String fieldsFilter = CalendarContract.Calendars.ACCOUNT_NAME + "= ?";
        String fieldsFilterArgs[] = {"vianchajohan@gmail.com"};
        Cursor cursor = getContentResolver().query(CalendarContract.Events.CONTENT_URI,fields,fieldsFilter,fieldsFilterArgs,null);

        calendar.setText("");
        cursor.moveToFirst();
        while(cursor.moveToNext()){
            this.calendar.append(cursor.getString(0));
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.M)

    private void validatePermissions(){
        if(checkSelfPermission(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED){
           /// ListContacts();
            //ListCalendar();
           // ListWordsDictionary();
            accesCallLog();

        }else{

            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.READ_CALENDAR, Manifest.permission.READ_CALL_LOG},REQUEST_PERMISSIONS);
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == REQUEST_PERMISSIONS){

            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
               // ListContacts();
            }
            if(grantResults[1] == PackageManager.PERMISSION_GRANTED){
                //ListCalendar();
            }
            if(grantResults[2] == PackageManager.PERMISSION_GRANTED){
                 accesCallLog();
            }

        }else{
            Toast.makeText(this, "Se necesita de los contactos y el calendario para que la app funcione", Toast.LENGTH_SHORT).show();
        }
    }

    private void accesCallLog(){

        String fields[] = {CallLog.Calls.DATE,CallLog.Calls.NUMBER, CallLog.Calls.DURATION};
        Cursor cursor = getContentResolver().query(CallLog.Calls.CONTENT_URI,fields,null,null,null);

        calendar.setText("");
        cursor.moveToFirst();
        while(cursor.moveToNext()){
            this.calllog.append(cursor.getString(0) + " " + cursor.getString(1) + " " + cursor.getString(2));
        }

    }

    private void accessBank(){

        String fields[] = {"name_cli","identification_cli"};
        final Uri CONTENT_URI = Uri.parse("content://com.example.wpossbank.contetProvider.ProviderBank/clients");
        Cursor cursor = getContentResolver().query(CONTENT_URI,fields,null,null,null);

        System.out.println(cursor.getCount());
        while(cursor.moveToNext()){
            System.out.println(cursor.getString(0) + " " + cursor.getString(1));
        }
    }

    private void insertClientBank(){


        ContentValues clients = new ContentValues();

        clients.put("name_cli","Javier");
        clients.put("identification_cli", "105744947935");
        clients.put("pin_cli", "9999");
        clients.put("balance_cli", "100000");

        final Uri CONTENT_URI = Uri.parse("content://com.example.wpossbank.contetProvider.ProviderBank/clients");

        Uri uri_new = getContentResolver().insert(CONTENT_URI,clients);

        if(uri_new!=null){
            System.out.println("El cliente ha sido creado");
        }else{
            System.out.println("Error al crear el cliente");
        }

    }



}