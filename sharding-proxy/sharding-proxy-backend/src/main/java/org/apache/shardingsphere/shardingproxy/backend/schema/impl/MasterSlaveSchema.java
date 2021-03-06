/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.shardingsphere.shardingproxy.backend.schema.impl;

import com.google.common.eventbus.Subscribe;
import lombok.Getter;
import org.apache.shardingsphere.api.config.masterslave.MasterSlaveRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.ShardingRuleConfiguration;
import org.apache.shardingsphere.core.log.ConfigurationLogger;
import org.apache.shardingsphere.core.rule.MasterSlaveRule;
import org.apache.shardingsphere.core.rule.ShardingRule;
import org.apache.shardingsphere.orchestration.core.common.event.MasterSlaveRuleChangedEvent;
import org.apache.shardingsphere.orchestration.core.common.rule.OrchestrationMasterSlaveRule;
import org.apache.shardingsphere.orchestration.core.registrycenter.event.DisabledStateChangedEvent;
import org.apache.shardingsphere.orchestration.core.registrycenter.schema.OrchestrationShardingSchema;
import org.apache.shardingsphere.shardingproxy.backend.schema.LogicSchemas;
import org.apache.shardingsphere.shardingproxy.backend.schema.MetaDataInitializedLogicSchema;
import org.apache.shardingsphere.shardingproxy.config.yaml.YamlDataSourceParameter;
import org.apache.shardingsphere.shardingproxy.context.ShardingProxyContext;
import org.apache.shardingsphere.sql.parser.binder.metadata.schema.SchemaMetaData;
import org.apache.shardingsphere.underlying.common.metadata.ShardingSphereMetaData;
import org.apache.shardingsphere.underlying.common.metadata.schema.RuleSchemaMetaDataLoader;

import java.sql.SQLException;
import java.util.Collections;
import java.util.Map;

/**
 * Master-slave schema.
 */
@Getter
public final class MasterSlaveSchema extends MetaDataInitializedLogicSchema {
    
    private final ShardingRule shardingRule;
    
    private MasterSlaveRule masterSlaveRule;
    
    public MasterSlaveSchema(final String name,
                             final Map<String, YamlDataSourceParameter> dataSources, final MasterSlaveRuleConfiguration masterSlaveRuleConfig, final boolean isUsingRegistry) throws SQLException {
        super(name, dataSources);
        masterSlaveRule = createMasterSlaveRule(masterSlaveRuleConfig, isUsingRegistry);
        // TODO we should remove it after none-sharding parsingEngine completed.
        shardingRule = new ShardingRule(new ShardingRuleConfiguration(), getDataSources().keySet());
    }
    
    private MasterSlaveRule createMasterSlaveRule(final MasterSlaveRuleConfiguration masterSlaveRuleConfig, final boolean isUsingRegistry) {
        return isUsingRegistry ? new OrchestrationMasterSlaveRule(masterSlaveRuleConfig) : new MasterSlaveRule(masterSlaveRuleConfig);
    }
    
    @Override
    public ShardingSphereMetaData getMetaData() throws SQLException {
        RuleSchemaMetaDataLoader loader = new RuleSchemaMetaDataLoader(Collections.singletonList(masterSlaveRule));
        SchemaMetaData schemaMetaData = loader.load(LogicSchemas.getInstance().getDatabaseType(), getBackendDataSource().getDataSources(), ShardingProxyContext.getInstance().getProperties());
        return new ShardingSphereMetaData(getPhysicalMetaData().getDataSources(), schemaMetaData);
    }
    
    /**
     * Renew master-slave rule.
     *
     * @param masterSlaveRuleChangedEvent master-slave rule changed event.
     */
    @Subscribe
    public synchronized void renew(final MasterSlaveRuleChangedEvent masterSlaveRuleChangedEvent) {
        if (getName().equals(masterSlaveRuleChangedEvent.getShardingSchemaName())) {
            ConfigurationLogger.log(masterSlaveRuleChangedEvent.getMasterSlaveRuleConfiguration());
            masterSlaveRule = new OrchestrationMasterSlaveRule(masterSlaveRuleChangedEvent.getMasterSlaveRuleConfiguration());
        }
    }
    
    /**
     * Renew disabled data source names.
     *
     * @param disabledStateChangedEvent disabled state changed event
     */
    @Subscribe
    public synchronized void renew(final DisabledStateChangedEvent disabledStateChangedEvent) {
        OrchestrationShardingSchema shardingSchema = disabledStateChangedEvent.getShardingSchema();
        if (getName().equals(shardingSchema.getSchemaName())) {
            ((OrchestrationMasterSlaveRule) masterSlaveRule).updateDisabledDataSourceNames(shardingSchema.getDataSourceName(), disabledStateChangedEvent.isDisabled());
        }
    }
}
