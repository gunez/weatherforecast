package com.finleap.weatherforecast.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class CacheCleanerConfig {
    private static final Logger LOG = LoggerFactory.getLogger(CacheCleanerConfig.class);

    @Scheduled(cron = "0 0 0 * * *")
    @CacheEvict(value = "temperatureSummary", allEntries = true)
    public void cleanCache() {
        LOG.info("Cache cleaned");
    }
}
