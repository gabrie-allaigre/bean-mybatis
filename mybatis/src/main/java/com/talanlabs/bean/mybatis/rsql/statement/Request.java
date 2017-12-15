package com.talanlabs.bean.mybatis.rsql.statement;


import com.talanlabs.bean.mybatis.helper.SqlResult;
import com.talanlabs.bean.mybatis.session.context.SqlContext;

public class Request {

    private String rsql;
    private ICustomRequest customRequest;
    private String sort;
    private ICustomSort customSortLeft;
    private ICustomSort customSortRight;
    private Rows rows;

    public static Builder newBuilder() {
        return new Builder();
    }

    public String getRsql() {
        return rsql;
    }

    public ICustomRequest getCustomRequest() {
        return customRequest;
    }

    public String getSort() {
        return sort;
    }

    public ICustomSort getCustomSortLeft() {
        return customSortLeft;
    }

    public ICustomSort getCustomSortRight() {
        return customSortRight;
    }

    public Rows getRows() {
        return rows;
    }

    public interface ICustomRequest {

        SqlResult buildSqlResult(SqlContext context);

    }

    public interface ICustomSort {

        SqlResult buildSqlResult(SqlContext context);

    }

    public static class Rows {

        public final long offset;
        public final long limit;

        private Rows(long offset, long limit) {
            super();

            this.offset = offset;
            this.limit = limit;
        }

        public static Rows of(long offset, long limit) {
            return new Rows(offset, limit);
        }
    }

    public static class Builder {

        private Request request;

        private Builder() {
            super();

            this.request = new Request();
        }

        public Builder rsql(String rsql) {
            request.rsql = rsql;
            return this;
        }

        public Builder customRequest(ICustomRequest customRequest) {
            request.customRequest = customRequest;
            return this;
        }

        public Builder sort(String sort) {
            request.sort = sort;
            return this;
        }

        public Builder customSortLeft(ICustomSort customSortLeft) {
            request.customSortLeft = customSortLeft;
            return this;
        }

        public Builder customSortRight(ICustomSort customSortRight) {
            request.customSortRight = customSortRight;
            return this;
        }

        public Builder rows(long offset, long limit) {
            request.rows = Rows.of(offset, limit);
            return this;
        }

        public Request build() {
            return request;
        }
    }
}
