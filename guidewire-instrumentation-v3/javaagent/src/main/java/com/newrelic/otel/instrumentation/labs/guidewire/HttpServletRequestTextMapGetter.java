/*
 * Copyright The OpenTelemetry Authors
 * SPDX-License-Identifier: Apache-2.0
 */

package com.newrelic.otel.instrumentation.labs.guidewire;

import io.opentelemetry.context.propagation.TextMapGetter;
import java.util.Collections;
import javax.servlet.http.HttpServletRequest;

public class HttpServletRequestTextMapGetter implements TextMapGetter<HttpServletRequest> {

  @Override
  public Iterable<String> keys(HttpServletRequest request) {
    return Collections.list(request.getHeaderNames());
  }

  @Override
  public String get(HttpServletRequest request, String key) {
    return request.getHeader(key);
  }
}
