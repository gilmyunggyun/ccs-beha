package com.hkmc.filter.event;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

import com.hkmc.filter.FilterProperties;
import com.hkmc.filter.wrapper.BufferedResponseWrapper;
import com.hkmc.filter.wrapper.LOGLEVEL;
import com.hkmc.filter.wrapper.MultiReadHttpServletRequest;
import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoggerEventHandler {

  private final String reqLog = "Logging Request [TID : {}] [XTID : {}] Method : {} URI : {} Headers : {} Body : {} ";

  private final String resLog = "Logging Response [TID : {}] [XTID : {}] Status : {} Response Body : {} ";

  private final FilterProperties properties;

  @EventListener
  public void onRequestEvent(RequestLogEvent event) {

    var multiReadRequest = new MultiReadHttpServletRequest(event.getRequest());
    var headers = headers(multiReadRequest);

    var tid = headers.getOrEmpty("TID").stream().findFirst().orElse("");
    var xtid = headers.getOrEmpty("XTID").stream().findFirst().orElse("");

    String body = "";

    if (headers.get(CONTENT_TYPE) != null) {
      try {
        body = event.getRequest().getReader().lines().collect(Collectors.joining());
      } catch (IOException e) {
        body = "Error occured while reading request body";
      }
    }

    var fullPath = UriComponentsBuilder.fromUriString(event.getRequest().getRequestURL().toString())
            .build().toUriString();

    log(reqLog, tid, xtid, event.getRequest().getMethod(), fullPath, headers(event.getRequest()), body);
  }

  @EventListener
  public void onResponseEvent(ResponseLogEvent event) {
    var multiReadRequest = new MultiReadHttpServletRequest(event.getRequest());
    var headers = headers(multiReadRequest);

    var tid = headers.getOrEmpty("TID").stream().findFirst().orElse("");
    var xtid = headers.getOrEmpty("XTID").stream().findFirst().orElse("");

    var response = (BufferedResponseWrapper) event.getResponse();

    log(resLog, tid, xtid, response.getStatus(), response.getContent());
  }

  private void log(String logString, Object... obj) {
    switch (properties.getLoglevel()) {
      case LOGLEVEL.INFO:
        log.info(logString, obj);
        break;
      case LOGLEVEL.DEBUG:
        log.debug(logString, obj);
        break;
      case LOGLEVEL.WARN:
        log.warn(logString, obj);
        break;
      case LOGLEVEL.ERROR:
        log.error(logString, obj);
        break;
      case LOGLEVEL.TRACE:
        log.trace(logString, obj);
        break;
      default:
        log.info(logString, obj);
    }
  }

  private HttpHeaders headers(HttpServletRequest req) {
    Enumeration<String> headerNames = req.getHeaderNames();
    HttpHeaders headers = new HttpHeaders();
    while (headerNames.hasMoreElements()) {
      String name = headerNames.nextElement();
      headers.put(name, Collections.list(req.getHeaders(name)));
    }
    return headers;
  }

}