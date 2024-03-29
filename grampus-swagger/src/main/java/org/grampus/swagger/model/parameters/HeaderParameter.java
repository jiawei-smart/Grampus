package org.grampus.swagger.model.parameters;

public class HeaderParameter extends AbstractSerializableParameter<HeaderParameter> {

    public HeaderParameter() {
        super.setIn("header");
    }
}
