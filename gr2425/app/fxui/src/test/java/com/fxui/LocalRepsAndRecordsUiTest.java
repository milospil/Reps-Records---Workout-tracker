package com.fxui;

import static org.junit.jupiter.api.Assertions.*;

import com.core.UserManager;
import com.json.AppPersistence;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import javafx.stage.Stage;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;

public class LocalRepsAndRecordsUiTest extends ApplicationTest {
    private RepsAndRecordsUi app;
    private Path tempFile;
    
    @BeforeEach
    public void setUp() {
        RepsAndRecordsUi.supportHeadless();
    }

    @Test
    public void testSupportHeadless() {
        // Test when headless property is true
        System.setProperty("headless", "true");
        RepsAndRecordsUi.supportHeadless();
        // Verify system properties were set
        assertEquals("glass", System.getProperty("testfx.robot"));
        assertEquals("true", System.getProperty("testfx.headless"));
        assertEquals("sw", System.getProperty("prism.order"));
        assertEquals("t2k", System.getProperty("prism.text"));
        assertEquals("true", System.getProperty("java.awt.headless"));
    }

    @Test
    public void testStartWithNoUserManagerAccess() {
        // Test the catch block in start()
        interact(() -> {
            try {
                UserManagerAccessProvider.setInstance(null);
                app = new RepsAndRecordsUi();
                app.start(new Stage());
                
                UserManagerAccess holder = UserManagerAccessProvider.getInstance();
                assertNotNull(holder);
                assertTrue(holder instanceof DirectUserManagerAccess);
            } catch (Exception e) {
                fail("Exception should not be thrown: " + e.getMessage());
            }
        });
    }

    @Test
    public void testGetInitialUserManager() throws Exception {
        tempFile = Files.createTempFile("test_users", ".json");
        
        UserManagerAccessProvider.setInstance(new DirectUserManagerAccess());
        UserManagerAccess holder = UserManagerAccessProvider.getInstance();
        
        AppPersistence persistence = new AppPersistence();
        persistence.setTestFile(tempFile);
        holder.setAppPersistence(persistence);
        
        holder.setUserManager(new UserManager());
        
        interact(() -> {
            try {
                app = new RepsAndRecordsUi();
                app.start(new Stage());
                
                // Test that a new UserManager is created when loading fails
                UserManager userManager = holder.getUserManager();
                assertNotNull(userManager);
                assertTrue(userManager.getUsers().isEmpty(), "New UserManager should start with empty users list");
            } catch (Exception e) {
                fail("Exception should not be thrown: " + e.getMessage());
            }
        });
    }

    @AfterEach
    public void tearDown() throws IOException {
        if (tempFile != null) {
            Files.deleteIfExists(tempFile);
        }
        UserManagerAccessProvider.setInstance(null);
        System.clearProperty("headless");
    }
} 