package cl.hiperactivo.pokeiza.DAO;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import cl.hiperactivo.pokeiza.Models.PokemonModel;

/**
 * Created by hernanBeiza on 6/21/17.
 */

public class PokemonDAO {

    private static String tag = "PokemonDAO";
    private static String urlPokemon = "http://pokeapi.co/api/v2/pokemon/";

    private PokemonModel pokemonModel;

    private Context contexto;

    public PokemonesDAODelegate delegate;

    public interface PokemonesDAODelegate {
        void onPokemonesDAOComplete(PokemonModel model);
        void onPokemonesDAOError(String error);
    }


    public PokemonDAO(Context contexto) {
        this.contexto = contexto;
    }

    /**
     * Carga el detalle del pokemon según el id
     * @param model PokemonModel
     */
    public void cargarDetalleConID(PokemonModel model){

        this.pokemonModel = model;
        Log.d(tag,"cargarDetalleConID:"+model.getNombre());
        Log.d(tag,pokemonModel.toString());

        DBLocalOpenHelper openHelper = new DBLocalOpenHelper(contexto);
        PokemonModel pokemon = openHelper.obtenerPokemonDetalle(model);

        if(pokemon!=null){
            Log.d(tag,"Cargar localmente");
            delegate.onPokemonesDAOComplete(pokemon);
        } else {
            Log.d(tag, "Cargar desde internet");
            new PokemonesDAOTask().execute(String.valueOf(model.getIdPokemon()));
        }
    }

    private class PokemonesDAOTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... params) {
            Log.d(tag,"doInBackground");

            String idString = params[0];
            StringBuilder servicioURL = new StringBuilder();
            servicioURL.append(urlPokemon);
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
            } catch (MalformedURLException e){
                e.printStackTrace();
            } catch (Exception ex){
                ex.printStackTrace();
            }
            return resultString.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d(tag,"onPostExecute");

            PokemonModel pokemon = null;

            try{
                JSONObject jsonResponse = new JSONObject(s);

                int idPokemon = jsonResponse.getInt("id");
                Log.d(tag,"idPokemon: " + idPokemon);

                String nombrePokemon = jsonResponse.getString("name");
                Log.d(tag,"nombrePokemon: " + nombrePokemon);

                JSONArray caracteristicas = jsonResponse.getJSONArray("stats");

                StringBuffer caracteristicaString = new StringBuffer();
                for (int i = 0; i < caracteristicas.length(); i++) {
                    JSONObject caracteristica = (JSONObject) caracteristicas.get(i);
                    String nombre = caracteristica.getJSONObject("stat").getString("name");
                    caracteristicaString.append(nombre);
                    if(i<caracteristicas.length()-1) {
                        caracteristicaString.append("-");
                    }
                }
                Log.d(tag,"características:" + caracteristicaString.toString());

                JSONArray habilidades = jsonResponse.getJSONArray("abilities");
                StringBuffer habilidadString = new StringBuffer();
                for (int i = 0; i < habilidades.length(); i++) {
                    JSONObject habilidad = (JSONObject) habilidades.get(i);
                    String nombre = habilidad.getJSONObject("ability").getString("name");
                    habilidadString.append(nombre);
                    if(i<habilidades.length()-1) {
                        habilidadString.append("-");
                    }
                }
                Log.d(tag,"habilidades:" + habilidadString.toString());

                JSONArray tipos = jsonResponse.getJSONArray("types");
                StringBuffer tiposString = new StringBuffer();
                for (int i = 0; i < tipos.length(); i++) {
                    JSONObject tipo = (JSONObject) tipos.get(i);
                    String nombre = tipo.getJSONObject("type").getString("name");
                    tiposString.append(nombre);
                    if(i<tipos.length()-1){
                        tiposString.append("-");
                    }
                }
                Log.d(tag,"tipos: " + tiposString.toString());

                String peso = jsonResponse.getString("weight").toString().toUpperCase();
                Log.d(tag, "peso: "+peso);

                String tamano = jsonResponse.getString("height").toString().toUpperCase();
                Log.d(tag,"tamaño: "+tamano);

                String foto = jsonResponse.getJSONObject("sprites").getString("front_default");
                if(foto!=null){
                    Log.d(tag,"foto: "+foto);
                } else {
                    Log.d(tag,"pokemon sin foto");
                    foto = null;
                }

                PokemonModel model = new PokemonModel();
                model.setIdPokemon(idPokemon);
                model.setIdTipo(pokemonModel.getIdTipo());

                model.setNombre(nombrePokemon.toUpperCase());

                model.setHabilidades(habilidadString.toString().toUpperCase());
                model.setTipos(tiposString.toString().toUpperCase());
                model.setCaracteristicas(caracteristicaString.toString().toUpperCase());

                model.setPeso(peso.toUpperCase());
                model.setTamano(tamano.toUpperCase());
                model.setFotografia(foto);

                Log.d(tag,model.toString());

                DBLocalOpenHelper openHelper = new DBLocalOpenHelper(contexto);
                if(openHelper.editarPokemon(model)){
                    Log.d(tag,"Pokemon actualizado correctamente");
                } else {
                    Log.d(tag,"Hubo un problema actualizando el pokemon");
                }

                delegate.onPokemonesDAOComplete(model);

            } catch (JSONException e){
                e.printStackTrace();
            }

        }

        @Override
        protected void onCancelled(String s) {
            super.onCancelled(s);
            Log.d(tag,"onCancelled");
            delegate.onPokemonesDAOError(s);
        }

    }

}
