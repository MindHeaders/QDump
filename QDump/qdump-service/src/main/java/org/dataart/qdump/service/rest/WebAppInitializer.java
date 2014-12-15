package org.dataart.qdump.service.rest;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.dataart.qdump.persistence.config.AppConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

@Order(value = Ordered.HIGHEST_PRECEDENCE)
public class WebAppInitializer implements WebApplicationInitializer {

	@Override
	public void onStartup(ServletContext servletContext)
			throws ServletException {
		servletContext.addListener(ContextLoaderListener.class);
		servletContext.setInitParameter(
				ContextLoaderListener.CONTEXT_CLASS_PARAM,
				AnnotationConfigWebApplicationContext.class.getName());
		servletContext.setInitParameter(
				ContextLoaderListener.CONFIG_LOCATION_PARAM,
				AppConfig.class.getName());
		ServletRegistration.Dynamic dynamic = servletContext.addServlet("app",
				ServletContainer.class.getName());
		dynamic.addMapping("/*");
		dynamic.setLoadOnStartup(1);
		dynamic.setInitParameter("javax.ws.rs.Application", MyApplication.class.getName());
	}

	
	/*@Override
	public void onStartup(ServletContext servletContext)
			throws ServletException {
		servletContext.setInitParameter("contextConfigLocation", "");
		AnnotationConfigWebApplicationContext root = new AnnotationConfigWebApplicationContext();
		root.register(AppConfig.class);
		registerListeners(servletContext, root);
		registerServlets(servletContext);
		root.refresh();
	}

	private void registerListeners(ServletContext servletContext,
			AnnotationConfigWebApplicationContext rootContext) {
		servletContext.addListener(ResteasyBootstrap.class);
		servletContext.addListener(new ContextLoaderListener(rootContext) {
			private SpringContextLoaderSupport springContextLoaderSupport = new SpringContextLoaderSupport();

			@Override
			protected void customizeContext(ServletContext servletContext,
					ConfigurableWebApplicationContext context) {
				super.customizeContext(servletContext, context);
				this.springContextLoaderSupport.customizeContext(
						servletContext, context);
			}

		});
	}

	private void registerServlets(ServletContext servletContext) {
		ServletRegistration.Dynamic restEasyDispatcher = servletContext
				.addServlet("RestEasyHttpServletDispatcher",
						new HttpServletDispatcher());
		restEasyDispatcher.setInitParameter("javax.ws.rs.Application",
				MyApplication.class.getName());
		restEasyDispatcher.addMapping("/*");
		restEasyDispatcher.setLoadOnStartup(1);
	}*/
}
