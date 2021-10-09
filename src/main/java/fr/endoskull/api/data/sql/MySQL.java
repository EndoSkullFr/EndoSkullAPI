package fr.endoskull.api.data.sql;

import fr.endoskull.api.commons.AccountProvider;
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

    public MySQL(BasicDataSource connectionPool) {
        this.connectionPool = connectionPool;
    }

    public Connection getConnection() throws SQLException {
        return connectionPool.getConnection();
    }

    public void createTables(){
        /*update("CREATE TABLE IF NOT EXISTS levels (" +
                "`#` INT NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
                "uuid VARCHAR(255), " +
                "level INT, " +
                "xp DOUBLE)");
        update("CREATE TABLE IF NOT EXISTS permissions (" +
                "`#` INT NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
                "grade VARCHAR(255), " +
                "permissions VARCHAR(2047))");
        update("CREATE TABLE IF NOT EXISTS player_key (" +
                "`#` INT NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
                "uuid VARCHAR(255), " +
                "ultime INT, " +
                "vote INT, " +
                "coins INT, " +
                "kit INT)");
        update("CREATE TABLE IF NOT EXISTS location (" +
                "`#` INT NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
                "name VARCHAR(255), " +
                "location VARCHAR(1023))");
        update("CREATE TABLE IF NOT EXISTS accounts (" +
                "`#` INT NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
                "uuid VARCHAR(255), " +
                "money DOUBLE)");*/
        update("CREATE TABLE IF NOT EXISTS accounts (" +
                "`#` INT NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
                "uuid VARCHAR(255), " +
                "name VARCHAR(255), " +
                "voteKey INT, " +
                "ultimeKey INT, " +
                "coinsKey INT, " +
                "kitKey INT, " +
                "level INT, " +
                "xp DOUBLE, " +
                "booster DOUBLE, " +
                "solde DOUBLE, " +
                "kits VARCHAR(4095), " +
                "kit_selected VARCHAR(255))");
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
}
