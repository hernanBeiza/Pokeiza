package cl.hiperactivo.pokeiza.Controllers.PokemonesController;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import cl.hiperactivo.pokeiza.Controllers.ConfiguracionController.ConfiguracionActivity;
import cl.hiperactivo.pokeiza.Controllers.CreditosController.CreditosActivity;
import cl.hiperactivo.pokeiza.Controllers.PokemonController.PokemonActivity;
import cl.hiperactivo.pokeiza.DAO.DBLocalOpenHelper;
import cl.hiperactivo.pokeiza.Models.PokemonModel;
import cl.hiperactivo.pokeiza.Models.TipoModel;
import cl.hiperactivo.pokeiza.R;
import cl.hiperactivo.pokeiza.libs.Alerta;
import cl.hiperactivo.pokeiza.libs.Sonido;

public class PokemonesActivity extends AppCompatActivity {

    private static final String tag = "PokemonesActivity";

    private ListView pokemonesListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemones);
        pokemonesListView = (ListView)findViewById(R.id.PokemonesListView);

        pokemonesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                Log.d(tag, "onItemClick " + position);
                v.setSelected(true);

                PokemonModel pokemon = (PokemonModel) pokemonesListView.getAdapter().getItem(position);
                Log.d(tag, pokemon.toString());

                Sonido sonido = new Sonido(getApplicationContext());
                sonido.intentarReproducir(R.raw.itemselected);
                // Pasamos un modelo de pokemon a la vista siguiente
                Intent intent = new Intent(getApplicationContext(), PokemonActivity.class);
                intent.putExtra(getString(R.string.ConstantePokemon),pokemon);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        TipoModel model = (TipoModel) getIntent().getParcelableExtra(getString(R.string.ConstanteTipo));
        Log.d(tag,model.getNombre());
        this.cargarPokemonesConTipo(model);

    }

    //Obtiene el menú desde el archivo xm en res/menu y lo pone en pantalla
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    //Al hacer click en un elemento del menú, muestra la vista correspondiente
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuItemConfiguracion:
                Intent intentConfiguracion = new Intent(getApplicationContext(), ConfiguracionActivity.class);
                startActivity(intentConfiguracion);
                return true;
            case R.id.menuItemCreditos:
                Intent intentCreditos = new Intent(getApplicationContext(), CreditosActivity.class);
                startActivity(intentCreditos);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void onVolver(View v){
        this.finish();
    }

    /**
     * Carga los pokemones según el tipo_item
     * @param tipo TipoModel
     */
    private void cargarPokemonesConTipo(TipoModel tipo){
        DBLocalOpenHelper openHelper = new DBLocalOpenHelper(getApplicationContext());
        ArrayList<PokemonModel> pokemones = openHelper.obtenerPokemonesConTipo(tipo);
        if(pokemones!=null){
            PokemonesAdapter adapter = new PokemonesAdapter(getApplicationContext(),R.layout.layout_pokemon,pokemones);
            pokemonesListView.setAdapter(adapter);
        } else {
            Log.d(tag,"No hay pokemones de ese tipo_item");
            Alerta.getInstance().mostrarme(this,"No hay pokemons de este tipo_item", true);
        }

    }

}
