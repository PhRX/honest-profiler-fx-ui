package com.insightfullogic.honest_profiler.delivery.console;

import com.insightfullogic.honest_profiler.model.parser.Method;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;

public class FakeConsole implements Console {

    private final ByteArrayOutputStream buffer;
    private final PrintStream out;

    private String output;

    public FakeConsole() {
        buffer = new ByteArrayOutputStream();
        out = new PrintStream(buffer);
    }

    @Override
    public PrintStream out() {
        return out;
    }

    public void isShowingTraces(int numberOfStackTraces) {
        outputContains("Number of stack traces: " + numberOfStackTraces);
    }

    public void outputContains(String message) {
        assertThat(getOutput(), containsString(message));
    }

    public String getOutput() {
        if (output == null) {
            output = new String(buffer.toByteArray());
            System.out.println("Received output:");
            System.out.println(output);
        }
        return output;
    }

    public void displaysMethod(Method method) {
        outputContains(method.getClassName());
        outputContains(method.getMethodName());
    }

}