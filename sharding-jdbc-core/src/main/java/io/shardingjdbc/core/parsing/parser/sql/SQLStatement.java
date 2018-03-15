/*
 * Copyright 1999-2015 dangdang.com.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * </p>
 */

package io.shardingjdbc.core.parsing.parser.sql;

import io.shardingjdbc.core.constant.SQLType;
import io.shardingjdbc.core.parsing.parser.context.condition.Conditions;
import io.shardingjdbc.core.parsing.parser.context.table.Tables;
import io.shardingjdbc.core.parsing.parser.token.SQLToken;

import java.util.List;

/**
 * SQL statement.
 *
 * @author zhangliang
 */
public interface SQLStatement {
    
    /**
     * Get SQL type.
     *
     * @return SQL type
     */
    SQLType getType();
    
    /**
     * Get tables.
     * 
     * @return tables
     */
    Tables getTables();
    
    /**
     * Get conditions.
     *
     * @return conditions
     */
    Conditions getConditions();
    
    /**
     * Get SQL Tokens.
     * 
     * @return SQL Tokens
     */
    List<SQLToken> getSqlTokens();
    
    /**
     * Get index of parameters.
     *
     * @return index of parameters
     */
    int getParametersIndex();
    
    /**
     * Set index of parameters.
     *
     * @param parametersIndex index of parameters
     */
    void setParametersIndex(int parametersIndex);
    
    /**
     * Increase parameters index.
     *
     * @return increased parameters index
     */
    int increaseParametersIndex();
}
