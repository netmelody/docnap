package org.netmelody.docnap.core.published;

import org.picocontainer.DefaultPicoContainer;

public class Bootstrap {  

	private DefaultPicoContainer pico = new DefaultPicoContainer();

	public void start() {  
		/*  pico.addComponent(A.class), pico.addComponent(B.class) */  
		/*  etc. */  

		this.pico.start();  
		//enter some sort of event loop.  


		//Event loop broken.  

		this.pico.stop();   
	}  


	public void stop() {  
		this.pico.stop();  
		this.pico.dispose();
	}  
}