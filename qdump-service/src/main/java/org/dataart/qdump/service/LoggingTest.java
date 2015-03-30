package org.dataart.qdump.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by artemvlasov on 30/03/15.
 */
public class LoggingTest {
    private static final Logger LOG = LoggerFactory.getLogger(org.dataart.qdump.service.LoggingTest.class);

    public static void main(String[] args) {
        LOG.trace("Hello World!");
        LOG.debug("How are you today?");
        LOG.info("I am fine.");
        LOG.warn("I love programming.");
        LOG.error("I am programming.");
    }
}
