/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0 and the Server Side Public License, v 1; you may not use this file except
 * in compliance with, at your election, the Elastic License 2.0 or the Server
 * Side Public License, v 1.
 */

package org.elasticsearch.qa.die_with_dignity;

import org.elasticsearch.client.Request;
import org.elasticsearch.core.PathUtils;
import org.elasticsearch.test.rest.ESRestTestCase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class DieWithDignityIT extends ESRestTestCase {

    public void testDieWithDignity() throws Exception {
        // there should be an Elasticsearch process running with the die.with.dignity.test system property
        {
            final Map<String, String> jvmCommandLines = getJvmCommandLines();
            final boolean found = jvmCommandLines.values().stream().anyMatch(line -> line.contains("-Ddie.with.dignity.test=true"));
            assertTrue(String.join(",", jvmCommandLines.values()), found);
        }

        expectThrows(IOException.class, () -> client().performRequest(new Request("GET", "/_die_with_dignity")));

        // the Elasticsearch process should die and disappear from the output of jps
        assertBusy(() -> {
            final Map<String, String> jvmCommandLines = getJvmCommandLines();
            final boolean notFound = jvmCommandLines.values().stream().noneMatch(line -> line.contains("-Ddie.with.dignity.test=true"));
            assertTrue(String.join(",", jvmCommandLines.values()), notFound);
        });

        // parse the logs and ensure that Elasticsearch died with the expected cause
        final List<String> lines = Files.readAllLines(PathUtils.get(System.getProperty("log")));
        final Iterator<String> it = lines.iterator();

        boolean fatalError = false;
        boolean fatalErrorInThreadExiting = false;
        try {
            while (it.hasNext() && (fatalError == false || fatalErrorInThreadExiting == false)) {
                final String line = it.next();
                if (containsAll(line, ".*ERROR.*", ".*ExceptionsHelper.*", ".*javaRestTest-0.*", ".*fatal error.*")) {
                    fatalError = true;
                } else if (containsAll(
                    line,
                    ".*ERROR.*",
                    ".*ElasticsearchUncaughtExceptionHandler.*",
                    ".*javaRestTest-0.*",
                    ".*fatal error in thread \\[Thread-\\d+\\], exiting.*",
                    ".*java.lang.OutOfMemoryError: Requested array size exceeds VM limit.*"
                )) {
                    fatalErrorInThreadExiting = true;
                }
            }

            assertTrue(fatalError);
            assertTrue(fatalErrorInThreadExiting);

        } catch (AssertionError ae) {
            Path path = PathUtils.get(System.getProperty("log"));
            debugLogs(path);
            throw ae;
        }
    }

    private Map<String, String> getJvmCommandLines() throws IOException {
        /*
         * jps will truncate the command line to 1024 characters; so we collect the pids and then run jcmd <pid> VM.command_line to get the
         * full command line.
         */
        final String jpsPath = PathUtils.get(System.getProperty("runtime.java.home"), "bin/jps").toString();
        final Process process = new ProcessBuilder().command(jpsPath, "-v").start();

        final List<String> pids = new ArrayList<>();
        try (
            InputStream is = process.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))
        ) {
            String line;
            while ((line = in.readLine()) != null) {
                pids.add(line.substring(0, line.indexOf(' ')));
            }
        }

        final String jcmdPath = PathUtils.get(System.getProperty("runtime.java.home"), "bin/jcmd").toString();
        final Map<String, String> jvmCommandLines = new HashMap<>();
        for (final String pid : pids) {
            final Process jcmdProcess = new ProcessBuilder().command(jcmdPath, pid, "VM.command_line").start();
            try (
                InputStream is = jcmdProcess.getInputStream();
                BufferedReader in = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))
            ) {
                String line;
                while ((line = in.readLine()) != null) {
                    if (line.startsWith("jvm_args")) {
                        jvmCommandLines.put(pid, line);
                    }
                }
            }
        }
        return jvmCommandLines;
    }

    private boolean containsAll(String line, String... subStrings) {
        for (String subString : subStrings) {
            if (line.matches(subString) == false) {
                return false;
            }
        }
        return true;
    }

    private void debugLogs(Path path) throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            reader.lines().forEach(line -> logger.info(line));
        }
    }

    @Override
    protected boolean preserveClusterUponCompletion() {
        // as the cluster is dead its state can not be wiped successfully so we have to bypass wiping the cluster
        return true;
    }

}
