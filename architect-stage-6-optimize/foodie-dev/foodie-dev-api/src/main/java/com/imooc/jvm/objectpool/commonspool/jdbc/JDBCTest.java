package com.imooc.jvm.objectpool.commonspool.jdbc;

import java.sql.*;

public class JDBCTest {
  public static void main(String[] args) throws ClassNotFoundException, SQLException {
    Class.forName("com.mysql.jdbc.Driver");
    Connection connection = DriverManager.getConnection(
      "jdbc:mysql://localhost:3306/foodie-shop-dev?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true",
      "root",
      "root123"
    );

    PreparedStatement preparedStatement = connection.prepareStatement(
      "select * from `foodie-shop-dev`.orders;"
    );
    ResultSet resultSet = preparedStatement.executeQuery();
    while (resultSet.next()) {
      System.out.println(resultSet.getString("id"));
      System.out.println(resultSet.getString("user_id"));
    }
    resultSet.close();
    preparedStatement.close();


    PreparedStatement preparedStatement2 = connection.prepareStatement(
      "select * from `foodie-shop-dev`.items;"
    );
    ResultSet resultSet2 = preparedStatement2.executeQuery();
    while (resultSet2.next()) {
      System.out.println(resultSet2.getString(1));
      System.out.println(resultSet2.getString(2));
    }
    resultSet2.close();
    preparedStatement2.close();

    connection.close();

  }
}
