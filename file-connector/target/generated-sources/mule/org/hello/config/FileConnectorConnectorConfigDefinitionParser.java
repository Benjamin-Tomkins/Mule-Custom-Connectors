
package org.hello.config;

import org.apache.commons.lang.StringUtils;
import org.hello.adapters.FileConnectorConnectorConnectionManager;
import org.mule.api.lifecycle.Disposable;
import org.mule.api.lifecycle.Initialisable;
import org.mule.config.PoolingProfile;
import org.mule.config.spring.MuleHierarchicalBeanDefinitionParserDelegate;
import org.mule.config.spring.parsers.generic.AutoIdUtils;
import org.mule.util.TemplateParser;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

public class FileConnectorConnectorConfigDefinitionParser
    implements BeanDefinitionParser
{

    /**
     * Mule Pattern Info
     * 
     */
    private TemplateParser.PatternInfo patternInfo;

    public FileConnectorConnectorConfigDefinitionParser() {
        patternInfo = TemplateParser.createMuleStyleParser().getStyle();
    }

    public BeanDefinition parse(Element element, ParserContext parserContent) {
        String name = element.getAttribute("name");
        if ((name == null)||StringUtils.isBlank(name)) {
            element.setAttribute("name", AutoIdUtils.getUniqueName(element, "mule-bean"));
        }
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(FileConnectorConnectorConnectionManager.class.getName());
        if (Initialisable.class.isAssignableFrom(FileConnectorConnectorConnectionManager.class)) {
            builder.setInitMethodName(Initialisable.PHASE_NAME);
        }
        if (Disposable.class.isAssignableFrom(FileConnectorConnectorConnectionManager.class)) {
            builder.setDestroyMethodName(Disposable.PHASE_NAME);
        }
        if ((element.getAttribute("myProperty")!= null)&&(!StringUtils.isBlank(element.getAttribute("myProperty")))) {
            builder.addPropertyValue("myProperty", element.getAttribute("myProperty"));
        }
        if ((element.getAttribute("username")!= null)&&(!StringUtils.isBlank(element.getAttribute("username")))) {
            builder.addPropertyValue("username", element.getAttribute("username"));
        }
        if ((element.getAttribute("password")!= null)&&(!StringUtils.isBlank(element.getAttribute("password")))) {
            builder.addPropertyValue("password", element.getAttribute("password"));
        }
        BeanDefinitionBuilder connectionPoolingProfileBuilder = BeanDefinitionBuilder.rootBeanDefinition(PoolingProfile.class.getName());
        Element connectionPoolingProfileElement = DomUtils.getChildElementByTagName(element, "connection-pooling-profile");
        if (connectionPoolingProfileElement!= null) {
            if ((connectionPoolingProfileElement.getAttribute("maxActive")!= null)&&(!StringUtils.isBlank(connectionPoolingProfileElement.getAttribute("maxActive")))) {
                connectionPoolingProfileBuilder.addPropertyValue("maxActive", connectionPoolingProfileElement.getAttribute("maxActive"));
            }
            if ((connectionPoolingProfileElement.getAttribute("maxIdle")!= null)&&(!StringUtils.isBlank(connectionPoolingProfileElement.getAttribute("maxIdle")))) {
                connectionPoolingProfileBuilder.addPropertyValue("maxIdle", connectionPoolingProfileElement.getAttribute("maxIdle"));
            }
            if ((connectionPoolingProfileElement.getAttribute("maxWait")!= null)&&(!StringUtils.isBlank(connectionPoolingProfileElement.getAttribute("maxWait")))) {
                connectionPoolingProfileBuilder.addPropertyValue("maxWait", connectionPoolingProfileElement.getAttribute("maxWait"));
            }
            if ((connectionPoolingProfileElement.getAttribute("exhaustedAction")!= null)&&(!StringUtils.isBlank(connectionPoolingProfileElement.getAttribute("exhaustedAction")))) {
                connectionPoolingProfileBuilder.addPropertyValue("exhaustedAction", PoolingProfile.POOL_EXHAUSTED_ACTIONS.get(connectionPoolingProfileElement.getAttribute("exhaustedAction")));
            }
            if ((connectionPoolingProfileElement.getAttribute("exhaustedAction")!= null)&&(!StringUtils.isBlank(connectionPoolingProfileElement.getAttribute("exhaustedAction")))) {
                connectionPoolingProfileBuilder.addPropertyValue("initialisationPolicy", PoolingProfile.POOL_INITIALISATION_POLICIES.get(connectionPoolingProfileElement.getAttribute("initialisationPolicy")));
            }
            builder.addPropertyValue("connectionPoolingProfile", connectionPoolingProfileBuilder.getBeanDefinition());
        }
        BeanDefinition definition = builder.getBeanDefinition();
        definition.setAttribute(MuleHierarchicalBeanDefinitionParserDelegate.MULE_NO_RECURSE, Boolean.TRUE);
        return definition;
    }

}
