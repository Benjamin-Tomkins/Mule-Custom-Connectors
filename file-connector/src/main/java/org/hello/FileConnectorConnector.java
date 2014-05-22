/**
 * This file was automatically generated by the Mule Development Kit
 */
package org.hello;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.mule.api.annotations.Connector;
import org.mule.api.annotations.Connect;
import org.mule.api.annotations.ValidateConnection;
import org.mule.api.annotations.ConnectionIdentifier;
import org.mule.api.annotations.Disconnect;
import org.mule.api.annotations.param.ConnectionKey;
import org.mule.api.ConnectionException;
import org.mule.api.annotations.Configurable;
import org.mule.api.annotations.Processor;

/**
 * Cloud Connector
 *
 * @author Santhosh, Kss.
 */
@Connector(name="fileconnector", schemaVersion="1.0-SNAPSHOT")
public class FileConnectorConnector
{
    /**
     * Configurable
     */
    @Configurable
    private String myProperty;

    /**
     * Set property
     *
     * @param myProperty My property
     */
    public void setMyProperty(String myProperty)
    {
        this.myProperty = myProperty;
    }

    /**
     * Connect
     *
     * @param username A username
     * @param password A password
     * @throws ConnectionException
     */
    @Connect
    public void connect(@ConnectionKey String username, String password)
        throws ConnectionException {
        /*
         * CODE FOR ESTABLISHING A CONNECTION GOES IN HERE
         */
    }

    /**
     * Disconnect
     */
    @Disconnect
    public void disconnect() {
        /*
         * CODE FOR CLOSING A CONNECTION GOES IN HERE
         */
    }

    /**
     * Are we connected
     */
    @ValidateConnection
    public boolean isConnected() {
        return true;
    }

    /**
     * Are we connected
     */
    @ConnectionIdentifier
    public String connectionId() {
        return "001";
    }

    /**
     * Custom processor
     *
     * {@sample.xml ../../../doc/FileConnector-connector.xml.sample fileconnector:my-processor}
     *
     * @param content Content to be processed
     * @return Some string
     */
    @Processor
    public String myProcessor(String content)
    {
        /*
         * MESSAGE PROCESSOR CODE GOES HERE
         */

        return content;
    }

/**
 * takes file path.
 *
 * {@sample.xml ../../../doc/FileConnector-connector.xml.sample hello:say-hello}
 *
 * @param path The path of file.
 * @return file Input Stream.
 * @throws FileNotFoundException 
 */
@Processor
public FileInputStream GetFile(String path) throws FileNotFoundException
{
	File file = new File(path);
	FileInputStream is;
	is=new FileInputStream(file);
	return is;
}


/**
 * Says hello to someone.
 *
 * {@sample.xml ../../../doc/FileConnector-connector.xml.sample hello:say-hello}
 *
 * @param name The name to say hello to.
 * @return The hello message.
 */
@Processor
public String sayHello(String name)
{
    return "Hello " + name;
}
}
