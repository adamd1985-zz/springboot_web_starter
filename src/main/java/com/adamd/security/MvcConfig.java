package com.adamd.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Views and resolutions mapping configurations.
 * <p/>
 * Responsibilities:
 * <ul>
 * <li>Forward to index pages.</li>
 * </ul>
 * <p/>
 * <b>NB: </b>Do not use: EnableWebMvc, it will disable all of springboots
 * configs.
 * 
 * @author adamd
 * @version 1
 */
@Configuration
public class MvcConfig extends WebMvcConfigurerAdapter {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("forward:/static/index.html");
    }
}
