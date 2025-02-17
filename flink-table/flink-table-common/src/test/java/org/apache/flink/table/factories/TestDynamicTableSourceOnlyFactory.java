/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.flink.table.factories;

import org.apache.flink.configuration.ConfigOption;
import org.apache.flink.configuration.ConfigOptions;
import org.apache.flink.table.connector.ChangelogMode;
import org.apache.flink.table.connector.source.DynamicTableSource;
import org.apache.flink.table.connector.source.ScanTableSource;

import java.util.Collections;
import java.util.Set;

/**
 * Test implementations for {@link DynamicTableSourceFactory} which only supports to be used as
 * source.
 */
public final class TestDynamicTableSourceOnlyFactory implements DynamicTableSourceFactory {

    public static final String IDENTIFIER = "source-only";

    private static final ConfigOption<Boolean> BOUNDED =
            ConfigOptions.key("bounded").booleanType().defaultValue(false);

    @Override
    public DynamicTableSource createDynamicTableSource(Context context) {
        FactoryUtil.TableFactoryHelper helper = FactoryUtil.createTableFactoryHelper(this, context);
        boolean isBounded = helper.getOptions().get(BOUNDED);
        return new MockedScanTableSource(isBounded);
    }

    @Override
    public String factoryIdentifier() {
        return IDENTIFIER;
    }

    @Override
    public Set<ConfigOption<?>> requiredOptions() {
        return Collections.emptySet();
    }

    @Override
    public Set<ConfigOption<?>> optionalOptions() {
        return Collections.singleton(BOUNDED);
    }

    /** A mocked {@link ScanTableSource} for validation test. */
    private static class MockedScanTableSource implements ScanTableSource {
        private final boolean isBounded;

        private MockedScanTableSource(boolean isBounded) {
            this.isBounded = isBounded;
        }

        @Override
        public DynamicTableSource copy() {
            return null;
        }

        @Override
        public String asSummaryString() {
            return null;
        }

        @Override
        public ChangelogMode getChangelogMode() {
            return ChangelogMode.insertOnly();
        }

        @Override
        public ScanRuntimeProvider getScanRuntimeProvider(ScanContext runtimeProviderContext) {
            return () -> isBounded;
        }
    }
}
