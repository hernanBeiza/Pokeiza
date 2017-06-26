package cl.hiperactivo.pokeiza.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hernanBeiza on 6/21/17.
 */

public class TipoModel implements Parcelable {

    private int idtipo;
    private String nombre;

    public TipoModel(int idtipo, String nombre) {
        this.idtipo = idtipo;
        this.nombre = nombre;
    }

    public int getIdtipo() {
        return idtipo;
    }

    public void setIdtipo(int idtipo) {
        this.idtipo = idtipo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return "TipoModel{" +
                "idtipo='" + idtipo + '\'' +
                ", nombre='" + nombre + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.idtipo);
        dest.writeString(this.nombre);
    }

    protected TipoModel(Parcel in) {
        this.idtipo = in.readInt();
        this.nombre = in.readString();
    }

    public static final Parcelable.Creator<TipoModel> CREATOR = new Parcelable.Creator<TipoModel>() {
        @Override
        public TipoModel createFromParcel(Parcel source) {
            return new TipoModel(source);
        }

        @Override
        public TipoModel[] newArray(int size) {
            return new TipoModel[size];
        }
    };
}
