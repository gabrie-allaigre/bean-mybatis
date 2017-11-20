package com.talanlabs.bean.mybatis.component.data;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TestTypeHandler extends BaseTypeHandler<String> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType) throws SQLException {

    }

    @Override
    public String getNullableResult(ResultSet rs, String columnName) throws SQLException {
        rs.getString(columnName);
        return "test";
    }

    @Override
    public String getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        rs.getString(columnIndex);
        return "test";
    }

    @Override
    public String getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        cs.getString(columnIndex);
        return "test";
    }
}
