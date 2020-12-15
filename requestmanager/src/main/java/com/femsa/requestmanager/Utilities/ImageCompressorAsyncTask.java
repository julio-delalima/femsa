package com.femsa.requestmanager.Utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;

import java.io.InputStream;
import java.lang.ref.WeakReference;

/**
 * <p>Clase que define una tarea asincrona para reducir la calidad y peso de las imagenes.</p>
 */
public class ImageCompressorAsyncTask extends AsyncTask<Uri, Void, Bitmap> {

    private String mImageContainerName;
    private int mWidthContainer;
    private int mHeightContainer;
    private ImageCompressorAsyncTaskInterface mInterface;
    private WeakReference<Context> mContext;

    /**
     * <p>Constructor de la tarea en segundo plano para comprimir imagenes.</p>
     * @param imageContainerName Contenedor de la imagen a la que se hace referencia.
     * @param mWidthContainer Ancho del contenedor.
     * @param mHeightContainer Alto del contenedor.
     * @param mInterface Interface de la clase que lo implementa.
     * @param context Contexto de donde se manda a llamar.
     */
    public ImageCompressorAsyncTask(String imageContainerName, int mWidthContainer, int mHeightContainer,
                                    ImageCompressorAsyncTaskInterface mInterface, Context context) {
        mImageContainerName = imageContainerName;
        this.mWidthContainer = mWidthContainer;
        this.mHeightContainer = mHeightContainer;
        this.mInterface = mInterface;
        mContext = new WeakReference<>(context);
    }

    /**
     * <p>Prepara los pasos necesarios antes de comenzar la compresión de las imagenes.</p>
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mInterface.onPreExecuteImage(mImageContainerName);
    }

    /**
     * <p>Realiza la compresión de las imágenes en segundo plano.</p>
     * @param bitmaps Uri de los bitmaps a crear.
     * @return Bitmap creado.
     */
    @Override
    protected Bitmap doInBackground(Uri... bitmaps) {
        InputStream inputStream;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        try {
            inputStream = mContext.get().getContentResolver().openInputStream(bitmaps[0]);
            BitmapFactory.decodeStream(inputStream, null, options);
            options.inSampleSize = Utilities.calculateInSampleSize(options, mWidthContainer, mHeightContainer);
            options.inJustDecodeBounds = false;
            if (inputStream != null) {
                inputStream.close();
            }
            inputStream = mContext.get().getContentResolver().openInputStream(bitmaps[0]);

            ExifInterface ei = new ExifInterface(bitmaps[0].getPath());

            Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, options);

            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);
            Bitmap rotatedBitmap;
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotatedBitmap = rotarImagen(bitmap, 90);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotatedBitmap = rotarImagen(bitmap, 180);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotatedBitmap = rotarImagen(bitmap, 270);
                    break;
                case ExifInterface.ORIENTATION_NORMAL:
                default:
                    rotatedBitmap = bitmap;
                    break;
            }

            bitmap = rotatedBitmap;
            return bitmap;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * <p>Rota la imagen. Hay dispositivos en los que la orientación por defecto es la horizontal,
     * por lo que se debe rotar la imagen.</p>
     *
     * @param bitmap bitmap a rotar.
     * @param angulo angulo.
     * @return bitmap rotado.
     */
    private static Bitmap rotarImagen(Bitmap bitmap, float angulo) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angulo);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    /**
     * <p>Método que se ejecuta cuando se terminaron de procesar las imagenes.</p>
     * @param bitmap Bitmap reducido.
     */
    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        mInterface.onPostExecuteImage(mImageContainerName, bitmap);
    }

    /**
     * <p>Interface a implementar para la compresión de imagenes.</p>
     */
    public interface ImageCompressorAsyncTaskInterface {
        /**
         * <p>Método de la interface que se ejecuta antes de realizar la compresión de imagenes.</p>
         * @param imageSelected Imagenes seleccionadas.
         */
        void onPreExecuteImage(String imageSelected);

        /**
         * <p>Método de la interface que se ejecuta después de realizar la compresión de imagenes.</p>
         * @param imageView Imagenes seleccionadas.
         * @param bitmap Bitmap creado.
         */
        void onPostExecuteImage(String imageView, Bitmap bitmap);
    }
}