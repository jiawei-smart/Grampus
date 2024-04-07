package org.grampus.core;

public class GConstant {
    public final static String DEFAULT_EVENT = "DEFAULT_EVENT";
    public final static String DEFAULT_CONFIG_YAML = "app.yaml";
    public final static String PLUGIN_SERVICE = "PLUGIN_SERVICE";
    public final static String EVENT_SPLIT_CHAR = ".";
    public static final String EVENT_ALL = "*";
    public final static String CHAIN_SPLIT_CHAR = "->";
    public final static String EVENT_FIRST_CELL_ID = "EVENT_FIRST_CELL_ID";
    public final static String CELL_ID_SPLIT_CHAR = "_";
    public static final String GLOBAL_OPEN_ALL_EVENT = "*";
    public static final int DEFAULT_MESSAGE_QUEUE_SIZE = 10000;
    public static final int DEFAULT_SCHEDULE_WORKER_POOL_SIZE = 2;
    public static final String REST_PLUGIN = "REST_PLUGIN";
    public static final String MONITOR_SERVICE_START_TIME = "service.start.time";
    public static final String MONITOR_SERVICE_NAME = "service.name";
    public static final String MONITOR_SERVICE_INCLUDED_EVENTS = "service.included.events";
    public static final String MONITOR_CELL_MESSAGE_QUEUE_SIZE = "cell.message.queue.size";
    public static final String MONITOR_CELL_START_TIME = "cell.start.time";
    public static final String MONITOR_CELL_LAST_HEARTBEAT_LATENCY = "cell.last.heartbeat.latency";
    public static final String MONITOR_CELL_ID = "cell.id";
    public static final String MONITOR_CELL_PNO_COUNT = "cell.pno.count";
    public static final String MONITOR_CELL_BATCH_SIZE = "cell.batch.size";

    public static final String GRAMPUS_LOGO = "\n" +
            "  ________                                          \n" +
            " /  _____/___________    _____ ______  __ __  ______\n" +
            "/   \\  __\\_  __ \\__  \\  /     \\\\____ \\|  |  \\/  ___/\n" +
            "\\    \\_\\  \\  | \\// __ \\|  Y Y  \\  |_> >  |  /\\___ \\ \n" +
            " \\______  /__|  (____  /__|_|  /   __/|____//____  >\n" +
            "        \\/           \\/      \\/|__|              \\/ ";
    public static final String GRAMPUS_TEST_LOGO = "\n" +
            "\n" +
            "   _____                                       _____         _   \n" +
            "  |  __ \\                                     |_   _|       | |  \n" +
            "  | |  \\/_ __ __ _ _ __ ___  _ __  _   _ ___    | | ___  ___| |_ \n" +
            "  | | __| '__/ _` | '_ ` _ \\| '_ \\| | | / __|   | |/ _ \\/ __| __|\n" +
            "  | |_\\ \\ | | (_| | | | | | | |_) | |_| \\__ \\   | |  __/\\__ \\ |_ \n" +
            "   \\____/_|  \\__,_|_| |_| |_| .__/ \\__,_|___/   \\_/\\___||___/\\__|\n" +
            "                            | |                                  \n" +
            "                            |_|                                  \n";
}
