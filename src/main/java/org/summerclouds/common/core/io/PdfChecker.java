/**
 * Copyright (C) 2022 Mike Hummel (mh@mhus.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.summerclouds.common.core.io;

import java.io.File;

import org.summerclouds.common.core.log.MLog;
import org.summerclouds.common.core.tool.MFile;
import org.summerclouds.common.core.tool.MString;

/**
 * For simple pdf content checking.
 *
 * @author markus hahn
 */
public class PdfChecker extends MLog {

    /**
     * checks for suspicious contents
     *
     * @param pdfFile
     * @return 0 = not suspicious, 1 = little suspicious, 2-3 = suspicious, 4-5 = highly suspicious
     */
    public int getSuspiciousScore(File pdfFile) {
        String content = MFile.readFile(pdfFile);
        int score = isSuspicious(content);
        return score;
    }

    /**
     * A score lesser then 1 is NO, 1 is MAYBE, greater is YES. If you want be sure reject
     * everything that is not NO. If you are generous reject everything that is YES.
     *
     * @param pdfFile
     * @return Suspicios
     */
    public FileChecker.SUSPICIOUS isSuspicious(File pdfFile) {
        int score = getSuspiciousScore(pdfFile);
        if (score == 0) return FileChecker.SUSPICIOUS.NO;
        if (score == 1) return FileChecker.SUSPICIOUS.MAYBE;
        return FileChecker.SUSPICIOUS.YES;
    }

    /**
     * checks for suspicious contents
     *
     * @param content
     * @return 0 = not suspicious, 1 = little suspicious, 2-3 = suspicious, 4-5 = highly suspicious
     */
    public int isSuspicious(String content) {
        String[] lines = MString.split(content, "\n");
        return isSuspicious(lines);
    }

    /**
     * checks for suspicious contents
     *
     * @param lines
     * @return 0 = not suspicious, 1 = little suspicious, 2-3 = suspicious, 4-5 = highly suspicious
     */
    public int isSuspicious(String[] lines) {

        int score = 0;
        int jsCnt = 0;
        int javaScriptCnt = 0;
        int aaCnt = 0;
        int openActionCnt = 0;
        int pageCnt = 0;
        /*
         * possible tags:
         * obj
         * endobj
         * stream
         * endstream
         * xref
         * trailer
         * startxref
         * /Page
         * /Encrypt
         * /ObjStm
         * /JS
         * /JavaScript
         * /AA
         * /OpenAction
         * /JBIG2Decode
         * /RichMedia
         * /Launch
         * /XFA
         *
         * suspicious tags:
         * /JS and /JavaScript (contain scripts)
         * /AA and /OpenAction (auto execution of scripts at startup is very suspicious)
         * /Page BUT NOT /Pages (if there's only 1 page, only in combination with other tags)
         * /ObjStm (can contain/obfuscate other objects)
         */
        for (String line : lines) {
            // only evaluate type descriptors
            if (!line.startsWith("<<")) continue;

            if (line.contains("/JS")) {
                jsCnt++;
            }
            if (line.contains("/JavaScript")) {
                javaScriptCnt++;
            }
            if (line.contains("/AA")) {
                aaCnt++;
            }
            if (line.contains("/OpenAction")) {
                openActionCnt++;
            }
            if (line.contains("/Page") && !line.contains("/Pages")) {
                pageCnt++;
            }
        }

        if (aaCnt > 0 || openActionCnt > 0) {
            if (score < 3) score += 2;
            else if (score < 5) score++;
        }
        if (jsCnt > 0 || javaScriptCnt > 0) {
            if (score < 3) score += 2;
            else if (score < 5) score++;
        }
        if (pageCnt == 1) {
            if (score > 0 && score < 5) score++;
        }

        return score;
    }
}
