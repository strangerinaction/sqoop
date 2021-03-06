/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.sqoop.client.shell;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.OptionBuilder;
import org.apache.log4j.Logger;
import org.apache.sqoop.client.SubmissionCallback;
import org.apache.sqoop.client.core.Constants;
import org.apache.sqoop.client.utils.SubmissionDisplayer;
import org.apache.sqoop.model.MSubmission;

import static org.apache.sqoop.client.shell.ShellEnvironment.*;

/**
 * Class used to perform the submission start function
 */
public class SubmissionStartFunction extends SqoopFunction {
  public static final Logger LOG = Logger.getLogger(SubmissionStartFunction.class);

  @SuppressWarnings("static-access")
  public SubmissionStartFunction() {
    this.addOption(OptionBuilder
      .withDescription(resourceString(Constants.RES_PROMPT_JOB_ID))
      .withLongOpt(Constants.OPT_JID)
      .hasArg()
      .create(Constants.OPT_JID_CHAR));
    this.addOption(OptionBuilder
      .withDescription(resourceString(Constants.RES_PROMPT_SYNCHRONOUS))
      .withLongOpt(Constants.OPT_SYNCHRONOUS)
      .create(Constants.OPT_SYNCHRONOUS_CHAR));
  }

  public Object executeFunction(CommandLine line) {
    if (!line.hasOption(Constants.OPT_JID)) {
      printlnResource(Constants.RES_ARGS_JID_MISSING);
      return null;
    }

    // Poll until finished
    if (line.hasOption(Constants.OPT_SYNCHRONOUS)) {
      long pollTimeout = getPollTimeout();
      SubmissionCallback callback = new SubmissionCallback() {
        @Override
        public void submitted(MSubmission submission) {
          SubmissionDisplayer.displayHeader(submission);
          SubmissionDisplayer.displayProgress(submission);
        }

        @Override
        public void updated(MSubmission submission) {
          SubmissionDisplayer.displayProgress(submission);
        }

        @Override
        public void finished(MSubmission submission) {
          SubmissionDisplayer.displayFooter(submission);
        }
      };

      try {
        client.startSubmission(getLong(line, Constants.OPT_JID), callback, pollTimeout);
      } catch (InterruptedException e) {
        LOG.error("Could not sleep");
      }
    } else {
      MSubmission submission = client.startSubmission(getLong(line, Constants.OPT_JID));
      if(submission.getStatus().isFailure()) {
        SubmissionDisplayer.displayFooter(submission);
      } else {
        SubmissionDisplayer.displayHeader(submission);
        SubmissionDisplayer.displayProgress(submission);
      }
    }
    return null;
  }
}
