
package com.kss.social.adapters;

import com.kss.social.FoursquareConnector;
import org.apache.commons.pool.KeyedPoolableObjectFactory;
import org.apache.commons.pool.impl.GenericKeyedObjectPool;
import org.mule.api.Capabilities;
import org.mule.api.Capability;
import org.mule.api.ConnectionManager;
import org.mule.api.MuleContext;
import org.mule.api.construct.FlowConstruct;
import org.mule.api.context.MuleContextAware;
import org.mule.api.lifecycle.Disposable;
import org.mule.api.lifecycle.Initialisable;
import org.mule.api.lifecycle.Startable;
import org.mule.api.lifecycle.Stoppable;
import org.mule.config.PoolingProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * A {@code FoursquareConnectorConnectionManager} is a wrapper around {@link FoursquareConnector } that adds connection management capabilities to the pojo.
 * 
 */
public class FoursquareConnectorConnectionManager
    implements Capabilities, ConnectionManager<FoursquareConnectorConnectionManager.ConnectionKey, FoursquareConnectorLifecycleAdapter> , MuleContextAware, Initialisable
{

    private String Client_ID;
    private String Client_Secret;
    private String Callback_URL;
    private static Logger logger = LoggerFactory.getLogger(FoursquareConnectorConnectionManager.class);
    /**
     * Mule Context
     * 
     */
    private MuleContext muleContext;
    /**
     * Flow construct
     * 
     */
    private FlowConstruct flowConstruct;
    /**
     * Connector Pool
     * 
     */
    private GenericKeyedObjectPool connectionPool;
    protected PoolingProfile connectionPoolingProfile;

    /**
     * Sets Client_ID
     * 
     * @param value Value to set
     */
    public void setClient_ID(String value) {
        this.Client_ID = value;
    }

    /**
     * Retrieves Client_ID
     * 
     */
    public String getClient_ID() {
        return this.Client_ID;
    }

    /**
     * Sets Client_Secret
     * 
     * @param value Value to set
     */
    public void setClient_Secret(String value) {
        this.Client_Secret = value;
    }

    /**
     * Retrieves Client_Secret
     * 
     */
    public String getClient_Secret() {
        return this.Client_Secret;
    }

    /**
     * Sets Callback_URL
     * 
     * @param value Value to set
     */
    public void setCallback_URL(String value) {
        this.Callback_URL = value;
    }

    /**
     * Retrieves Callback_URL
     * 
     */
    public String getCallback_URL() {
        return this.Callback_URL;
    }

    /**
     * Sets connectionPoolingProfile
     * 
     * @param value Value to set
     */
    public void setConnectionPoolingProfile(PoolingProfile value) {
        this.connectionPoolingProfile = value;
    }

    /**
     * Retrieves connectionPoolingProfile
     * 
     */
    public PoolingProfile getConnectionPoolingProfile() {
        return this.connectionPoolingProfile;
    }

    /**
     * Sets flow construct
     * 
     * @param flowConstruct Flow construct to set
     */
    public void setFlowConstruct(FlowConstruct flowConstruct) {
        this.flowConstruct = flowConstruct;
    }

    /**
     * Set the Mule context
     * 
     * @param context Mule context to set
     */
    public void setMuleContext(MuleContext context) {
        this.muleContext = context;
    }

    public void initialise() {
        GenericKeyedObjectPool.Config config = new GenericKeyedObjectPool.Config();
        if (connectionPoolingProfile!= null) {
            config.maxIdle = connectionPoolingProfile.getMaxIdle();
            config.maxActive = connectionPoolingProfile.getMaxActive();
            config.maxWait = connectionPoolingProfile.getMaxWait();
            config.whenExhaustedAction = ((byte) connectionPoolingProfile.getExhaustedAction());
        }
        connectionPool = new GenericKeyedObjectPool(new FoursquareConnectorConnectionManager.ConnectionFactory(this), config);
    }

    public FoursquareConnectorLifecycleAdapter acquireConnection(FoursquareConnectorConnectionManager.ConnectionKey key)
        throws Exception
    {
        return ((FoursquareConnectorLifecycleAdapter) connectionPool.borrowObject(key));
    }

    public void releaseConnection(FoursquareConnectorConnectionManager.ConnectionKey key, FoursquareConnectorLifecycleAdapter connection)
        throws Exception
    {
        connectionPool.returnObject(key, connection);
    }

    public void destroyConnection(FoursquareConnectorConnectionManager.ConnectionKey key, FoursquareConnectorLifecycleAdapter connection)
        throws Exception
    {
        connectionPool.invalidateObject(key, connection);
    }

    /**
     * Returns true if this module implements such capability
     * 
     */
    public boolean isCapableOf(Capability capability) {
        if (capability == Capability.LIFECYCLE_CAPABLE) {
            return true;
        }
        if (capability == Capability.CONNECTION_MANAGEMENT_CAPABLE) {
            return true;
        }
        return false;
    }

    private static class ConnectionFactory
        implements KeyedPoolableObjectFactory
    {

        private FoursquareConnectorConnectionManager connectionManager;

        public ConnectionFactory(FoursquareConnectorConnectionManager connectionManager) {
            this.connectionManager = connectionManager;
        }

        public Object makeObject(Object key)
            throws Exception
        {
            if (!(key instanceof FoursquareConnectorConnectionManager.ConnectionKey)) {
                throw new RuntimeException("Invalid key type");
            }
            FoursquareConnectorLifecycleAdapter connector = new FoursquareConnectorLifecycleAdapter();
            connector.setClient_ID(connectionManager.getClient_ID());
            connector.setClient_Secret(connectionManager.getClient_Secret());
            connector.setCallback_URL(connectionManager.getCallback_URL());
            if (connector instanceof Initialisable) {
                connector.initialise();
            }
            if (connector instanceof Startable) {
                connector.start();
            }
            return connector;
        }

        public void destroyObject(Object key, Object obj)
            throws Exception
        {
            if (!(key instanceof FoursquareConnectorConnectionManager.ConnectionKey)) {
                throw new RuntimeException("Invalid key type");
            }
            if (!(obj instanceof FoursquareConnectorLifecycleAdapter)) {
                throw new RuntimeException("Invalid connector type");
            }
            try {
                ((FoursquareConnectorLifecycleAdapter) obj).disconnect();
            } catch (Exception e) {
                throw e;
            } finally {
                if (((FoursquareConnectorLifecycleAdapter) obj) instanceof Stoppable) {
                    ((FoursquareConnectorLifecycleAdapter) obj).stop();
                }
                if (((FoursquareConnectorLifecycleAdapter) obj) instanceof Disposable) {
                    ((FoursquareConnectorLifecycleAdapter) obj).dispose();
                }
            }
        }

        public boolean validateObject(Object key, Object obj) {
            if (!(obj instanceof FoursquareConnectorLifecycleAdapter)) {
                throw new RuntimeException("Invalid connector type");
            }
            try {
                return ((FoursquareConnectorLifecycleAdapter) obj).isConnected();
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                return false;
            }
        }

        public void activateObject(Object key, Object obj)
            throws Exception
        {
            if (!(key instanceof FoursquareConnectorConnectionManager.ConnectionKey)) {
                throw new RuntimeException("Invalid key type");
            }
            if (!(obj instanceof FoursquareConnectorLifecycleAdapter)) {
                throw new RuntimeException("Invalid connector type");
            }
            try {
                if (!((FoursquareConnectorLifecycleAdapter) obj).isConnected()) {
                    ((FoursquareConnectorLifecycleAdapter) obj).connect();
                }
            } catch (Exception e) {
                throw e;
            }
        }

        public void passivateObject(Object key, Object obj)
            throws Exception
        {
        }

    }


    /**
     * A tuple of connection parameters
     * 
     */
    public static class ConnectionKey {


        public ConnectionKey() {
        }

        public int hashCode() {
            int hash = 1;
            return hash;
        }

        public boolean equals(Object obj) {
            return (obj instanceof FoursquareConnectorConnectionManager.ConnectionKey);
        }

    }

}
