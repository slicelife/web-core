package com.slice.auto;

import java.sql.SQLException;
import java.util.List;

class DatabaseClientHelpers {

    static final String queryShopName = "select name from shop_master WHERE shop_id=\"15425\"";
    static final String queryShopNamesAndIds = "select shop_id, name from shop_master";
    static List<String> listQueryResult;
    static String queryResult;

    static boolean isParameterPresent(List<String> queryResult, String param) {
        for (String strName : queryResult) {
            if (strName.contains(param)) {
                return true;
            }
        }
        return false;
    }

    static void executeListQuery(String query) throws SQLException {
        listQueryResult = DatabaseClient.executeSQLQueryList(query);
    }

    static void executeQuery(String query) throws SQLException {
        queryResult = DatabaseClient.executeSQLQuery(query);
    }
}