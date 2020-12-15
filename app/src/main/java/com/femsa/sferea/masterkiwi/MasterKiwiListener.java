package com.femsa.sferea.masterkiwi;

/**
 * <p>Interfaz que cuenta con los eventos que reporta MasterKiwi.</p>
 */
public interface MasterKiwiListener
    {
    /**
     * <p>Recibio un evento login.</p>
     *
     * @param sender    Quien lo mando a llamar.
     * @param eventArgs Los argumentos con los que se llamo
     */
    void onLogin(Object sender, MasterKiwiArgs eventArgs);

    /**
     * <p>Recibio un evento logout.</p>
     *
     * @param sender    Quien lo mando a llamar.
     * @param eventArgs Los argumentos con los que se llamo
     */
    void onLogout(Object sender, MasterKiwiArgs eventArgs);

    /**
     * <p>Recibio un evento de error de red.</p>
     *
     * @param sender    Quien lo mando a llamar.
     * @param eventArgs Los argumentos con los que se llamo
     */
    void onNetError(Object sender, MasterKiwiArgs eventArgs);

    /**
     * <p>Recibio un evento de inicio de compresion.</p>
     *
     * @param sender    Quien lo mando a llamar.
     * @param eventArgs Los argumentos con los que se llamo
     */
    void onInitiatingCompresure(Object sender, MasterKiwiArgs eventArgs);

    /**
     * <p>Recibio un evento inicio de Texturas..</p>
     *
     * @param sender    Quien lo mando a llamar.
     * @param eventArgs Los argumentos con los que se llamo
     */
    void onInitiatingTextures(Object sender, MasterKiwiArgs eventArgs);

    /**
     * <p>Recibio un evento generando lista de descarga.</p>
     *
     * @param sender    Quien lo mando a llamar.
     * @param eventArgs Los argumentos con los que se llamo
     */
    void onInitiatingAsstesList(Object sender, MasterKiwiArgs eventArgs);

    /**
     * <p>Recibio un evento de inicio de descarga.</p>
     *
     * @param sender    Quien lo mando a llamar.
     * @param eventArgs Los argumentos con los que se llamo
     */
    void onInitiatingDownload(Object sender, MasterKiwiArgs eventArgs);

    /**
     * <p>Recibio un evento de descarga terminada.</p>
     *
     * @param sender    Quien lo mando a llamar.
     * @param eventArgs Los argumentos con los que se llamo
     */
    void onDownloadFinished(Object sender, MasterKiwiArgs eventArgs);

    /**
     * <p>Recibio un evento de archivo descargado.</p>
     *
     * @param sender    Quien lo mando a llamar.
     * @param eventArgs Los argumentos con los que se llamo
     */
    void onAssetsDownload(Object sender, MasterKiwiArgs eventArgs);

    /**
     * <p>Recibio un evento de inicio de juego.</p>
     *
     * @param sender    Quien lo mando a llamar.
     * @param eventArgs Los argumentos con los que se llamo
     */
    void onSatrtingGame(Object sender, MasterKiwiArgs eventArgs);

    /**
     * <p>Recibio un evento de gameOver.</p>
     *
     * @param sender    Quien lo mando a llamar.
     * @param eventArgs Los argumentos con los que se llamo
     */
    void onGameOver(Object sender, MasterKiwiArgs eventArgs);

    /**
     * <p>Recibio un evento de salida del juego.</p>
     *
     * @param sender    Quien lo mando a llamar.
     * @param eventArgs Los argumentos con los que se llamo
     */
    void onExitGame(Object sender, MasterKiwiArgs eventArgs);

    /**
     * <p>Recibio un evento de usuario no ha iniciado sesión.</p>
     *
     * @param sender    Quien lo mando a llamar.
     * @param eventArgs Los argumentos con los que se llamo
     */
    void onNoneUserLogged(Object sender, MasterKiwiArgs eventArgs);

    /**
     * <p>Recibio un evento de que no se encontraron datos del juego.</p>
     *
     * @param sender    Quien lo mando a llamar.
     * @param eventArgs Los argumentos con los que se llamo
     */
    void onNoGameData(Object sender, MasterKiwiArgs eventArgs);

    void jsonSQData(Object sender, MasterKiwiArgs eventArgs);

    void mandarDatos(Object sender, MasterKiwiArgs eventArgs);
    }



