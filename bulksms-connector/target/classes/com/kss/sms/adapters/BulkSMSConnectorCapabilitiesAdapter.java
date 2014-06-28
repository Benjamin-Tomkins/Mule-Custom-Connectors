
package com.kss.sms.adapters;

import org.mule.api.Capabilities;
import org.mule.api.Capability;


/**
 * A <code>BulkSMSConnectorCapabilitiesAdapter</code> is a wrapper around {@link com.kss.sms.BulkSMSConnector } that implements {@link org.mule.api.Capabilities} interface.
 * 
 */
public class BulkSMSConnectorCapabilitiesAdapter
    extends com.kss.sms.BulkSMSConnector
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
