package org.launchcode.liftoffproject.models;

import java.util.ArrayList;

public class VendorData {

    public static ArrayList<Vendor> findByValue(String value, Iterable<Vendor> allVendors) {
        String lower_val = value.toLowerCase();

        ArrayList<Vendor> results = new ArrayList<>();

        for (Vendor vendor: allVendors) {

            if (vendor.getLocation().toLowerCase().trim().contains(lower_val)) {
                results.add(vendor);
            } else if (vendor.getName().toLowerCase().contains(lower_val)) {
                results.add(vendor);
            } else if (vendor.getEmail().toLowerCase().contains(lower_val)) {
                results.add(vendor);
            } else if (vendor.getProducts().contains(lower_val)) {
                results.add(vendor);
            }
        }

        return results;
    }
}
