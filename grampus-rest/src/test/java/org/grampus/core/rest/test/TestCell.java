package org.grampus.core.rest.test;

import org.grampus.core.GCell;
import org.grampus.core.GConstant;
import org.grampus.core.annotation.plugin.GPlugin;
import org.grampus.core.annotation.rest.GRestGroup;
import org.grampus.core.annotation.rest.GRestMethod;


@GRestGroup(id="test")
public class TestCell extends GCell {
    @GRestMethod(path = "/me")
    public String test(){
        return "success";
    }
}
