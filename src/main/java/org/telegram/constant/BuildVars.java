package org.telegram.constant;

/**
 * @author Ruben Bermudez
 * @version 1.0
 * @brief Custom build vars FILL EVERYTHING CORRECTLY
 * @date 20 of June of 2015
 */

public class BuildVars {
    public static final Boolean debug = true;
    public static final Boolean useWebHook = false;
    public static final int PORT = 8443;
    public static final String EXTERNALWEBHOOKURL = "https://example.changeme.com:" + PORT; // https://(xyz.)externaldomain.tld
    public static final String INTERNALWEBHOOKURL = "https://localhost.changeme.com:" + PORT; // https://(xyz.)localip/domain(.tld)
    public static final String pathToCertificatePublicKey = "./YOURPEM.pem"; //only for self-signed webhooks
    public static final String pathToCertificateStore = "./YOURSTORE.jks"; //self-signed and non-self-signed.
    public static final String certificateStorePassword = "yourpass"; //password for your certificate-store

    public static final String OPENWEATHERAPIKEY = "2e264cf646195f0cda0d2449c7252b77";

    public static final String DirectionsApiKey = "AIzaSyC5RRqsAaZNo7Y74oLM_Zlh7jseH9q7l2E";

    public static final String TRANSIFEXUSER = "love5757@me.com";
    public static final String TRANSIFEXPASSWORD = "xhrxhrdl0504";

    public static final String pathToLogs = "./";

    public static final String linkDB = "jdbc:mysql://yerina.kr:3360/dev_foodtruck?useUnicode=true&characterEncoding=UTF-8";
    public static final String controllerDB = "com.mysql.jdbc.Driver";
    public static final String userDB = "aapp24";
    public static final String password = "wnddud00";
}
