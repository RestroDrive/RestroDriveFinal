package com.android.mno.restrodrive.restrodrive.Model;

/**
 * To share selected address between Activity and Fragments
 */
public class MapAddress {

    private String sourceAddress;
    private String destinationAddress;

    public MapAddress(String sourceAddress, String destinationAddress) {
        this.sourceAddress = sourceAddress;
        this.destinationAddress = destinationAddress;
    }

    public String getSourceAddress() {
        return sourceAddress;
    }

    public String getDestinationAddress() {
        return destinationAddress;
    }
}
