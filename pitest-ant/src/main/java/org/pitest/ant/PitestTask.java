/*
 * Copyright 2012 Nicolas Rusconi
 * 
 * Licensed under the Apache License, Version 2.0 ("the "License""); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, 
 * software distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and limitations under the License. 
 */

package org.pitest.ant;

import java.util.HashMap;
import java.util.Map;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.Java;
import org.apache.tools.ant.types.Path;
import org.pitest.mutationtest.MutationCoverageReport;
import org.pitest.mutationtest.config.ConfigOption;

public class PitestTask extends Task {

  private static final String[]     REQUIRED_OPTIONS = {
      ConfigOption.TARGET_CLASSES.getParamName(),
      ConfigOption.REPORT_DIR.getParamName(),
      ConfigOption.SOURCE_DIR.getParamName()        };
  private final Map<String, String> options          = new HashMap<String, String>();
  private String                    classpath;

  @Override
  public void execute() throws BuildException {
    try {
      execute(new Java(this));
    } catch (final Throwable t) {
      throw new BuildException(t);
    }
  }

  void execute(final Java java) {
    java.setClasspath(generateClasspath());
    java.setClassname(MutationCoverageReport.class.getCanonicalName());
    java.setFailonerror(true);
    java.setFork(true);

    checkRequiredOptions();
    for (final Map.Entry<String, String> option : this.options.entrySet()) {
      java.createArg().setValue(
          "--" + option.getKey() + "=" + option.getValue());
    }

    java.execute();
  }

  private void checkRequiredOptions() {
    for (final String requiredOption : REQUIRED_OPTIONS) {
      if (optionMissing(requiredOption)) {
        throw new BuildException("You must specify the " + requiredOption + ".");
      }
    }
  }

  private boolean optionMissing(final String option) {
    return !this.options.keySet().contains(option);
  }

  private Path generateClasspath() {
    if (this.classpath == null) {
      throw new BuildException("You must specify the classpath.");
    }

    final Object reference = getProject().getReference(this.classpath);
    if (reference != null) {
      this.classpath = reference.toString();
    }

    return new Path(getProject(), this.classpath);
  }

  public void setReportDir(final String value) {
    this.setOption(ConfigOption.REPORT_DIR, value);
  }

  public void setTargetClasses(final String value) {
    this.setOption(ConfigOption.TARGET_CLASSES, value);
  }

  public void setTargetTests(final String value) {
    this.setOption(ConfigOption.TEST_FILTER, value);
  }

  public void setDependencyDistance(final String value) {
    this.setOption(ConfigOption.DEPENDENCY_DISTANCE, value);
  }

  public void setThreads(final String value) {
    this.setOption(ConfigOption.THREADS, value);
  }

  public void setMutateStaticInits(final String value) {
    this.setOption(ConfigOption.MUTATE_STATIC_INITIALIZERS, value);
  }

  public void setTimestampedReports(final String value) {
    this.setOption(ConfigOption.TIME_STAMPED_REPORTS, value);
  }

  public void setMutators(final String value) {
    this.setOption(ConfigOption.MUTATIONS, value);
  }

  public void setExcludedMethods(final String value) {
    this.setOption(ConfigOption.EXCLUDED_METHOD, value);
  }

  public void setExcludedClasses(final String value) {
    this.setOption(ConfigOption.EXCLUDED_CLASSES, value);
  }

  public void setAvoidCallsTo(final String value) {
    this.setOption(ConfigOption.AVOID_CALLS, value);
  }

  public void setVerbose(final String value) {
    this.setOption(ConfigOption.VERBOSE, value);
  }

  public void setTimeoutFactor(final String value) {
    this.setOption(ConfigOption.TIMEOUT_FACTOR, value);
  }

  public void setTimeoutConst(final String value) {
    this.setOption(ConfigOption.TIMEOUT_CONST, value);
  }

  public void setMaxMutationsPerClass(final String value) {
    this.setOption(ConfigOption.MAX_MUTATIONS_PER_CLASS, value);
  }

  public void setJvmArgs(final String value) {
    this.setOption(ConfigOption.CHILD_JVM, value);
  }

  public void setOutputFormats(final String value) {
    this.setOption(ConfigOption.OUTPUT_FORMATS, value);
  }

  public void setSourceDir(final String value) {
    this.setOption(ConfigOption.SOURCE_DIR, value);
  }

  public void setClasspath(final String classpath) {
    this.classpath = classpath;
  }

  public void setMutableCodePaths(final String glob) {
    setOption(ConfigOption.CODE_PATHS, glob);
  }

  public void setIncludedTestNGGroups(final String value) {
    this.setOption(ConfigOption.INCLUDED_GROUPS, value);
  }

  public void setExcludedTestNGGroups(final String value) {
    this.setOption(ConfigOption.EXCLUDED_GROUPS, value);
  }

  private void setOption(final ConfigOption option, final String value) {
    this.options.put(option.getParamName(), value);
  }

}