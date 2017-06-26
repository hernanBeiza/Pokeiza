package cl.hiperactivo.pokeiza.libs;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/** Clase que carga en segundo plano los archivos o imágenes desde internet
 * Created by hernanBeiza on 6/23/17.
 */

public class FileManager extends AsyncTask<String, Void, Bitmap> {

    private static final String tag = "FileManager";
    public FileManagerDelegate delegate;

    public interface FileManagerDelegate {
        /**
         * Llamado en caso de lograr cargar la imagen correctamente
         * @param imageBitmap
         */
        void onFileManagerComplete(Bitmap imageBitmap);

        /***
         * Llamado en caso de existir error al cargar la imagen
         * @param error
         */
        void onFileManagerError(String error);
    }

    /**
     * Cargar una ruta desde internet
     * @param ruta String ruta de la imagen. Comunica a la clase que lo implementa un
     *             onFileManagerComplete si la carga se realiza correctamente
     *             En caso contrario onFileManagerError
     */
    public void cargar(String ruta) {
        Log.d(tag,"cargar: "+ruta);
        if(!ruta.equals("null")){
            this.execute(ruta);
        } else {
            Log.d(tag,"Este pokemon no tiene imagen");
            this.delegate.onFileManagerError("Este pokemon no tiene imagen");
        }
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        try {
            URL url = new URL(params[0]);
            InputStream is = url.openConnection().getInputStream();
            Bitmap bitMap = BitmapFactory.decodeStream(is);
            return bitMap;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            this.delegate.onFileManagerError(e.getLocalizedMessage());
        } catch (IOException e) {
            e.printStackTrace();
            this.delegate.onFileManagerError(e.getLocalizedMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        super.onPostExecute(result);
        this.delegate.onFileManagerComplete(result);
    }

    @Override
    protected void onCancelled(Bitmap bitmap) {
        super.onCancelled(bitmap);
        this.delegate.onFileManagerError("Se ha cancelado la carga de la imagen");
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        this.delegate.onFileManagerError("Se ha cancelado la carga de la imagen");
    }
}