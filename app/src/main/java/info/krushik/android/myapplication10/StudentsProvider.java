package info.krushik.android.myapplication10;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

public class StudentsProvider extends ContentProvider {

    private DataBaseHelper mOpenHelper;
    private SQLiteDatabase db;

    static final String AUTHORITY = "info.krushik.android.myapplication10";
    static final String STUDENTS_PATH = "students";

    public static final Uri STUDENT_URI = Uri.parse("content://"
            + AUTHORITY + "/" + STUDENTS_PATH);

    //CONTENT_TYPE которые говорят андроиду что мы можем вернуть как массив эл-тов, так и один эл-т
    // Без него работает, но рекомендуется писать
    static final String STUDENT_CONTENT_TYPE = "vnd.android.cursor.dir/vnd."
            + AUTHORITY + "." + STUDENTS_PATH; //cursor.dir - много
    static final String STUDENT_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd."
            + AUTHORITY + "." + STUDENTS_PATH; //cursor.item - один


    static final int URI_STUDENTS = 1;
    static final int URI_STUDENTS_ID = 2;

    private static final UriMatcher uriMatcher;
    //UriMatcher-класс в который мы добавляем правильные Uri
    //когда нам кто-то передаст Uri, с пом него мы поймем это Uri(правильное/неправильное)
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, STUDENTS_PATH, URI_STUDENTS);
        uriMatcher.addURI(AUTHORITY, STUDENTS_PATH + "/#", URI_STUDENTS_ID);
    }

    //Создание ContentProvider, подключение к БД
    @Override
    public boolean onCreate() {
        mOpenHelper = new DataBaseHelper(getContext());

        return true;
    }

    //Обработка запроса на чтение
    @Nullable
    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings1, String s1) {
        db = mOpenHelper.getWritableDatabase();
        Cursor cursor = null;

        switch (uriMatcher.match(uri)) {//если uriMatcher uri нашол
            case URI_STUDENTS:
                cursor = db.query("Students", null, null, null, null, null, null);
                break;
            case URI_STUDENTS_ID:
                String id = uri.getLastPathSegment();
                cursor = db.query("Students", null, "_id=?", new String[]{String.valueOf(id)}, null, null, null);
                break;
        }

        return cursor;
    }

    //Обработка запроса на вставку
    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        db = mOpenHelper.getWritableDatabase();
        long id = 0;

        if (uriMatcher.match(uri) == URI_STUDENTS) {//если это uri студентов
            id = db.insert("Students", null, contentValues);//новый студент, то мы вызываем .insert
        }

        Uri resultUri = ContentUris.withAppendedId(STUDENT_URI, id);

        return resultUri;
    }

    //Обработка запроса на удаление
    @Override
    public int delete(Uri uri, String s, String[] strings) {
        db = mOpenHelper.getWritableDatabase();

        if (uriMatcher.match(uri) == URI_STUDENTS_ID) {//если он с ID
            s = s + " AND _id=" + uri.getLastPathSegment();//то мы удаляем студента с ID
        }

        int count = db.delete("Students", s, strings);

        return count;
    }

    //Обработка запроса на обновление
    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        db = mOpenHelper.getWritableDatabase();

        if (uriMatcher.match(uri) == URI_STUDENTS_ID) {
            s = s + " AND _id=" + uri.getLastPathSegment();
        }

        int count = db.update("Students", contentValues, s, strings);

        return count;
    }

    //MIME типы данных
    @Nullable
    @Override
    public String getType(Uri uri) {//метод в котором нам возвращаются CONTENT_TYPEы(это нужно андроиду)
        switch (uriMatcher.match(uri)) {
            case URI_STUDENTS:
                return STUDENT_CONTENT_TYPE;
            case URI_STUDENTS_ID:
                return STUDENT_CONTENT_ITEM_TYPE;
        }

        return null;
    }
}
