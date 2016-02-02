package edu.uw.kartikey.yama;

        import android.content.Intent;
        import android.database.Cursor;
        import android.net.Uri;
        import android.provider.Telephony;
        import android.support.v4.app.LoaderManager;
        import android.support.v4.content.CursorLoader;
        import android.support.v4.content.Loader;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.Menu;
        import android.view.MenuInflater;
        import android.view.MenuItem;
        import android.view.View;
        import android.widget.CursorAdapter;
        import android.widget.ListView;
        import android.widget.SimpleCursorAdapter;
        import android.widget.TextView;

        import java.text.SimpleDateFormat;
        import java.util.Calendar;
        import java.util.Date;
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

        adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int i) {

                if (i == 1) {
                    String text = cursor.getString(i);
                    TextView textView = (TextView) view;
                    textView.setText("From: " + text);
                    return true;
                }

                if (i == 2) {
                    String text = cursor.getString(i);
                    TextView textView = (TextView) view;
                    int length = text.length() > 10 ? 10:text.length();
                    textView.setText("Message: " + text);
                    //textView.setText("Message: " + text.substring(0,length)+"...");
                    return true;
                }

                if (i == 3) {
                    String createDate = cursor.getString(i);
                    TextView textView = (TextView) view;
                    SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm");
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(Long.parseLong(createDate));
                    String date = sdf.format(calendar.getTime());

                    textView.setText("Date: " + date);
                    return true;
                }

                return false;
            }
        });

        ListView lv = (ListView)findViewById(R.id.mainMessageList);
        lv.setAdapter(adapter);

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.composeItem:
                startComposing();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void startComposing () {
        Intent intent =  new Intent(MainActivity.this,ComposeActivity.class);
        startActivity(intent);
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
