
package com.kss.sms.config;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;


/**
 * Registers bean definitions parsers for handling elements in <code>http://www.mulesoft.org/schema/mule/bulksms</code>.
 * 
 */
public class BulkSMSConnectorNamespaceHandler
    extends NamespaceHandlerSupport
{


    /**
     * Invoked by the {@link DefaultBeanDefinitionDocumentReader} after construction but before any custom elements are parsed. 
     * @see NamespaceHandlerSupport#registerBeanDefinitionParser(String, BeanDefinitionParser)
     * 
     */
    public void init() {
        registerBeanDefinitionParser("config", new BulkSMSConnectorConfigDefinitionParser());
        registerBeanDefinitionParser("send-message", new SendMessageDefinitionParser());
    }

}
