package cl.hiperactivo.pokeiza.libs;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/** Clase para métodos varios
 * Created by hernanBeiza on 6/22/17.
 */

public class Utilidades {

    /***
     * Función para saber si hay o no conexión a internet
     * @param context Context de la aplicación
     * @return true o false en caso de existir internet
     */
    public static boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager)  context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
