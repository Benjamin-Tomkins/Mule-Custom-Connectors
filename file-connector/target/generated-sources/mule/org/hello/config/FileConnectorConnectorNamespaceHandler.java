
package org.hello.config;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;


/**
 * Registers bean definitions parsers for handling elements in <code>http://www.mulesoft.org/schema/mule/fileconnector</code>.
 * 
 */
public class FileConnectorConnectorNamespaceHandler
    extends NamespaceHandlerSupport
{


    /**
     * Invoked by the {@link DefaultBeanDefinitionDocumentReader} after construction but before any custom elements are parsed. 
     * @see NamespaceHandlerSupport#registerBeanDefinitionParser(String, BeanDefinitionParser)
     * 
     */
    public void init() {
        registerBeanDefinitionParser("config", new FileConnectorConnectorConfigDefinitionParser());
        registerBeanDefinitionParser("my-processor", new MyProcessorDefinitionParser());
        registerBeanDefinitionParser("get-file", new GetFileDefinitionParser());
        registerBeanDefinitionParser("say-hello", new SayHelloDefinitionParser());
    }

}
