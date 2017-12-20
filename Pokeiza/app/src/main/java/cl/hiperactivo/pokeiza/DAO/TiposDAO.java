package cl.hiperactivo.pokeiza.DAO;

import android.os.AsyncTask;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import cl.hiperactivo.pokeiza.Models.PokemonModel;
import cl.hiperactivo.pokeiza.Models.TipoModel;
import cl.hiperactivo.pokeiza.libs.Cargador;
import cl.hiperactivo.pokeiza.libs.Utilidades;

/**
 * Created by hernanBeiza on 6/21/17.
 */

public class TiposDAO {

    private static String tag = "TipoDAO";
    private static String urlTipos = "https://pokeapi.co/api/v2/type/";
    private int idtipo = 1;

    private Context contexto;

    public TiposDAODelegate delegate;

    public interface TiposDAODelegate {
        void onTiposDAOCargando(String tipo);
        void onTiposDAOComplete(ArrayList<TipoModel> tipos);
        void onTiposDAOError(String error);
    }


    public TiposDAO(Context contexto) {
        this.contexto = contexto;
    }

    /**
     * Carga los tipos de pokemon
     */
    public void cargar(){

        DBLocalOpenHelper openHelper = new DBLocalOpenHelper(contexto);
        ArrayList<TipoModel> tiposList = openHelper.obtenerTipos();
        if (Utilidades.isConnected(contexto)) {
            if(tiposList!=null && tiposList.size()>=18){
                Log.d(tag,"Cargar localmente");
                delegate.onTiposDAOComplete(tiposList);
            } else {
                Log.d(tag, "Cargar desde internet");
                Log.d(tag,String.valueOf(this.idtipo));
                if(this.idtipo<19){
                    new TiposDAOTask().execute(String.valueOf(this.idtipo));
                }
            }
        } else {
            Log.d(tag,"Sin conexión");
        }

    }

    private class TiposDAOTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... params) {
            Log.d(tag,"doInBackground");
            String idString = params[0];
            StringBuilder servicioURL = new StringBuilder();
            servicioURL.append(urlTipos);
            servicioURL.append(idString);
            Log.d(tag,servicioURL.toString());

            StringBuilder resultString = new StringBuilder();
            try {
                URL urlSevice = new URL(servicioURL.toString());
                HttpURLConnection conexion = (HttpURLConnection) urlSevice.openConnection();
                BufferedReader reader = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    resultString.append(line);
                }
                conexion.disconnect();
            } catch (MalformedURLException e){
                Log.d(tag,"Error al cargar los tipos. Ruta Mal Formada");
                //e.printStackTrace();
            } catch (Exception ex){
                Log.d(tag,"Error al cargar los tipos");
                ex.printStackTrace();
            }

            return resultString.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d(tag,"onPostExecute");
            DBLocalOpenHelper dbLocalOpenHelper = new DBLocalOpenHelper(contexto);

            try {
                JSONObject jsonResponse = new JSONObject(s);
                //Log.d(tag,jsonResponse.toString());
                int id = jsonResponse.getInt("id");
                //Log.d(tag,"id: " + String.valueOf(id));

                JSONArray nombres = jsonResponse.getJSONArray("names");
                JSONObject spanish = (JSONObject)nombres.get(4);
                //Log.d(tag,spanish.toString());

                String nombre = spanish.getString("name");
                //Log.d(tag, nombre);
                TipoModel model = new TipoModel(id,nombre.toUpperCase());
                //Log.d(tag,model.toString());
                if(dbLocalOpenHelper.agregarTipo(model)){
                    //Log.d(tag,"tipo agregado");
                    String cargando = "Tipo " + nombre + " cargado. "  + " Tipo: " + idtipo + " de 18";
                    delegate.onTiposDAOCargando(cargando);
                } else {
                    //Log.d(tag,"tipo no agregado");
                }

                JSONArray pokemones = jsonResponse.getJSONArray("pokemon");
                for (int i = 0;i<pokemones.length();i++) {
                    JSONObject itemJSON = pokemones.getJSONObject(i);
                    JSONObject pokemonJSON = itemJSON.getJSONObject("pokemon");
                    String nombrePokemon = pokemonJSON.getString("name").toString();
                    String rutaPokemon  = pokemonJSON.getString("url").toString();
                    String[] rutaSplit = rutaPokemon.split("/");
                    int idpokemon = Integer.parseInt(rutaSplit[rutaSplit.length-1]);
                    //Log.d(tag,nombrePokemon);
                    PokemonModel pokemonModel = new PokemonModel();
                    pokemonModel.setNombre(nombrePokemon.toUpperCase());
                    pokemonModel.setIdTipo(idtipo);
                    pokemonModel.setIdPokemon(idpokemon);
                    if(dbLocalOpenHelper.agregarPokemon(pokemonModel)){
                        //Log.d(tag,"pokemon agregado");
                    } else {
                        //Log.d(tag,"pokemon no agregado");
                    }
                }

                //Cargar el siguiente tipo
                idtipo++;
                cargar();

            } catch (JSONException e){
                Log.d(tag,"Error al obtener el JSON");
                e.printStackTrace();
            }

        }

        @Override
        protected void onCancelled(String s) {
            super.onCancelled(s);
            Log.d(tag,"onCancelled");
            delegate.onTiposDAOError(s);
        }

    }


}
