/*
 * Copyright The OpenTelemetry Authors
 * SPDX-License-Identifier: Apache-2.0
 */

package com.newrelic.otel.instrumentation.labs.guidewire;

import static java.util.logging.Level.FINE;
import static net.bytebuddy.matcher.ElementMatchers.namedOneOf;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.Scope;
import io.opentelemetry.javaagent.bootstrap.Java8BytecodeBridge;
import io.opentelemetry.javaagent.extension.instrumentation.TypeInstrumentation;
import io.opentelemetry.javaagent.extension.instrumentation.TypeTransformer;
import io.opentelemetry.javaagent.extension.matcher.AgentElementMatchers;
import io.opentelemetry.javaagent.tooling.TransformSafeLogger;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;

public class GuidewireServletInstrumentation implements TypeInstrumentation {

  public static final Map<String, String> selectedParametersMap;
  public static final TransformSafeLogger logger =
      TransformSafeLogger.getLogger(GuidewireServletInstrumentation.class);

  static {
    Map<String, String> map = new HashMap<>();
    map.put("eventSource", "eventSource");
    map.put("eventParam", "eventParam");
    map.put(
        "SimpleClaimSearch:SimpleClaimSearchScreen:SimpleClaimSearchDV:ClaimNumber",
        "Claim Number");
    map.put(
        "SimpleClaimSearch:SimpleClaimSearchScreen:SimpleClaimSearchDV:PolicyNumber",
        "Policy Number");
    map.put(
        "SimpleClaimSearch:SimpleClaimSearchScreen:SimpleClaimSearchDV:FirstName", "First Name");
    map.put("SimpleClaimSearch:SimpleClaimSearchScreen:SimpleClaimSearchDV:LastName", "Last Name");
    map.put(
        "SimpleClaimSearch:SimpleClaimSearchScreen:SimpleClaimSearchDV:CompanyName",
        "Organization Name");
    map.put(
        "ClaimSearch:ClaimSearchScreen:ClaimSearchDV:ClaimSearchRequiredInputSet:ClaimNumber",
        "Claim Number");
    map.put(
        "ClaimSearch:ClaimSearchScreen:ClaimSearchDV:ClaimSearchRequiredInputSet:PolicyNumber",
        "Policy Number");
    map.put(
        "ClaimSearch:ClaimSearchScreen:ClaimSearchDV:ClaimSearchRequiredInputSet:FirstName",
        "First Name");
    map.put(
        "ClaimSearch:ClaimSearchScreen:ClaimSearchDV:ClaimSearchRequiredInputSet:LastName",
        "Last Name");
    map.put(
        "ClaimSearch:ClaimSearchScreen:ClaimSearchDV:ClaimSearchRequiredInputSet:AssignedToGroup",
        "Assigned To Group");
    map.put(
        "ClaimSearch:ClaimSearchScreen:ClaimSearchDV:ClaimSearchRequiredInputSet:AssignedToUser",
        "Assigned To User");
    map.put(
        "ClaimSearch:ClaimSearchScreen:ClaimSearchDV:ClaimSearchRequiredInputSet:CreatedBy",
        "Created By");
    map.put(
        "ClaimSearch:ClaimSearchScreen:ClaimSearchDV:ClaimSearchRequiredInputSet:CatNumber",
        "CAT/STORM");
    map.put(
        "ClaimSearch:ClaimSearchScreen:ClaimSearchDV:ClaimSearchRequiredInputSet:VinNumber", "VIN");
    map.put(
        "ClaimSearch:ClaimSearchScreen:ClaimSearchDV:ClaimSearchOptionalInputSet:lossStateActiveSearch",
        "Loss State");
    map.put(
        "ClaimSearch:ClaimSearchScreen:ClaimSearchDV:ClaimSearchOptionalInputSet:ClaimStatus",
        "Claim Status");
    map.put(
        "ClaimSearch:ClaimSearchScreen:ClaimSearchDV:ClaimSearchOptionalInputSet:nwPolicyType",
        "Policy Type");
    map.put(
        "ClaimSearch:ClaimSearchScreen:ClaimSearchDV:ClaimSearchOptionalInputSet:LossType",
        "Loss Type");
    map.put(
        "ClaimSearch:ClaimSearchScreen:ClaimSearchDV:ClaimSearchOptionalInputSet:DateSearch:DateSearchRangeValue",
        "Search For Date Since");
    map.put(
        "ClaimSearch:ClaimSearchScreen:ClaimSearchDV:ClaimSearchOptionalInputSet:DateSearch:DateSearchEndDate",
        "Search For Data To");
    map.put(
        "ClaimSearch:ClaimSearchScreen:ClaimSearchDV:ClaimSearchAndResetInputSet:Search_act",
        "Search_act");
    map.put(
        "ClaimNewDocumentFromTemplateWorksheet:NewDocumentFromTemplateScreen:NewTemplateDocumentDV:CreateDocument",
        "Create Document From Template");
    map.put(
        "ClaimNewDocumentFromTemplateWorksheet:NewDocumentFromTemplateScreen:NewTemplateDocumentDV:CreateDocument_act",
        "Create Document From act");
    map.put(
        "ClaimNewDocumentFromTemplateWorksheet:NewDocumentFromTemplateScreen:NewTemplateDocumentDV:ViewLink_link",
        "ViewLink_link");
    map.put("Login:LoginScreen:LoginDV:username", "User Name");
    map.put("NewSubmission:NewSubmissionScreen:ProductSettingsDV:DefaultBaseState", "State");
    selectedParametersMap = Collections.unmodifiableMap(map);
  }

  @Override
  public ElementMatcher<TypeDescription> typeMatcher() {
    return AgentElementMatchers.hasSuperType(namedOneOf("javax.servlet.Servlet"));
  }

  @Override
  public void transform(TypeTransformer typeTransformer) {
    typeTransformer.applyAdviceToMethod(
        namedOneOf("service")
            .and(
                ElementMatchers.takesArgument(
                    0, ElementMatchers.named("javax.servlet.ServletRequest")))
            .and(
                ElementMatchers.takesArgument(
                    1, ElementMatchers.named("javax.servlet.ServletResponse")))
            .and(ElementMatchers.isPublic()),
        this.getClass().getName() + "$GuidewireServletAdvice");
  }

  public static class GuidewireServletAdvice {

    @Advice.OnMethodEnter(suppress = Throwable.class)
    public static Scope onEnter(
        @Advice.Argument(0) ServletRequest request,
        @Advice.Argument(1) ServletResponse response,
        @Advice.Local("otelScope") Scope scope,
        @Advice.Local("otelSpan") Span span) {

      if (!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse)) {
        return null;
      }

      HttpServletRequest httpRequest = (HttpServletRequest) request;
      Tracer tracer =
          GlobalOpenTelemetry.getTracer(
              "com.newrelic.otel.instrumentation.labs.guidewire", "1.0.0");

      // Use Java8BytecodeBridge to get the current context
      Context currentContext = Java8BytecodeBridge.currentContext();
      Span currentSpan = Java8BytecodeBridge.spanFromContext(currentContext);

      // Determine the custom span name
      String eventSource = httpRequest.getParameter("eventSource");
      String customSpanName = determineCustomSpanName(eventSource, httpRequest);

      // Create a new span with the custom name and make it current
      span =
          tracer
              .spanBuilder(customSpanName)
              .setParent(currentContext.with(currentSpan))
              .setAttribute("Method", httpRequest.getMethod())
              .startSpan();
      scope = span.makeCurrent();

      // Log the start of the service method
      logger.log(FINE, "GUIDEWIRE - Starting HttpServlet service method");
      logger.log(
          FINE,
          "GUIDEWIRE - browser character encoding before GW: {0}",
          httpRequest.getCharacterEncoding());
      logger.log(FINE, "GUIDEWIRE - Calling original HttpServlet.service method");

      // Additional attributes
      if (eventSource != null) {
        span.setAttribute("eventSource", eventSource);
      }

      Map<String, String[]> paramMap = httpRequest.getParameterMap();
      for (String key : paramMap.keySet()) {
        logger.log(FINE, "GUIDEWIRE - Processing request param: {0}", key);
        String paramDisplayName = selectedParametersMap.get(key);
        if (paramDisplayName != null) {
          logger.log(FINE, "GUIDEWIRE - Found request param: {0}", key);
          String[] values = paramMap.get(key);
          if (values != null && values.length > 0) {
            String tempAttr = String.join(" ", values);
            span.setAttribute(paramDisplayName, tempAttr);
            logger.log(
                FINE,
                "GUIDEWIRE - adding attribute for request parameter: {0}",
                paramDisplayName + "=" + tempAttr);
          }
        }
      }

      // Process cookies
      logger.log(FINE, "GUIDEWIRE - processing cookies");
      Cookie[] cookies = httpRequest.getCookies();
      if (cookies != null) {
        for (Cookie cookie : cookies) {
          String cookieName = cookie.getName();
          if (cookieName != null && cookieName.startsWith("JSESSIONID")) {
            span.setAttribute(cookieName, cookie.getValue());
            logger.log(
                FINE,
                "GUIDEWIRE - adding attribute for JSESSIONID cookie: {0}",
                cookieName + "=" + cookie.getValue());
          }
        }
      }

      // Set additional attributes based on eventSource
      logger.log(FINE, "GUIDEWIRE - processing eventSource");
      if (eventSource != null && !eventSource.isEmpty()) {
        if ("_refresh_".equals(eventSource)) {
          String eventParam = request.getParameter("eventParam");
          if (eventParam != null && !eventParam.isEmpty()) {
            span.setAttribute("transaction.name", eventParam);
          } else {
            span.setAttribute("transaction.name", eventSource);
          }
        } else {
          boolean wizardAndScreenCheckNeeded = eventSource.contains("Wizard:Next_act");
          boolean wizardAndScreenFound = false;
          String wizard = "";
          String screen = "";

          for (String key : paramMap.keySet()) {
            if (wizardAndScreenCheckNeeded && !wizardAndScreenFound) {
              if (key.contains("Wizard:") && key.contains("Screen:")) {
                String[] keyParts = key.split(":");
                boolean wizardFound = false;
                boolean screenFound = false;
                for (String splitKey : keyParts) {
                  if (splitKey.endsWith("Wizard")) {
                    wizard = splitKey.trim();
                    wizardFound = true;
                  }
                  if (splitKey.endsWith("Screen")) {
                    screen = splitKey.trim();
                    screenFound = true;
                  }
                  if (wizardFound && screenFound) {
                    wizardAndScreenFound = true;
                    span.setAttribute("Wizard", wizard);
                    span.setAttribute("Screen", screen);
                    span.setAttribute("transaction.name", wizard + ":" + screen);
                    break;
                  }
                }
              }
            }
          }
          if (!wizardAndScreenFound) {
            span.setAttribute("transaction.name", eventSource);
          }
        }
      }

      // Set the thread name as an attribute
      String threadName = Thread.currentThread().getName();
      span.setAttribute("ThreadName", threadName);

      return scope;
    }

    public static String determineCustomSpanName(
        String eventSource, HttpServletRequest httpRequest) {
      if (eventSource != null && !eventSource.isEmpty()) {
        if ("_refresh_".equals(eventSource)) {
          String eventParam = httpRequest.getParameter("eventParam");
          if (eventParam != null && !eventParam.isEmpty()) {
            return "eventParam:" + eventParam;
          } else {
            return "eventSource:" + eventSource;
          }
        } else {
          boolean wizardAndScreenCheckNeeded = eventSource.contains("Wizard:Next_act");
          boolean wizardAndScreenFound = false;
          String wizard = "";
          String screen = "";

          Map<String, String[]> paramMap = httpRequest.getParameterMap();
          for (String key : paramMap.keySet()) {
            if (wizardAndScreenCheckNeeded && !wizardAndScreenFound) {
              if (key.contains("Wizard:") && key.contains("Screen:")) {
                String[] keyParts = key.split(":");
                boolean wizardFound = false;
                boolean screenFound = false;
                for (String splitKey : keyParts) {
                  if (splitKey.endsWith("Wizard")) {
                    wizard = splitKey.trim();
                    wizardFound = true;
                  }
                  if (splitKey.endsWith("Screen")) {
                    screen = splitKey.trim();
                    screenFound = true;
                  }
                  if (wizardFound && screenFound) {
                    wizardAndScreenFound = true;
                    return "WizardAndScreenName:" + wizard + ":" + screen;
                  }
                }
              }
            }
          }
          return "eventSource:" + eventSource;
        }
      }
      return "guidewire/javax/servlet/Servlet/Service";
    }

    @Advice.OnMethodExit(onThrowable = Throwable.class, suppress = Throwable.class)
    public static void onExit(
        @Advice.Argument(0) ServletRequest request,
        @Advice.Argument(1) ServletResponse response,
        @Advice.Thrown Throwable throwable,
        @Advice.Enter Scope scope,
        @Advice.Local("otelSpan") Span span) {

      if (throwable != null) {
        span.setStatus(StatusCode.ERROR, "Exception in Service Method");
      }
      HttpServletRequest httpRequest = (HttpServletRequest) request;
      logger.log(
          FINE,
          "GUIDEWIRE - browser character encoding after GW: {0}",
          httpRequest.getCharacterEncoding());
      scope.close();
      span.end();
    }
  }
}
