package com.project.of.busnavigationsystem.dummy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<DummyItem> ITEMS = new ArrayList<DummyItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, DummyItem> ITEM_MAP = new HashMap<String, DummyItem>();

    private static final int COUNT = 25;

    static {
        // Add some sample items.
        addItem(new DummyItem("1", "Attarkhel-Purano Buspark", "Bus Stops : \n 1. Attarkhel \n 2. Purano Buspark \nDistance : 9.6 km\nDuration :31 min"));
        addItem(new DummyItem("2", "Bagbazaar-Kamalbinayak", "Bus Stops : \n 1. Bagbazaar \n 2. Kamalbinayak \nDistance : 15.7 km\nDuration : 39 min"));
        addItem(new DummyItem("3", "Bagbazaar-Ratnapark", "Bus Stops : \n 1. Bagbazaar \n 2. Ratnapark \nDistance : 0.2 km\nDuration : 1 min"));
        addItem(new DummyItem("4", "Balaju-Ratnapark", "Bus Stops : \n 1. Balaju \n 2. Ratnapark \nDistance : 3.3 km\nDuration : 9 min"));
        addItem(new DummyItem("5", "Balkhu-NayaBaneshwor-Sankhamul", "Bus Stops : \n 1. Balkhu \n 2. Naya Baneshwor \n 3. Sankhamul \nDistance : 11 km\nDuration : 23 min"));
        addItem(new DummyItem("6", "Balkhu-NayaBaneshwor-Shantinagar-Bhatkya Pul", "Bus Stops : \n 1. Balkhu \n 2. Naya Baneshwor \n 3. Shantinagar \n 4. Bhatkya Pul \nDistance : 17.2 km\nDuration : 41 min"));
        addItem(new DummyItem("7", "Balkumari-Gopi Krishna", "Bus Stops : \n 1. Balkumari \n 2. Gopi Krishna \nDistance : 6.7 km\nDuration : 16 min"));
        addItem(new DummyItem("8", "Bhaktapur-Purano Thimi-Purano Bus Park", "Bus Stops : \n 1. Bhaktapur \n 2. Purano Thimi \n 3. Purano Bus Park \nDistance : \nDuration :"));
        addItem(new DummyItem("9", "Boudha-Dillibazaar", "Bus Stops : \n 1. Boudha \n 2. Dillibazaar \nDistance : 4.2 km\nDuration : 13 min"));
        addItem(new DummyItem("10", "Budhanilkantha School-Ratna Park", "Bus Stops : \n 1. Budhanilkantha School \n 2. Ratna Park \nDistance : 10.3 km\nDuration : 31 min"));
        addItem(new DummyItem("11", "Bungamati-Bhaisipati-Charghare", "Bus Stops : \n 1. Bungamati \n 2. Bhaisipati \n 3. Charghare \nDistance : 10.7 km\nDuration : 31 min"));
        addItem(new DummyItem("12", "Changu-Ratnapark", "Bus Stops : \n 1. Changu \n 2. Ratnapark \nDistance : 15.4 km\nDuration : 46 min"));
        addItem(new DummyItem("13", "Lagankhel-Jawalakhel-Kupondol-Tripureshor", "Bus Stops : \n 1. Lagankhel \n 2. Jawalakhel \n 3. Kupondol \n 4. Tripureshor \nTotal Distance : 4 km \nDuration : 9 min"));
        addItem(new DummyItem("14", "Lagankhel-Kupondol-Thapathali", "Bus Stops : \n 1. Lagankhel \n 2. Kupondol \n 3. Thapathali \nDistance : 4.6 km \nDuration : 14 min"));
        addItem(new DummyItem("15", "Putalisadak-Pulchowk", "Bus Stops : \n 1. Putalisadak \n 2. Pulchowk \nDistance : 4.8 km\nDuration : 18 min"));
        addItem(new DummyItem("16", "Ratnapark-Samakhusi Chowk", "Bus Stops : \n 1. Ratnapark \n 2. Samakhusi Chowk \nDistance : 3.9 km\nDuration : 11 min"));
        //addItem(new DummyItem("16","Chakrapath Parikrama","Bus Stops : \n \n \nDistance : \nDuration :"));
    }

    private static void addItem(DummyItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class DummyItem {
        public String id;
        public final String content;
        public final String details;

        public DummyItem(String id, String content, String details) {
            this.id = id;
            this.content = content;
            this.details = details;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}
