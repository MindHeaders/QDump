package org.dataart.qdump.service.security;

import org.ocpsoft.rewrite.annotation.RewriteConfiguration;
import org.ocpsoft.rewrite.config.Configuration;
import org.ocpsoft.rewrite.config.ConfigurationBuilder;
import org.ocpsoft.rewrite.servlet.config.HttpConfigurationProvider;
import org.ocpsoft.rewrite.servlet.config.rule.Join;

import javax.servlet.ServletContext;

/**
 * Created by artemvlasov on 01/02/15.
 */
@RewriteConfiguration
public class QdumpConfigurationProvider extends HttpConfigurationProvider {
    @Override
    public Configuration getConfiguration(ServletContext servletContext) {
        return ConfigurationBuilder.begin()
                .addRule(Join.path("/").to("login/index.html"))
                .addRule(Join.path("/reg").to("login/index.html"))
                .addRule(Join.path("/auth").to("login/index.html"))
                .addRule(Join.path("/user-list").to("login/index.html"));
    }

    @Override
    public int priority() {
        return 10;
    }
}
