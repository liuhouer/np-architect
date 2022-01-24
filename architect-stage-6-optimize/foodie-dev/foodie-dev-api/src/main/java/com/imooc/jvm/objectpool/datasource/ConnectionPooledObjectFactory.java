package com.imooc.jvm.objectpool.datasource;

import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import java.sql.*;

public class ConnectionPooledObjectFactory
  implements PooledObjectFactory<MyConnection> {
  private ObjectPool<MyConnection> objectPool;

  public ObjectPool<MyConnection> getObjectPool() {
    return objectPool;
  }

  public void setObjectPool(ObjectPool<MyConnection> objectPool) {
    this.objectPool = objectPool;
  }

  @Override
  public PooledObject<MyConnection> makeObject() throws Exception {
    Class.forName("com.mysql.jdbc.Driver");
    Connection connection = DriverManager.getConnection(
      "jdbc:mysql://localhost:3306/foodie-shop-dev?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true",
      "root",
      "root123"
    );
    MyConnection myConnection = new MyConnection();
    myConnection.setConnection(connection);
    myConnection.setObjectPool(objectPool);
    return new DefaultPooledObject<>(myConnection);
  }

  @Override
  public void destroyObject(PooledObject<MyConnection> p) throws Exception {
    p.getObject().close();
  }

  @Override
  public boolean validateObject(PooledObject<MyConnection> p) {
    Connection connection = p.getObject();
    try {
      PreparedStatement statement = connection.prepareStatement("SELECT 1");
      ResultSet resultSet = statement.executeQuery();
      int i = resultSet.getInt(1);
      return i == 1;
    } catch (SQLException e) {
      return false;
    }
  }

  @Override
  public void activateObject(PooledObject<MyConnection> p) throws Exception {
    // 可以把connection额外的配置放到这里
  }

  @Override
  public void passivateObject(PooledObject<MyConnection> p) throws Exception {
    // 钝化
    MyConnection myConnection = p.getObject();
    Statement statement = myConnection.getStatement();
    if (statement != null) {
      statement.close();
    }
  }
}
