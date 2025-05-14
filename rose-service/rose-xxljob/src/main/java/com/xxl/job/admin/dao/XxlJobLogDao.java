/*
 * Copyright © 2025 rose-group.github.io
 *
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
 */
package com.xxl.job.admin.dao;

import com.xxl.job.admin.core.model.XxlJobLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * job log
 *
 * @author xuxueli 2016-1-12 18:03:06
 */
@Mapper
public interface XxlJobLogDao {

    // exist jobId not use jobGroup, not exist use jobGroup
    List<XxlJobLog> pageList(
        @Param("offset") int offset,
        @Param("pagesize") int pagesize,
        @Param("jobGroup") int jobGroup,
        @Param("jobId") int jobId,
        @Param("triggerTimeStart") Date triggerTimeStart,
        @Param("triggerTimeEnd") Date triggerTimeEnd,
        @Param("logStatus") int logStatus);

    int pageListCount(
        @Param("offset") int offset,
        @Param("pagesize") int pagesize,
        @Param("jobGroup") int jobGroup,
        @Param("jobId") int jobId,
        @Param("triggerTimeStart") Date triggerTimeStart,
        @Param("triggerTimeEnd") Date triggerTimeEnd,
        @Param("logStatus") int logStatus);

    XxlJobLog load(@Param("id") long id);

    long save(XxlJobLog xxlJobLog);

    int updateTriggerInfo(XxlJobLog xxlJobLog);

    int updateHandleInfo(XxlJobLog xxlJobLog);

    int delete(@Param("jobId") int jobId);

    Map<String, Object> findLogReport(@Param("from") Date from, @Param("to") Date to);

    List<Long> findClearLogIds(
        @Param("jobGroup") int jobGroup,
        @Param("jobId") int jobId,
        @Param("clearBeforeTime") Date clearBeforeTime,
        @Param("clearBeforeNum") int clearBeforeNum,
        @Param("pagesize") int pagesize);

    int clearLog(@Param("logIds") List<Long> logIds);

    List<Long> findFailJobLogIds(@Param("pagesize") int pagesize);

    int updateAlarmStatus(
        @Param("logId") long logId,
        @Param("oldAlarmStatus") int oldAlarmStatus,
        @Param("newAlarmStatus") int newAlarmStatus);

    List<Long> findLostJobIds(@Param("losedTime") Date losedTime);
}
