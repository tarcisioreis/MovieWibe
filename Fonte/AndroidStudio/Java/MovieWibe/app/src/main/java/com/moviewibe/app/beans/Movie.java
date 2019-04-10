package com.moviewibe.app.beans;

import android.os.Parcel;
import android.os.Parcelable;

import com.moviewibe.app.utils.Utilitarios;

/**
 * @author Tarcisio Machado dos Reis
 *
 * 
 * Classe DTO representando tabela de dados para armazenar local via API dados de Filme
 * 
 * 
 */
public class Movie extends AbstractBean implements Parcelable {

    private int id;
    private String title;
    private String overview;
    private String release_date;
    private String poster_path;
    private String backdrop_path;
    private String listGenres;

    public Movie() {}

    public Movie(int id,
                 String title,
                 String overview,
                 String release_date,
                 String poster_path,
                 String backdrop_path) {
        this.id = id;
        this.title = title;
        this.overview = overview;
        this.release_date = release_date;
        this.poster_path = poster_path;
        this.backdrop_path = backdrop_path;
    }

    public Movie(Parcel parcel) {
        this.id = parcel.readInt();
        this.title = parcel.readString();
        this.overview = parcel.readString();
        this.release_date = parcel.readString();
        this.poster_path = parcel.readString();
        this.backdrop_path = parcel.readString();
        this.listGenres = parcel.readString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = Utilitarios.StringToDateFormat(release_date);
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public String getListGenres() {
        return listGenres;
    }

    public void setListGenres(String listGenres) {
        this.listGenres = listGenres;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(getId());
        dest.writeString(getTitle());
        dest.writeString(getOverview());
        dest.writeString(getRelease_date());
        dest.writeString(getPoster_path());
        dest.writeString(getBackdrop_path());
        dest.writeString(getListGenres());
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

}
