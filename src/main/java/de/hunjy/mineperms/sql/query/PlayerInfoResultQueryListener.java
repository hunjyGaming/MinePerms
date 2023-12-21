package de.hunjy.mineperms.sql.query;

import java.util.HashMap;
import java.util.List;

public interface PlayerInfoResultQueryListener {

    void onQueryResult(HashMap<String, String> rawUserData);
    void onQueryError(Exception exception);

}
