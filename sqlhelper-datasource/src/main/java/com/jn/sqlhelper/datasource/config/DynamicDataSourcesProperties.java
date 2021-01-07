/*
 * Copyright 2020 the original author or authors.
 *
 * Licensed under the Apache, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at  http://www.gnu.org/licenses/lgpl-2.0.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jn.sqlhelper.datasource.config;

import com.jn.langx.invocation.aop.expression.AspectJExpressionPointcutAdvisorProperties;
import com.jn.langx.util.Emptys;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Function;

import java.util.List;
import java.util.Map;

/**
 * 动态数据源特性的所有配置
 *
 * @since 3.4.1
 */
public class DynamicDataSourcesProperties {
    /**
     * 是否启用动态数据源特性
     */
    private boolean enabled;
    /**
     * 所有的数据源配置
     */
    private List<DataSourceProperties> dataSources = Collects.emptyArrayList();
    /**
     * 默认的 slave 路由名称
     */
    private String defaultRouter;
    /**
     * 数据源组配置
     */
    private List<DataSourceGroupProperties> groups = Collects.emptyArrayList();
    /**
     * 整个工程中，能够决定 数据源 key的 方法的拦截器 expression。所有 配置了 @DataSource的地方，都要加上
     */
    private AspectJExpressionPointcutAdvisorProperties keyChoices = new AspectJExpressionPointcutAdvisorProperties();
    /**
     * 整个工程中，需要启用自动化事务管理的地方，都有包含在此拦截器里
     */
    private AspectJExpressionPointcutAdvisorProperties transaction = new AspectJExpressionPointcutAdvisorProperties();

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public List<DataSourceProperties> getDataSources() {
        return dataSources;
    }

    public void setDataSources(List<DataSourceProperties> dataSources) {
        this.dataSources = dataSources;
    }

    public String getDefaultRouter() {
        return defaultRouter;
    }

    public void setDefaultRouter(String defaultRouter) {
        this.defaultRouter = defaultRouter;
    }

    public void setGroups(List<DataSourceGroupProperties> groups) {
        this.groups = groups;
    }

    public List<DataSourceGroupProperties> getGroups() {
        return groups;
    }

    public Map<String, String> getGroupRouters() {
        return Collects.collect(groups, Collects.toHashMap(new Function<DataSourceGroupProperties, String>() {
            @Override
            public String apply(DataSourceGroupProperties dataSourceGroupProperties) {
                return dataSourceGroupProperties.getName();
            }
        }, new Function<DataSourceGroupProperties, String>() {
            @Override
            public String apply(DataSourceGroupProperties dataSourceGroupProperties) {
                return dataSourceGroupProperties.getRouter();
            }
        }, true));
    }

    public Map<String, String> getGroupWriterPatternMap() {
        return Collects.collect(groups, Collects.toHashMap(new Function<DataSourceGroupProperties, String>() {
            @Override
            public String apply(DataSourceGroupProperties dataSourceGroupProperties) {
                return dataSourceGroupProperties.getName();
            }
        }, new Function<DataSourceGroupProperties, String>() {
            @Override
            public String apply(DataSourceGroupProperties dataSourceGroupProperties) {
                return dataSourceGroupProperties.getWritePattern();
            }
        }, true));
    }

    public AspectJExpressionPointcutAdvisorProperties getKeyChoices() {
        return keyChoices;
    }

    public void setKeyChoices(AspectJExpressionPointcutAdvisorProperties keyChoices) {
        this.keyChoices = keyChoices;
    }

    public AspectJExpressionPointcutAdvisorProperties getTransaction() {
        return transaction;
    }

    public void setTransaction(AspectJExpressionPointcutAdvisorProperties transaction) {
        this.transaction = transaction;
    }

    public boolean isTransactionEnabled() {
        return this.transaction != null && Emptys.isNotEmpty(transaction.getExpression());
    }
}