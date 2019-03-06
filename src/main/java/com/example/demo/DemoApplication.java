package com.example.demo;

import io.opencensus.contrib.http.servlet.OcHttpServletFilter;
import io.opencensus.exporter.trace.zipkin.ZipkinTraceExporter;
import io.opencensus.trace.Tracing;
import io.opencensus.trace.config.TraceConfig;
import io.opencensus.trace.config.TraceParams;
import io.opencensus.trace.samplers.Samplers;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;

import javax.servlet.Filter;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        ZipkinTraceExporter.createAndRegister(
            "http://localhost:9411/api/v2/spans", "DemoApplication");
        TraceConfig traceConfig = Tracing.getTraceConfig();
        TraceParams activeTraceParams = traceConfig.getActiveTraceParams();
        traceConfig.updateActiveTraceParams(
            activeTraceParams.toBuilder().setSampler(
                Samplers.alwaysSample()).build());
        SpringApplication.run(DemoApplication.class, args);
    }

    @Bean
    public FilterRegistrationBean ocHttpFilter() {
        FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new OcHttpServletFilter());
        registration.addUrlPatterns("/*");
        registration.setName("OcHttpServletFilter");
        registration.setOrder(1);
        return registration;
    }

    @Bean
    public ServletListenerRegistrationBean<ServletContextListener> servletListener() {
        ServletListenerRegistrationBean<ServletContextListener> srb
            = new ServletListenerRegistrationBean<>();
        srb.setListener(new ServletContextListener() {
            @Override
            public void contextDestroyed(ServletContextEvent sce) {
                Tracing.getExportComponent().shutdown();
            }
        });
        return srb;
    }
}
