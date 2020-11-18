package de.chess.fx.app.client;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import de.chess.dto.response.OpenGameResponse;

import java.util.logging.Logger;

public class RspHandler {
    private static final Logger LOGGER =  Logger.getGlobal();
    private byte[] rsp = null;
    private Gson gson = new Gson();
    public synchronized boolean handleResponse(byte[] rsp) {
        this.rsp = rsp;
        this.notify();
        return true;
    }

    public synchronized void waitForResponse() {

        while(this.rsp == null) {
            try {
                this.wait();
            } catch (InterruptedException e) {
            }
        }
        String responseString = new String(this.rsp);
        JsonObject jobj = gson.fromJson(responseString, JsonObject.class);
        String requestType = jobj.get("requestType").getAsString();
        if (requestType.equals("NEW_GAME")){
            OpenGameResponse response = gson.fromJson(responseString, OpenGameResponse.class);
            if (response.isGranted()){
                LOGGER.info("OPEN GAME GRANT: "+ response.getGameUUID());
            }
        }
    }


}