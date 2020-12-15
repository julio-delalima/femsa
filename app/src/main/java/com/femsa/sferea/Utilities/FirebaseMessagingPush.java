package com.femsa.sferea.Utilities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import androidx.core.app.NotificationCompat;

import com.femsa.sferea.R;
import com.femsa.sferea.home.inicio.componentes.activity.HomeActivity;
import com.femsa.sferea.Login.LoginActivity;
import com.femsa.sferea.home.inicio.programa.detallePrograma.DetalleProgramaActivity;
import com.femsa.sferea.home.inicio.programa.objetosAprendizaje.ObjetoAprendizajeActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Objects;

import static com.femsa.sferea.home.inicio.programa.detallePrograma.DetalleProgramaActivity.ObjetosAprendizaje.JUEGO_MULTIJUGADOR;
import static com.femsa.sferea.home.inicio.programa.objetosAprendizaje.ObjetoAprendizajeActivity.ID_OBJECT_KEY;
import static com.femsa.sferea.home.inicio.programa.objetosAprendizaje.ObjetoAprendizajeActivity.ID_PROGRAMA_KEY;
import static com.femsa.sferea.home.inicio.programa.objetosAprendizaje.ObjetoAprendizajeActivity.TIPO_OBJETO_KEY;

public class FirebaseMessagingPush extends FirebaseMessagingService {
    public static final String TAG = "PUSH";

    public static final String ID_SOLICITUD = "SolicitudPush";

    public static final String CELULAS_CLICK_ACTION_KEY = "Celulas";
    public static final String RETADOR_NOMBRE_KEY = "retador";
    public static final String RETADOR_ID_KEY = "retadorID";
    public static final String GUSANOS_CLICK_ACTION_KEY = "Gusanos";

    private int idSolicitud;
    private int idObjeto;
    private int idPrograma;
    private int idRetador;

    private String idNotificacion;
    private String nombreRetador;



    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if(remoteMessage != null)
            {
                try
                    {
                        idSolicitud = Integer.parseInt(Objects.requireNonNull(remoteMessage.getData().get("idSolicitud")));
                        idNotificacion = Objects.requireNonNull(remoteMessage.getData().get("idNotif"));
                    }
                catch (Exception ex)
                    {
                        idSolicitud = 0;
                        idNotificacion = "";
                    }

                try
                    {
                    idPrograma = Integer.parseInt(Objects.requireNonNull(remoteMessage.getData().get("idPrograma")));
                    idObjeto = Integer.parseInt(Objects.requireNonNull(remoteMessage.getData().get("idObjeto")));
                    nombreRetador = Objects.requireNonNull(remoteMessage.getData().get("nombre"));
                    idRetador = Integer.parseInt(Objects.requireNonNull(remoteMessage.getData().get("idEmpleado")));
                }
                catch (Exception ex){
                    idPrograma = 0;
                    idObjeto = 0;
                    nombreRetador = "";
                    idRetador = 0;
                }

                LogManager.d("PUSHES", "idNotificacion: " + idNotificacion + " \n " + " idSolicitud: " + idSolicitud + " \nidObjeto: " + idObjeto + " \nidPrograma: " + idPrograma);
                manejadorIntent(remoteMessage);

              /*  if(remoteMessage.getNotification().getClickAction() != null)
                    {
                    }*/

            }
        super.onMessageReceived(remoteMessage);
    }

    public void manejadorIntent(RemoteMessage remoteMessage)
        {
            Intent intent;

            intent = creaIntent(remoteMessage);

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            String channelId = "Default";
            String titulo, cuerpo;
            if(remoteMessage.getData() != null)
                {
                    titulo = remoteMessage.getData().get("title");
                    cuerpo = remoteMessage.getData().get("body");

                    if(titulo == null && remoteMessage.getNotification() != null)
                        {
                            titulo = remoteMessage.getNotification().getTitle();
                            cuerpo = remoteMessage.getNotification().getBody();
                        }
                }
            else if(remoteMessage.getNotification() != null)
                {
                    titulo = remoteMessage.getNotification().getTitle();
                    cuerpo = remoteMessage.getNotification().getBody();

            }
            else
            {
                titulo = "-";
                cuerpo = "-";
            }
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                    .setSmallIcon(R.mipmap.ic_push)
                    .setContentTitle(titulo)
                    .setContentText(cuerpo)
                    .setAutoCancel(true)
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(cuerpo))
                    .setContentIntent(pendingIntent);
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            {
                NotificationChannel channel = new NotificationChannel(channelId, "Default channel", NotificationManager.IMPORTANCE_DEFAULT);
                manager.createNotificationChannel(channel);
            }
            manager.notify(0, builder.build());
        }


        private Intent creaIntent(RemoteMessage remoteMessage)
        {
            Intent intent = null;
            if(SharedPreferencesUtil.getInstance().isLogged())
            {
                if(remoteMessage.getNotification() != null && remoteMessage.getNotification().getClickAction() != null)
                    {
                        if(remoteMessage.getNotification().getClickAction().equals(CELULAS_CLICK_ACTION_KEY))
                            {
                                LogManager.d("PUSHES", getClass().getCanonicalName() + ":Células <- idSolicitud: " + idSolicitud);
                                intent = new Intent(this, HomeActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.putExtra(CELULAS_CLICK_ACTION_KEY, true);
                                intent.putExtra(ID_SOLICITUD, idSolicitud);
                            }
                        else if(remoteMessage.getNotification().getClickAction().equals(GUSANOS_CLICK_ACTION_KEY))
                            {
                                LogManager.d("PUSHES", getClass().getCanonicalName() + ":Gusanos <- NombreRetador: " + nombreRetador + " \nidObjeto: " + idObjeto + " \nidPrograma: " + idPrograma + " \nidRetador: " + idRetador);
                                intent = nombreRetador.equals("0")
                                        ? new Intent(this, DetalleProgramaActivity.class)
                                        : new Intent(this, ObjetoAprendizajeActivity.class);
                                intent.putExtra(ID_OBJECT_KEY, idObjeto);
                                intent.putExtra(ID_PROGRAMA_KEY, idPrograma);
                                intent.putExtra(RETADOR_NOMBRE_KEY, nombreRetador);
                                intent.putExtra(RETADOR_ID_KEY, idRetador);
                                intent.putExtra(TIPO_OBJETO_KEY, JUEGO_MULTIJUGADOR);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            }
                        else{
                            LogManager.d("PUSHES", getClass().getCanonicalName() + ":Error <- Home");
                            intent = new Intent(this, HomeActivity.class);
                        }

                    }
            }
            else
            {
                intent = new Intent(this, LoginActivity.class);
            }
            return intent;
        }


    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    @Override
    public void onNewToken(String token) {
        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        LogManager.d("PUSHES", token);
        GlobalActions g = new GlobalActions(null);
        g.SetTokenFirebase(token, "A");
    }
}
