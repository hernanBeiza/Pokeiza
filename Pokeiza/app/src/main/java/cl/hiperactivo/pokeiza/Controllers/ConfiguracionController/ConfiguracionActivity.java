package cl.hiperactivo.pokeiza.Controllers.ConfiguracionController;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ToggleButton;

import cl.hiperactivo.pokeiza.R;

public class ConfiguracionActivity extends AppCompatActivity {

    private ToggleButton sonidoToggleButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);

        sonidoToggleButton = (ToggleButton)findViewById(R.id.sonidoToggleButton);

        SharedPreferences sp = getSharedPreferences(getString(R.string.ConstanteShared),MODE_PRIVATE);
        boolean sonido = sp.getBoolean(getString(R.string.ConstanteConfiguracion),true);
        sonidoToggleButton.setChecked(sonido);
    }

    public void onToggleClicked(View view) {

        boolean on = ((ToggleButton) view).isChecked();

        SharedPreferences sp = getSharedPreferences(getString(R.string.ConstanteShared),MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        editor.putBoolean(getString(R.string.ConstanteConfiguracion),on);
        editor.commit();
    }



}
