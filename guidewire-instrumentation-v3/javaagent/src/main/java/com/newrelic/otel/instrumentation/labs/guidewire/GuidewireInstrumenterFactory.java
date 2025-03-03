/*
 * Copyright The OpenTelemetry Authors
 * SPDX-License-Identifier: Apache-2.0
 */

package com.newrelic.otel.instrumentation.labs.guidewire;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.instrumentation.api.instrumenter.AttributesExtractor;
import io.opentelemetry.instrumentation.api.instrumenter.Instrumenter;
import io.opentelemetry.instrumentation.api.semconv.http.HttpServerAttributesExtractor;
import io.opentelemetry.instrumentation.api.semconv.http.HttpSpanNameExtractor;
import io.opentelemetry.instrumentation.api.semconv.http.HttpSpanStatusExtractor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public final class GuidewireInstrumenterFactory {

  private static final String INSTRUMENTATION_NAME = "com.newrelic.otel.guidewire";

  public static Instrumenter<HttpServletRequest, HttpServletResponse> create(
      OpenTelemetry openTelemetry) {
    CombinedHttpNetAttributesGetter attributesGetter = new CombinedHttpNetAttributesGetter();

    AttributesExtractor<HttpServletRequest, HttpServletResponse> httpAttributesExtractor =
        HttpServerAttributesExtractor.create(attributesGetter);
    return Instrumenter.<HttpServletRequest, HttpServletResponse>builder(
            openTelemetry, INSTRUMENTATION_NAME, HttpSpanNameExtractor.create(attributesGetter))
        .setSpanStatusExtractor(HttpSpanStatusExtractor.create(attributesGetter))
        .addAttributesExtractor(httpAttributesExtractor)
        .buildServerInstrumenter(new HttpServletRequestTextMapGetter());
  }

  private GuidewireInstrumenterFactory() {}
}
