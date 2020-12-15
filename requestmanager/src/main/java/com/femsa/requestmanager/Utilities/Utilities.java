package com.femsa.requestmanager.Utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;


public class Utilities
    {
        /**
         * <p>
         *     Método que detecta el estado de la conectividad.
         * </p>
         * @param context Contexto de la aplicación.
         * @return Bandera con eo resultado de la consulta.
         */
        public static boolean getConnectivityStatus(Context context) {
            ConnectivityManager connectivityManager =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnected();
        }

        /**
         * <p>Método que convierte un bitmap en base64.</p>
         * @param bitmap Bitmap a convertir.
         * @return Imagen en base 64.
         */
        public static String bitmapToBase64(Bitmap bitmap) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            return Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT).replace("\n", "");
        }

        public static String convertUrlToBase64(Context context, String dir, int width, int height) {
            InputStream inputStream;
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            try {
                inputStream = context.getContentResolver().openInputStream(getURIFromRealPath(dir));
                BitmapFactory.decodeStream(inputStream, null, options);
                options.inSampleSize = Utilities.calculateInSampleSize(options, width, height);
                options.inJustDecodeBounds = false;
                if (inputStream != null) {
                    inputStream.close();
                }
                inputStream = context.getContentResolver().openInputStream(getURIFromRealPath(dir));


                ExifInterface ei = new ExifInterface(getURIFromRealPath(dir).getPath());
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
                return bitmapToBase64(bitmap);
            } catch (Exception e) {
                return "";
            }

        }

        private static String encodeImage(Bitmap bm)
        {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG,100,baos);
            byte[] b = baos.toByteArray();
            String encImage = Base64.encodeToString(b, Base64.DEFAULT);

            return encImage;
        }

        private String encodeImage(String path)
        {
            File imagefile = new File(path);
            FileInputStream fis = null;
            try{
                fis = new FileInputStream(imagefile);
            }catch(FileNotFoundException e){
                e.printStackTrace();
            }
            Bitmap bm = BitmapFactory.decodeStream(fis);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG,100,baos);
            byte[] b = baos.toByteArray();
            String encImage = Base64.encodeToString(b, Base64.DEFAULT);
            //Base64.de
            return encImage;

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

        public static Uri getURIFromRealPath(String realPath) {
            return Uri.fromFile(new File(realPath));
        }

        /**
         * <p>Calcula el factor de reducción de la imagen de acuerdo al tamaño deseado por el usuario.</p>
         * @param options Opciones del bitmap.
         * @param reqWidth Ancho requerido.
         * @param reqHeight Alto requerido.
         * @return Factor de reducción.
         */
        public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
            final int height = options.outHeight;
            final int width = options.outWidth;
            int inSampleSize = 1;

            if (height > reqHeight || width > reqWidth) {

                final int halfHeight = height / 2;
                final int halfWidth = width / 2;

                while ((halfHeight / inSampleSize) >= reqHeight
                        && (halfWidth / inSampleSize) >= reqWidth) {
                    inSampleSize *= 2;
                }
            }
            return inSampleSize;
        }

    }
