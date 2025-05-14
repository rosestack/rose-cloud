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

import com.xxl.job.admin.core.model.XxlJobRegistry;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by xuxueli on 16/9/30.
 */
@Mapper
public interface XxlJobRegistryDao {

    List<Integer> findDead(@Param("timeout") int timeout, @Param("nowTime") Date nowTime);

    int removeDead(@Param("ids") List<Integer> ids);

    List<XxlJobRegistry> findAll(@Param("timeout") int timeout, @Param("nowTime") Date nowTime);

    int registryUpdate(
        @Param("registryGroup") String registryGroup,
        @Param("registryKey") String registryKey,
        @Param("registryValue") String registryValue,
        @Param("updateTime") Date updateTime);

    int registrySave(
        @Param("registryGroup") String registryGroup,
        @Param("registryKey") String registryKey,
        @Param("registryValue") String registryValue,
        @Param("updateTime") Date updateTime);

    int registryDelete(
        @Param("registryGroup") String registryGroup,
        @Param("registryKey") String registryKey,
        @Param("registryValue") String registryValue);
}
