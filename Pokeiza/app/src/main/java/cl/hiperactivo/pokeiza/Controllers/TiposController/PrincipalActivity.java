package cl.hiperactivo.pokeiza.Controllers.TiposController;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Parcel;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

import cl.hiperactivo.pokeiza.Controllers.ConfiguracionController.ConfiguracionActivity;
import cl.hiperactivo.pokeiza.Controllers.CreditosController.CreditosActivity;
import cl.hiperactivo.pokeiza.Controllers.PokemonesController.PokemonesActivity;
import cl.hiperactivo.pokeiza.DAO.TiposDAO;
import cl.hiperactivo.pokeiza.Models.TipoModel;
import cl.hiperactivo.pokeiza.R;
import cl.hiperactivo.pokeiza.libs.Alerta;
import cl.hiperactivo.pokeiza.libs.Cargador;
import cl.hiperactivo.pokeiza.libs.Sonido;

public class PrincipalActivity extends AppCompatActivity implements TiposDAO.TiposDAODelegate {

    private static final String tag = "PrincipalActivity";
    private ListView tiposListView;
    private int idtipo = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        this.tiposListView = (ListView)this.findViewById(R.id.TiposListView);

        tiposListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                //Log.d(tag, "onItemClick " + position);
                TipoModel tipo = (TipoModel) tiposListView.getAdapter().getItem(position);
                Log.d(tag, tipo.toString());

                Sonido sonido = new Sonido(getApplicationContext());
                sonido.intentarReproducir(R.raw.itemselected);
                //Pasamos un modelo de Tipo al intent siguiente
                Intent intent = new Intent(getApplicationContext(), PokemonesActivity.class);
                intent.putExtra(getString(R.string.ConstanteTipo),tipo);
                startActivity(intent);
            }
        });

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

    @Override
    protected void onResume() {
        super.onResume();

        this.cargarTipos();
    }

    //Carga los tipos de pokemon
    private void cargarTipos(){

        Cargador.getInstance().mostrarme(this,"Cargando tipos...");

        TiposDAO dao = new TiposDAO(getApplicationContext());
        dao.delegate = this;
        dao.cargar();
    }

    // Se implementan los delegados de TipoDAO
    @Override
    public void onTiposDAOCargando(String tipo) {
        Cargador.getInstance().actualizarMensaje(tipo);
    }

    @Override
    public void onTiposDAOComplete(ArrayList<TipoModel> tipos) {
        Log.d(tag,"onTiposDAOComplete");

        Cargador.getInstance().ocultarme();
        Toast.makeText(PrincipalActivity.this, "Tipos cargados correctamente", Toast.LENGTH_SHORT).show();

        TiposAdapter adapter = new TiposAdapter(getApplicationContext(),R.layout.layout_tipo,tipos);
        tiposListView.setAdapter(adapter);
    }

    @Override
    public void onTiposDAOError(String error) {
        Log.d(tag,"onTiposDAOERRO");
        Log.d(tag,error);

        Cargador.getInstance().ocultarme();
        Toast.makeText(PrincipalActivity.this, error, Toast.LENGTH_LONG).show();
    }

}
