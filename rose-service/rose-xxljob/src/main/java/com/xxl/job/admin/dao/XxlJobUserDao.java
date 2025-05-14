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

import com.xxl.job.admin.core.model.XxlJobUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author xuxueli 2019-05-04 16:44:59
 */
@Mapper
public interface XxlJobUserDao {

    List<XxlJobUser> pageList(
        @Param("offset") int offset,
        @Param("pagesize") int pagesize,
        @Param("username") String username,
        @Param("role") int role);

    int pageListCount(
        @Param("offset") int offset,
        @Param("pagesize") int pagesize,
        @Param("username") String username,
        @Param("role") int role);

    XxlJobUser loadByUserName(@Param("username") String username);

    int save(XxlJobUser xxlJobUser);

    int update(XxlJobUser xxlJobUser);

    int delete(@Param("id") int id);
}
