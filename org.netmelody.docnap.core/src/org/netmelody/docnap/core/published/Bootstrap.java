package org.netmelody.docnap.core.published;

import org.netmelody.docnap.core.repository.DocnapStore;
import org.netmelody.docnap.core.repository.DocnapStoreConnection;
import org.netmelody.docnap.core.repository.DocumentRepository;
import org.netmelody.docnap.core.repository.TagRepository;
import org.netmelody.docnap.core.schema.DatabaseUpdater;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.PicoBuilder;
import org.picocontainer.PicoContainer;

public class Bootstrap {  

    private MutablePicoContainer internalContainer = new PicoBuilder().withLifecycle().withCaching().build();
    private MutablePicoContainer publicContainer = new PicoBuilder(this.internalContainer).withLifecycle().withCaching().build();
    
    public Bootstrap() {
        this.internalContainer.addComponent(DocnapStoreConnection.class);
        this.internalContainer.addComponent(DatabaseUpdater.class);
        
        this.publicContainer.addComponent(DocnapStore.class);
        this.publicContainer.addComponent(DocumentRepository.class);
        this.publicContainer.addComponent(TagRepository.class);
    }

    public PicoContainer start() {
        this.internalContainer.start();
        this.publicContainer.start();
        return this.publicContainer;
    }

    public void stop() {
        this.publicContainer.stop();
        this.publicContainer.dispose();
        
        this.internalContainer.stop();
        this.internalContainer.dispose();
    }
}