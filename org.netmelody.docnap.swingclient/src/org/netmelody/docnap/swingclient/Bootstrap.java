package org.netmelody.docnap.swingclient;

import org.jdesktop.application.ApplicationContext;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.PicoBuilder;
import org.picocontainer.PicoContainer;

public class Bootstrap {

    private final org.netmelody.docnap.core.published.Bootstrap coreBootStrap = new org.netmelody.docnap.core.published.Bootstrap();
    private MutablePicoContainer container;
    
    public PicoContainer start(ApplicationContext applicationContext) {
        this.container = new PicoBuilder(this.coreBootStrap.start()).withLifecycle().withCaching().build();;
        this.container.addComponent(applicationContext);
        this.container.start();
                
        return this.container;
    }  

    public void stop() {
        this.container.stop();
        this.container.dispose();
        
        this.coreBootStrap.stop();
    }  
}
