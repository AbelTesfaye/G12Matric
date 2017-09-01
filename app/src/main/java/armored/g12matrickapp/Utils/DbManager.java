package armored.g12matrickapp.Utils;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;

public class DbManager extends SQLiteOpenHelper {

    public static String TAG = "G12Tests:DataBasePart";
    private static String DB_PATH = "";
    private static String DB_NAME = "core.db";
    public static final String TABLE_NAME_Questions = "Questions";
    public static final String TABLE_NAME_TopicWise = "TopicWise";
    public static final String TABLE_NAME_Explanation = "Explanation";

    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_YEAR = "Year";
    public static final String COLUMN_SUBJECT = "Subject";
    public static final String COLUMN_UNIT = "Unit";
    public static final String COLUMN_CONTENT = "Content";
    public static final String COLUMN_QUESTION_NUM = "Question_No";
    public static final String COLUMN_QUESTION_TESTED = "Question_Tested";
    public static final String COLUMN_QUESTION_ANSWERED = "Questions_Answered";
    public static final String COLUMN_ANS = "Ans";
    public static final String COLUMN_ATTEMPTED_BEFORE = "Attempted_before";
    public static final String COLUMN_ANSWERED_BEFORE = "Answered_before";
    public static final String COLUMN_EXPLANATION_TEXT = "Explanation_Text";


    private SQLiteDatabase mDatabase;
    private final Context mContext;

    public DbManager(Context context){

        super(context , DB_NAME , null , 1);
        if(Build.VERSION.SDK_INT >= 17){
            DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
        }
        else{
            DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        }
        this.mContext = context;
        if(checkDB()){  openDataBase();}
        else{
            try {
                createDatabase();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    public void createDatabase() throws IOException {
        boolean mDataBaseExist = checkDB();
        if(!mDataBaseExist){
            this.getWritableDatabase();
            this.close();

            try {
                copyDB();

            }catch (IOException mIoException){
                mIoException.printStackTrace();
                // throw new Error("ErrorCopingDatabase");
            }
        }
    }

    private boolean checkDB(){
        File dbFile = new File(DB_PATH + DB_NAME);
        return dbFile.exists();
    }

    private void copyDB() throws IOException{

        InputStream mInput = mContext.getAssets().open(DB_NAME);
        String outputFileName = DB_PATH + DB_NAME;
        OutputStream mOutput = new FileOutputStream(outputFileName);
        byte[] mBuffer = new byte[1024];
        int mLength;
        while((mLength = mInput.read(mBuffer)) > 0){
            mOutput.write(mBuffer , 0 ,mLength);
        }
        mOutput.flush();
        mOutput.close();
        mInput.close();
    }

    public boolean openDataBase() throws SQLException {
        String mPath = DB_PATH + DB_NAME;
        mDatabase = SQLiteDatabase.openDatabase(mPath , null , SQLiteDatabase.CREATE_IF_NECESSARY);
        return mDatabase != null;
    }

    @Override
    public synchronized void close() {
        if(mDatabase != null){
            mDatabase.close();
        }
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            createDatabase();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }



    public ArrayList<Integer> getIntArray(String tableName , String requestedColumn , String whereStatement , String orderBy){

        // TODO: 6/13/2017 USAGE: getIntArray(DbManager.TABLE_NAME_Questions , COLUMN_UNIT , "Year = 2005 AND Subject = 0" , "Question_No ASEM");

        ArrayList<Integer> toBeReturnedArray = new ArrayList<Integer>();
        try {
            SQLiteDatabase database = getReadableDatabase();
            Cursor resultSet = database.rawQuery("SELECT * FROM " + tableName + " WHERE " + whereStatement + " ORDER BY " + orderBy, null);
            if(resultSet.getCount() > 0 && resultSet.moveToFirst()) {
                int columnIndex = resultSet.getColumnIndex(requestedColumn);
                for (int i = 0; i < resultSet.getCount(); i++) {
                    String tempHolder = resultSet.getString(columnIndex);
                    toBeReturnedArray.add(Integer.parseInt(tempHolder));
                    resultSet.moveToNext();
                }
                resultSet.close();
                database.close();
                return toBeReturnedArray;
            }else{
                toBeReturnedArray.clear();
                toBeReturnedArray.add(-1);
                database.close();
                return toBeReturnedArray;
            }
        }catch (Exception e){
            e.printStackTrace();
            toBeReturnedArray.clear();
            toBeReturnedArray.add(-1);
            return toBeReturnedArray;
        }
    }

    public ArrayList<String> getStringArray(String tableName , String requestedColumn , String whereStatement , String orderBy){

        // TODO: 6/13/2017 USAGE: getStringArray(DbManager.TABLE_NAME_Questions , COLUMN_UNIT , "Year = 2005 AND Subject = 0" , "Question_No ASEM");

        ArrayList<String> toBeReturnedArray = new ArrayList<String>();
        try {
            SQLiteDatabase database = getReadableDatabase();
            Cursor resultSet = database.rawQuery("SELECT * FROM " + tableName +" WHERE " + whereStatement + " ORDER BY " + orderBy, null);
            if(resultSet.getCount() > 0 && resultSet.moveToFirst()) {
                int columnIndex = resultSet.getColumnIndex(requestedColumn);
                for (int i = 0; i < resultSet.getCount(); i++) {
                    String tempHolder = resultSet.getString(columnIndex);
                    toBeReturnedArray.add(tempHolder);
                    resultSet.moveToNext();
                }
                resultSet.close();
                database.close();
                return toBeReturnedArray;
            }else{
                toBeReturnedArray.clear();
                toBeReturnedArray.add("null");
                database.close();
                return toBeReturnedArray;
            }
        }catch (Exception e){
            e.printStackTrace();
            toBeReturnedArray.clear();
            toBeReturnedArray.add("null");
            return toBeReturnedArray;
        }
    }

    private int numberOfEntries(String tbname){
        SQLiteDatabase db = getReadableDatabase();
        return (int) DatabaseUtils.queryNumEntries(db , tbname);
    }

    public boolean updateDataInsideDb(String table , ContentValues cv , String where){
        SQLiteDatabase db = getWritableDatabase();
        int DP = db.update(table , cv , where , null );
        db.close();
        return DP == 1;
    }

    public boolean insertNewData(String table , ContentValues cv){
        SQLiteDatabase db = getWritableDatabase();
        int how_many = numberOfEntries(table);
        cv.put(COLUMN_ID , how_many + 1);
        db.insert(table , null , cv);
        db.close();
        return true;
    }

    public int getSingleInt(String tableName , String requestedColumn, String whereStatement){
        int toBeReturnedInt = -1;
        try {
            SQLiteDatabase database = getReadableDatabase();
            Cursor resultSet = database.rawQuery("SELECT * FROM " + tableName + " WHERE " + whereStatement , null);
            if(resultSet.getCount() > 0 && resultSet.moveToFirst()) {
                int columnIndex = resultSet.getColumnIndex(requestedColumn);
                for (int i = 0; i < resultSet.getCount(); i++) {
                    String tempHolder = resultSet.getString(columnIndex);
                    toBeReturnedInt = Integer.parseInt(tempHolder);
                    resultSet.moveToNext();
                }
                resultSet.close();
                database.close();
                return toBeReturnedInt;
            }else{
                toBeReturnedInt = -1;
                database.close();
                return toBeReturnedInt;
            }
        }catch (Exception e){
            e.printStackTrace();
            toBeReturnedInt = -1;
            return toBeReturnedInt;
        }
    }

    public String getSingleString(String tableName , String requestedColumn, String whereStatement){
        String toBeReturnedInt = "null";
        try {
            SQLiteDatabase database = getReadableDatabase();
            Cursor resultSet = database.rawQuery("SELECT * FROM " + tableName + " WHERE " + whereStatement , null);
            if(resultSet.getCount() > 0 && resultSet.moveToFirst()) {
                int columnIndex = resultSet.getColumnIndex(requestedColumn);
                for (int i = 0; i < resultSet.getCount(); i++) {
                    toBeReturnedInt = resultSet.getString(columnIndex);
                    resultSet.moveToNext();
                }
                resultSet.close();
                database.close();
                return toBeReturnedInt;
            }else{
                toBeReturnedInt = "null";
                database.close();
                return toBeReturnedInt;
            }
        }catch (Exception e){
            e.printStackTrace();
            toBeReturnedInt = "null";
            return toBeReturnedInt;
        }
    }

    public Cursor getSearchCursor(String query){
            Cursor c;
            SQLiteDatabase database = getReadableDatabase();
            String q = "SELECT * FROM " + TABLE_NAME_Questions + " WHERE " + COLUMN_CONTENT + " LIKE '%" + query + "%' LIMIT 100";
            c = database.rawQuery(q , null);
            if(c != null){
                c.moveToFirst();
            }
            return c;
    }



}
