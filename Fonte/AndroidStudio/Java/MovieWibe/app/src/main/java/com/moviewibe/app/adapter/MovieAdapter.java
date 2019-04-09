package com.moviewibe.app.adapter;

import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.moviewibe.app.MovieListActivity;
import com.moviewibe.app.R;
import com.moviewibe.app.beans.Movie;
import com.moviewibe.app.constantes.Constantes;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.ContextThemeWrapper;

/**
 * @author Tarcisio Machado dos Reis
 */
public class MovieAdapter extends BaseAdapter {

    private Context context;
    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog alertDialog;

    private List<Movie> list = null;
    private LayoutInflater inflater;
    private ArrayList<Movie> arraylist;

    protected void errorAlert(String message) {
        alertDialogBuilder = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.StyleDialogs));

        alertDialogBuilder.setTitle(context.getString(R.string.alerta_title));
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton(context.getString(R.string.hint_fechar), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public MovieAdapter(Context c, List<Movie> l){
        list = l;
        arraylist = new ArrayList<Movie>();
        arraylist.addAll(list);

        context = c;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return list.get(position).getId();
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder;

        if (view == null) {
            view = inflater.inflate(R.layout.movie, null);
        }

        holder = new ViewHolder();
        holder.ivImg = (ImageView) view.findViewById(R.id.ivImg);
        holder.tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        holder.tvGenre = (TextView) view.findViewById(R.id.tvGenre);
        holder.tvReleaseDate = (TextView) view.findViewById(R.id.tvReleaseDate);

        String imgPath = null;

        if (list.get(position).getPoster_path() != null) {
            imgPath = list.get(position).getPoster_path();
        } else imgPath = list.get(position).getBackdrop_path();

        if (imgPath != null) {
            try {
                Picasso.get().load(Constantes.BASE_URL_IMAGE_LIST + imgPath).into(holder.ivImg);
            }catch(Exception e){
                errorAlert(e.getMessage());
            }
        }

        holder.tvTitle.setText(list.get(position).getTitle());
        holder.tvGenre.setText(list.get(position).getListGenres());
        holder.tvReleaseDate.setText(list.get(position).getRelease_date());

        if (position % 2 == 0){
            view.setBackgroundResource(R.drawable.alterselector1);
        } else {
            view.setBackgroundResource(R.drawable.alterselector2);
        }

        return view;
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        list.clear();

        if (charText.length() == 0) {
            list.addAll(arraylist);
        } else {
            for (Movie m: arraylist) {
                if (m.getTitle().toLowerCase(Locale.getDefault()).contains(charText)) {
                    list.add(m);
                }
            }
        }

        notifyDataSetChanged();
    }

    // INNER CLASS
    private static class ViewHolder{
        ImageView ivImg;
        TextView tvTitle;
        TextView tvGenre;
        TextView tvReleaseDate;
    }

}
