package com.example.andriodlabs;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;


public class ChatRoomActivity extends AppCompatActivity {

    public static EditText editText;
    public static Button deleteButton;
    public static ChatListAdapter chatListAdapter;
    protected SQLiteDatabase db = null;
    public static final String ITEM_SELECTED = "ITEM";
    public static final String ITEM_POSITION = "POSITION";
    public static final String ITEM_ID = "ID";
    public static final String MESSAGE_ID = "MESSAGE_ID";
    public static final int EMPTY_ACTIVITY = 345;
    public ArrayList<Message> messageList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listy);

        setupListView();

    }

    private void setupListView() {
        ListView listView = findViewById(R.id.listview);
        deleteButton = findViewById(R.id.deleteButton);
        boolean isTablet = findViewById(R.id.fragmentLocation) != null; //check if the FrameLayout is loaded
        //get the database:
        MyDatabaseOpenHelper opener = new MyDatabaseOpenHelper(this);
        db =  opener.getWritableDatabase();

        //query all the results from the database:
        String [] columns = {MyDatabaseOpenHelper.COL_ID, MyDatabaseOpenHelper.COL_MESSAGE, MyDatabaseOpenHelper.COL_IS_SENDER};
        Cursor results = db.query(false, MyDatabaseOpenHelper.TABLE_NAME, columns, null, null, null, null, null, null);
        printCursor(results);

        //find the column indices:
        int messageColumnIndex = results.getColumnIndex(MyDatabaseOpenHelper.COL_MESSAGE);
        int isSenderColIndex = results.getColumnIndex(MyDatabaseOpenHelper.COL_IS_SENDER);
        int idColIndex = results.getColumnIndex(MyDatabaseOpenHelper.COL_ID);

        //iterate over the results, return true if there is a next item:
        messageList = new ArrayList<>();
        while(results.moveToNext())
        {
            Log.d("isSender",Integer.toString(results.getInt(isSenderColIndex)));
            String id = results.getString(idColIndex);
            String message = results.getString(messageColumnIndex);
            Boolean isSender = results.getInt(isSenderColIndex) == 1;
            //long id = results.getLong(idColIndex);

            //add the new Contact to the array list:
            Message newMessage = new Message(isSender, message, id);
            messageList.add(newMessage);
        }

        chatListAdapter = new ChatListAdapter(this, messageList);
        listView.setAdapter(chatListAdapter);

        listView.setOnItemClickListener( (list, item, position, id) -> {
            Bundle dataToPass = new Bundle();
            dataToPass.putString(ITEM_SELECTED, messageList.get(position).getMessage() );
            dataToPass.putInt(ITEM_POSITION, position);
            dataToPass.putLong(ITEM_ID, id);
            dataToPass.putString(MESSAGE_ID, messageList.get(position).getId());

            if(isTablet)
            {
                DetailFragment dFragment = new DetailFragment(); //add a DetailFragment
                dFragment.setArguments( dataToPass ); //pass it a bundle for information
                dFragment.setTablet(true);  //tell the fragment if it's running on a tablet or not
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.fragmentLocation, dFragment) //Add the fragment in FrameLayout
                        .addToBackStack("AnyName") //make the back button undo the transaction
                        .commit(); //actually load the fragment.
            }
            else //isPhone
            {
                Intent nextActivity = new Intent(ChatRoomActivity.this, EmptyActivity.class);
                nextActivity.putExtras(dataToPass); //send data to next activity
                startActivityForResult(nextActivity, EMPTY_ACTIVITY); //make the transition
            }
        });
    }

    public void printCursor( Cursor c){
        Log.d("DATABASE_VERSION",Integer.toString(db.getVersion()));
        Log.d("COLUMNS",Integer.toString(c.getColumnCount()));
        for(String columnName : c.getColumnNames()){
            Log.d("COLUMN_NAMES",columnName);
        }

        Log.d("NUM_RESULTS",Integer.toString(c.getCount()));

        //find the column indices:
        int messageColumnIndex = c.getColumnIndex(MyDatabaseOpenHelper.COL_MESSAGE);
        int isSenderColIndex = c.getColumnIndex(MyDatabaseOpenHelper.COL_IS_SENDER);

        while(c.moveToNext())
        {
            //long id = results.getLong(idColIndex);
            Log.d("message",c.getString(messageColumnIndex));
            Log.d("isSender",Integer.toString(c.getInt(isSenderColIndex)));
        }
        c.moveToFirst();
    }

    public void receiveMessage(View view){
        editText = findViewById(R.id.message);
        String message = editText.getText().toString();

        //add to the database and get the new ID
        ContentValues newRowValues = new ContentValues();
        //put is sender bool in the IS_SENDER column:
        newRowValues.put(MyDatabaseOpenHelper.COL_IS_SENDER, 0);
        //put string email in the EMAIL column:
        newRowValues.put(MyDatabaseOpenHelper.COL_MESSAGE, message);
        //insert in the database:
        db.insert(MyDatabaseOpenHelper.TABLE_NAME, null, newRowValues);

        chatListAdapter.getArray().add(new Message(false, message));
        chatListAdapter.notifyDataSetChanged();
        editText.setText(new String(""));
        setupListView();

    }

    public void sendMessage(View view){
        editText = findViewById(R.id.message);
        String message = editText.getText().toString();

        //add to the database and get the new ID
        ContentValues newRowValues = new ContentValues();
        //put is sender bool in the IS_SENDER column:
        newRowValues.put(MyDatabaseOpenHelper.COL_IS_SENDER, 1);
        //put string email in the EMAIL column:
        newRowValues.put(MyDatabaseOpenHelper.COL_MESSAGE, message);
        //insert in the database:
        db.insert(MyDatabaseOpenHelper.TABLE_NAME, null, newRowValues);

        chatListAdapter.getArray().add(new Message(true, message));
        chatListAdapter.notifyDataSetChanged();
        editText.setText(new String(""));
        setupListView();
    }

    //This function only gets called on the phone. The tablet never goes to a new activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == EMPTY_ACTIVITY)
        {
            if(resultCode == RESULT_OK) //if you hit the delete button instead of back button
            {
                Bundle extras = data.getExtras();
                long id = extras.getLong(ITEM_ID);
                String messageId = extras.getString(MESSAGE_ID);
                deleteMessageId((int)id, messageId);
            }
        }
    }

    public void deleteMessageId(int id, String messageId)
    {
        Log.i("Delete this message:" , " id="+id);
        String whereClause = "_id=?";
        String[] whereArgs = new String[] { String.valueOf(messageId) };
        int success = db.delete(MyDatabaseOpenHelper.TABLE_NAME, whereClause, whereArgs);
        Log.i("Delete success" , Integer.toString(success));
        messageList.remove(id);
        chatListAdapter.notifyDataSetChanged();
    }


}