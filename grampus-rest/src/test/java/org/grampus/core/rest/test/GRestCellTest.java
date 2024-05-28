package org.grampus.core.rest.test;

import org.grampus.core.GWorkflow;

public class GRestCellTest {
    public static void main(String[] args) {
        GWorkflow workflow = new GWorkflow(){
            @Override
            public void buildWorkflow() {
                service("TEST_SERVICE").cell(new TestCell());
            }
        };
        workflow.start();
    }
}
