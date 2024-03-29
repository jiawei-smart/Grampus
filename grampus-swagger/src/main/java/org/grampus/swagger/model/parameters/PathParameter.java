package org.grampus.swagger.model.parameters;

public class PathParameter extends AbstractSerializableParameter<PathParameter> {

    public PathParameter() {
        super.setIn("path");
        super.setRequired(true);
    }
}
