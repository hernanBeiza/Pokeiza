package cl.hiperactivo.pokeiza.libs;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

/** Singletón que muetra un ProgressDialog. Usado para mostrar una alerta al momento de
 * cargar algo desde internet. No es cancelable por el usuario
 * Created by hernanBeiza on 6/22/17.
 */

public class Cargador {
    private static final String tag = "Cargador";

    private static final Cargador ourInstance = new Cargador();

    public static Cargador getInstance() {
        return ourInstance;
    }

    private ProgressDialog miProgressDialog;

    private Cargador() { }

    /***
     * Muestra el cargador. No es cancelable por el usuario
     * @param contexto Context: Contexto de la aplicación
     * @param mensaje String: Mensaje a mostrar en la alerta con el ProgressDialog
     */
    public void mostrarme(Context contexto, String mensaje){
        Log.d(tag,"mostrarme");
        if(miProgressDialog==null){
            miProgressDialog = new ProgressDialog(contexto);
            miProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            miProgressDialog.setCancelable(false);
        }
        miProgressDialog.setMessage(mensaje);
        miProgressDialog.show();
    }

    public void actualizarMensaje(String mensaje){
        if(miProgressDialog!=null) {
            miProgressDialog.setMessage(mensaje);
        }
    }

    public void ocultarme() {
        Log.d(tag,"ocultarme");
        if(miProgressDialog!=null){
            miProgressDialog.setMessage("");
            miProgressDialog.cancel();
            miProgressDialog = null;
        }
    }


}
