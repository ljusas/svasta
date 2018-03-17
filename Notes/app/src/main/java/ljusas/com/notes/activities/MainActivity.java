package ljusas.com.notes.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ljusas.com.notes.R;
import ljusas.com.notes.db.DatabaseHelper;
import ljusas.com.notes.db.model.Notes;
import ljusas.com.notes.dialog.AboutDialog;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private DatePickerDialog.OnDateSetListener notesDate;
    String chosenDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Email = new Intent(Intent.ACTION_SEND);
                Email.setType("text/email");
                Email.putExtra(Intent.EXTRA_EMAIL,
                        new String[]{"ljusa@sbb.rs"});  //developer 's email
                Email.putExtra(Intent.EXTRA_SUBJECT,
                        getString(R.string.subject)); // Email 's Subject
                Email.putExtra(Intent.EXTRA_TEXT, getString(R.string.developer) + "");  //Email 's Greeting text
                startActivity(Intent.createChooser(Email, getString(R.string.feedback)));
            }
        });
        final ListView listView = findViewById(R.id.list_notes);

        try {
            List<Notes> list = getDatabaseHelper().getNotesDao().queryForAll();

            ListAdapter adapter = new ArrayAdapter<>(MainActivity.this, R.layout.list_item, list);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Notes n = (Notes) listView.getItemAtPosition(position);

                    Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                    intent.putExtra("notesID", n.getmId());
                    startActivity(intent);
                }
            });

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }

        if (id == R.id.about){
            AlertDialog dialog = new AboutDialog(MainActivity.this).prepareDialog();
            dialog.show();
        }

        if (id == R.id.action_add){
            final Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.input_dialog);

            final EditText notesTitle = dialog.findViewById(R.id.notes_title);
            final EditText notesDescription = dialog.findViewById(R.id.notes_description);
            final TextView date = dialog.findViewById(R.id.tv_date);

            date.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Calendar calendar = Calendar.getInstance();
                    int year = calendar.get(Calendar.YEAR);
                    int month = calendar.get(Calendar.MONTH);
                    int day = calendar.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog dialog1 = new DatePickerDialog(
                            MainActivity.this,
                            android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                            notesDate,
                            year,month,day);
                    dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog1.show();

                    chosenDate = String.format("%s/%d/%d", year, month, day);
                }
            });

            Button ok = dialog.findViewById(R.id.button_notes_add);
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String title = notesTitle.getText().toString();
                    String description = notesDescription.getText().toString();
                    String date1 = chosenDate;

                    Notes notes = new Notes();
                    notes.setTitle(title);
                    notes.setDescription(description);
                    notes.setDate(date1);

                    try {
                        getDatabaseHelper().getNotesDao().create(notes);
                        refresh();

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    Toast.makeText(MainActivity.this, "Notes inserted", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });

            Button cancel = dialog.findViewById(R.id.button_notes_dismis);
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            dialog.show();
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }

    private void refresh() {
        ListView listview = findViewById(R.id.list_notes);

        if (listview != null){
            ArrayAdapter<Notes> adapter = (ArrayAdapter<Notes>) listview.getAdapter();
            if(adapter!= null)
            {
                try {
                    adapter.clear();
                    List<Notes> list = getDatabaseHelper().getNotesDao().queryForAll();
                    adapter.addAll(list);
                    adapter.notifyDataSetChanged();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
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
