package de.chess.io.client;

import de.chess.dto.response.Response;

public interface IResponseAnalyzer {

    public void analyze(Response response);

    public Response analyze(String jsonResponse);
}
