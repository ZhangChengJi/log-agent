/*
 * Copyright (c) 2020 pig4cloud Authors. All Rights Reserved.
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

package com.log.agent.domain;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 日志表
 * </p>
 *
 * @author zcj
 * @since 2021/3/1
 */
@Data
public class SysLog   {






	/**
	 * 日志标题
	 */
	@NotBlank(message = "日志标题不能为空")
	@ApiModelProperty(value = "日志标题")
	private String title;

	/**
	 * 创建者
	 */
	@ApiModelProperty(value = "创建人")
	private String createBy;

	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime createTime;



	/**
	 * 操作IP地址
	 */
	@ApiModelProperty(value = "操作ip地址")
	private String remoteAddr;

	/**
	 * 用户浏览器
	 */
	@ApiModelProperty(value = "用户代理")
	private String userAgent;

	/**
	 * 请求URI
	 */
	@ApiModelProperty(value = "请求uri")
	private String requestUri;

	/**
	 * 操作方式
	 */
	@ApiModelProperty(value = "操作方式")
	private String method;

	@ApiModelProperty(value = "操作方法")
	private String methodName;

	@ApiModelProperty(value = "操作类")
	private String className;

	/**
	 * 操作提交的数据
	 */
	@ApiModelProperty(value = "数据")
	private String params;

	/**
	 * 执行时间
	 */
	@ApiModelProperty(value = "方法执行时间")
	private Long time;



	/**
	 * 服务ID
	 */
	@ApiModelProperty(value = "应用标识")//项目名
	private String serviceId;



}
