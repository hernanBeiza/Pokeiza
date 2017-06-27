package cl.hiperactivo.pokeiza.Controllers.PokemonController;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import cl.hiperactivo.pokeiza.Controllers.ConfiguracionController.ConfiguracionActivity;
import cl.hiperactivo.pokeiza.Controllers.CreditosController.CreditosActivity;
import cl.hiperactivo.pokeiza.DAO.PokemonDAO;
import cl.hiperactivo.pokeiza.Models.PokemonModel;
import cl.hiperactivo.pokeiza.R;
import cl.hiperactivo.pokeiza.libs.Cargador;
import cl.hiperactivo.pokeiza.libs.Alerta;
import cl.hiperactivo.pokeiza.libs.FileManager;
import cl.hiperactivo.pokeiza.libs.Sonido;

public class PokemonActivity extends AppCompatActivity implements PokemonDAO.PokemonesDAODelegate, FileManager.FileManagerDelegate {

    private static final String tag = "PokemonActivity";

    private EditText caracteristicasEditText;
    private EditText habilidadesEditText;
    private EditText tiposEditText;

    private TextView nombreTextView;
    private TextView pesoTextView;
    private TextView tamanoTextView;

    private ImageView fotoImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon);

        this.caracteristicasEditText = (EditText)findViewById(R.id.caracteristicasEditText);
        this.habilidadesEditText = (EditText)findViewById(R.id.habilidadesEditText);
        this.tiposEditText = (EditText)findViewById(R.id.tiposEditText);

        this.caracteristicasEditText.setEnabled(false);
        this.habilidadesEditText.setEnabled(false);
        this.tiposEditText.setEnabled(false);

        this.nombreTextView = (TextView)findViewById(R.id.nombreTextView);
        //this.nombreTextView.setEnabled(false);

        this.pesoTextView = (TextView)findViewById(R.id.pesoTextView);
        //this.pesoTextView.setEnabled(false);

        this.tamanoTextView = (TextView)findViewById(R.id.tamanoTextView);
        //this.tamanoTextView.setEnabled(false);

        this.fotoImageView = (ImageView)findViewById(R.id.fotoImageView);
    }

    public void onVolver(View v){
        this.finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(tag,"onResume");
        // Obtengo el modelo pasado de la vista anterior
        PokemonModel model = (PokemonModel) getIntent().getParcelableExtra(getString(R.string.ConstantePokemon));
        Log.d(tag,model.getNombre());

        this.cargarDetallesPokemon(model);
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

    /**
     * Carga los detalles del pokemon según el POkemonModel seleccionado de la lista anterior
     * @param pokemon
     */
    private void cargarDetallesPokemon(PokemonModel pokemon){
        Cargador.getInstance().mostrarme(this,"¡Cargando detalles de este pokemon!");
        PokemonDAO dao = new PokemonDAO(this);
        dao.delegate = this;
        dao.cargarDetalleConID(pokemon);
    }

    // Se implementa el dao de PokemonDAO
    @Override
    public void onPokemonesDAOComplete(PokemonModel model) {
        Cargador.getInstance().ocultarme();

        Log.d(tag,model.toString());
        String nombre = getString(R.string.NombreTitulo)+ " ";
        String peso = getString(R.string.PesoTitulo)+ " ";
        String tamano = getString(R.string.TamanoTitulo)+ " ";
        //Mostrar la data del pokemon acá, en esta vista
        nombreTextView.setText(nombre + model.getNombre());
        pesoTextView.setText(peso + model.getPeso());
        tamanoTextView.setText(tamano + model.getTamano());

        String caracteristicas = getString(R.string.CaracteristicasTitulo)+" ";
        String habilidades = getString(R.string.HabilidadesTitulo)+" ";
        String tipos = getString(R.string.TiposTitulo)+" ";
        caracteristicasEditText.setText(caracteristicas + model.getCaracteristicas());
        habilidadesEditText.setText(habilidades + model.getHabilidades());
        tiposEditText.setText(tipos + model.getTipos());

        FileManager file = new FileManager();
        file.delegate = this;
        file.cargar(model.getFotografia());
    }

    @Override
    public void onPokemonesDAOError(String error) {
        Log.d(tag,error);
        Cargador.getInstance().ocultarme();
        Alerta.getInstance().mostrarme(this,"Error al cargar el detalle del pokemon", true);

        Sonido sonido = new Sonido(getApplicationContext());
        sonido.intentarReproducir(R.raw.cargafotoerror);

    }

    /* Se implementa el delegado de FileManager para saber si se carga o
     no la imagen y mostrar una alerta
     */
    @Override
    public void onFileManagerComplete(Bitmap imageBitmap) {
        Log.d(tag,"onFileManagerComplete");

        this.fotoImageView.setImageBitmap(imageBitmap);

        Sonido sonido = new Sonido(getApplicationContext());
        sonido.intentarReproducir(R.raw.cargafotook);
    }

    @Override
    public void onFileManagerError(String error) {
        Log.d(tag,"onFileManagerError");

        Alerta.getInstance().mostrarme(this,error,true);

        Sonido sonido = new Sonido(getApplicationContext());
        sonido.intentarReproducir(R.raw.cargafotoerror);
    }

}
