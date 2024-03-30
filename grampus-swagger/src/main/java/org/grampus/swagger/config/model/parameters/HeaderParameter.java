package org.grampus.swagger.config.model.parameters;

public class HeaderParameter extends AbstractSerializableParameter<HeaderParameter> {

    public HeaderParameter() {
        super.setIn("header");
    }
}
