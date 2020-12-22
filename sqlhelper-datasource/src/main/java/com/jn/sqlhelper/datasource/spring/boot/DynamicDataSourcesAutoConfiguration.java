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

package com.jn.sqlhelper.datasource.spring.boot;

import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.function.Function;
import com.jn.sqlhelper.datasource.DataSourceRegistry;
import com.jn.sqlhelper.datasource.NamedDataSource;
import com.jn.sqlhelper.datasource.definition.DataSourceProperties;
import com.jn.sqlhelper.datasource.definition.NamedDataSourcesProperties;
import com.jn.sqlhelper.datasource.factory.CentralizedDataSourceFactory;
import com.jn.sqlhelper.datasource.key.DataSourceKeyRegistry;
import com.jn.sqlhelper.datasource.key.DataSourceKeySelector;
import com.jn.sqlhelper.datasource.key.filter.DataSourceKeyFilter;
import com.jn.sqlhelper.datasource.key.parser.DataSourceAnnotationParser;
import com.jn.sqlhelper.datasource.key.parser.DataSourceKeyAnnotationParser;
import com.jn.sqlhelper.datasource.key.parser.DataSourceKeyDataSourceParser;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.ListFactoryBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class DynamicDataSourcesAutoConfiguration {

    @Bean
    public DataSourceRegistry dataSourceRegistry(ObjectProvider<DataSourceKeyDataSourceParser> dataSourceKeyParserProvider) {
        DataSourceRegistry dataSourceRegistry = new DataSourceRegistry();
        DataSourceKeyDataSourceParser dataSourceKeyParser = dataSourceKeyParserProvider.getIfAvailable();
        dataSourceRegistry.setKeyParser(dataSourceKeyParser);
        return dataSourceRegistry;
    }

    @Bean
    public CentralizedDataSourceFactory centralizedDataSourceFactory(DataSourceRegistry dataSourceRegistry) {
        CentralizedDataSourceFactory factory = new CentralizedDataSourceFactory();
        factory.setRegistry(dataSourceRegistry);
        return factory;
    }

    @Bean
    @ConfigurationProperties(prefix = "sqlhelper.namedDataSources")
    public NamedDataSourcesProperties namedDataSourcesProperties() {
        return new NamedDataSourcesProperties();
    }

    @Bean(name = "dataSourcesFactoryBean")
    public ListFactoryBean dataSourcesFactoryBean(final CentralizedDataSourceFactory centralizedDataSourceFactory,
                                                  NamedDataSourcesProperties namedDataSourcesProperties) {
        List<DataSourceProperties> dataSourcePropertiesList = namedDataSourcesProperties.getDataSources();
        List<NamedDataSource> dataSources = Pipeline.of(dataSourcePropertiesList).map(new Function<DataSourceProperties, NamedDataSource>() {
            @Override
            public NamedDataSource apply(DataSourceProperties dataSourceProperties) {
                return centralizedDataSourceFactory.get(dataSourceProperties);
            }
        }).clearNulls().asList();
        ListFactoryBean dataSourcesFactoryBean = new ListFactoryBean();
        dataSourcesFactoryBean.setTargetListClass(ArrayList.class);
        dataSourcesFactoryBean.setSourceList(dataSources);
        return dataSourcesFactoryBean;
    }

    @Bean
    public DataSourceKeyRegistry dataSourceKeyRegistry(ObjectProvider<List<DataSourceKeyAnnotationParser>> dataSourceKeyAnnotationParser){
        DataSourceKeyRegistry registry = new DataSourceKeyRegistry();

        return registry;
    }

    @Bean
    public DataSourceKeySelector dataSourceKeySelector(
            DataSourceRegistry registry,
            DataSourceKeyRegistry keyRegistry,
            ObjectProvider<List<DataSourceKeyFilter>> filtersProvider) {
        DataSourceKeySelector selector = new DataSourceKeySelector();
        selector.setDataSourceRegistry(registry);
        List<DataSourceKeyFilter> filters = filtersProvider.getIfAvailable();
        selector.addDataSourceKeyFilters(filters);
        selector.setDataSourceKeyRegistry(keyRegistry);
        return selector;
    }

}