
package com.kss.social.adapters;

import com.kss.social.FoursquareConnector;
import org.mule.api.Capabilities;
import org.mule.api.Capability;


/**
 * A <code>FoursquareConnectorCapabilitiesAdapter</code> is a wrapper around {@link FoursquareConnector } that implements {@link org.mule.api.Capabilities} interface.
 * 
 */
public class FoursquareConnectorCapabilitiesAdapter
    extends FoursquareConnector
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
