package edu.uw.kartikey.yama;

        import android.database.Cursor;
        import android.net.Uri;
        import android.provider.Telephony;
        import android.support.v4.app.LoaderManager;
        import android.support.v4.content.CursorLoader;
        import android.support.v4.content.Loader;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.Log;
        import android.widget.CursorAdapter;
        import android.widget.ListView;
        import android.widget.SimpleCursorAdapter;

        import java.util.Objects;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks {

    private static final String TAG = "MainActivity";

    SimpleCursorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportLoaderManager().initLoader(0, null, this);

        String[] cols = new String[]{Telephony.Sms.Conversations.ADDRESS, Telephony.Sms.Conversations.BODY,Telephony.Sms.Conversations.DATE };
        int[] ids = new int[]{R.id.fromText,R.id.msgText,R.id.dateText};

        adapter = new SimpleCursorAdapter(this,R.layout.list_item,null,cols,ids, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

        ListView lv = (ListView)findViewById(R.id.mainMessageList);
        lv.setAdapter(adapter);

    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {

        Uri uri = Telephony.Sms.Inbox.CONTENT_URI;

        String[] columnToFetch = {Telephony.Sms.Conversations._ID, Telephony.Sms.Conversations.ADDRESS, Telephony.Sms.Conversations.BODY,Telephony.Sms.Conversations.DATE};


        return new CursorLoader(
                this,   // Parent activity context
                uri,        // Table to query
                columnToFetch,     // Projection to return
                null,            // No selection clause
                null,            // No selection arguments
                null             // Default sort order
        );
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
        adapter.swapCursor((Cursor)data);
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }
}
    /*Cursor c = getContentResolver().query(uri, null, null, null, null);

        c.moveToFirst();
        while(!c.isAfterLast()){
            String address = c.getString(c.getColumnIndexOrThrow("address"));
            String subject = c.getString(c.getColumnIndexOrThrow("subject"));
            String body = c.getString(c.getColumnIndexOrThrow("body"));
            long date = c.getInt(c.getColumnIndexOrThrow("date"));
            int type = c.getInt(c.getColumnIndexOrThrow("type"));

            Log.v(TAG, address + ", " + subject + ", " + body + ", " + date + ", " + type);
            c.moveToNext();
        }

        return null;*/