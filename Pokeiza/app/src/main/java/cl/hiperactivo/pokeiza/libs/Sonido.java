package cl.hiperactivo.pokeiza.libs;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.util.Log;

import cl.hiperactivo.pokeiza.R;

/**
 * Created by hernanBeiza on 6/25/17.
 */

public class Sonido {

    private Context contexto;

    public Sonido(Context contexto) {
        this.contexto = contexto;
    }

    public void intentarReproducir(int elSonido){

        SharedPreferences sp = this.contexto.getSharedPreferences(this.contexto.getString(R.string.ConstanteShared), Context.MODE_PRIVATE);
        boolean reproducir = sp.getBoolean(this.contexto.getString(R.string.ConstanteConfiguracion),true);

        if(reproducir){
            Log.d("Sonido","Reproducir");
            MediaPlayer mp = MediaPlayer.create(this.contexto.getApplicationContext(),elSonido);
            mp.start();
        }

    }

}
