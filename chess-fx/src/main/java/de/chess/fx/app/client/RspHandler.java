package de.chess.fx.app.client;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import de.chess.dto.RequestType;
import de.chess.dto.response.OpenGameResponse;
import de.chess.dto.response.ReceiveGameListResponse;
import de.chess.fx.app.provider.GameListProvider;

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
        if (requestType.equals(RequestType.NEW_GAME.name())){
            OpenGameResponse response = gson.fromJson(responseString, OpenGameResponse.class);
            if (response.isGranted()){
                LOGGER.info("OPEN GAME GRANT: "+ response.getGameUUID());
            }
        }else if(requestType.equals(RequestType.REQUEST_GAME_LIST.name())){
            ReceiveGameListResponse receiveGameListResponse = gson.fromJson(responseString, ReceiveGameListResponse.class);
            GameListProvider.getInstance().setGameList(receiveGameListResponse.getGameList());
            System.out.println(receiveGameListResponse);
        }
    }


}