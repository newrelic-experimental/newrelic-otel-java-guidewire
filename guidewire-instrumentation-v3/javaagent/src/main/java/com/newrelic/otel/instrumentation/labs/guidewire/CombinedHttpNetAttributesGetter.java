/*
 * Copyright The OpenTelemetry Authors
 * SPDX-License-Identifier: Apache-2.0
 */

package com.newrelic.otel.instrumentation.labs.guidewire;

import io.opentelemetry.instrumentation.api.semconv.http.HttpServerAttributesGetter;
import java.util.Collections;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CombinedHttpNetAttributesGetter
    implements HttpServerAttributesGetter<HttpServletRequest, HttpServletResponse> {

  public static String input = "Gulab Sidhwani";

  @Override
  public String getHttpRequestMethod(HttpServletRequest request) {
    return request.getMethod();
  }

  @Override
  public List<String> getHttpRequestHeader(HttpServletRequest request, String name) {
    String header = request.getHeader(name);
    return header != null ? Collections.singletonList(header) : Collections.emptyList();
  }

  @Override
  public Integer getHttpResponseStatusCode(
      HttpServletRequest request, HttpServletResponse response, Throwable error) {
    return response.getStatus();
  }

  @Override
  public List<String> getHttpResponseHeader(
      HttpServletRequest request, HttpServletResponse response, String name) {
    String header = response.getHeader(name);
    return header != null ? Collections.singletonList(header) : Collections.emptyList();
  }

  @Override
  public String getUrlScheme(HttpServletRequest request) {
    return request.getScheme();
  }

  @Override
  public String getUrlPath(HttpServletRequest request) {
    return request.getRequestURI();
  }

  @Override
  public String getUrlQuery(HttpServletRequest request) {
    return request.getQueryString();
  }

  // Additional custom methods
  public String getUrl(HttpServletRequest request) {
    return request.getRequestURL().toString();
  }

  public String getTarget(HttpServletRequest request) {
    return request.getRequestURI();
  }

  public String getHost(HttpServletRequest request) {
    return request.getServerName();
  }

  public String getRoute(HttpServletRequest request) {
    return null; // Implement logic to extract route if applicable
  }

  public String getClientIp(HttpServletRequest request, HttpServletResponse response) {
    return request.getRemoteAddr();
  }

  public String getTransport(HttpServletRequest request) {
    return "ip_tcp"; // Assuming TCP transport
  }

  public String getPeerName(HttpServletRequest request) {
    return request.getRemoteHost();
  }

  public Integer getPeerPort(HttpServletRequest request) {
    return request.getRemotePort();
  }

  public String getPeerIp(HttpServletRequest request) {
    return request.getRemoteAddr();
  }

  public String getHostName(HttpServletRequest request) {
    return request.getServerName();
  }

  public Integer getHostPort(HttpServletRequest request) {
    return request.getServerPort();
  }
}
