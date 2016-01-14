package com.example.dorian.sopraandroid;

/**
 * Created by dorian on 14/01/16.
 * Class used to parameter the compilation options
 */
public class Constantes {
    /**
     * The IP address of the server the application will connect to. Plus the port seprated by ":".
     * If you run the server on the same computer as you launch the Android emulator, you need to use
     * the address 10.0.2.2, it's the equivalent of localhost. 10.0.2.2 is where the emulator sees
     * the computer.
     * (for example, value during test : "10.0.2.2:8080")
     */
    public static final String SERVER = "10.0.2.2:8080";

    /**
     * Allows to compile in Remember user mode. If set to true, the user will just have to login once
     * and the his informations will be stored on the phone. If he wants to reset it, he has to go in
     * the apps configurations of phone and remove the data of this application. Then he'll be asked
     * again his informations.
     *
     * BE AWARE THAT IT IS :
     * /!\ Not safe to use REMEMBER_USER = true because the password will be stored in clear on the
     * phone, use at your own risk /!\**/
    public static final boolean REMEMBER_USER = false;
}
