package org.grampus.core.customized;
import org.grampus.core.GCell;
import org.grampus.core.message.GMessage;
import org.grampus.util.GStringUtil;

public class Redirector extends GCell {
    private String event;

    public Redirector() {
    }

    public Redirector(final String event) {
        this.event = event;
    }

    @Override
    public void handle(GMessage msg) {
        redirectEvent(GStringUtil.isNotEmpty(event) ? event : getEvent(),msg);
    }
}
