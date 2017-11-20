package com.talanlabs.bean.mybatis.it;

import com.talanlabs.bean.mybatis.cache.BeanCacheFactory;
import com.talanlabs.bean.mybatis.handler.IdTypeHandler;
import com.talanlabs.bean.mybatis.it.config.DefaultNlsColumnHandler;
import com.talanlabs.bean.mybatis.it.config.DefaultUserByHandler;
import com.talanlabs.bean.mybatis.it.mapper.NlsMapper;
import com.talanlabs.bean.mybatis.it.observer.TracableTriggerObserver;
import com.talanlabs.bean.mybatis.resultmap.BeanResultMapFactory;
import com.talanlabs.bean.mybatis.rsql.configuration.RsqlConfigurationBuilder;
import com.talanlabs.bean.mybatis.rsql.database.HSQLDBHandler;
import com.talanlabs.bean.mybatis.rsql.engine.policy.NothingStringPolicy;
import com.talanlabs.bean.mybatis.rsql.statement.CountRsqlMappedStatementFactory;
import com.talanlabs.bean.mybatis.rsql.statement.RsqlMappedStatementFactory;
import com.talanlabs.bean.mybatis.session.BeanConfiguration;
import com.talanlabs.bean.mybatis.session.BeanSqlSessionManager;
import com.talanlabs.bean.mybatis.statement.DeleteBeansByMappedStatementFactory;
import com.talanlabs.bean.mybatis.statement.DeleteEntityByIdMappedStatementFactory;
import com.talanlabs.bean.mybatis.statement.DeleteMappedStatementFactory;
import com.talanlabs.bean.mybatis.statement.FindBeansByJoinTableMappedStatementFactory;
import com.talanlabs.bean.mybatis.statement.FindBeansByMappedStatementFactory;
import com.talanlabs.bean.mybatis.statement.FindEntityByIdMappedStatementFactory;
import com.talanlabs.bean.mybatis.statement.FindNlsColumnMappedStatementFactory;
import com.talanlabs.bean.mybatis.statement.InsertMappedStatementFactory;
import com.talanlabs.bean.mybatis.statement.UpdateMappedStatementFactory;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.session.SqlSessionManager;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import java.io.IOException;

public abstract class AbstractHSQLIntegration {

    private static BeanConfiguration beanConfiguration;
    private static SqlSessionManager sqlSessionManager;
    private static BeanSqlSessionManager beanSqlSessionManager;
    private static DefaultNlsColumnHandler defaultNlsColumnHandler;

    public static BeanConfiguration getBeanConfiguration() {
        return beanConfiguration;
    }

    public static SqlSessionManager getSqlSessionManager() {
        return sqlSessionManager;
    }

    public static BeanSqlSessionManager getBeanSqlSessionManager() {
        return beanSqlSessionManager;
    }

    public static DefaultNlsColumnHandler getDefaultNlsColumnHandler() {
        return defaultNlsColumnHandler;
    }

    @BeforeClass
    public static void beforeClass() {
        Environment environment = new Environment.Builder("test").dataSource(new PooledDataSource(null, "org.hsqldb.jdbcDriver", "jdbc:hsqldb:mem:mybatis-guice_TEST", "sa", ""))
                .transactionFactory(new JdbcTransactionFactory()).build();

        beanConfiguration = new BeanConfiguration(environment);
        beanConfiguration.setLazyLoadingEnabled(true);
        beanConfiguration.setAggressiveLazyLoading(false);
        defaultNlsColumnHandler = new DefaultNlsColumnHandler(beanConfiguration);
        beanConfiguration.setNlsColumnHandler(defaultNlsColumnHandler);

        beanConfiguration.setRsqlConfiguration(
                RsqlConfigurationBuilder.newBuilder(beanConfiguration).stringPolicy(new NothingStringPolicy()).pageStatementFactory(new HSQLDBHandler(beanConfiguration)).build());

        beanConfiguration.getMappedStatementFactoryRegistry().registry(new FindEntityByIdMappedStatementFactory(beanConfiguration));
        beanConfiguration.getMappedStatementFactoryRegistry().registry(new FindBeansByMappedStatementFactory(beanConfiguration));
        beanConfiguration.getMappedStatementFactoryRegistry().registry(new FindBeansByJoinTableMappedStatementFactory(beanConfiguration));
        beanConfiguration.getMappedStatementFactoryRegistry().registry(new FindNlsColumnMappedStatementFactory(beanConfiguration));
        beanConfiguration.getMappedStatementFactoryRegistry().registry(new InsertMappedStatementFactory(beanConfiguration));
        beanConfiguration.getMappedStatementFactoryRegistry().registry(new UpdateMappedStatementFactory(beanConfiguration));
        beanConfiguration.getMappedStatementFactoryRegistry().registry(new DeleteMappedStatementFactory(beanConfiguration));
        beanConfiguration.getMappedStatementFactoryRegistry().registry(new DeleteEntityByIdMappedStatementFactory(beanConfiguration));
        beanConfiguration.getMappedStatementFactoryRegistry().registry(new DeleteBeansByMappedStatementFactory(beanConfiguration));
        beanConfiguration.getMappedStatementFactoryRegistry().registry(new RsqlMappedStatementFactory(beanConfiguration));
        beanConfiguration.getMappedStatementFactoryRegistry().registry(new CountRsqlMappedStatementFactory(beanConfiguration));

        beanConfiguration.getResultMapFactoryRegistry().registry(new BeanResultMapFactory(beanConfiguration));

        beanConfiguration.getCacheFactoryRegistry().registry(new BeanCacheFactory(beanConfiguration));

        beanConfiguration.getTriggerDispatcher().addTriggerObserver(new TracableTriggerObserver(new DefaultUserByHandler()));

        beanConfiguration.getTypeHandlerRegistry().register(IdTypeHandler.class);

        beanConfiguration.addMapper(NlsMapper.class);

        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(beanConfiguration);
        sqlSessionManager = SqlSessionManager.newInstance(sqlSessionFactory);

        beanSqlSessionManager = BeanSqlSessionManager.newInstance(sqlSessionManager);
    }

    @AfterClass
    public static void afterClass() {
        sqlSessionManager = null;
        beanConfiguration = null;
    }

    @Before
    public void startSqlSession() {
        sqlSessionManager.startManagedSession();

        ScriptRunner scriptRunner = new ScriptRunner(sqlSessionManager.getConnection());
        scriptRunner.setLogWriter(null);
        try {
            scriptRunner.runScript(Resources.getResourceAsReader("init-script.sql"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @After
    public void closeSqlSession() {
        sqlSessionManager.close();
    }

}
