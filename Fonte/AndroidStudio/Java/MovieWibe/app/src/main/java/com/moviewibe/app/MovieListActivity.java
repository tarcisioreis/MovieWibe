package com.moviewibe.app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import com.moviewibe.app.adapter.MovieAdapter;
import com.moviewibe.app.beans.AbstractBean;
import com.moviewibe.app.beans.Genres;
import com.moviewibe.app.beans.Movie;
import com.moviewibe.app.common.CommonActivity;
import com.moviewibe.app.constantes.Constantes;
import com.moviewibe.app.control.HttpClient;
import com.moviewibe.app.dao.GenresBD;
import com.moviewibe.app.utils.Utilitarios;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.ContextThemeWrapper;

import static com.moviewibe.app.utils.Utilitarios.isConnected;

/**
 * @author Tarcisio Machado dos Reis
 */
public class MovieListActivity extends CommonActivity implements AdapterView.OnItemClickListener,
                                                                 SearchView.OnQueryTextListener {

    private Context context = this;
    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog alertDialog;

    private ListView listView;

    private List<Movie> list;
    private MovieAdapter adapter;

    private SearchView svBuscar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);
    }

    protected void init() {
        Utilitarios.showSimpleProgressDialog(context,
                                             getString(R.string.alerta_title),
                                             getString(R.string.hint_aguarde), true);

        if (isConnected(context)) {
            initViews();
            initUser();
        } else {
            messageAlert(getString(R.string.error_network));
            return;
        }
    }

    @Override
    protected void initViews() {
        listView = findViewById(R.id.listView);
        svBuscar = findViewById(R.id.svBuscar);

        svBuscar.setIconifiedByDefault(false);
        svBuscar.setOnQueryTextListener(this);
        svBuscar.setSubmitButtonEnabled(true);
        svBuscar.setQueryHint(getString(R.string.hint_pesquisar));

        listView.setOnItemClickListener(this);
        listView.setTextFilterEnabled(true);
        listView.setAdapter(null);

        Resources res = getResources();

        String appName       = res.getString(R.string.app_name);
        String serviceName   = " - " +  res.getString(R.string.title_movie_list);

        setTitle(appName + serviceName);
    }

    @Override
    protected void initUser() {
        try {
            list = new ArrayList<Movie>();

            String[] params = new String[3];

            params[0] = Constantes.BASE_URL + Constantes.URL_GENRE + "?";
            params[1] = "api_key="  + Constantes.API_KEY;
            params[2] = "&language=" + Constantes.LANGUAGE;

            getGenre(params);

            params = new String[3];

            params[0] = Constantes.BASE_URL + Constantes.URL_LIST + "?";
            params[1] = "api_key="  + Constantes.API_KEY;
            params[2] = "&language=" + Constantes.LANGUAGE;

            getListMovies(params);
        } catch (Exception e) {
            errorAlert(e.getMessage());
        }
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

        if (message.equals(getString(R.string.alerta_pesquisar))) {
            alertDialogBuilder.setPositiveButton(getString(R.string.hint_ok), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });

        } else {
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
        }

        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void getGenre(String[] params) {

        HttpClient httpClient = new HttpClient(context);

        try {
            JSONObject json = httpClient.execute(params).get();

            if (json != null) {
                try {
                    JSONArray jsonArray = json.getJSONArray("genres");
                    GenresBD genresBD = new GenresBD(this);

                    String acao = "";

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object  = jsonArray.getJSONObject(i);
                        Genres genres = new Genres(Integer.parseInt(object.getString("id")),
                                                   object.getString("name"));

                        if (genresBD.search(genres.getId()).size() > 0) {
                            if (genresBD.update(genres) == 0) {
                                throw new Exception(getString(R.string.error_update_genres));
                            } else acao = "update";
                        } else {
                            if (genresBD.insert(genres) == 0) {
                                throw new Exception(getString(R.string.error_insert_genres));
                            } else acao = "insert";
                        }

                        Log.d(Constantes.TAG, acao + " >> " + genres.getId() + " > " + genres.getName());
                    }
                } catch (Exception e) {
                    errorAlert(e.getMessage());
                }

                Utilitarios.removeSimpleProgressDialog();
            }
        } catch (ExecutionException e) {
            errorAlert(e.getMessage());
        } catch (InterruptedException e) {
            errorAlert(e.getMessage());
        }

    }

    private void getListMovies(String[] params) {

        HttpClient httpClient = new HttpClient(context);

        try {
            JSONObject json = httpClient.execute(params).get();

            if (json != null) {
                try {
                    JSONArray jsonArray = json.getJSONArray("results");
                    GenresBD genresBD = new GenresBD(this);
                    List<AbstractBean> listSearch = null;
                    String listNameGenres = "";

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object  = jsonArray.getJSONObject(i);
                        Movie movie = new Movie(Integer.parseInt(object.getString("id")),
                                                object.getString("title"),
                                                object.getString("overview"),
                                                Utilitarios.StringToDateFormat(object.getString("release_date")),
                                                object.getString("poster_path"),
                                                object.getString("backdrop_path"));

                        JSONArray jsonGenreIds = object.getJSONArray("genre_ids");
                        for (int j = 0; j < jsonGenreIds.length(); j++) {
                             Object obj = jsonGenreIds.get(j);

                             Genres genres = new Genres();

                             genres.setId((int) obj);

                             listSearch = genresBD.search(genres.getId());
                             if (listSearch.size() == 0) {
                                 throw new Exception(getString(R.string.error_search_genres));
                             } else {
                                 genres = (Genres) listSearch.get(0);

                                 listNameGenres += genres.getName() + ",";
                             }
                        }

                        if (listNameGenres.substring(listNameGenres.toString().trim().length()-1).equals(",")) {
                            listNameGenres = listNameGenres.substring(0,(listNameGenres.toString().trim().length()-1));
                        }

                        movie.setListGenres(listNameGenres);

                        list.add(movie);

                        listNameGenres = "";

                        Log.d(Constantes.TAG, movie.getId() + " > " + movie.getTitle() + " >> " + movie.getRelease_date() + " " + movie.getListGenres());
                    }
                } catch (Exception e) {
                    errorAlert(e.getMessage());
                }

                Utilitarios.removeSimpleProgressDialog();

                if (list.size() > 0) {
                    adapter = new MovieAdapter(context, list);
                    listView.setAdapter(adapter);

                    adapter.notifyDataSetChanged();
                }

            }
        } catch (ExecutionException e) {
            errorAlert(e.getMessage());
        } catch (InterruptedException e) {
            errorAlert(e.getMessage());
        }

    }

    private int searchMovie(String[] params) {

        HttpClient httpClient = new HttpClient(context);

        try {
            JSONObject json = httpClient.execute(params).get();

            if (json != null) {
                try {
                    JSONArray jsonArray = json.getJSONArray("results");
                    GenresBD genresBD = new GenresBD(this);
                    List<AbstractBean> listSearch = null;
                    String listNameGenres = "";

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object  = jsonArray.getJSONObject(i);
                        Movie movie = new Movie(Integer.parseInt(object.getString("id")),
                                object.getString("title"),
                                object.getString("overview"),
                                Utilitarios.StringToDateFormat(object.getString("release_date")),
                                object.getString("poster_path"),
                                object.getString("backdrop_path"));

                        JSONArray jsonGenreIds = object.getJSONArray("genre_ids");
                        for (int j = 0; j < jsonGenreIds.length(); j++) {
                            Object obj = jsonGenreIds.get(j);

                            Genres genres = new Genres();

                            genres.setId((int) obj);

                            listSearch = genresBD.search(genres.getId());
                            if (listSearch.size() == 0) {
                                throw new Exception(getString(R.string.error_search_genres));
                            } else {
                                genres = (Genres) listSearch.get(0);

                                listNameGenres += genres.getName() + ",";
                            }
                        }

                        if (listNameGenres.length() > 0) {
                            if (listNameGenres.substring(listNameGenres.toString().trim().length() - 1).equals(",")) {
                                listNameGenres = listNameGenres.substring(0, (listNameGenres.toString().trim().length() - 1));
                            }
                        }

                        movie.setListGenres(listNameGenres);

                        list.add(movie);

                        listNameGenres = "";

                        Log.d(Constantes.TAG, movie.getId() + " > " + movie.getTitle() + " >> " + movie.getRelease_date() + " " + movie.getListGenres());
                    }
                } catch (Exception e) {
                    errorAlert(e.getMessage());
                }

                Utilitarios.removeSimpleProgressDialog();

                if (list.size() > 0) {
                    adapter = new MovieAdapter(context, list);
                    listView.setAdapter(adapter);

                    adapter.notifyDataSetChanged();
                }

            }
        } catch (ExecutionException e) {
            errorAlert(e.getMessage());
        } catch (InterruptedException e) {
            errorAlert(e.getMessage());
        }

        return list.size();

    }

    private void sair() {
        alertDialogBuilder = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.StyleDialogs));

        alertDialogBuilder.setTitle(getString(R.string.alerta_title));

        alertDialogBuilder.setMessage(getString(R.string.alerta_sair));
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton(getString(R.string.hint_sim), new DialogInterface.OnClickListener() {
            @SuppressLint("NewApi")
            public void onClick(DialogInterface dialog, int id) {
                finishAndRemoveTask();
                dialog.dismiss();
            }
        });
        alertDialogBuilder.setNegativeButton(getString(R.string.hint_nao), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    @Override
    public void onStart() {
        super.onStart();

        init();
    }

    @Override
    public void onBackPressed() {
        Utilitarios.removeSimpleProgressDialog();
    }

/*
*
*
*       Ao clicar sobre um filme qualquer irá abrir tela com detalhes do filme.
*
*
 */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent it = new Intent(context, MovieDetailActivity.class);

        it.putExtra("movie", (Parcelable) list.get(position));

        startActivity(it);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_movielist, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_sair) {
            sair();
            return true;
        }

        if (id == R.id.action_refresh) {
            init();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        MovieListActivity.this.adapter.filter(query);

        if (MovieListActivity.this.adapter.getCount() == 0) {

            Utilitarios.showSimpleProgressDialog(context,
                                                 getString(R.string.alerta_title),
                                                 getString(R.string.hint_aguarde), true);

            String[] params = new String[4];

            params[0] = Constantes.BASE_URL + Constantes.URL_SEARCH + "?";
            params[1] = "api_key="  + Constantes.API_KEY;
            params[2] = "&language=" + Constantes.LANGUAGE;
            params[3] = "&query=" + query;

            Utilitarios.removeSimpleProgressDialog();

            if (searchMovie(params) == 0) {
                messageAlert(getString(R.string.alerta_pesquisar));
                init();
            }
        }

        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {

        if (TextUtils.isEmpty(query.toString())) {
            listView.clearTextFilter();
            list.clear();

            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }

            if (isConnected(context)) {
                init();
            } else {
                messageAlert(getString(R.string.error_network));
            }
        } else {
            listView.setFilterText(query.toString());
        }

        return true;
    }

}
