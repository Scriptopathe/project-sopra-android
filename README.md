# project-sopra-android
This is the android client for this project's server : https://github.com/Scriptopathe/project-sopra

--------------------------
Setting the server address
--------------------------

In order to specify the good Rest API server, you need to edit the Constantes.java file located in app/src/main/java/com/example/dorian/sopraandroid/

And change the following line:

    public static final String SERVER = "[ADDRESS:PORT]";

By replacing `[ADDRESS:PORT]` with the value you need.

Note that if your server is at *example.com* and and the API at *example.com/api*, you should just put *example.com* because the software automatically add the */api*.
