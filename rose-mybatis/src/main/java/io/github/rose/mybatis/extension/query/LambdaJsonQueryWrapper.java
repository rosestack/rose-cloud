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
package io.github.rose.mybatis.extension.query;

import com.baomidou.mybatisplus.core.conditions.AbstractLambdaWrapper;
import com.baomidou.mybatisplus.core.conditions.ISqlSegment;
import com.baomidou.mybatisplus.core.conditions.SharedString;
import com.baomidou.mybatisplus.core.conditions.query.Query;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.baomidou.mybatisplus.core.enums.SqlKeyword;
import com.baomidou.mybatisplus.core.enums.SqlLike;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlUtils;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
public class LambdaJsonQueryWrapper<T> extends AbstractLambdaWrapper<T, LambdaJsonQueryWrapper<T>>
    implements Query<LambdaJsonQueryWrapper<T>, T, SFunction<T, ?>> {

    private SharedString sqlSelect;

    public LambdaJsonQueryWrapper() {
        this((T) null);
    }

    /**
     * @param entity
     */
    public LambdaJsonQueryWrapper(T entity) {
        this.sqlSelect = new SharedString();
        super.setEntity(entity);
        super.initNeed();
    }

    /**
     * @param entityClass
     */
    public LambdaJsonQueryWrapper(Class<T> entityClass) {
        this.sqlSelect = new SharedString();
        super.setEntityClass(entityClass);
        super.initNeed();
    }

    /**
     * @param entity
     * @param entityClass
     * @param sqlSelect
     * @param paramNameSeq
     * @param paramNameValuePairs
     * @param mergeSegments
     * @param lastSql
     * @param sqlComment
     * @param sqlFirst
     */
    LambdaJsonQueryWrapper(
        T entity,
        Class<T> entityClass,
        SharedString sqlSelect,
        AtomicInteger paramNameSeq,
        Map<String, Object> paramNameValuePairs,
        MergeSegments mergeSegments,
        SharedString lastSql,
        SharedString sqlComment,
        SharedString sqlFirst) {
        this.sqlSelect = new SharedString();
        super.setEntity(entity);
        super.setEntityClass(entityClass);
        this.paramNameSeq = paramNameSeq;
        this.paramNameValuePairs = paramNameValuePairs;
        this.expression = mergeSegments;
        this.sqlSelect = sqlSelect;
        this.lastSql = lastSql;
        this.sqlComment = sqlComment;
        this.sqlFirst = sqlFirst;
    }

    public LambdaJsonQueryWrapper<T> select(boolean condition, List<SFunction<T, ?>> columns) {
        return this.doSelect(condition, columns);
    }

    public LambdaJsonQueryWrapper<T> doSelect(boolean condition, List<SFunction<T, ?>> columns) {
        if (condition && CollectionUtils.isNotEmpty(columns)) {
            this.sqlSelect.setStringValue(this.columnsToString(false, columns));
        }

        return this.typedThis;
    }

    @Override
    public LambdaJsonQueryWrapper<T> select(SFunction<T, ?>... columns) {
        return this.doSelect(true, Arrays.asList(columns));
    }

    /**
     * @param entityClass
     * @param predicate   过滤方式
     * @return
     */
    @Override
    public LambdaJsonQueryWrapper<T> select(Class<T> entityClass, Predicate<TableFieldInfo> predicate) {
        if (entityClass == null) {
            entityClass = this.getEntityClass();
        } else {
            this.setEntityClass(entityClass);
        }

        Assert.notNull(entityClass, "entityClass can not be null");
        this.sqlSelect.setStringValue(TableInfoHelper.getTableInfo(entityClass).chooseSelect(predicate));
        return this.typedThis;
    }

    /**
     * @return
     */
    @Override
    public String getSqlSelect() {
        return this.sqlSelect.getStringValue();
    }

    /**
     * @return
     */
    @Override
    protected LambdaJsonQueryWrapper<T> instance() {
        return new LambdaJsonQueryWrapper(
            this.getEntity(),
            this.getEntityClass(),
            null,
            this.paramNameSeq,
            this.paramNameValuePairs,
            new MergeSegments(),
            SharedString.emptyString(),
            SharedString.emptyString(),
            SharedString.emptyString());
    }

    /**
     *
     */
    @Override
    public void clear() {
        super.clear();
        this.sqlSelect.toNull();
    }

    /**
     * @param column
     * @param key
     * @param value
     * @return
     */
    public LambdaJsonQueryWrapper<T> eq(SFunction<T, ?> column, String key, Object value) {
        return eq(true, column, key, value);
    }

    /**
     * @param condition
     * @param column
     * @param key
     * @param value
     * @return
     */
    public LambdaJsonQueryWrapper<T> eq(boolean condition, SFunction<T, ?> column, String key, Object value) {
        return addCondition(condition, column, SqlKeyword.EQ, key, value);
    }

    /**
     * @param column
     * @param key
     * @param value
     * @return
     */
    public LambdaJsonQueryWrapper<T> ne(SFunction<T, ?> column, String key, Object value) {
        return addCondition(true, column, SqlKeyword.NE, key, value);
    }

    /**
     * @param condition
     * @param column
     * @param key
     * @param value
     * @return
     */
    public LambdaJsonQueryWrapper<T> ne(boolean condition, SFunction<T, ?> column, String key, Object value) {
        return addCondition(condition, column, SqlKeyword.EQ, key, value);
    }

    /**
     * @param column
     * @param key
     * @param value
     * @return
     */
    public LambdaJsonQueryWrapper<T> like(SFunction<T, ?> column, String key, Object value) {
        return like(true, column, key, value);
    }

    /**
     * @param condition
     * @param column
     * @param key
     * @param value
     * @return
     */
    public LambdaJsonQueryWrapper<T> like(boolean condition, SFunction<T, ?> column, String key, Object value) {
        return this.likeValue(condition, SqlKeyword.LIKE, column, key, value, SqlLike.DEFAULT);
    }

    /**
     * @param column
     * @param key
     * @param value
     * @return
     */
    public LambdaJsonQueryWrapper<T> notLike(SFunction<T, ?> column, String key, Object value) {
        return notLike(true, column, key, value);
    }

    /**
     * @param condition
     * @param column
     * @param key
     * @param value
     * @return
     */
    public LambdaJsonQueryWrapper<T> notLike(boolean condition, SFunction<T, ?> column, String key, Object value) {
        return this.likeValue(condition, SqlKeyword.NOT_LIKE, column, key, value, SqlLike.DEFAULT);
    }

    /**
     * @param column
     * @param key
     * @param value
     * @return
     */
    public LambdaJsonQueryWrapper<T> likeLeft(SFunction<T, ?> column, String key, Object value) {
        return likeLeft(true, column, key, value);
    }

    /**
     * @param condition
     * @param column
     * @param key
     * @param value
     * @return
     */
    public LambdaJsonQueryWrapper<T> likeLeft(boolean condition, SFunction<T, ?> column, String key, Object value) {
        return this.likeValue(condition, SqlKeyword.LIKE, column, key, value, SqlLike.LEFT);
    }

    /**
     * @param column
     * @param key
     * @param value
     * @return
     */
    public LambdaJsonQueryWrapper<T> likeRight(SFunction<T, ?> column, String key, Object value) {
        return likeRight(true, column, key, value);
    }

    /**
     * @param condition
     * @param column
     * @param key
     * @param value
     * @return
     */
    public LambdaJsonQueryWrapper<T> likeRight(boolean condition, SFunction<T, ?> column, String key, Object value) {
        return this.likeValue(condition, SqlKeyword.LIKE, column, key, value, SqlLike.RIGHT);
    }

    /**
     * @param column
     * @param key
     * @return
     */
    public LambdaJsonQueryWrapper<T> isNull(SFunction<T, ?> column, String key) {
        return isNull(true, column, key);
    }

    /**
     * @param condition
     * @param column
     * @param key
     * @return
     */
    public LambdaJsonQueryWrapper<T> isNull(boolean condition, SFunction<T, ?> column, String key) {
        return maybeDo(
            condition,
            () -> appendSqlSegments(
                columnToSqlSegment(column), getSqlKeyword(), getJsonKeySqlSegment(key), SqlKeyword.IS_NULL));
    }

    /**
     * @param column
     * @param key
     * @return
     */
    public LambdaJsonQueryWrapper<T> isNotNull(SFunction<T, ?> column, String key) {
        return isNotNull(true, column, key);
    }

    /**
     * @param condition
     * @param column
     * @param key
     * @return
     */
    public LambdaJsonQueryWrapper<T> isNotNull(boolean condition, SFunction<T, ?> column, String key) {
        return maybeDo(
            condition,
            () -> appendSqlSegments(
                columnToSqlSegment(column),
                getSqlKeyword(),
                getJsonKeySqlSegment(key),
                SqlKeyword.IS_NOT_NULL));
    }

    /**
     * @param condition
     * @param column
     * @param sqlKeyword
     * @param key
     * @param value
     * @return
     */
    protected LambdaJsonQueryWrapper<T> addCondition(
        boolean condition, SFunction<T, ?> column, SqlKeyword sqlKeyword, String key, Object value) {
        return maybeDo(
            condition,
            () -> appendSqlSegments(
                columnToSqlSegment(column),
                getSqlKeyword(),
                getJsonKeySqlSegment(key),
                sqlKeyword,
                () -> formatParam(null, value)));
    }

    /**
     * @param condition
     * @param sqlKeyword
     * @param column
     * @param key
     * @param value
     * @param sqlLike
     * @return
     */
    protected LambdaJsonQueryWrapper<T> likeValue(
        boolean condition,
        SqlKeyword sqlKeyword,
        SFunction<T, ?> column,
        String key,
        Object value,
        SqlLike sqlLike) {
        return maybeDo(
            condition,
            () -> appendSqlSegments(
                columnToSqlSegment(column),
                getSqlKeyword(),
                getJsonKeySqlSegment(key),
                sqlKeyword,
                () -> formatParam(null, SqlUtils.concatLike(value, sqlLike))));
    }

    /**
     * @return
     */
    private ISqlSegment getSqlKeyword() {
        return () -> "->";
    }

    /**
     * @param key
     * @return
     */
    private ISqlSegment getJsonKeySqlSegment(String key) {
        return () -> String.format("'$.%s'", key);
    }
}
