package controllers;

/**
 * Created by edu on 14/08/14.
 */
public class Security extends Secure.Security{
    static boolean authentify(String username, String password) {
        return true;
    }
    static void onDisconnected() {
        Titus.search();
    }
}
