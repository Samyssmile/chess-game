package de.chess.io.server;

import de.chess.dto.request.Request;
import de.chess.dto.response.Response;

public interface IRequestAnalyzer {

    public Response analyze(Request request, ClientThread clientThread);
}
