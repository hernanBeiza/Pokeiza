package cl.hiperactivo.pokeiza.libs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/** Singleton para manejar las alertas. Pueden ser cancelables usando un botón "OK" o solo de
 * espera a que el sistema por si solo retorne algo
 * Created by hernanBeiza on 6/23/17.
 */

public class Alerta {
    private static final Alerta ourInstance = new Alerta();

    public static Alerta getInstance() {
        return ourInstance;
    }

    private AlertDialog miDialog;

    private Alerta() { }

    /***
     * Muestra la alerta
     * @param contexto Context: Contexto de la aplicación
     * @param mensaje String: Mensaje a mostrar en la alerta
     * @param cancelable boolean: Sí es cancelable o no mediante un botón "OK"
     */
    public void mostrarme(Context contexto, String mensaje, boolean cancelable) {
        if(this.miDialog==null){
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(contexto);
            alertDialog.setCancelable(cancelable);
            if(cancelable){
                alertDialog.setNegativeButton("OK",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        miDialog.cancel();
                        miDialog = null;
                    }
                });
            }
            alertDialog.setTitle("Atención");
            alertDialog.setMessage(mensaje);
            this.miDialog = alertDialog.create();
            this.miDialog.show();
        }
    }

    public void ocultarme() {
        if(this.miDialog!=null) {
            this.miDialog.cancel();
            this.miDialog = null;
        }
    }

}
