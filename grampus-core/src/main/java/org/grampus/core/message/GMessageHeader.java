package org.grampus.core.message;

import java.util.ArrayList;
import java.util.List;

public class GMessageHeader {
    private List<String> throughPath = new ArrayList<String>();
    private String currentEventId;
}
