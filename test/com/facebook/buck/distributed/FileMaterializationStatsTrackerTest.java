/*
 * Copyright 2017-present Facebook, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.facebook.buck.distributed;

import com.facebook.buck.distributed.thrift.FileMaterializationStats;
import org.junit.Assert;
import org.junit.Test;

public class FileMaterializationStatsTrackerTest {
  @Test
  public void testRecordsStatsSuccessfully() {
    FileMaterializationStatsTracker tracker = new FileMaterializationStatsTracker();

    tracker.recordLocalFileMaterialized();
    tracker.recordLocalFileMaterialized();
    tracker.recordLocalFileMaterialized();

    Assert.assertEquals(3, tracker.getFilesMaterializedFromLocalCacheCount());

    tracker.recordRemoteFileMaterialized(10);
    tracker.recordRemoteFileMaterialized(20);

    Assert.assertEquals(2, tracker.getFilesMaterializedFromCASCount());
    Assert.assertEquals(30, tracker.getTotalTimeSpentMaterializingFilesFromCASMillis());

    tracker.recordPeriodicCasMultiFetch(50);
    tracker.recordPeriodicCasMultiFetch(75);
    tracker.recordFullBufferCasMultiFetch(100);

    Assert.assertEquals(2, tracker.getPeriodicCasMultiFetchCount());
    Assert.assertEquals(1, tracker.getFullBufferCasMultiFetchCount());
    Assert.assertEquals(225, tracker.getTimeSpentInMultiFetchNetworkCallsMs());

    FileMaterializationStats stats = tracker.getFileMaterializationStats();
    Assert.assertEquals(5, stats.getTotalFilesMaterializedCount());
    Assert.assertEquals(2, stats.getFilesMaterializedFromCASCount());
    Assert.assertEquals(30, stats.getTotalTimeSpentMaterializingFilesFromCASMillis());
    Assert.assertEquals(2, stats.getPeriodicCasMultiFetchCount());
    Assert.assertEquals(1, stats.getFullBufferCasMultiFetchCount());
    Assert.assertEquals(225, stats.getTimeSpentInMultiFetchNetworkCallsMs());
  }
}
