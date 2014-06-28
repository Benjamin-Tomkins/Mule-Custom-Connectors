
package com.kss.sms.adapters;

import com.kss.sms.BulkSMSConnector;
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
 * A {@code BulkSMSConnectorConnectionManager} is a wrapper around {@link BulkSMSConnector } that adds connection management capabilities to the pojo.
 * 
 */
public class BulkSMSConnectorConnectionManager
    implements Capabilities, ConnectionManager<BulkSMSConnectorConnectionManager.ConnectionKey, BulkSMSConnectorLifecycleAdapter> , MuleContextAware, Initialisable
{

    private String Uname;
    private String URL;
    private String Pass;
    private static Logger logger = LoggerFactory.getLogger(BulkSMSConnectorConnectionManager.class);
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
     * Sets Uname
     * 
     * @param value Value to set
     */
    public void setUname(String value) {
        this.Uname = value;
    }

    /**
     * Retrieves Uname
     * 
     */
    public String getUname() {
        return this.Uname;
    }

    /**
     * Sets URL
     * 
     * @param value Value to set
     */
    public void setURL(String value) {
        this.URL = value;
    }

    /**
     * Retrieves URL
     * 
     */
    public String getURL() {
        return this.URL;
    }

    /**
     * Sets Pass
     * 
     * @param value Value to set
     */
    public void setPass(String value) {
        this.Pass = value;
    }

    /**
     * Retrieves Pass
     * 
     */
    public String getPass() {
        return this.Pass;
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
        connectionPool = new GenericKeyedObjectPool(new BulkSMSConnectorConnectionManager.ConnectionFactory(this), config);
    }

    public BulkSMSConnectorLifecycleAdapter acquireConnection(BulkSMSConnectorConnectionManager.ConnectionKey key)
        throws Exception
    {
        return ((BulkSMSConnectorLifecycleAdapter) connectionPool.borrowObject(key));
    }

    public void releaseConnection(BulkSMSConnectorConnectionManager.ConnectionKey key, BulkSMSConnectorLifecycleAdapter connection)
        throws Exception
    {
        connectionPool.returnObject(key, connection);
    }

    public void destroyConnection(BulkSMSConnectorConnectionManager.ConnectionKey key, BulkSMSConnectorLifecycleAdapter connection)
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

        private BulkSMSConnectorConnectionManager connectionManager;

        public ConnectionFactory(BulkSMSConnectorConnectionManager connectionManager) {
            this.connectionManager = connectionManager;
        }

        public Object makeObject(Object key)
            throws Exception
        {
            if (!(key instanceof BulkSMSConnectorConnectionManager.ConnectionKey)) {
                throw new RuntimeException("Invalid key type");
            }
            BulkSMSConnectorLifecycleAdapter connector = new BulkSMSConnectorLifecycleAdapter();
            connector.setUname(connectionManager.getUname());
            connector.setURL(connectionManager.getURL());
            connector.setPass(connectionManager.getPass());
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
            if (!(key instanceof BulkSMSConnectorConnectionManager.ConnectionKey)) {
                throw new RuntimeException("Invalid key type");
            }
            if (!(obj instanceof BulkSMSConnectorLifecycleAdapter)) {
                throw new RuntimeException("Invalid connector type");
            }
            try {
                ((BulkSMSConnectorLifecycleAdapter) obj).disconnect();
            } catch (Exception e) {
                throw e;
            } finally {
                if (((BulkSMSConnectorLifecycleAdapter) obj) instanceof Stoppable) {
                    ((BulkSMSConnectorLifecycleAdapter) obj).stop();
                }
                if (((BulkSMSConnectorLifecycleAdapter) obj) instanceof Disposable) {
                    ((BulkSMSConnectorLifecycleAdapter) obj).dispose();
                }
            }
        }

        public boolean validateObject(Object key, Object obj) {
            if (!(obj instanceof BulkSMSConnectorLifecycleAdapter)) {
                throw new RuntimeException("Invalid connector type");
            }
            try {
                return ((BulkSMSConnectorLifecycleAdapter) obj).isConnected();
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                return false;
            }
        }

        public void activateObject(Object key, Object obj)
            throws Exception
        {
            if (!(key instanceof BulkSMSConnectorConnectionManager.ConnectionKey)) {
                throw new RuntimeException("Invalid key type");
            }
            if (!(obj instanceof BulkSMSConnectorLifecycleAdapter)) {
                throw new RuntimeException("Invalid connector type");
            }
            try {
                if (!((BulkSMSConnectorLifecycleAdapter) obj).isConnected()) {
                    ((BulkSMSConnectorLifecycleAdapter) obj).connect();
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
            return (obj instanceof BulkSMSConnectorConnectionManager.ConnectionKey);
        }

    }

}
