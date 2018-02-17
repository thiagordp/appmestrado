package com.example.lenovo.appmestrado;

import java.io.InputStream;
import java.util.Scanner;

/**
 * Created by lenovo on 10/01/2018.
 */

public class Util {
/*
    public static final String FILE_NAME = "a16.txt";

    public static final String NAME_KEY = "name";

    public static final String TIME_KEY = "time";

    public static final String DATE_KEY = "date";

    public static final String MESSAGE_KEY = "message";

    public static final String TYPE_KEY = "type";

    public static final String TYPE_DAILY = "daily";

    public static final String TYPE_UNIQUE = "unique";

    public static final String TYPE_WEEKLY = "weekly";

    public static final Long DELAY_24H = 86400000L;     // 24 * 60 * 60 * 1000L;

    public static final String URL_SERVER = "http://192.168.1.232:8080/WebService-Mestrado";

    public static final String LIST_RESOURCE = "/tasks";

    public static final String NEW_TASK_RESOURCE = "/new-task";

    public static final String DELETE_USER_TASKS = "/delete-all";

    public static final String USER_ID_KEY = "id_user";*/


    public static final String FILE_NAME = "a16.txt";
    public static final String NAME_KEY = "nome";
    public static final String TIME_KEY = "hora";
    public static final String DATE_KEY = "data";
    public static final String MESSAGE_KEY = "mensagem";
    public static final String TYPE_KEY = "frequencia";
    public static final String TYPE_DAILY = "daily";
    public static final String TYPE_UNIQUE = "unique";
    public static final String TYPE_WEEKLY = "weekly";
    public static final Long DELAY_1H = 90000L;//3600000L; // 60*60*1000
    public static final Long DELAY_24H = 86400000L;     // 24 * 60 * 60 * 1000L;
    public static final String URL_SERVER = "http://150.162.234.150:8080/castelan/json.php?";
    //http://150.162.234.150:8080/castelan/json.php?type=consulta&id_user=1
    public static final String LIST_RESOURCE = "type=consulta";
    public static final String NEW_TASK_RESOURCE = "type=insere";
    public static final String DELETE_USER_TASKS = "type=removeall";
    public static final String USER_ID_KEY = "id_user";
    public static final String AND = "&";

    ///////////////////////////////////////////////////////////////////////

    public static String convertStreamToString(InputStream inputStream) {
        Scanner s = new java.util.Scanner(inputStream).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }


}
