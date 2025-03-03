/*
 * Copyright The OpenTelemetry Authors
 * SPDX-License-Identifier: Apache-2.0
 */

package com.newrelic.otel.instrumentation.labs.guidewire;

import com.google.auto.service.AutoService;
import io.opentelemetry.javaagent.extension.instrumentation.InstrumentationModule;
import io.opentelemetry.javaagent.extension.instrumentation.TypeInstrumentation;
import io.opentelemetry.javaagent.extension.matcher.AgentElementMatchers;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import net.bytebuddy.matcher.ElementMatcher;

@AutoService(InstrumentationModule.class)
public class GuidewireInstrumentationModule extends InstrumentationModule {

  public GuidewireInstrumentationModule() {
    super("guidewire-instrumentation-v3", "servlet-3");
  }

  @Override
  public int order() {
    return 2; // Set a high value to ensure it's processed last
  }

  @Override
  public ElementMatcher.Junction<ClassLoader> classLoaderMatcher() {
    return AgentElementMatchers.hasClassesNamed("javax.servlet.http.HttpServlet");
  }

  @Override
  public List<TypeInstrumentation> typeInstrumentations() {
    return Collections.singletonList(new GuidewireServletInstrumentation());
  }

  @Override
  public boolean isHelperClass(String className) {
    List<String> helperPackages =
        Arrays.asList(
            "com.newrelic.otel.instrumentation.labs.guidewire",
            "io.opentelemetry.javaagent.extension.instrumentation",
            "net.bytebuddy",
            "org.objectweb",
            "io.opentelemetry.javaagent.tooling",
            "io.opentelemetry.javaagent.extension.matcher");
    for (String helperPackage : helperPackages) {
      if (className.startsWith(helperPackage)) {
        return true;
      }
    }
    return false;
  }
}
