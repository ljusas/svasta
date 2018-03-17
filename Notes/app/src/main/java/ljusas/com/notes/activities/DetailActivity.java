package ljusas.com.notes.activities;

import android.app.NotificationManager;
import android.content.Context;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.util.List;

import ljusas.com.notes.R;
import ljusas.com.notes.db.DatabaseHelper;
import ljusas.com.notes.db.model.Notes;

public class DetailActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private Notes n;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        int notesID = getIntent().getExtras().getInt("notesID");

        try {
            n = getDatabaseHelper().getNotesDao().queryForId(notesID);

            TextView title1 = findViewById(R.id.tv_title);
            title1.setText(n.getTitle());

            TextView description1 = findViewById(R.id.tv_description);
            description1.setText(n.getDescription());

            TextView date1 = findViewById(R.id.tv_date);
            date1.setText(n.getDate());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_delete){
            try {
                getDatabaseHelper().getNotesDao().delete(n);
                Toast.makeText(this, "Notes deleted", Toast.LENGTH_SHORT).show();
                finish();

            } catch (SQLException e) {
                e.printStackTrace();
            }

            NotificationManager manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            Context context = getApplicationContext();
            android.support.v4.app.NotificationCompat.Builder builder = new android.support.v4.app.NotificationCompat.Builder(context);
            builder.setContentTitle("Obaveštenje");
            builder.setContentText("Upravo ste obrisali kontakt.");
            builder.setSmallIcon(R.drawable.ic_stat_name);
            builder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
            manager.notify(1,builder.build());
        }
        return super.onOptionsItemSelected(item);
    }
    public DatabaseHelper getDatabaseHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
        }
        return databaseHelper;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }
}
