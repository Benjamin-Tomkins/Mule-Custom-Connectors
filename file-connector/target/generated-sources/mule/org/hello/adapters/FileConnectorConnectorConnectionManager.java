
package org.hello.adapters;

import org.apache.commons.pool.KeyedPoolableObjectFactory;
import org.apache.commons.pool.impl.GenericKeyedObjectPool;
import org.hello.FileConnectorConnector;
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
 * A {@code FileConnectorConnectorConnectionManager} is a wrapper around {@link FileConnectorConnector } that adds connection management capabilities to the pojo.
 * 
 */
public class FileConnectorConnectorConnectionManager
    implements Capabilities, ConnectionManager<FileConnectorConnectorConnectionManager.ConnectionKey, FileConnectorConnectorLifecycleAdapter> , MuleContextAware, Initialisable
{

    /**
     * 
     */
    private String username;
    /**
     * 
     */
    private String password;
    private String myProperty;
    private static Logger logger = LoggerFactory.getLogger(FileConnectorConnectorConnectionManager.class);
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
     * Sets myProperty
     * 
     * @param value Value to set
     */
    public void setMyProperty(String value) {
        this.myProperty = value;
    }

    /**
     * Retrieves myProperty
     * 
     */
    public String getMyProperty() {
        return this.myProperty;
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
     * Sets username
     * 
     * @param value Value to set
     */
    public void setUsername(String value) {
        this.username = value;
    }

    /**
     * Retrieves username
     * 
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * Sets password
     * 
     * @param value Value to set
     */
    public void setPassword(String value) {
        this.password = value;
    }

    /**
     * Retrieves password
     * 
     */
    public String getPassword() {
        return this.password;
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
        connectionPool = new GenericKeyedObjectPool(new FileConnectorConnectorConnectionManager.ConnectionFactory(this), config);
    }

    public FileConnectorConnectorLifecycleAdapter acquireConnection(FileConnectorConnectorConnectionManager.ConnectionKey key)
        throws Exception
    {
        return ((FileConnectorConnectorLifecycleAdapter) connectionPool.borrowObject(key));
    }

    public void releaseConnection(FileConnectorConnectorConnectionManager.ConnectionKey key, FileConnectorConnectorLifecycleAdapter connection)
        throws Exception
    {
        connectionPool.returnObject(key, connection);
    }

    public void destroyConnection(FileConnectorConnectorConnectionManager.ConnectionKey key, FileConnectorConnectorLifecycleAdapter connection)
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

        private FileConnectorConnectorConnectionManager connectionManager;

        public ConnectionFactory(FileConnectorConnectorConnectionManager connectionManager) {
            this.connectionManager = connectionManager;
        }

        public Object makeObject(Object key)
            throws Exception
        {
            if (!(key instanceof FileConnectorConnectorConnectionManager.ConnectionKey)) {
                throw new RuntimeException("Invalid key type");
            }
            FileConnectorConnectorLifecycleAdapter connector = new FileConnectorConnectorLifecycleAdapter();
            connector.setMyProperty(connectionManager.getMyProperty());
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
            if (!(key instanceof FileConnectorConnectorConnectionManager.ConnectionKey)) {
                throw new RuntimeException("Invalid key type");
            }
            if (!(obj instanceof FileConnectorConnectorLifecycleAdapter)) {
                throw new RuntimeException("Invalid connector type");
            }
            try {
                ((FileConnectorConnectorLifecycleAdapter) obj).disconnect();
            } catch (Exception e) {
                throw e;
            } finally {
                if (((FileConnectorConnectorLifecycleAdapter) obj) instanceof Stoppable) {
                    ((FileConnectorConnectorLifecycleAdapter) obj).stop();
                }
                if (((FileConnectorConnectorLifecycleAdapter) obj) instanceof Disposable) {
                    ((FileConnectorConnectorLifecycleAdapter) obj).dispose();
                }
            }
        }

        public boolean validateObject(Object key, Object obj) {
            if (!(obj instanceof FileConnectorConnectorLifecycleAdapter)) {
                throw new RuntimeException("Invalid connector type");
            }
            try {
                return ((FileConnectorConnectorLifecycleAdapter) obj).isConnected();
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                return false;
            }
        }

        public void activateObject(Object key, Object obj)
            throws Exception
        {
            if (!(key instanceof FileConnectorConnectorConnectionManager.ConnectionKey)) {
                throw new RuntimeException("Invalid key type");
            }
            if (!(obj instanceof FileConnectorConnectorLifecycleAdapter)) {
                throw new RuntimeException("Invalid connector type");
            }
            try {
                if (!((FileConnectorConnectorLifecycleAdapter) obj).isConnected()) {
                    ((FileConnectorConnectorLifecycleAdapter) obj).connect(((FileConnectorConnectorConnectionManager.ConnectionKey) key).getUsername(), ((FileConnectorConnectorConnectionManager.ConnectionKey) key).getPassword());
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

        /**
         * 
         */
        private String username;
        /**
         * 
         */
        private String password;

        public ConnectionKey(String username, String password) {
            this.username = username;
            this.password = password;
        }

        /**
         * Sets username
         * 
         * @param value Value to set
         */
        public void setUsername(String value) {
            this.username = value;
        }

        /**
         * Retrieves username
         * 
         */
        public String getUsername() {
            return this.username;
        }

        /**
         * Sets password
         * 
         * @param value Value to set
         */
        public void setPassword(String value) {
            this.password = value;
        }

        /**
         * Retrieves password
         * 
         */
        public String getPassword() {
            return this.password;
        }

        public int hashCode() {
            int hash = 1;
            hash = ((hash* 31)+ this.username.hashCode());
            return hash;
        }

        public boolean equals(Object obj) {
            return ((obj instanceof FileConnectorConnectorConnectionManager.ConnectionKey)&&(this.username == ((FileConnectorConnectorConnectionManager.ConnectionKey) obj).username));
        }

    }

}
