
package com.cts.qr.adapters;

import org.mule.api.Capabilities;
import org.mule.api.Capability;


/**
 * A <code>QRCodeConnectorCapabilitiesAdapter</code> is a wrapper around {@link com.cts.qr.QRCodeConnector } that implements {@link org.mule.api.Capabilities} interface.
 * 
 */
public class QRCodeConnectorCapabilitiesAdapter
    extends com.cts.qr.QRCodeConnector
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
