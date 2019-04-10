package com.moviewibe.app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.moviewibe.app.beans.Movie;
import com.moviewibe.app.common.CommonActivity;
import com.moviewibe.app.constantes.Constantes;
import com.moviewibe.app.utils.Utilitarios;
import com.squareup.picasso.Picasso;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.ContextThemeWrapper;

/**
 * @author Tarcisio Machado dos Reis
 *
 * 
 * Classe usada mostrar detalhes do Filme selecionado na listagem 
 * 
 * 
 */
public class MovieDetailActivity extends CommonActivity {

    private Context context = this;
    private Bundle bundle;
    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog alertDialog;

    private Movie movie;

    private ImageView ivDetailImg;
    private TextView tvTitle;
    private TextView tvOverView;
    private TextView tvGenre;
    private TextView tvReleaseDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        init();
    }

    protected void init() {
        if (Utilitarios.isConnected(context)) {
            bundle = getIntent().getExtras();

            if (bundle != null) {
                movie = bundle.getParcelable("movie");

                if (movie != null) {
                    initViews();
                    initUser();
                }
            }
        } else {
            messageAlert(getString(R.string.error_network));
            return;
        }
    }

    @Override
    protected void initViews() {
        ivDetailImg = findViewById(R.id.ivDetailImg);
        tvTitle = findViewById(R.id.tvTitle);
        tvOverView = findViewById(R.id.tvOverView);
        tvGenre = findViewById(R.id.tvGenre);
        tvReleaseDate = findViewById(R.id.tvReleaseDate);

        Resources res = getResources();

        String appName       = res.getString(R.string.app_name);
        String serviceName   = " - " +  res.getString(R.string.title_movie_detail);

        setTitle(appName + serviceName);
    }

    @Override
    protected void initUser() {
        String imgPath = null;

        if (movie.getPoster_path() != null) {
            imgPath = movie.getPoster_path();
        } else imgPath = movie.getBackdrop_path();

        if (imgPath != null) {
            try {
                Picasso.get().load(Constantes.BASE_URL_IMAGE_DETAIL + imgPath).into(ivDetailImg);
            }catch(Exception e){
                errorAlert(e.getMessage());
            }
        }

        tvTitle.setText((movie.getTitle().isEmpty() ? getString(R.string.lbl_title) : movie.getTitle()));
        tvOverView.setText((movie.getOverview().isEmpty() ? getString(R.string.lbl_overview) : movie.getOverview()));
        tvReleaseDate.setText((movie.getRelease_date().isEmpty() ? getString(R.string.lbl_releasedate) : movie.getRelease_date()));
        tvGenre.setText((movie.getListGenres().isEmpty() ? getString(R.string.lbl_genre) : movie.getListGenres()));
    }

    @Override
    protected boolean isValidate() {
        return false;
    }

    @Override
    protected void errorAlert(String message) {
        alertDialogBuilder = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.StyleDialogs));

        alertDialogBuilder.setTitle(getString(R.string.alerta_title));
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton(getString(R.string.hint_fechar), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    protected void messageAlert(String message) {
        alertDialogBuilder = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.StyleDialogs));

        alertDialogBuilder.setTitle(getString(R.string.alerta_title));

        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton(getString(R.string.hint_sim), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        alertDialogBuilder.setNegativeButton(getString(R.string.hint_nao), new DialogInterface.OnClickListener() {
            @SuppressLint("NewApi")
            public void onClick(DialogInterface dialog, int id) {
                finishAndRemoveTask();
                dialog.dismiss();
            }
        });

        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void voltar() {
        Intent it = new Intent(context, MovieListActivity.class);

        startActivity(it);
        finish();
    }

    @Override
    public void onBackPressed() {
        voltar();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_moviedetail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_back) {
            voltar();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
