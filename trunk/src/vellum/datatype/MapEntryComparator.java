/*
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers 2011, iPay (Pty) Ltd
 */

package vellum.datatype;

import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author evans
 */
public class MapEntryComparator implements Comparator<Map.Entry> {

    @Override
    public int compare(Entry o1, Entry o2) {
        Comparable v1 = (Comparable) o1.getValue();
        Comparable v2 = (Comparable) o2.getValue();
        return v2.compareTo(v1);
    }
}