package org.grampus.core.rest.test;

import org.grampus.core.GProcessor;
import org.grampus.core.annotation.rest.GRestGroup;
import org.grampus.core.annotation.rest.GRestMethod;


@GRestGroup(id="test")
public class TestProcessor extends GProcessor {
    @GRestMethod(path = "/me")
    public String test(){
        return "success";
    }
}
