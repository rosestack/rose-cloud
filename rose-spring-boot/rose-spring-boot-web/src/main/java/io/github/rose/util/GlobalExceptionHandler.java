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
// package cc.chensoul.boot.mvc.handler;
//
// import cc.chensoul.support.exception.BusinessException;
// import cc.chensoul.support.exception.NotFoundException;
// import cc.chensoul.support.exception.ResultCode;
// import cc.chensoul.support.exception.TooManyRequestException;
// import cc.chensoul.support.util.ErrorResponse;
// import lombok.RequiredArgsConstructor;
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.support.NestedExceptionUtils;
// import org.springframework.http.HttpStatus;
// import org.springframework.security.access.AccessDeniedException;
// import org.springframework.validation.BindException;
// import org.springframework.validation.FieldError;
// import org.springframework.validation.ObjectError;
// import org.springframework.web.HttpRequestMethodNotSupportedException;
// import org.springframework.web.bind.MethodArgumentNotValidException;
// import org.springframework.web.bind.MissingServletRequestParameterException;
// import org.springframework.web.bind.annotation.ExceptionHandler;
// import org.springframework.web.bind.annotation.ResponseStatus;
// import org.springframework.web.bind.annotation.RestControllerAdvice;
// import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
// import org.springframework.web.servlet.NoHandlerFoundException;
//
// import javax.servlet.http.HttpServletRequest;
// import javax.validation.ConstraintViolation;
// import javax.validation.ConstraintViolationException;
// import java.sql.SQLIntegrityConstraintViolationException;
//
// import static cc.chensoul.support.exception.ResultCode.INTERNAL_ERROR;
//
/// **
// * Global Exception Handler
// *
// * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
// * @since 0.0.1
// */
// @Slf4j
// @RestControllerAdvice
// @RequiredArgsConstructor
// public class GlobalExceptionHandler {
// private final HttpServletRequest request;
//
// /**
// * 处理所有异常，主要是提供给 Filter 使用
// * 因为 Filter 不走 SpringMVC 的流程，但是我们又需要兜底处理异常，所以这里提供一个全量的异常处理过程，保持逻辑统一。
// */
// public ErrorResponse handleException(Throwable ex) {
// if (ex instanceof MissingServletRequestParameterException) {
// return
// handleMissingServletRequestParameterException((MissingServletRequestParameterException)
// ex);
// }
// if (ex instanceof MethodArgumentTypeMismatchException) {
// return handleMethodArgumentTypeMismatchException((MethodArgumentTypeMismatchException)
// ex);
// }
// if (ex instanceof MethodArgumentNotValidException) {
// return handleMethodArgumentNotValidException((MethodArgumentNotValidException) ex);
// }
// if (ex instanceof BindException) {
// return handleBindException((BindException) ex);
// }
// if (ex instanceof ConstraintViolationException) {
// return handleConstraintViolationException((ConstraintViolationException) ex);
// }
// if (ex instanceof SQLIntegrityConstraintViolationException) {
// return handleSQLIntegrityConstraintViolationException((ConstraintViolationException)
// ex);
// }
// if (ex instanceof NoHandlerFoundException) {
// return handleNoHandlerFoundException((NoHandlerFoundException) ex);
// }
// if (ex instanceof HttpRequestMethodNotSupportedException) {
// return
// handleHttpRequestMethodNotSupportedException((HttpRequestMethodNotSupportedException)
// ex);
// }
// if (ex instanceof TooManyRequestException) {
// return handlerTooManyRequestException((TooManyRequestException) ex);
// }
// if (ex instanceof NotFoundException) {
// return handleNotFoundExceptions((NotFoundException) ex);
// }
// if (ex instanceof BusinessException) {
// return handleBusinessException((BusinessException) ex);
// }
// if (ex instanceof AccessDeniedException) {
// return handleAccessDeniedException((AccessDeniedException) ex);
// }
// return handleDefaultException(ex);
// }
//
// /**
// * 处理 SpringMVC 请求参数缺失
// * <p>
// * 例如说，接口上设置了 @RequestParam("xx") 参数，结果并未传递 xx 参数
// */
// @ExceptionHandler(value = MissingServletRequestParameterException.class)
// public ErrorResponse handleMissingServletRequestParameterException(final
// MissingServletRequestParameterException ex) {
// logException(ex, request);
//
// return ErrorResponse.of(ResultCode.BAD_REQUEST.getCode(), String.format("请求参数缺失: %s",
// ex.getParameterName()));
// }
//
// /**
// * 处理 SpringMVC 请求参数类型错误
// * <p>
// * 例如说，接口上设置了 @RequestParam("xx") 参数为 Integer，结果传递 xx 参数类型为 String
// */
// @ExceptionHandler(MethodArgumentTypeMismatchException.class)
// public ErrorResponse handleMethodArgumentTypeMismatchException(final
// MethodArgumentTypeMismatchException ex) {
// logException(ex, request);
//
// return ErrorResponse.of(ResultCode.BAD_REQUEST.getCode(), String.format("请求参数类型错误: %s",
// ex.getValue()));
// }
//
// /**
// * 处理 SpringMVC 参数校验不正确
// */
// @ExceptionHandler(MethodArgumentNotValidException.class)
// public ErrorResponse handleMethodArgumentNotValidException(final
// MethodArgumentNotValidException ex) {
// logException(ex, request);
//
// String message = null;
// for (ObjectError error : ex.getBindingResult().getAllErrors()) {
// if (error instanceof FieldError) {
// FieldError field = (FieldError) error;
// message = field.getDefaultMessage();
// break;
// }
// }
// return ErrorResponse.of(ResultCode.BAD_REQUEST.getCode(), String.format("请求参数不正确: %s",
// message));
// }
//
// /**
// * 处理 SpringMVC 请求方法不正确
// * <p>
// * 例如说，A 接口的方法为 GET 方式，结果请求方法为 POST 方式，导致不匹配
// */
// @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
// public ErrorResponse handleHttpRequestMethodNotSupportedException(final
// HttpRequestMethodNotSupportedException ex) {
// logException(ex, request);
//
// return ErrorResponse.of(ResultCode.BAD_REQUEST.getCode(), String.format("%s请求方法不支持",
// ex.getMethod()));
// }
//
// /**
// * 处理 SpringMVC 参数绑定不正确，本质上也是通过 Validator 校验
// */
// @ExceptionHandler(BindException.class)
// public ErrorResponse handleBindException(final BindException ex) {
// logException(ex, request);
//
// String message = null;
// for (ObjectError error : ex.getBindingResult().getAllErrors()) {
// if (error instanceof FieldError) {
// FieldError field = (FieldError) error;
// message = field.getDefaultMessage();
// break;
// }
// }
//
// return ErrorResponse.of(ResultCode.BAD_REQUEST.getCode(), String.format("请求参数不正确: %s",
// message));
// }
//
// /**
// * 处理 @Validated 校验不通过产生的异常
// */
// @ExceptionHandler(ConstraintViolationException.class)
// public ErrorResponse handleConstraintViolationException(final
// ConstraintViolationException ex) {
// logException(ex, request);
//
// String message = null;
// for (ConstraintViolation<?> constraintViolation : ex.getConstraintViolations()) {
// message = constraintViolation.getMessage();
// break;
// }
// return ErrorResponse.of(ResultCode.BAD_REQUEST.getCode(), String.format("请求参数不正确: %s",
// message));
// }
//
// @ExceptionHandler({SQLIntegrityConstraintViolationException.class})
// public ErrorResponse handleSQLIntegrityConstraintViolationException(final Exception ex)
// {
// logException(ex, request);
//
// final Throwable cause = NestedExceptionUtils.getMostSpecificCause(ex);
// String message = "系统异常";
// if (cause.getMessage().contains("Duplicate entry")) {
// final String[] split = cause.getMessage().split(" ");
// message = split[2] + "已存在";
// }
//
// return ErrorResponse.of(ResultCode.BAD_REQUEST.getCode(), String.format("数据库异常: %s",
// message));
// }
//
// /**
// * 处理 Spring Security 权限不足的异常
// * <p>
// * 来源是，使用 @PreAuthorize 注解，AOP 进行权限拦截
// */
// @ResponseStatus(HttpStatus.FORBIDDEN)
// @ExceptionHandler(AccessDeniedException.class)
// public ErrorResponse handleAccessDeniedException(final AccessDeniedException ex) {
// logException(ex, request);
// return ErrorResponse.of(ResultCode.FORBIDDEN.getCode(),
// ResultCode.FORBIDDEN.getName());
// }
//
// @ExceptionHandler(TooManyRequestException.class)
// @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
// public ErrorResponse handlerTooManyRequestException(final TooManyRequestException ex) {
// logException(ex, request);
// return ErrorResponse.of(ResultCode.TOO_MANY_REQUESTS.getCode(), ex.getMessage());
// }
//
// @ExceptionHandler(NotFoundException.class)
// public ErrorResponse handleNotFoundExceptions(final NotFoundException ex) {
// logException(ex, request);
// return ErrorResponse.of(ResultCode.NOT_FOUND.getCode(), ex.getMessage());
// }
//
// /**
// * 处理 SpringMVC 请求地址不存在
// * <p>
// * 注意，它需要设置如下两个配置项：
// * 1. spring.mvc.throw-exception-if-no-handler-found 为 true
// * 2. spring.mvc.static-path-pattern 为 /statics/**
// */
// @ExceptionHandler(NoHandlerFoundException.class)
// public ErrorResponse handleNoHandlerFoundException(NoHandlerFoundException ex) {
// logException(ex, request);
//
// return ErrorResponse.of(ResultCode.NOT_FOUND.getCode(), String.format("请求地址不存在: %s",
// ex.getRequestURL()));
// }
//
// @ExceptionHandler({BusinessException.class})
// public ErrorResponse handleBusinessException(final BusinessException ex) {
// logException(ex, request);
// return ErrorResponse.of(INTERNAL_ERROR.getCode(), ex.getMessage());
// }
//
// @ExceptionHandler({Throwable.class})
// public ErrorResponse handleDefaultException(final Throwable ex) {
// logException(ex, request);
// return ErrorResponse.of(INTERNAL_ERROR.getCode(), INTERNAL_ERROR.getName());
// }
//
// /**
// * @param exception
// * @see <a
// href="https://github.com/jhipster/jhipster-lite/blob/main/src/main/java/tech/jhipster/lite/shared/error/infrastructure/primary/GeneratorErrorsHandler.java">GeneratorErrorsHandler.java</a>
// */
// private void logException(Throwable exception, HttpServletRequest request) {
// log.error("Processing exception for {}", request.getRequestURI(), exception);
// }
// }
