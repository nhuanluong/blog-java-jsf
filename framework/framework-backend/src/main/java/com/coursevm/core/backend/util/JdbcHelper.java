/*
 * Created on 2020.12.10 (y.M.d) 08:23
 *
 * Copyright(c) 2020 VietInfo Company, Inc.  All Rights Reserved.
 * This software is the proprietary information of VietInfo Company.
 *
 */
package com.coursevm.core.backend.util;

import com.google.common.collect.Lists;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.NumberUtils;
import org.springframework.web.context.WebApplicationContext;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author Nhuan Luong
 */
@Slf4j
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
public class JdbcHelper implements AutoCloseable {

    private Connection connection;

    @Autowired
    DataSourceContext dataSourceContext;

    @Override
    public void close() {
        JdbcUtils.closeConnection(connection);
    }

    @SneakyThrows
    public ParameterStatement statement(String sql) {
        return ParameterStatement.of(dataSourceContext.getDataSource().getConnection(), sql);
    }

    @SneakyThrows
    public ParameterStatement statement(DataSourceContext dataSourceContext, String sql) {
        return ParameterStatement.of(dataSourceContext.getDataSource().getConnection(), sql);
    }

    @SneakyThrows
    public static ParameterStatement of(Connection connection, String query) {
        return ParameterStatement.of(connection, query);
    }

    public int fetchInteger(ParameterStatement preparedStatement) {
        return fetchCount(preparedStatement, Integer.class).intValue();
    }

    public int fetchInteger(String sql) {
        return fetchInteger(statement(sql));
    }

    public long fetchLong(ParameterStatement preparedStatement) {
        return fetchCount(preparedStatement, Long.class).intValue();
    }

    public <T> List<T> fetch(ParameterStatement preparedStatement, Class<T> clazz) {
        return getList(preparedStatement, clazz);
    }

    public <T> List<T> fetch(String query, Class<T> clazz) {
        return getList(statement(query), clazz);
    }

    public <T> T fetchOne(ParameterStatement preparedStatement, Class<T> clazz) {
        return getSingle(preparedStatement, clazz);
    }

    public int update(ParameterStatement preparedStatement) {
        return exeUpdate(preparedStatement);
    }

    private int exeUpdate(ParameterStatement preparedStatement) {
        int rows = 0;
        try (preparedStatement) {
            connection = preparedStatement.getConnection();
            log.info("\nexecuteUpdate Query: {}", preparedStatement.getStatement());
            try {
                rows = preparedStatement.executeUpdate();
            } catch (SQLException e) {
                log.error("executeUpdate query err: {}", preparedStatement.getStatement());
                log.error("executeUpdate err: ", e);
            }
        }
        return rows;
    }

    private <T> List<T> getList(ParameterStatement preparedStatement, Class<T> clazz) {
        List<T> lstResult;
        try (preparedStatement) {
            connection = preparedStatement.getConnection();
            lstResult = executeQuery(preparedStatement, clazz);
        }
        return lstResult;
    }

    private <T> T getSingle(ParameterStatement preparedStatement, Class<T> clazz) {
        T result = null;
        try (preparedStatement) {
            connection = preparedStatement.getConnection();
            List<T> listT = executeQuery(preparedStatement, clazz);
            if (!listT.isEmpty()) {
                if (listT.size() > 1) {
                    log.warn("getSingle but the query returns more than one({})", listT.size());
                }
                result = listT.get(0);
            }
        }
        return result;
    }

    private <T> List<T> executeQuery(ParameterStatement preparedStatement, Class<T> clazz) {
        try {
            return convertPojoList(preparedStatement, clazz);

        } catch (SQLException e) {
            log.error("\nexecuteQuery error: {}", preparedStatement.getStatement());
            log.error("\nexecuteQuery error:", e);
        }
        return Lists.newArrayList();
    }

    private <T> List<T> convertPojoList(ParameterStatement preparedStatement, Class<T> clazz) throws SQLException {
        List<T> listT = new ArrayList<>();
        try (ResultSet rs = preparedStatement.executeQuery()) {
            log.info("preparedStatement: {}", preparedStatement.getStatement());
            while (rs.next()) {
                try {
                    listT.add(callSetters(clazz.getDeclaredConstructor().newInstance(), rs));
                } catch (Exception e) {
                    log.error("convertPojoList error:", e);
                }
            }
        }
        return listT;
    }

    private <T> T callSetters(T ob, ResultSet rs) {
        List<Field> fields = getFields(ob);
        List<Method> methods = getMethods(ob);
        fields.stream()
                .<Consumer<? super Method>>map(field -> method -> setFieldValue(ob, rs, field, method))
                .forEach(methods::forEach);
        return ob;
    }

    private <T> void setFieldValue(T ob, ResultSet rs, Field field, Method method) {
        String methodName = method.getName();

        if ((methodName.startsWith("set")) && (methodName.length() == (field.getName().length() + 3))) {
            String fieldName = field.getName().toLowerCase();

            if (methodName.toLowerCase().endsWith(fieldName)) {
                try {
                    method.setAccessible(true);

                    Class<?> fieldType = field.getType();

                    if (String.class == fieldType) {
                        method.invoke(ob, rs.getString(fieldName));
                    } else if (boolean.class == fieldType || Boolean.class == fieldType) {
                        method.invoke(ob, rs.getBoolean(fieldName));
                    } else if (byte.class == fieldType || Byte.class == fieldType) {
                        method.invoke(ob, rs.getByte(fieldName));
                    } else if (short.class == fieldType || Short.class == fieldType) {
                        method.invoke(ob, rs.getShort(fieldName));
                    } else if (int.class == fieldType || Integer.class == fieldType) {
                        method.invoke(ob, rs.getInt(fieldName));
                    } else if (long.class == fieldType || Long.class == fieldType) {
                        method.invoke(ob, rs.getLong(fieldName));
                    } else if (float.class == fieldType || Float.class == fieldType) {
                        method.invoke(ob, rs.getFloat(fieldName));
                    } else if (double.class == fieldType || Double.class == fieldType || Number.class == fieldType) {
                        method.invoke(ob, rs.getDouble(fieldName));
                    } else if (BigDecimal.class == fieldType) {
                        method.invoke(ob, rs.getBigDecimal(fieldName));
                    } else if (Date.class == fieldType) {
                        method.invoke(ob, rs.getDate(fieldName));
                    } else if (Time.class == fieldType) {
                        method.invoke(ob, rs.getTime(fieldName));
                    } else if (Timestamp.class == fieldType || java.util.Date.class == fieldType) {
                        method.invoke(ob, rs.getTimestamp(fieldName));
                    } else if (byte[].class == fieldType) {
                        method.invoke(ob, rs.getByte(fieldName));
                    } else if (Blob.class == fieldType) {
                        method.invoke(ob, rs.getBlob(fieldName));
                    } else if (Clob.class == fieldType) {
                        method.invoke(ob, rs.getClob(fieldName));
                    } else if (fieldType.isEnum()) {
                        Object obj = rs.getObject(fieldName);
                        if (obj instanceof String) {
                            method.invoke(ob, rs.getString(fieldName));
                        } else if (obj instanceof Number) {
                            NumberUtils.convertNumberToTargetClass((Number) obj, Integer.class);
                            method.invoke(ob, rs.getString(fieldName));
                        } else {
                            rs.getString(fieldName);
                        }

                    } else {
                        try {
                            method.invoke(ob, rs.getObject(fieldName));
                        } catch (AbstractMethodError err) {
                            log.debug("JDBC driver does not implement JDBC 4.1 'getObject(int, Class)' method", err);
                        } catch (SQLFeatureNotSupportedException ex) {
                            log.debug("JDBC driver does not support JDBC 4.1 'getObject(int, Class)' method", ex);
                        } catch (SQLException ex) {
                            log.debug("JDBC driver has limited support for JDBC 4.1 'getObject(int, Class)' method", ex);
                        }

                        String typeName = fieldType.getSimpleName();
                        switch (typeName) {
                            case "LocalDate":
                                method.invoke(ob, rs.getDate(fieldName));
                                break;
                            case "LocalTime":
                                method.invoke(ob, rs.getTime(fieldName));
                                break;
                            case "LocalDateTime":
                                method.invoke(ob, rs.getTimestamp(fieldName));
                                break;
                        }
                    }
                } catch (Exception e) {
                    //log.warn("setFieldValue warn {}", e.getMessage());
                }
            }
        }
    }

    private <T> List<Field> getFields(T t) {
        List<Field> fields = new ArrayList<>();
        Class<?> clazz = t.getClass();
        while (clazz != Object.class) {
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
            clazz = clazz.getSuperclass();
        }
        return fields;
    }

    private <T> List<Method> getMethods(T t) {
        List<Method> methods = new ArrayList<>();
        Class<?> clazz = t.getClass();
        while (clazz != Object.class) {
            methods.addAll(Arrays.asList(clazz.getDeclaredMethods()));
            clazz = clazz.getSuperclass();
        }
        return methods;
    }

    private <T> Number fetchCount(ParameterStatement preparedStatement, Class<T> clazz) {
        if (clazz == Integer.class) {
            return getInt(preparedStatement);
        }
        return getLong(preparedStatement);
    }

    private Integer getInt(ParameterStatement preparedStatement) {
        int aInt = 0;
        try (preparedStatement) {
            connection = preparedStatement.getConnection();
            try (ResultSet rs = preparedStatement.executeQuery()) {
                log.debug("preparedStatement int: {}", preparedStatement.getStatement());
                while (rs.next()) {
                    aInt = rs.getInt(1);
                }
                log.debug("getInt: {}", aInt);
            } catch (SQLException ex) {
                log.error("getInt Query error: {}", preparedStatement.getStatement());
                log.error("getInt err:", ex);
            }
        }
        return aInt;
    }

    private Long getLong(ParameterStatement preparedStatement) {
        long aLong = 0L;
        try (preparedStatement) {
            connection = preparedStatement.getConnection();
            try (ResultSet rs = preparedStatement.executeQuery()) {
                log.debug("preparedStatement long: {}", preparedStatement.getStatement());
                while (rs.next()) {
                    aLong = rs.getLong(1);
                }
                log.debug("getLong: {}", aLong);
            } catch (SQLException ex) {
                log.error("getLong Query error: {}", preparedStatement.getStatement());
                log.error("getLong err:", ex);
            }
        }
        return aLong;
    }
}
