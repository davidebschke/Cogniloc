package kitool.backend.service.chatService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.DriverManager;
import java.sql.SQLException;

public class OllamaChatService {

    static final String chatDbUrl = "JDBC:sqlite:froggyChat.db";
    private static final Logger log = LoggerFactory.getLogger(OllamaChatService.class);


    public void initChatDB() throws SQLException {

        try{
            DriverManager.getConnection(chatDbUrl);
            log.info("Chat DB connection successful");
        } catch (SQLException e) {
            log.error("Cannot connect to database. Failure: {}", e.getMessage());
        }
    }
}
