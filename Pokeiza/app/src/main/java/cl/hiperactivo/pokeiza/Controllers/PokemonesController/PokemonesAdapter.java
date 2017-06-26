package cl.hiperactivo.pokeiza.Controllers.PokemonesController;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import cl.hiperactivo.pokeiza.Models.PokemonModel;
import cl.hiperactivo.pokeiza.Models.TipoModel;
import cl.hiperactivo.pokeiza.R;

/**
 * Created by hernanBeiza on 6/21/17.
 */

public class PokemonesAdapter extends ArrayAdapter <PokemonModel> {


    public PokemonesAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<PokemonModel> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        PokemonModel pokemon = getItem(position);

        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.layout_pokemon,null);
        }

        if(pokemon!=null){
            TextView nombreTextView = (TextView)convertView.findViewById(R.id.nombreTextView);
            nombreTextView.setText(pokemon.getNombre());
        }
        return convertView;

    }

}



