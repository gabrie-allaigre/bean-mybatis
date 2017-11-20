package com.talanlabs.bean.mybatis.rsql.configuration;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.talanlabs.bean.mybatis.rsql.engine.ILikePolicy;
import com.talanlabs.bean.mybatis.rsql.engine.IStringPolicy;
import com.talanlabs.bean.mybatis.rsql.engine.orderby.BeanSortVisitor;
import com.talanlabs.bean.mybatis.rsql.engine.orderby.registry.DefaultSortDirectionManagerRegistry;
import com.talanlabs.bean.mybatis.rsql.engine.orderby.registry.ISortDirectionManagerRegistry;
import com.talanlabs.bean.mybatis.rsql.engine.policy.DefaultLikePolicy;
import com.talanlabs.bean.mybatis.rsql.engine.policy.NothingStringPolicy;
import com.talanlabs.bean.mybatis.rsql.engine.where.BeanRsqlVisitor;
import com.talanlabs.bean.mybatis.rsql.engine.where.registry.DefaultComparisonOperatorManagerRegistry;
import com.talanlabs.bean.mybatis.rsql.engine.where.registry.IComparisonOperatorManagerRegistry;
import com.talanlabs.bean.mybatis.rsql.sort.SortParser;
import com.talanlabs.bean.mybatis.rsql.statement.IPageStatementFactory;
import com.talanlabs.bean.mybatis.session.BeanConfiguration;
import com.talanlabs.rtext.Rtext;
import com.talanlabs.rtext.configuration.RtextConfigurationBuilder;
import cz.jirutka.rsql.parser.RSQLParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;

public class RsqlConfigurationBuilder {

    private final BeanConfiguration beanConfiguration;

    private RsqlConfigurationImpl rsqlConfiguration;

    private RsqlConfigurationBuilder(BeanConfiguration beanConfiguration) {
        super();

        this.beanConfiguration = beanConfiguration;

        this.rsqlConfiguration = new RsqlConfigurationImpl();
    }

    public static RsqlConfigurationBuilder newBuilder(BeanConfiguration beanConfiguration) {
        return new RsqlConfigurationBuilder(beanConfiguration);
    }

    /**
     * Rtext
     *
     * @param rtext Rtext
     */
    public RsqlConfigurationBuilder rtext(Rtext rtext) {
        this.rsqlConfiguration.rtext = rtext;
        return this;
    }

    /**
     * Rsql parser
     *
     * @param rsqlParser a parser
     */
    public RsqlConfigurationBuilder rsqlParser(RSQLParser rsqlParser) {
        this.rsqlConfiguration.rsqlParser = rsqlParser;
        return this;
    }

    /**
     * Sort parser
     *
     * @param sortParser a parser
     */
    public RsqlConfigurationBuilder sortParser(SortParser sortParser) {
        this.rsqlConfiguration.sortParser = sortParser;
        return this;
    }

    /**
     * A comparison operator manager registry
     *
     * @param comparisonOperatorManagerRegistry a comparison operator registry
     */
    public RsqlConfigurationBuilder comparisonOperatorManagerRegistry(IComparisonOperatorManagerRegistry comparisonOperatorManagerRegistry) {
        this.rsqlConfiguration.comparisonOperatorManagerRegistry = comparisonOperatorManagerRegistry;
        return this;
    }

    /**
     * A sort direction manager registry
     *
     * @param sortDirectionManagerRegistry a sort direction registry
     */
    public RsqlConfigurationBuilder sortDirectionManagerRegistry(ISortDirectionManagerRegistry sortDirectionManagerRegistry) {
        this.rsqlConfiguration.sortDirectionManagerRegistry = sortDirectionManagerRegistry;
        return this;
    }

    /**
     * @param stringPolicy a string policy
     */
    public RsqlConfigurationBuilder stringPolicy(IStringPolicy stringPolicy) {
        this.rsqlConfiguration.stringPolicy = stringPolicy;
        return this;
    }

    /**
     * @param likePolicy a like policy (default %)
     */
    public RsqlConfigurationBuilder likePolicy(ILikePolicy likePolicy) {
        this.rsqlConfiguration.likePolicy = likePolicy;
        return this;
    }

    public RsqlConfigurationBuilder pageStatementFactory(IPageStatementFactory pageStatementFactory) {
        this.rsqlConfiguration.pageStatementFactory = pageStatementFactory;
        return this;
    }

    public IRsqlConfiguration build() {
        if (rsqlConfiguration.comparisonOperatorManagerRegistry == null) {
            rsqlConfiguration.comparisonOperatorManagerRegistry = new DefaultComparisonOperatorManagerRegistry(beanConfiguration);
        }
        if (rsqlConfiguration.sortDirectionManagerRegistry == null) {
            rsqlConfiguration.sortDirectionManagerRegistry = new DefaultSortDirectionManagerRegistry(beanConfiguration);
        }
        return rsqlConfiguration;
    }

    private static class RsqlConfigurationImpl implements IRsqlConfiguration {

        private static final Logger LOG = LoggerFactory.getLogger(RsqlConfigurationImpl.class);

        private Rtext rtext;
        private RSQLParser rsqlParser;
        private SortParser sortParser;
        private IComparisonOperatorManagerRegistry comparisonOperatorManagerRegistry;
        private ISortDirectionManagerRegistry sortDirectionManagerRegistry;
        private IStringPolicy stringPolicy;
        private ILikePolicy likePolicy;
        private IPageStatementFactory pageStatementFactory;

        private Cache<Class<?>, BeanRsqlVisitor<?>> rsqlCache = CacheBuilder.newBuilder().build();
        private Cache<Class<?>, BeanSortVisitor<?>> sortCache = CacheBuilder.newBuilder().build();

        private RsqlConfigurationImpl() {
            super();

            this.rtext = new Rtext(RtextConfigurationBuilder.newBuilder().build());
            this.rsqlParser = new RSQLParser();
            this.sortParser = new SortParser();
            this.stringPolicy = new NothingStringPolicy();
            this.likePolicy = new DefaultLikePolicy();
        }

        @Override
        public Rtext getRtext() {
            return rtext;
        }

        @Override
        public <E> BeanRsqlVisitor<E> getBeanRsqlVisitor(Class<E> beanClass) {
            try {
                //noinspection unchecked
                return (BeanRsqlVisitor<E>) rsqlCache.get(beanClass, () -> new BeanRsqlVisitor<>(beanClass, comparisonOperatorManagerRegistry));
            } catch (ExecutionException e) {
                LOG.error("Failed to get visitor", e);
            }
            return null;
        }

        @Override
        public <E> BeanSortVisitor<E> getBeanSortVisitor(Class<E> beanClass) {
            try {
                //noinspection unchecked
                return (BeanSortVisitor<E>) sortCache.get(beanClass, () -> new BeanSortVisitor<>(beanClass, sortDirectionManagerRegistry));
            } catch (ExecutionException e) {
                LOG.error("Failed to get visitor", e);
            }
            return null;
        }

        @Override
        public RSQLParser getRsqlParser() {
            return rsqlParser;
        }

        @Override
        public SortParser getSortParser() {
            return sortParser;
        }

        @Override
        public IStringPolicy getStringPolicy() {
            return stringPolicy;
        }

        @Override
        public ILikePolicy getLikePolicy() {
            return likePolicy;
        }

        @Override
        public IPageStatementFactory getPageStatementFactory() {
            return pageStatementFactory;
        }
    }
}
