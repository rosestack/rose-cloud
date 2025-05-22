/*
 * Copyright © 2025 rosestack.github.io
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
package io.github.rose.iot.model;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since
 */
@Data
public class Product {

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    private String name;

    /**
     * 10 位随机字符串
     */
    private String key;

    private String icon;

    /**
     * 数据格式
     */
    private Integer dataFormat;

    /**
     * 开发中
     */
    private Integer status;

    /**
     * Wi-Fi
     * <p>
     * 蜂窝（2G/3G/4G/5G）
     * <p>
     * 以太网
     * <p>
     * 其他
     */
    private Integer netType;

    /**
     * 直连设备: 直连物联网平台，且不能挂载子设备，但能作为子设备挂载到网关下的设备。 网关子设备：不直接连接物联网平台，而是通过网关设备接入物联网平台的设备。
     * 网关设备：可以挂载子设备的直连设备。网关具有子设备管理模块，可以维持子设备的拓扑关系，将与子设备的拓扑关系同步到云端。
     */
    private String nodeType;

    /**
     * 授权类型：secret
     */
    private String authType;

    private String protocol;

    /**
     *
     */
    private Integer validateType;

    /**
     * 保活时间
     */
    private Long keepAliveTime;

    /**
     * 产品密钥
     */
    private String productSecret;

    /**
     * 是否透传
     */
    private Boolean transparent;

    private String description;

    private String thingModelId;

    private String tenantId;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @TableField(value = "created_time", fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createdTime;

    @TableField(value = "created_by", fill = FieldFill.INSERT)
    private String createdBy;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @TableField(value = "updated_time", fill = FieldFill.UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updatedTime;

    @TableField(value = "updated_by", fill = FieldFill.UPDATE)
    private String updatedBy;
}
