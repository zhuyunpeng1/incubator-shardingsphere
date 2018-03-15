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

package io.shardingjdbc.core.parsing.parser.dialect.postgresql.clause.facade;

import io.shardingjdbc.core.constant.DatabaseType;
import io.shardingjdbc.core.rule.ShardingRule;
import io.shardingjdbc.core.parsing.lexer.LexerEngine;
import io.shardingjdbc.core.parsing.parser.clause.UpdateSetItemsClauseParser;
import io.shardingjdbc.core.parsing.parser.clause.WhereClauseParser;
import io.shardingjdbc.core.parsing.parser.clause.facade.AbstractUpdateClauseParserFacade;
import io.shardingjdbc.core.parsing.parser.dialect.postgresql.clause.PostgreSQLTableReferencesClauseParser;

/**
 * Update clause parser facade for PostgreSQL.
 *
 * @author zhangliang
 */
public final class PostgreSQLUpdateClauseParserFacade extends AbstractUpdateClauseParserFacade {
    
    public PostgreSQLUpdateClauseParserFacade(final ShardingRule shardingRule, final LexerEngine lexerEngine) {
        super(new PostgreSQLTableReferencesClauseParser(shardingRule, lexerEngine), new UpdateSetItemsClauseParser(lexerEngine), new WhereClauseParser(DatabaseType.PostgreSQL, lexerEngine));
    }
}
