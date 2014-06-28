
package com.kss.social.config;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;


/**
 * Registers bean definitions parsers for handling elements in <code>http://www.mulesoft.org/schema/mule/foursquare</code>.
 * 
 */
public class FoursquareConnectorNamespaceHandler
    extends NamespaceHandlerSupport
{


    /**
     * Invoked by the {@link DefaultBeanDefinitionDocumentReader} after construction but before any custom elements are parsed. 
     * @see NamespaceHandlerSupport#registerBeanDefinitionParser(String, BeanDefinitionParser)
     * 
     */
    public void init() {
        registerBeanDefinitionParser("config", new FoursquareConnectorConfigDefinitionParser());
        registerBeanDefinitionParser("search-venues", new SearchVenuesDefinitionParser());
    }

}
