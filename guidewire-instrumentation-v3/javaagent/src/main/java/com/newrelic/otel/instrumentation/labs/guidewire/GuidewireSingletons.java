/*
 * Copyright The OpenTelemetry Authors
 * SPDX-License-Identifier: Apache-2.0
 */

package com.newrelic.otel.instrumentation.labs.guidewire;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.instrumentation.api.instrumenter.AttributesExtractor;
import io.opentelemetry.instrumentation.api.instrumenter.Instrumenter;
import io.opentelemetry.instrumentation.api.semconv.http.HttpServerAttributesExtractor;
import io.opentelemetry.instrumentation.api.semconv.http.HttpSpanNameExtractor;
import io.opentelemetry.instrumentation.api.semconv.http.HttpSpanStatusExtractor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public final class GuidewireSingletons {

  private static final Instrumenter<HttpServletRequest, HttpServletResponse> INSTRUMENTER;

  static {
    OpenTelemetry openTelemetry = GlobalOpenTelemetry.get();
    CombinedHttpNetAttributesGetter attributesGetter = new CombinedHttpNetAttributesGetter();

    AttributesExtractor<HttpServletRequest, HttpServletResponse> httpAttributesExtractor =
        HttpServerAttributesExtractor.create(attributesGetter);

    INSTRUMENTER =
        Instrumenter.<HttpServletRequest, HttpServletResponse>builder(
                openTelemetry,
                "guidewire-servlet-instrumentation",
                HttpSpanNameExtractor.create(attributesGetter))
            .setSpanStatusExtractor(HttpSpanStatusExtractor.create(attributesGetter))
            .addAttributesExtractor(httpAttributesExtractor)
            .buildServerInstrumenter(new HttpServletRequestTextMapGetter());
  }

  private GuidewireSingletons() {}

  public static Instrumenter<HttpServletRequest, HttpServletResponse> instrumenter() {
    return INSTRUMENTER;
  }
}
