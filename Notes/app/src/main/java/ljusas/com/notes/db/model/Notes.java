package ljusas.com.notes.db.model;

import android.widget.Spinner;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

/**
 * Created by Fujitsu on 15.03.2018.
 */
@DatabaseTable(tableName = Notes.TABLE_NAME_USERS)
public class Notes {

    public static final String TABLE_NAME_USERS = "notes";

    public static final String FIELD_NAME_ID     = "id";
    public static final String FIELD_NAME_TITLE   = "title";
    public static final String FIELD_NAME_DESCRIPTION   = "description";
    public static final String FIELD_NAME_DATE   = "date";

    @DatabaseField(columnName = FIELD_NAME_ID, generatedId = true)
    private int mId;

    @DatabaseField(columnName = FIELD_NAME_TITLE)
    private String title;

    @DatabaseField(columnName = FIELD_NAME_DESCRIPTION)
    private String description;

    @DatabaseField(columnName = FIELD_NAME_DATE)
    private String date;

    public Notes() {
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return title;
    }
}
