/*
 * Copyright 2020 the original author or authors.
 *
 * Licensed under the LGPL, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at  http://www.gnu.org/licenses/lgpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jn.sqlhelper.datasource;

import com.jn.langx.Named;
import com.jn.langx.annotation.Nullable;
import com.jn.sqlhelper.datasource.config.DataSourceProperties;
import com.jn.sqlhelper.datasource.key.DataSourceKey;

import javax.sql.DataSource;

/**
 * 为数据源提供 name, properties 属性
 * @since 3.4.0
 */
public interface NamedDataSource extends DataSource, Named {
    String getGroup();

    void setGroup(String group);

    DataSourceKey getDataSourceKey();

    /**
     * @since 3.4.1
     */
    DataSourceProperties getDataSourceProperties();

    void setDataSourceProperties(@Nullable DataSourceProperties properties);

    boolean isSlave();
}
