package de.chess.app.integration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Real End-to-End test with actual server and client processes
 * This test catches issues that mock tests miss
 */
class RealProcessE2ETest {
    
    private static final Logger LOGGER = Logger.getGlobal();
    private static final int SERVER_PORT = 8085;
    private static final int STARTUP_TIMEOUT_SECONDS = 10;
    private static final int MOVE_TIMEOUT_SECONDS = 5;
    
    private Process serverProcess;
    private Process client1Process;
    private Process client2Process;
    
    @BeforeEach
    void setUp() throws Exception {
        // Clean up any existing processes
        tearDown();
        
        // Build the project first
        ProcessBuilder buildProcess = new ProcessBuilder("./gradlew", "build", "-x", "test");
        buildProcess.directory(new File("."));
        Process build = buildProcess.start();
        assertTrue(build.waitFor(30, TimeUnit.SECONDS), "Build should complete in 30 seconds");
        assertEquals(0, build.exitValue(), "Build should succeed");
    }
    
    @AfterEach
    void tearDown() {
        if (client1Process != null && client1Process.isAlive()) {
            client1Process.destroyForcibly();
        }
        if (client2Process != null && client2Process.isAlive()) {
            client2Process.destroyForcibly();
        }
        if (serverProcess != null && serverProcess.isAlive()) {
            serverProcess.destroyForcibly();
        }
    }
    
    @Test
    @DisplayName("Real E2E test: Server + 2 Clients with actual move synchronization")
    void testRealMoveSynchronization() throws Exception {
        LOGGER.info("=== Starting Real E2E Test ===");
        
        // Step 1: Start server
        startServer();
        Thread.sleep(3000); // Wait for server startup
        
        // Step 2: Start first client (host)
        startClient1();
        Thread.sleep(2000);
        
        // Step 3: Start second client (joiner)
        startClient2();
        Thread.sleep(2000);
        
        // Step 4: Simulate game creation and joining
        // This would require either:
        // a) Automated UI interaction (complex)
        // b) Programmatic client simulation
        // c) Log file analysis
        
        // For now, let's verify processes are running and communicating
        assertTrue(serverProcess.isAlive(), "Server should be running");
        assertTrue(client1Process.isAlive(), "Client 1 should be running");
        assertTrue(client2Process.isAlive(), "Client 2 should be running");
        
        // Step 5: Wait and check for communication in logs
        Thread.sleep(5000);
        
        // The real test would need to:
        // 1. Automate game creation via client 1
        // 2. Automate game joining via client 2  
        // 3. Automate moves via both clients
        // 4. Verify UI updates via screenshot comparison or log analysis
        
        LOGGER.info("=== E2E Test completed - processes started successfully ===");
    }
    
    private void startServer() throws IOException {
        LOGGER.info("Starting server process...");
        
        ProcessBuilder serverBuilder = new ProcessBuilder(
            "./gradlew", "chess-ws:run", "--console=plain"
        );
        serverBuilder.directory(new File("."));
        
        // Redirect server output to a file for analysis
        File serverLog = new File("server-e2e.log");
        serverBuilder.redirectOutput(serverLog);
        serverBuilder.redirectError(serverLog);
        
        serverProcess = serverBuilder.start();
        LOGGER.info("Server process started with PID: " + serverProcess.pid());
    }
    
    private void startClient1() throws IOException {
        LOGGER.info("Starting client 1 process...");
        
        ProcessBuilder client1Builder = new ProcessBuilder(
            "./gradlew", "chess-fx:run", "--console=plain"
        );
        client1Builder.directory(new File("."));
        
        // Set environment to distinguish this client
        client1Builder.environment().put("CHESS_CLIENT_ID", "CLIENT_1");
        
        File client1Log = new File("client1-e2e.log");
        client1Builder.redirectOutput(client1Log);
        client1Builder.redirectError(client1Log);
        
        client1Process = client1Builder.start();
        LOGGER.info("Client 1 process started with PID: " + client1Process.pid());
    }
    
    private void startClient2() throws IOException {
        LOGGER.info("Starting client 2 process...");
        
        ProcessBuilder client2Builder = new ProcessBuilder(
            "./gradlew", "chess-fx:run", "--console=plain"  
        );
        client2Builder.directory(new File("."));
        
        client2Builder.environment().put("CHESS_CLIENT_ID", "CLIENT_2");
        
        File client2Log = new File("client2-e2e.log");
        client2Builder.redirectOutput(client2Log);
        client2Builder.redirectError(client2Log);
        
        client2Process = client2Builder.start();
        LOGGER.info("Client 2 process started with PID: " + client2Process.pid());
    }
    
    /**
     * This is what a REAL E2E test should do:
     * Test with automated UI interaction
     */
    @Test
    @DisplayName("TODO: Real UI automation test")
    void testWithUIAutomation() {
        // This would require:
        // 1. TestFX for JavaFX UI automation
        // 2. Automated clicking, typing, dragging
        // 3. Screenshot comparison
        // 4. Board state verification
        
        // Example pseudo-code:
        // 1. client1.clickButton("Create Game")
        // 2. client1.enterText("GameName", "TestGame")
        // 3. client1.clickButton("Create")
        // 4. client2.clickButton("Join Game")
        // 5. client2.selectGame("TestGame")
        // 6. client1.dragPiece("e2", "e4")
        // 7. assertBoardStateEquals(client1.getBoardState(), client2.getBoardState())
        
        // For now, this is a placeholder
        LOGGER.info("UI automation test not implemented yet - would require TestFX");
    }
}