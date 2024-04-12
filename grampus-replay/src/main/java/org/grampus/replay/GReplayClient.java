package org.grampus.replay;

import net.openhft.chronicle.map.ChronicleMap;
import org.grampus.core.client.GAPIBase;
import org.grampus.log.GLogger;

import java.io.File;
import java.io.IOException;
import java.util.Set;

public class GReplayClient implements GAPIBase<GReplayOptions> {
    private ChronicleMap<String, byte[]> chronicleMap;
    private GMarsheller marsheller;

    @Override
    public boolean start(GReplayOptions config) {
        if (marsheller == null) {
            GLogger.error("GReplay client marsheller is null");
            return false;
        }
        try {
            this.chronicleMap = ChronicleMap
                    .of(String.class, byte[].class)
                    .name(config.getName())
                    .averageKeySize(config.getKeyByteSize())
                    .averageValueSize(config.getValueByteSize())
                    .entries(config.getSize())
                    .createOrRecoverPersistedTo(new File(config.getDataFolder(), config.getName()));
            return true;
        } catch (IOException e) {
            GLogger.error("GReplay failure to start relay client with {}", e);
            return false;
        }
    }

    @Override
    public boolean stop() {
        chronicleMap.close();
        return true;
    }

    public void save(String key, Object message) throws Exception {
        byte[] value = marsheller.encode(message);
        this.chronicleMap.put(key, value);
    }

    public Set<String> getKeys() {
        return this.chronicleMap.keySet();
    }

    public Object get(String key) throws Exception {
        return this.marsheller.decode(this.chronicleMap.get(key));
    }

    public void setMarsheller(GMarsheller marsheller) {
        this.marsheller = marsheller;
    }
}
