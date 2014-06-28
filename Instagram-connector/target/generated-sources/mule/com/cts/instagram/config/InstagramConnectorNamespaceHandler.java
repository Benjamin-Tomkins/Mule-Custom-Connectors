
package com.cts.instagram.config;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;


/**
 * Registers bean definitions parsers for handling elements in <code>http://www.mulesoft.org/schema/mule/instagram</code>.
 * 
 */
public class InstagramConnectorNamespaceHandler
    extends NamespaceHandlerSupport
{


    /**
     * Invoked by the {@link DefaultBeanDefinitionDocumentReader} after construction but before any custom elements are parsed. 
     * @see NamespaceHandlerSupport#registerBeanDefinitionParser(String, BeanDefinitionParser)
     * 
     */
    public void init() {
        registerBeanDefinitionParser("config", new InstagramConnectorConfigDefinitionParser());
        registerBeanDefinitionParser("my-processor", new MyProcessorDefinitionParser());
        registerBeanDefinitionParser("start-instagram", new StartInstagramDefinitionParser());
    }

}
