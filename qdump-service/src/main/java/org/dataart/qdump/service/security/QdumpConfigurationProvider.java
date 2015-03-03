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
    private static final String BASE_URI = new String("login/index.html");
    @Override
    public Configuration getConfiguration(ServletContext servletContext) {
        return ConfigurationBuilder.begin()
                .addRule(Join.path("/").to(BASE_URI))
                .addRule(Join.path("/reg").to(BASE_URI))
                .addRule(Join.path("/auth").to(BASE_URI))
                .addRule(Join.path("/user-list").to(BASE_URI))
                .addRule(Join.path("/error").to(BASE_URI))
                .addRule(Join.path("/success").to(BASE_URI))
                .addRule(Join.path("/account").to(BASE_URI))
                .addRule(Join.path("/questionnaires").to(BASE_URI))
                .addRule(Join.path("/account/personal").to("/account").withChaining())
                .addRule(Join.path("/account/change").to("/account").withChaining())
                .addRule(Join.path("/account/completed").to("/account").withChaining())
                .addRule(Join.path("/questionnaires/create").to("/questionnaires").withChaining());
    }

    @Override
    public int priority() {
        return 10;
    }
}
