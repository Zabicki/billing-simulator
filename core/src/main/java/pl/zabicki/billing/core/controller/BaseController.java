package pl.zabicki.billing.core.controller;

public class BaseController {

    protected final String PROCESSING_TIME = "processingTime";
    protected final String COUNT = "count";

    protected String wrapJson(String name, long val) {
        return String.format("""
                {
                    "%s": %s
                }
                """, name, val);
    }

    protected long toSeconds(long processingTimeMillis) {
        return processingTimeMillis / 1000;
    }
}
