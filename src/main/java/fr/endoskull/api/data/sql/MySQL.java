package fr.endoskull.api.data.sql;

import fr.endoskull.api.commons.account.AccountProvider;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

public class MySQL {
    private BasicDataSource connectionPool;
    private static MySQL instance;

    public MySQL(BasicDataSource connectionPool) {
        this.connectionPool = connectionPool;
        instance = this;
    }

    public Connection getConnection() throws SQLException {
        return connectionPool.getConnection();
    }

    public void createTables(){
        update("CREATE TABLE IF NOT EXISTS accounts (" +
                "`#` INT NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
                "uuid VARCHAR(255), " +
                "name VARCHAR(255), " +
                "level INT, " +
                "xp DOUBLE, " +
                "solde DOUBLE, " +
                "first_join BIGINT)");
        update("CREATE TABLE IF NOT EXISTS stats ( `#` INT NOT NULL AUTO_INCREMENT PRIMARY KEY , uuid VARCHAR(255) , `key` VARCHAR(4095) NOT NULL , `value` INT NOT NULL )");
        update("CREATE TABLE IF NOT EXISTS properties ( `#` INT NOT NULL AUTO_INCREMENT PRIMARY KEY , uuid VARCHAR(255) , `key` VARCHAR(4095) NOT NULL , `value` LONGTEXT NOT NULL )");
        update("CREATE TABLE IF NOT EXISTS friends ( `#` INT NOT NULL AUTO_INCREMENT PRIMARY KEY , uuid1 VARCHAR(255) , uuid2 VARCHAR(255) )");
        update("CREATE TABLE IF NOT EXISTS friend_settings ( `#` INT NOT NULL AUTO_INCREMENT PRIMARY KEY , uuid VARCHAR(255) , setting VARCHAR(255) , `value` VARCHAR(255) )");
        update("CREATE TABLE IF NOT EXISTS friend_requests ( `#` INT NOT NULL AUTO_INCREMENT PRIMARY KEY , sender VARCHAR(255) , receiver VARCHAR(255) , `expiry` BIGINT )");
        update("CREATE TABLE IF NOT EXISTS whitelist ( `#` INT NOT NULL AUTO_INCREMENT PRIMARY KEY , name VARCHAR(255) )");
        update("CREATE TABLE IF NOT EXISTS maintenance ( `#` INT NOT NULL AUTO_INCREMENT PRIMARY KEY , service VARCHAR(255) , `value` BOOLEAN )");
        update("CREATE TABLE IF NOT EXISTS online_count ( service VARCHAR(255) , `online` INT , `date` TIMESTAMP DEFAULT CURRENT_TIMESTAMP )");
        update("CREATE TABLE IF NOT EXISTS discord_count ( `members` INT , `date` TIMESTAMP DEFAULT CURRENT_TIMESTAMP )");
        update("CREATE TABLE IF NOT EXISTS account_count ( `accounts` INT , `date` TIMESTAMP DEFAULT CURRENT_TIMESTAMP )");
    }

    public void update(String qry){
        try (Connection c = getConnection();
             PreparedStatement s = c.prepareStatement(qry)) {
            s.executeUpdate();
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public Object query(String qry, Function<ResultSet, Object> consumer){
        try (Connection c = getConnection();
             PreparedStatement s = c.prepareStatement(qry);
             ResultSet rs = s.executeQuery()) {
            return consumer.apply(rs);
        } catch(SQLException e){
            throw new IllegalStateException(e.getMessage());
        }
    }

    public void query(String qry, Consumer<ResultSet> consumer){
        try (Connection c = getConnection();
             PreparedStatement s = c.prepareStatement(qry);
             ResultSet rs = s.executeQuery()) {
            consumer.accept(rs);
        } catch(SQLException e){
            throw new IllegalStateException(e.getMessage());
        }
    }

    //SELECT `uuid` FROM `accounts` WHERE 1
    public List<UUID> getAllUniqueId() {
        return (List<UUID>) query("SELECT `uuid` FROM " + AccountProvider.TABLE + " WHERE 1", rs -> {
            List<UUID> result = new ArrayList<>();
            try {
                while (rs.next()) {
                    result.add(UUID.fromString(rs.getString("uuid")));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return result;
        });
    }

    public static MySQL getInstance() {
        return instance;
    }
}
