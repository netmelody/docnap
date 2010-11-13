package org.netmelody.docnap.gwt.server;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.picocontainer.MutablePicoContainer;
import org.picocontainer.PicoBuilder;

public final class Bootstrap implements ServletContextListener {

    private final org.netmelody.docnap.core.published.Bootstrap coreBootStrap = new org.netmelody.docnap.core.published.Bootstrap();
    private MutablePicoContainer container;
    
    public void contextInitialized(ServletContextEvent event) {
        this.container = new PicoBuilder(this.coreBootStrap.start()).withLifecycle().withCaching().build();
        
        this.container.addComponent(Archivist.class);
        this.container.start();
        event.getServletContext().setAttribute(Archivist.NAME, this.container.getComponent(Archivist.class));
    }

    public void contextDestroyed(ServletContextEvent event) {
        this.container.stop();
        this.container.dispose();
        this.coreBootStrap.stop();
    }
}