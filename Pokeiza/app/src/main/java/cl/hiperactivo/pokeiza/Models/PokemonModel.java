package cl.hiperactivo.pokeiza.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hernanBeiza on 6/22/17.
 */

public class PokemonModel implements Parcelable {

    private int idPokemon;
    private int idTipo;
    private String nombre;
    private String tamano;
    private String peso;
    private String caracteristicas;
    private String habilidades;
    private String tipos;
    private String fotografia;

    public PokemonModel() { }

    public PokemonModel(int idPokemon){
        this.idPokemon = idPokemon;
    }

    public PokemonModel(int idPokemon, int idTipo, String nombre, String tamano, String peso, String caracteristicas, String habilidades, String tipos, String fotografia) {
        this.idPokemon = idPokemon;
        this.idTipo = idTipo;
        this.nombre = nombre;
        this.tamano = tamano;
        this.peso = peso;
        this.caracteristicas = caracteristicas;
        this.habilidades = habilidades;
        this.tipos = tipos;
        this.fotografia = fotografia;
    }

    public int getIdPokemon() {
        return idPokemon;
    }

    public void setIdPokemon(int idPokemon) {
        this.idPokemon = idPokemon;
    }

    public int getIdTipo() {
        return idTipo;
    }

    public void setIdTipo(int idTipo) {
        this.idTipo = idTipo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTamano() {
        return tamano;
    }

    public void setTamano(String tamano) {
        this.tamano = tamano;
    }

    public String getPeso() {
        return peso;
    }

    public void setPeso(String peso) {
        this.peso = peso;
    }

    public String getCaracteristicas() {
        return caracteristicas;
    }

    public void setCaracteristicas(String caracteristicas) {
        this.caracteristicas = caracteristicas;
    }

    public String getHabilidades() {
        return habilidades;
    }

    public void setHabilidades(String habilidades) {
        this.habilidades = habilidades;
    }

    public String getTipos() {
        return tipos;
    }

    public void setTipos(String tipos) {
        this.tipos = tipos;
    }

    public String getFotografia() {
        return fotografia;
    }

    public void setFotografia(String fotografia) {
        this.fotografia = fotografia;
    }

    @Override
    public String toString() {
        return "PokemonModel{" +
                "idPokemon=" + idPokemon +
                ", idTipo=" + idTipo +
                ", nombre='" + nombre + '\'' +
                ", tamano='" + tamano + '\'' +
                ", peso='" + peso + '\'' +
                ", caracteristicas='" + caracteristicas + '\'' +
                ", habilidades='" + habilidades + '\'' +
                ", tipos='" + tipos + '\'' +
                ", fotografia='" + fotografia + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.idPokemon);
        dest.writeInt(this.idTipo);
        dest.writeString(this.nombre);
        dest.writeString(this.tamano);
        dest.writeString(this.peso);
        dest.writeString(this.caracteristicas);
        dest.writeString(this.habilidades);
        dest.writeString(this.tipos);
        dest.writeString(this.fotografia);
    }

    protected PokemonModel(Parcel in) {
        this.idPokemon = in.readInt();
        this.idTipo = in.readInt();
        this.nombre = in.readString();
        this.tamano = in.readString();
        this.peso = in.readString();
        this.caracteristicas = in.readString();
        this.habilidades = in.readString();
        this.tipos = in.readString();
        this.fotografia = in.readString();
    }

    public static final Parcelable.Creator<PokemonModel> CREATOR = new Parcelable.Creator<PokemonModel>() {
        @Override
        public PokemonModel createFromParcel(Parcel source) {
            return new PokemonModel(source);
        }

        @Override
        public PokemonModel[] newArray(int size) {
            return new PokemonModel[size];
        }
    };
}
