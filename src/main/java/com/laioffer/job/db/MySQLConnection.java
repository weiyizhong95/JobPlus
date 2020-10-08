package com.laioffer.job.db;

import com.laioffer.job.entity.Item;

import java.sql.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class MySQLConnection {
    private Connection conn;

    public MySQLConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection(MySQLDBUtil.URL);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() { // close connection API
        if (conn != null) {
            try {
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setFavoriteItems(String userId, Item item) {
        if (conn == null) {
            System.err.println("DB connection failed");
            return;
        }

        saveItem(item);// 点赞时先存起来, 才能将insert into history, 因为user_id, item_id是foreign key
        String sql = "INSERT IGNORE INTO history (user_id, item_id) VALUES (?, ?)";
        try {
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, userId);
            statement.setString(2, item.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void unsetFavoriteItems(String userId, String itemId) { // unlike an item
        if (conn == null) {
            System.err.println("DB connection failed");
            return;
        }
        String sql = "DELETE FROM history WHERE user_id = ? AND item_id = ?";
        try {
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, userId);
            statement.setString(2, itemId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveItem(Item item) {
        if (conn == null) {
            System.err.println("DB connection failed");
            return;
        }
        String sql = "INSERT IGNORE INTO items VALUES (?,?,?,?,?)"; // 默认全部columns
        try {
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, item.getId());
            statement.setString(2, item.getTitle());
            statement.setString(3, item.getLocation());
            statement.setString(4, item.getCompanyLogo());
            statement.setString(5, item.getUrl());
            statement.executeUpdate();

            sql = "INSERT IGNORE INTO keywords VALUES (?, ?)"; // itemId, keywordId
            statement = conn.prepareStatement(sql);
            statement.setString(1, item.getId());
            for (String keyword : item.getKeywords()) { // one item has at most 3 keywords
                statement.setString(2, keyword);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public Set<String> getFavoriteItemIds(String userId) {
        if (conn == null) {
            System.err.println("DB connection failed");
            return new HashSet<>();
        }
        Set<String> favoriteItems = new HashSet<>();

        try {
            String sql = "SELECT item_id FROM history WHERE user_id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, userId);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                String itemId = rs.getString("item_id");
                favoriteItems.add(itemId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return favoriteItems;
    }

    public Set<Item> getFavoriteItems(String userId) {
        if (conn == null) {
            System.err.println("DB connection failed");
            return Collections.emptySet(); // good practice, immutable 不能往里面添加
        }
        Set<Item> favoriteItems = new HashSet<>();
        Set<String> favoriteItemIds = getFavoriteItemIds(userId);

        String sql = "SELECT * FROM items WHERE item_id = ?";
        try {
            PreparedStatement statement = conn.prepareStatement(sql);
            for (String itemId : favoriteItemIds) {
                statement.setString(1, itemId);
                ResultSet rs = statement.executeQuery();
                if (rs.next()) {
                    favoriteItems.add(new Item.Builder()
                            .id(rs.getString("item_id"))
                            .title(rs.getString("name"))
                            .location(rs.getString("address"))
                            .companyLogo(rs.getString("image_url"))
                            .url(rs.getString("url"))
                            .keywords(getKeywords(itemId))
                            .favorite(true)
                            .build());

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return favoriteItems;
    }

    public Set<String> getKeywords(String itemId) {
        if (conn == null) {
            System.err.println("DB connection failed");
            return Collections.emptySet();
        }
        Set<String> keywords = new HashSet<>();
        String sql = "SELECT keyword from keywords WHERE item_id = ? ";
        try {
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, itemId);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                String keyword = rs.getString("keyword");
                keywords.add(keyword);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return keywords;
    }

    public String getFullname(String userId) {
        if (conn == null) {
            System.err.println("DB connection failed");
            return "";
        }
        String name = "";
        String sql = "SELECT first_name, last_name FROM users WHERE user_id = ?";
        try {
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, userId);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                name = rs.getString("first_name") + " " + rs.getString("last_name");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return name;
    }


    public boolean verifyLogin(String userId, String password) {
        if (conn == null) {
            System.err.println("DB connection failed");
            return false;
        }
        String sql = "SELECT user_id FROM users WHERE user_id = ? AND password = ?";
        try {
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, userId);
            statement.setString(2, password);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }


    public boolean addUser(String userId, String password, String firstname, String lastname) {
        if (conn == null) {
            System.err.println("DB connection failed");
            return false;
        }

        String sql = "INSERT IGNORE INTO users VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, userId);
            statement.setString(2, password);
            statement.setString(3, firstname);
            statement.setString(4, lastname);


            return statement.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}











