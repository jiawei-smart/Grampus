package org.grampus.swagger.spec;


import org.grampus.swagger.config.model.Tag;

public class EndpointSpec {

    private String nameSpace;
    private String path;
    private Tag tag;
    private String description;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNameSpace() {
        return nameSpace;
    }

    public void setNameSpace(String nameSpace) {
        this.nameSpace = nameSpace;
    }

    public static Builder endpointPath(final String path) {
        return new Builder().withPath(path);
    }

    public static final class Builder {
        private String name;
        private String path;
        private String description;

        private Builder() {
        }

        public static Builder newBuilder() {
            return new Builder();
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withPath(String path) {
            this.path = path;
            return this;
        }

        public Builder withDescription(String description) {
            this.description = description;
            return this;
        }

        private String getName() {
            if (name != null) return name;
            return path.contains("/") ? path.substring(1) : path;
        }

        public EndpointSpec build() {
            EndpointSpec endpointSpec = new EndpointSpec();
            endpointSpec.setPath(path);
            endpointSpec.setTag(Tag.newBuilder()
                    .withName(getName())
                    .withDescription(description)
                    .build());
            endpointSpec.setDescription(description);
            return endpointSpec;
        }
    }
}
