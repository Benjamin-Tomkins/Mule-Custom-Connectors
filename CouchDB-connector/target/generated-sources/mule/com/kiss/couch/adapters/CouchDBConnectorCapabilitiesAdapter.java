
package com.kiss.couch.adapters;

import org.mule.api.Capabilities;
import org.mule.api.Capability;


/**
 * A <code>CouchDBConnectorCapabilitiesAdapter</code> is a wrapper around {@link com.kiss.couch.CouchDBConnector } that implements {@link org.mule.api.Capabilities} interface.
 * 
 */
public class CouchDBConnectorCapabilitiesAdapter
    extends com.kiss.couch.CouchDBConnector
    implements Capabilities
{


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

}
