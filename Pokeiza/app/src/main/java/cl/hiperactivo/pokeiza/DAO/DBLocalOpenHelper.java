package cl.hiperactivo.pokeiza.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import cl.hiperactivo.pokeiza.Models.PokemonModel;
import cl.hiperactivo.pokeiza.Models.TipoModel;

/**
 * Created by hernanBeiza on 6/22/17.
 */

public class DBLocalOpenHelper extends SQLiteOpenHelper {

    private static final String tag = "DBLocalOpenHelper";

    static public final String DATABASE_NAME ="pokeizadb";
    static public final int DATABASE_VERSION = 1;

    public DBLocalOpenHelper(Context context) {
        super(context, DATABASE_NAME,null, DATABASE_VERSION);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        //Log.d(tag,"onOpen();");
        super.onOpen(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Log.d(tag,"onCreate();");
        //db.execSQL("CREATE TABLE 'tipo_item' ( `idtipo` INTEGER NOT NULL UNIQUE, `nombre` TEXT NOT NULL )");
        db.execSQL("CREATE TABLE 'tipo' ( `idtipo` INTEGER NOT NULL UNIQUE, `nombre` TEXT NOT NULL )");
        //db.execSQL("CREATE TABLE 'pokemon' ( `idpokemon` INTEGER, `idtipo` INTEGER, `nombre` TEXT, `tamano` TEXT, `peso` TEXT, `caracteristicas` TEXT, `habilidades` TEXT, `tipos` TEXT, `fotografia` TEXT )");
        db.execSQL("CREATE TABLE 'pokemon' ( `idpokemon` INTEGER, `idtipo` INTEGER, `nombre` TEXT DEFAULT NULL, `tamano` TEXT DEFAULT NULL, `peso` TEXT DEFAULT NULL, `caracteristicas` TEXT DEFAULT NULL, `habilidades` TEXT DEFAULT NULL, `tipos` TEXT DEFAULT NULL, `fotografia` TEXT DEFAULT NULL )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(tag,"onUpgrade();");
        if (oldVersion!=newVersion){
            db.execSQL("DROP TABLE tipo");
            Log.d(tag,"DROP TABLE tipo_item");

            Log.d(tag,"DROP TABLE pokemon");
            db.execSQL("DROP TABLE pokemon");

            this.onCreate(db);
        }
    }

    /***
     * Agrega un tipo_item de pokemón a la DB local
     * @param model TipoModel
     * @return true en caso de éxito, false en caso contrario
     */
    public boolean agregarTipo(TipoModel model) {
        if(obtenerTipo(model)==null){
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("idtipo",model.getIdtipo());
            values.put("nombre",model.getNombre().toUpperCase());
            long resultado = db.insert("tipo_item",null,values);
            // Log.d(tag,String.valueOf(resultado));
            db.close();
            if(resultado==-1){
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    /***
     * Retorna un arreglo co los tipos de pokemon
     * @return ArrayList<PokemonModel> con datos en caso de tener. NULL en caso contrario
     */
    public ArrayList <TipoModel> obtenerTipos() {
        Log.d(tag,"obtenerTipos");
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<TipoModel> tipos = new ArrayList<TipoModel>();
        String[] columnas = {"idtipo","nombre"};
        Cursor c = db.query("tipo",columnas,null,null,null,null,"nombre ASC");
        if(c!=null){
            if(c.moveToFirst()){
                Log.d(tag,"¡Tipos de pokemon encontrados!");
                do {
                    TipoModel model = new TipoModel(c.getInt(0),c.getString(1));
                    tipos.add(model);
                } while (c.moveToNext());
            } else {
                Log.d(tag,"No tengo tipos de pokemon");
                tipos = null;
            }
        } else {
            Log.d(tag,"Error");
            tipos = null;
        }
        return tipos;
    }

    /***
     * Edita un tipo_item de pokemon desde la DB local
     * @param model TipoModel
     * @return true en caso de éxito, false en caso contrario
     */
    public boolean editarTipo(TipoModel model){
        Log.w(tag,"editarTipo");
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("idtipo",model.getIdtipo());
        values.put("nombre",model.getNombre().toUpperCase());
        String[] whereString = {String.valueOf(model.getIdtipo())};
        long resultado = db.update("tipo_item",values,"idtipo=?",whereString);
        db.close();
        if(resultado==-1){
            return false;
        }
        return true;
    }

    /**
     * Obtiene un tipo_item de pokemon según el idtipo desde la DB Local
     * @param model TipoModel
     * @return el tipo_item en caso de éxito, null en caso contrario
     */
    public TipoModel obtenerTipo(TipoModel model){
        SQLiteDatabase db = getReadableDatabase();
        String[] columnas = {"idtipo","nombre"};
        String[] whereString = {String.valueOf(model.getIdtipo())};
        Cursor c = db.query("tipo_item", columnas, "idtipo=?", whereString, null, null, null);
        if(c!=null){
            if(c.moveToFirst()){
                Log.d(tag,"¡Tipo "+model.getNombre() + " encontrado localmente!");
                do {
                    TipoModel amigo = new TipoModel(c.getInt(0),c.getString(1));
                    return amigo;
                } while (c.moveToNext());
            } else {
                Log.d(tag,"¡No existe tipo_item " + model.getNombre() +" localmente!");
                return null;
            }
        } else {
            Log.d(tag,"Error");
            return null;
        }
    }

    // POkemon

    /***
     * Agrega un pokemon a la DB Local
     * @param model PokemonModel
     * @return true en caso de éxito, false en caso contrario
     */
    public boolean agregarPokemon(PokemonModel model) {
        if(obtenerPokemon(model)==null){
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("idpokemon",model.getIdPokemon());
            values.put("idtipo",model.getIdTipo());
            values.put("nombre",model.getNombre().toUpperCase());
            if(model.getTamano()!=null){
                values.put("tamano",model.getTamano().toUpperCase());
            }
            if(model.getPeso()!=null) {
                values.put("peso", model.getPeso().toUpperCase());
            }
            if(model.getTipos()!=null) {
                values.put("tipos", model.getTipos().toUpperCase());
            }
            if(model.getHabilidades()!=null) {
                values.put("habilidades", model.getHabilidades().toUpperCase());
            }
            if(model.getCaracteristicas()!=null) {
                values.put("caracteristicas", model.getCaracteristicas().toUpperCase());
            }
            if(model.getFotografia()!=null) {
                values.put("fotografia", model.getFotografia());
            }
            long resultado = db.insert("pokemon",null,values);
            //Log.d(tag,String.valueOf(resultado));
            db.close();
            if(resultado==-1){
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Edita un pokemon de la DB local según el idpokemon
     * @param model PokemonModel
     * @return true en caso de éxito, false en caso contrario
     */
    public boolean editarPokemon(PokemonModel model){
        Log.w(tag,"editarPokemon");
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("idtipo",model.getIdTipo());
        values.put("nombre",model.getNombre().toUpperCase());
        if(model.getTamano()!=null){
            values.put("tamano",model.getTamano().toUpperCase());
        }
        if(model.getPeso()!=null) {
            values.put("peso", model.getPeso().toUpperCase());
        }
        if(model.getTipos()!=null) {
            values.put("tipos", model.getTipos().toUpperCase());
        }
        if(model.getHabilidades()!=null) {
            values.put("habilidades", model.getHabilidades().toUpperCase());
        }
        if(model.getCaracteristicas()!=null) {
            values.put("caracteristicas", model.getCaracteristicas().toUpperCase());
        }
        if(model.getFotografia()!=null) {
            values.put("fotografia", model.getFotografia());
        }
        String[] whereString = {String.valueOf(model.getIdPokemon())};
        long resultado = db.update("pokemon",values,"idpokemon=?",whereString);
        db.close();
        if(resultado==-1){
            return false;
        }
        return true;
    }

    /**
     * Obtiene un pokemon desde la DB local según el nombre del pokemón
     * @param model PokemonModel
     * @return PokemonModel en caso de éxito, null en caso de no encontrar el pokemon
     */
    public PokemonModel obtenerPokemon(PokemonModel model){
        SQLiteDatabase db = getReadableDatabase();
        String[] columnas = {"idpokemon","idtipo","nombre","tamano","peso","tipos","habilidades","caracteristicas","fotografia"};

        String[] whereString = {String.valueOf(model.getNombre())};
        Cursor c = db.query("pokemon", columnas, "nombre=?", whereString, null, null, null);

        if(c!=null){
            if(c.moveToFirst()){
                Log.d(tag,"¡Pokemón "+model.getNombre() + " encontrado localmente!");
                do {
                    PokemonModel pokemon = new PokemonModel(c.getInt(0),c.getInt(1),c.getString(2),c.getString(3),c.getString(4),c.getString(5),c.getString(6),c.getString(7),c.getString(8));
                    return pokemon;
                } while (c.moveToNext());
            } else {
                Log.d(tag,"¡No existe Pokemon " + model.getNombre() +" localmente!");
                return null;
            }
        } else {
            Log.d(tag,"Error");
            return null;
        }
    }

    /**
     * Retorna el detalle del pokemon, el pokemon completo buscado según el nombre y cuando no tiene tamaño (o no tiene los datos)
     * @param model PokemonModel
     * @return PokemonModel en caso de éxito, null en caso contrario
     */
    public PokemonModel obtenerPokemonDetalle(PokemonModel model){
        SQLiteDatabase db = getReadableDatabase();
        //String[] columnas = {"idpokemon","idtipo","nombre","tamano","peso","tipos","habilidades","caracteristicas","fotografia"};
        //String[] whereString = {String.valueOf(model.getIdPokemon()),String.valueOf(model.getNombre())};
        Cursor c = db.rawQuery("SELECT * FROM pokemon WHERE tamano!='' AND nombre='"+model.getNombre()+"'", null);

        if(c!=null){
            if(c.moveToFirst()){
                Log.d(tag,"¡Pokemón "+model.getNombre() + " encontrado localmente!");
                do {
                    PokemonModel pokemon = new PokemonModel(c.getInt(0),c.getInt(1),c.getString(2),c.getString(3),c.getString(4),c.getString(5),c.getString(6),c.getString(7),c.getString(8));
                    return pokemon;
                } while (c.moveToNext());
            } else {
                Log.d(tag,"¡Existe localmente pero no está actualizado el Pokemon " + model.getNombre()+"!");
                return null;
            }
        } else {
            Log.d(tag,"Error");
            return null;
        }
    }

    /**
     * Obtiene los pokemon según el tipo_item de pokemon
     * @param tipo TipoModel
     * @return ArrayList<PokemonModel> en caso de éxito, null en caso contrario
     */
    public ArrayList <PokemonModel> obtenerPokemonesConTipo(TipoModel tipo) {
        Log.d(tag,"obtenerPokemonesConTipo");
        ArrayList<PokemonModel> pokemones = new ArrayList<PokemonModel>();

        SQLiteDatabase db = getReadableDatabase();
        String[] columnas = {"idpokemon","idtipo","nombre","tamano","peso","tipos","habilidades","caracteristicas","fotografia"};
        String[] whereString = {String.valueOf(tipo.getIdtipo())};
        Cursor c = db.query("pokemon", columnas, "idtipo=?", whereString, null, null, "nombre ASC");
        if(c!=null){
            if(c.moveToFirst()){
                Log.d(tag,"¡Pokemónes de tipo_item "+tipo.getNombre() + " encontrado localmente!");
                do {
                    PokemonModel pokemon = new PokemonModel(c.getInt(0),c.getInt(1),c.getString(2),c.getString(3),c.getString(4),c.getString(5),c.getString(6),c.getString(7),c.getString(8));
                    pokemones.add(pokemon);
                } while (c.moveToNext());
            } else {
                Log.d(tag,"No hay pokemones de ese tipo_item");
                pokemones = null;
            }
        } else {
            Log.d(tag,"Error");
            pokemones = null;
        }
        return pokemones;
    }

}
