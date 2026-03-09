package kitool.backend.service.chatService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class OllamaChatService {


    private static final Logger log = LoggerFactory.getLogger(OllamaChatService.class);



    public String createChatDbUrl(String dbPath){

        return "JDBC:sqlite:"+dbPath;
    }

    public void initChatDB(String chatDbUrl) throws SQLException {

        String sqlChatDB="""
            CREATE TABLE IF NOT EXISTS chats (
                id          INTEGER PRIMARY KEY AUTOINCREMENT,
                title   TEXT    NOT NULL,
                content     TEXT    Not Null,
                lastusing   DATETIME DEFAULT CURRENT_TIMESTAMP
            )
            """;

        String sqlMessagesDB="""
            CREATE TABLE IF NOT EXISTS chatMessages (
                id          INTEGER PRIMARY KEY AUTOINCREMENT,
                chat_id     INTEGER NOT NULL,
                content     TEXT    NOT NULL,
                timestamp   DATETIME DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (chat_id) REFERENCES chats(id)
            )
            """;

            Connection connChatDB=DriverManager.getConnection(chatDbUrl);
            Statement creStm=connChatDB.createStatement();
        try {
            connChatDB.setAutoCommit(true);
            creStm.execute(sqlChatDB);
            creStm.execute(sqlMessagesDB);
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw e;
        }


        try (Connection conn = DriverManager.getConnection(chatDbUrl);
             ResultSet rs = conn.getMetaData().getTables(null, null, "%", null)) {
            while (rs.next()) {
                log.info("Table found: {}", rs.getString("TABLE_NAME"));
            }
        } catch (SQLException e) {
            log.error("Failure: {}", e.getMessage());
        }

    }
}


