package com.example.whatsthemove.config;

import com.example.whatsthemove.models.User;

public class AppData {
    static AppData appData;
    User user;

    private AppData() {
    }

    AppData getInstance() {

        if (appData == null) {
            appData = new AppData();
        }
        return appData;
    }

}
