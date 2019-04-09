package com.moviewibe.app.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import com.moviewibe.app.beans.AbstractBean;
import com.moviewibe.app.beans.Genres;
import com.moviewibe.app.dao.iDao.iBD;

/**
 * @author Tarcisio Machado dos Reis
 *
 */
public class GenresBD implements iBD {

    private Context context;
    private SQLiteDatabase bd;

    public GenresBD(Context context) {
        BDCore auxBd = new BDCore(context);
        bd = auxBd.getWritableDatabase();

        this.setContext(context);
    }

    protected Context getContext() {
        return context;
    }

    protected void setContext(Context context) {
        this.context = context;
    }

    @Override
    public int update(AbstractBean bean) {
        ContentValues valores = new ContentValues();

        valores.put("name", ((Genres) bean).getName());

        return(bd.update("genres", valores, "id = " + String.valueOf(((Genres) bean).getId()), null));
    }

    @Override
    public long insert(AbstractBean bean) {
        ContentValues valores = new ContentValues();

        valores.put("id", ((Genres) bean).getId());
        valores.put("name", ((Genres) bean).getName());

        return(bd.insert("genres", null, valores));
    }

    @Override
    public int delete(AbstractBean bean) {
        return(bd.delete("genres", "id = " + String.valueOf(((Genres) bean).getId()), null));
    }

    @Override
    public int delete() {
        return(bd.delete("genres", null, null));
    }

    @Override
    public List<AbstractBean> search() throws Exception {
        List<AbstractBean> list = new ArrayList<AbstractBean>();
        String[] colunas = new String[]{"id", "name"};

        Cursor cursor = bd.query("genres", colunas, null, null, null, null, null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();

            do {

                Genres g = new Genres(cursor.getInt(0), cursor.getString(1));

                list.add(g);

            } while (cursor.moveToNext());
        }

        cursor.close();

        return (list);
    }

    @Override
    public List<AbstractBean> search(int id) throws Exception {
        List<AbstractBean> list = new ArrayList<AbstractBean>();
        String[] colunas = new String[]{"id", "name"};

        Cursor cursor = bd.query("genres", colunas, "id = " + String.valueOf(id), null, null, null, null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();

            do {

                Genres g = new Genres(cursor.getInt(0), cursor.getString(1));

                list.add(g);

            } while (cursor.moveToNext());
        }

        cursor.close();

        return (list);
    }

}
