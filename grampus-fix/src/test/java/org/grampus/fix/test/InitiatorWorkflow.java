package org.grampus.fix.test;

import org.grampus.core.GCell;
import org.grampus.core.GConstant;
import org.grampus.core.GWorkflow;
import org.grampus.fix.GFixCell;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import quickfix.Message;

import java.util.Map;

public class InitiatorWorkflow {
    @Test
    public void testFix() {
        GWorkflow workflow = new GWorkflow() {
            @Override
            public void buildWorkflow() {
                service("INIT_SERVICE")
                        .cell(new GFixCell())
                        .cell(GFixCell.ON_LOGON, new GCell() {
                            String newOrdersSingleString = "8=FIX.4.4\u00019=8\u000135=D\u000134=62\u000149=BANZAI\u000152=20160803-12:55:42.094\u0001"
                                    + "56=EXEC\u000111=16H03A0000021\u000115=CHF\u000122=4\u000138=13\u000140=2\u000144=132\u000148=CH000000000\u000154=1\u000155=[N/A]\u000159=0\u0001"
                                    + "60=20160803-12:55:41.866\u0001207=XXXX\u0001423=2\u0001526=foo\u0001528=P\u0001"
                                    + "453=1\u0001448=test\u0001447=D\u0001452=7\u000120000=0\u0001802=1\u0001523=test\u0001803=25\u000122000=foobar\u000110=244\u0001";

                            @Override
                            public void handle(Object payload, Map meta) {
                                onEvent(GConstant.DEFAULT_EVENT, newOrdersSingleString);
                            }
                        })
                        .cell(GFixCell.FROM_APP, new GCell() {
                            @Override
                            public void handle(Object payload, Map meta) {
                                assertTask(() -> {
                                    Assertions.assertTrue(payload instanceof Message);
                                });
                            }
                        });

                service("ACCEPT_SERVICE")
                        .cell(new GFixCell())
                        .cell(GFixCell.FROM_APP, new GCell() {
                            String response = "FIX.4.4\u00019=9\u000135=AE\u000134=545\u000149=EXEC\u000152=20220210-02:44:00.820\u000156=BANZAI\u0001" +
                                    "15=AUD\u000122=4\u000131=27\u000132=5000.000000000000\u000148=CH000000000\u000155=ANZ\u000160=20220210-02:43:27.796\u000164=20220214\u000175=20220210\u0001106=4075\u0001167=CS\u0001381=135000\u0001461=Exxxxx\u0001487=0\u0001762=1\u0001880=7533509260093686098:0#NORMAL#1644451200000000000\u00011003=1120000338\u00011015=0\u00011301=XASX\u0001" +
                                    "115=ON_BHEHALF\u00011128=9\u0001" +
                                    "552=2\u0001" +
                                    "54=1\u0001453=1\u0001448=338-3\u0001447=D\u0001452=1\u00011=1040445\u0001576=1\u0001577=0\u000111=16H03A0000021\u0001" +
                                    "54=2\u0001453=1\u0001448=338-3\u0001447=D\u0001452=1\u00011=1040445\u0001576=1\u0001577=0\u000111=16H03A0000021\u0001" +
                                    "627=2\u0001628=HOPID1\u0001629=20220414-15:22:54\u0001628=HOPID2\u0001629=20220414-15:22:54\u0001" +
                                    "10=129\u0001";

                            @Override
                            public void handle(Object payload, Map meta) {
                                super.handle(payload, meta);
                            }
                        });
            }
        };
        workflow.test();
    }

}
