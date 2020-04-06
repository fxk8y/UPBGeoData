package de.fxk8y.dice.upbgeo;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.text.html.HTMLDocument.HTMLReader.IsindexAction;

import org.openstreetmap.osmosis.core.task.v0_6.Sink;
import org.openstreetmap.osmosis.core.container.v0_6.EntityContainer;
import org.openstreetmap.osmosis.core.container.v0_6.NodeContainer;
import org.openstreetmap.osmosis.core.container.v0_6.RelationContainer;
import org.openstreetmap.osmosis.core.container.v0_6.WayContainer;
import org.openstreetmap.osmosis.core.domain.v0_6.Node;
import org.openstreetmap.osmosis.core.domain.v0_6.Relation;
import org.openstreetmap.osmosis.core.domain.v0_6.RelationMember;
import org.openstreetmap.osmosis.core.domain.v0_6.Tag;


public class OSMSink implements Sink {
	
	public static final double UPB_LAT = 51.707879;
	public static final double UPB_LON = 8.771645;

	/**
	 * 
	 * @return distance in meters
	 */
	public double distance(double lat0, double lon0, double lat1, double lon1) {
		if ((lat0 == lat1) && (lon0 == lon1)) return 0;

		double d = lon0 - lon1;
		double dist = Math.sin(Math.toRadians(lat0)) * Math.sin(Math.toRadians(lat1)) + Math.cos(Math.toRadians(lat0)) * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(d));

		dist = Math.acos(dist);
		dist = Math.toDegrees(dist);
		dist = dist * 60 * 1.85315962 * 1000;
		
		return (dist);
	}
	
	public double distanceUPB(double lat, double lon ) {
		return distance(lat, lon, UPB_LAT, UPB_LON);
	}

	public boolean containsTag(Collection<Tag> tags, String key, String value) {
		for (Tag t : tags) {
			if (key != null && key.equals(t.getKey())) {
				if (value == null) return true;
				else return value.equals(t.getValue());
			} else {
				if (value == null) return true;
				else return value.equals(t.getValue());
			}
		}
		
		return false;
	}
	
	public Map<Long, Node> interestingNodes = new HashMap<>();
	
	@SuppressWarnings("unused")
	@Override
	public void process(EntityContainer entityContainer) {
		if (entityContainer instanceof NodeContainer) {
            NodeContainer nc = (NodeContainer)entityContainer;
            Node n = nc.getEntity();
            
            if (distanceUPB(n.getLatitude(), n.getLongitude()) < 100) {
            	Collection<Tag> tags = n.getTags();
            	
            	//interestingNodes.put(n.getId(), n);
            	
            	if (containsTag(tags, null, "IW")) {
                	System.out.println("Found Node with Tags: " + n.toString());
                	
                	for (Tag t : tags) {
                		System.out.println("Tag: " + t.getKey() + "=" + t.getValue());
                	}
            	}
            }
        } else if (entityContainer instanceof WayContainer) {
            /* Way myWay = ((WayContainer) entityContainer).getEntity();
            for (Tag myTag : myWay.getTags()) {
                if ("highway".equalsIgnoreCase(myTag.getKey())) {
                    System.out.println(" Woha, it's a highway: " + myWay.getId());
                    break;
                }
            } */
        } else if (entityContainer instanceof RelationContainer && false) {
            RelationContainer rc = (RelationContainer)entityContainer;
            Relation r = rc.getEntity();
            Collection<Tag> tags = r.getTags();
            
            if (containsTag(tags, "building", null)) {
            	List<RelationMember> m = r.getMembers();
            	
            	/*boolean nearby = false;
            	
            	for (RelationMember rm : m) {
            		if (interestingNodes.get(rm.getMemberId()) != null) {
            			nearby = true;
            			break;
            		}
            	}*/
            	
            	//if (nearby) {
    	        	if (tags.size() > 0) {
    		        	System.out.println("Found Relation with Tags: " + r.toString());
    	            	for (RelationMember rm : m) {
    	            		System.out.println(rm.toString());
    	            	}
    		        	
    		        	/*for (Tag t : tags) {
    		        		System.out.println("Tag: " + t.getKey() + "=" + t.getValue());
    		        	}*/
    	        	}
            	//}
            }
        }
	}

	
	
	@Override
	public void initialize(Map<String, Object> metaData) {}

	@Override
	public void complete() {}

	@Override
	public void close() {}
}
