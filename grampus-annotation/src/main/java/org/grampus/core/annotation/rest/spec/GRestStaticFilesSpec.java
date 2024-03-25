package org.grampus.core.annotation.rest.spec;

import org.grampus.core.annotation.rest.GRestStaticFiles;

public class GRestStaticFilesSpec {
    String path;

    public GRestStaticFilesSpec(GRestStaticFiles restStaticFiles) {
        this.path = restStaticFiles.path();
    }

    public GRestStaticFilesSpec(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
