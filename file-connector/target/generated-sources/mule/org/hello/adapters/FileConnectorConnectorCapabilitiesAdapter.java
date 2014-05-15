
package org.hello.adapters;

import org.mule.api.Capabilities;
import org.mule.api.Capability;


/**
 * A <code>FileConnectorConnectorCapabilitiesAdapter</code> is a wrapper around {@link org.hello.FileConnectorConnector } that implements {@link org.mule.api.Capabilities} interface.
 * 
 */
public class FileConnectorConnectorCapabilitiesAdapter
    extends org.hello.FileConnectorConnector
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
