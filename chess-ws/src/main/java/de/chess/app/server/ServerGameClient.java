package de.chess.app.server;

import de.chess.dto.Declaration;

import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class ServerGameClient {
    private final Selector selector;
    private final SocketChannel serverSocket;
    private final SelectionKey key;
    private final Declaration filledDeclaration;

    public ServerGameClient(Selector selector, SocketChannel serverSocket, SelectionKey key, Declaration filledDeclaration) {
        this.selector = selector;
        this.serverSocket = serverSocket;
        this.key = key;
        this.filledDeclaration = filledDeclaration;

    }

    public Selector getSelector() {
        return selector;
    }

    public SocketChannel getServerSocket() {
        return serverSocket;
    }

    public SelectionKey getKey() {
        return key;
    }

    public Declaration getFilledDeclaration() {
        return filledDeclaration;
    }
}
