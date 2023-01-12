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
package org.summerclouds.common.core.error;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.support.AbstractMessageSource;
import org.summerclouds.common.core.cfg.BeanRef;
import org.summerclouds.common.core.log.Log;
import org.summerclouds.common.core.tool.MJson;
import org.summerclouds.common.core.tool.MSecurity;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

public class RC {

    public enum CAUSE {
        ENCAPSULATE, // encapsulate cause state
        ADAPT, // if possible adapt cause state
        APPEND, // if possible adapt cause state and append message entries, will ADAPT the Error
        // Code (!)
        IGNORE, // do print as parameters
        HIDE // print as parameters but do not link as cause
    }

    public enum STATUS {
        WARNING_TEMPORARILY(199),
        OK(200),
        CREATED(201),
        ACCEPTED(202),
        WARNING(299),
        ERROR(400),
        ACCESS_DENIED(401),
        FORBIDDEN(403),
        NOT_FOUND(404),
        CONFLICT(409),
        GONE(410),
        TOO_LARGE(413),
        SYNTAX_ERROR(415),
        TEAPOT(418),
        USAGE(422),
        LIMIT(427),
        INTERNAL_ERROR(500),
        NOT_SUPPORTED(501),
        BUSY(503),
        TIMEOUT(504),
        TOO_DEEP(508);

        private final int rc;

        private STATUS(int rc) {
            this.rc = rc;
        }

        public int rc() {
            return rc;
        }
    }

    /** Miscellaneous warning */
    public static final int WARNING_TEMPORARILY = 199; // Miscellaneous warning

    public static final int OK = 200;
    public static final int CREATED = 201;
    public static final int ACCEPTED = 202;
    /** Miscellaneous persistent warning */
    public static final int WARNING = 299; // Miscellaneous persistent warning,
    // https://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.46

    // do not retry with these errors - professional errors
    /** Default Error, client error */
    public static final int ERROR = 400;
    /**
     * you are not allowed to access the system, causes the client system to ask for a password,
     * exception for authentication
     */
    public static final int ACCESS_DENIED = 401;
    /**
     * you are not allowed to access this resource, user and password is given but access ins not
     * granted, exception for authorization
     */
    public static final int FORBIDDEN = 403;
    /** Resource was not found and thats ok - in contrast to CONFLICT - resource should be there */
    public static final int NOT_FOUND = 404;
    /**
     * conflict state or wrong state, to many retries, key to remove not found, key already set
     * wrong configuration - if not fixed fast, result is null, duplicate entry, device is not
     * enabled ... is not writable / read only
     */
    public static final int CONFLICT = 409; // conflict state or wrong state
    /**
     * indicating that the resource requested by the client has been permanently deleted, and that
     * the client should not expect an alternative redirection or forwarding address
     */
    public static final int GONE = 410;

    public static final int TOO_LARGE = 413;
    /**
     * Unsupported Media Type - string instead of int, wrong parameter value type, malformed format
     */
    public static final int SYNTAX_ERROR = 415; // Unsupported Media Type - string instead of int
    /**
     * Unprocessable Entity - parameter from client not set, parameter not found, parameter data is
     * null
     */
    public static final int USAGE = 422; // Unprocessable Entity - parameter not set

    /** Too Many Requests, Limit exceeded */
    public static final int LIMIT = 427; // Too Many Requests

    public static final int TEAPOT = 418; // I’m a teapot - joke

    // retry later - technical errors
    /** Internal Server Error, general server error */
    public static final int INTERNAL_ERROR = 500; // Internal Server Error
    /** Not Implemented, method or operation not found */
    public static final int NOT_SUPPORTED = 501; // Not Implemented
    /**
     * Service Unavailable, do not use 403 because it could be repeated, Resource currently not
     * available, locked
     */
    public static final int BUSY =
            503; // Service Unavailable, do not use 403 because it could be repeated
    /** Gateway Timeout */
    public static final int TIMEOUT = 504; // Gateway Timeout
    /** Loop Detected, to deep iteration, stack overflow */
    public static final int TOO_DEEP = 508; // Loop Detected

    public static final int RANGE_MIN_SUCCESSFUL = 200;
    public static final int RANGE_MAX_SUCCESSFUL = 299;
    public static final int RANGE_MIN_PROFESSIONAL = 400; // do not retry
    public static final int RANGE_MAX_PROFESSIONAL = 499; // do not retry
    public static final int RANGE_MIN_TECHNICAL = 500; // do retry later
    public static final int RANGE_MAX_TECHNICAL = 599; // do retry later

    public static final int RANGE_MIN_CUSTOM = 900;
    public static final int RANGE_MAX_CUSTOM = 999;

    public static final int RANGE_MAX = 999;

    public static String toMessage(
            int rc, IResult cause, String msg, Object[] parameters, int maxSize) {
        return toMessage(rc, CAUSE.IGNORE, msg, parameters, maxSize, cause);
    }

    public static String toMessage(
            int rc, CAUSE causeHandling, String msg, Object[] parameters, int maxSize) {
        return toMessage(rc, causeHandling, msg, parameters, maxSize, null);
    }

    public static String toMessage(
            int rc,
            CAUSE causeHandling,
            String msg,
            Object[] parameters,
            int maxSize,
            IResult cause) {
        if (causeHandling == null) causeHandling = CAUSE.ENCAPSULATE;
        // short cuts
        if (msg == null && parameters == null) return "[" + (rc >= 0 ? rc : "") + "]";
        if (parameters == null && msg.indexOf('"') == -1)
            return "[" + (rc >= 0 ? rc + "," : "") + "\"" + msg + "\"]";
        // pipe to colon
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        if (rc >= 0) sb.append(rc).append(",");
        addEncoded(sb, msg, maxSize);
        boolean isCause = false;
        if (parameters != null && parameters.length > 0) {
            boolean firstException = true;
            IResult appendCause = null;
            for (Object parameter : parameters) {

                sb.append(",");
                if (truncateMessage(sb, maxSize)) return sb.toString();

                if (parameter != null) {

                    if (parameter instanceof IResult && causeHandling == CAUSE.ADAPT) {
                        String m = ((IResult) parameter).getMessage();
                        if (m != null && maxSize > 0 && m.length() > maxSize) {
                            m = m.substring(0, maxSize) + "...\"]"; // TODO not well truncated
                        }
                        return m;
                    }
                    if (parameter instanceof IResult && causeHandling == CAUSE.APPEND) {
                        appendCause = (IResult) parameter;
                        firstException =
                                false; // ignore only first exception - it's the cause exception
                        sb.setLength(sb.length() - 1);
                        continue;
                    }
                    if (parameter instanceof Throwable
                            && causeHandling != CAUSE.IGNORE
                            && firstException) {
                        firstException =
                                false; // ignore only first exception - it's the cause exception
                        sb.setLength(sb.length() - 1);
                        continue;
                    }
                    if (parameter instanceof Object[])
                        addEncoded(sb, Arrays.deepToString((Object[]) parameter), maxSize);
                    else if (parameter instanceof int[])
                        addEncoded(sb, Arrays.toString((int[]) parameter), maxSize);
                    else if (parameter instanceof double[])
                        addEncoded(sb, Arrays.toString((double[]) parameter), maxSize);
                    else if (parameter instanceof long[])
                        addEncoded(sb, Arrays.toString((long[]) parameter), maxSize);
                    else if (parameter instanceof byte[])
                        addEncoded(sb, Arrays.toString((byte[]) parameter), maxSize);
                    else if (parameter instanceof float[])
                        addEncoded(sb, Arrays.toString((float[]) parameter), maxSize);
                    else if (parameter instanceof short[])
                        addEncoded(sb, Arrays.toString((short[]) parameter), maxSize);
                    else if (parameter instanceof char[])
                        addEncoded(sb, Arrays.toString((char[]) parameter), maxSize);
                    else addEncoded(sb, parameter, maxSize);
                } else sb.append("null");
            }

            if (appendCause != null) {
                String msg2 = appendCause.getMessage();
                if (msg2 != null) {
                    if (sb.length() > 0) sb.append(",");
                    int beforeLen = sb.length();
                    if (msg2.startsWith("[") && msg2.endsWith("]")) sb.append(msg2);
                    else addEncoded(sb, msg2, maxSize);
                    if (maxSize > 0 && sb.length() > maxSize) {
                        // remove full cause
                        sb.setLength(beforeLen);
                        sb.append("[" + appendCause.getReturnCode() + ",\"...cause...\"]");
                    }
                    isCause = true;
                }
            }
            if (cause != null) {
                String msg2 = cause.getMessage();
                if (msg2 != null) {
                    if (sb.length() > 0) sb.append(",");
                    int beforeLen = sb.length();
                    if (msg2.startsWith("[") && msg2.endsWith("]")) sb.append(msg2);
                    else addEncoded(sb, msg2, maxSize);
                    if (maxSize > 0 && sb.length() > maxSize) {
                        // remove full cause
                        sb.setLength(beforeLen);
                        sb.append("[" + cause.getReturnCode() + ",\"...cause...\"]");
                    }
                    isCause = true;
                }
            }
        }
        if (isCause || !truncateMessage(sb, maxSize)) sb.append("]");
        return sb.toString();
    }

    private static boolean truncateMessage(StringBuilder sb, int maxSize) {
        if (maxSize > 0) {
            if (sb.length() == maxSize) {
                if (maxSize < 3) { // fallback - should not be
                    sb.append("\"...\"]");
                    return true;
                }
                char c1 = sb.charAt(maxSize - 1);
                char c2 = sb.charAt(maxSize - 2);
                if (c1 == '\"' && c2 != ',' && c2 != '\\') sb.append(",\"...\"]");
                else if (c1 == '\\' && c2 != '\\') sb.append("\\...\"]");
                else sb.append("\"...\"]");
                return true;

            } else if (sb.length() > maxSize) {
                sb.setLength(maxSize);
                if (maxSize < 3) { // fallback - should not be
                    sb.append("...\"]");
                    return true;
                }
                char c1 = sb.charAt(maxSize - 1);
                char c2 = sb.charAt(maxSize - 2);
                if (c1 == '\"' && c2 != ',' && c2 != '\\') sb.append(",\"...\"]");
                else if (c1 == '\\' && c2 != '\\') sb.append("\\...\"]");
                else sb.append("...\"]");
                return true;
            }
        }
        return false;
    }

    private static void addEncoded(StringBuilder sb, Object obj, int maxSize) {
        if (obj == null) {
            sb.append("null");
            return;
        }
        if (maxSize > 0 && sb.length() > maxSize) {
            return;
        }
        String msg = String.valueOf(obj);
        int pos = 0;
        int nextPos;
        sb.append("\"");
        while ((nextPos = msg.indexOf('"', pos)) != -1) {
            encodeBackslash(sb, msg.substring(pos, nextPos), maxSize);
            sb.append("\\\"");
            pos = nextPos + 1;
            if (maxSize > 0 && sb.length() > maxSize) {
                return;
            }
            if (pos >= msg.length()) break;
        }
        if (pos < msg.length()) encodeBackslash(sb, msg.substring(pos), maxSize);
        sb.append("\"");
    }

    private static void encodeBackslash(StringBuilder sb, String msg, int maxSize) {
        if (maxSize > 0 && sb.length() > maxSize) {
            return;
        }
        int pos = 0;
        int nextPos;
        while ((nextPos = msg.indexOf('\\', pos)) != -1) {
            sb.append(msg.substring(pos, nextPos));
            sb.append("\\\\");
            pos = nextPos + 1;
            if (pos >= msg.length()) break;
            if (pos < msg.length()) sb.append(msg.substring(pos));
        }
        if (pos < msg.length()) sb.append(msg.substring(pos));
    }

    public static Throwable findCause(CAUSE causeHandling, Object... in) {
        if (in == null || (causeHandling != null && causeHandling == CAUSE.HIDE)) return null;
        for (Object o : in) {
            if (o instanceof Throwable) {
                return (Throwable) o;
            }
        }
        return null;
    }

    public static int findReturnCode(CAUSE causeHandling, int rc, Object... in) {
        if (causeHandling == null
                || in == null
                || (causeHandling != CAUSE.ADAPT && causeHandling != CAUSE.APPEND)) return rc;
        for (Object o : in) {
            if (o instanceof IResult) {
                return ((IResult) o).getReturnCode();
            }
        }
        return rc;
    }

    /**
     * Allow all between 0 - 999, otherwise 400 (ERROR)
     *
     * @param rc
     * @return normalize error code
     */
    public int normalize(int rc) {
        if (rc < 0) return ERROR;
        if (rc >= 1000) return ERROR;
        return rc;
    }
    /**
     * is successful but a warning
     *
     * @param rc
     * @return true if this kind of error
     */
    public static boolean isWarning(int rc) {
        return rc == 0 || rc == WARNING || rc == WARNING_TEMPORARILY;
    }

    /**
     * not permanent, could be fixed
     *
     * @param rc
     * @return true if this kind of error
     */
    public static boolean isTechnicalError(int rc) {
        return rc >= RANGE_MIN_TECHNICAL && rc <= RANGE_MAX_TECHNICAL;
    }

    /**
     * permanent error, retry will not fix it
     *
     * @param rc
     * @return true if this kind of error
     */
    public static boolean isProfessionalError(int rc) {
        return rc < 0
                || rc >= RANGE_MIN_PROFESSIONAL && rc <= RANGE_MAX_PROFESSIONAL
                || rc > RANGE_MIN_PROFESSIONAL;
    }

    /**
     * not an error - should not be used for exceptions
     *
     * @param rc
     * @return true if this kind of error
     */
    public static boolean isSuccessful(int rc) {
        return rc >= 0 && rc <= RANGE_MAX_SUCCESSFUL;
    }

    public static boolean canRetry(int rc) {
        return rc >= RANGE_MIN_TECHNICAL && rc <= RANGE_MAX_TECHNICAL && rc != NOT_SUPPORTED;
    }

    public static String toString(int rc) {
        switch (rc) {
            case WARNING_TEMPORARILY:
                return "WARNING_TEMPORARILY";
            case OK:
                return "OK";
            case CREATED:
                return "CREATED";
            case ACCEPTED:
                return "ACCEPTED";
            case WARNING:
                return "WARNING";
            case ERROR:
                return "ERROR";
            case ACCESS_DENIED:
                return "ACCESS_DENIED";
            case FORBIDDEN:
                return "FORBIDDEN";
            case NOT_FOUND:
                return "NOT_FOUND";
            case GONE:
                return "GONE";
            case TOO_LARGE:
                return "TOO_LARGE";
            case SYNTAX_ERROR:
                return "SYNTAX_ERROR";
            case USAGE:
                return "USAGE";
            case TEAPOT:
                return "TEAPOT";
            case INTERNAL_ERROR:
                return "INTERNAL_ERROR";
            case NOT_SUPPORTED:
                return "NOT_SUPPORTED";
            case BUSY:
                return "BUSY";
            case TIMEOUT:
                return "TIMEOUT";
            case TOO_DEEP:
                return "TOO_DEEP";
            case CONFLICT:
                return "CONFLICT";
            case LIMIT:
                return "LIMIT";
        }
        return String.valueOf(rc);
    }

    private static BeanRef<MessageSource> messageSource = new BeanRef<>(MessageSource.class);
    private static MessageSource helperMessageSource =
            new AbstractMessageSource() {
                @Override
                protected MessageFormat resolveCode(String code, Locale locale) {
                    return createMessageFormat(code, locale);
                }
            };

    private static Log log = Log.getLog(RC.class);

    public static Translated translate(Locale locale, String message) {

        if (locale == null) locale = MSecurity.get().getLocale();
        if (locale == null) locale = Locale.getDefault();

        Translated out = new Translated();
        out.original = message;
        out.locale = locale;
        out.message = message;
        if (message == null) return out;

        if (message.startsWith("[") && message.endsWith("]")) {
            try {
                out.array = (ArrayNode) MJson.load(message);
                out.returnCode = out.array.get(0).asInt();
                out.message = out.array.get(1).asText();
            } catch (Throwable t) {
                log.d("can't parse error message", message, t);
            }
        }

        out.translate();

        return out;
    }

    public static class Translated {

        private int returnCode;
        private String message;
        private Locale locale;
        private String original;
        private String translated;
        private ArrayNode array;
        private Translated nested;

        public void translate() {
            MessageSource ms = messageSource.bean();
            Object[] args = createArgs();
            if (ms != null) {
                translated = ms.getMessage(message, args, message, locale);
            } else {
                translated = helperMessageSource.getMessage(message, args, message, locale);
            }
        }

        private Object[] createArgs() {
            if (array == null) return new Object[0];
            Object[] args = new Object[array.size() - 1];
            for (int i = 1; i < array.size(); i++) {
                JsonNode item = array.get(i);
                if (item.isArray()) {
                    try {
                        nested = new Translated();
                        nested.returnCode = item.get(0).asInt();
                        nested.message = item.get(1).asText();
                        nested.array = (ArrayNode) item;
                        nested.translate();
                        args[i - 1] = nested.translated;
                    } catch (Throwable t) {
                        log.d("can't translate nested message", message, t);
                    }
                } else args[i - 1] = item.toString();
            }
            return args;
        }

        @Override
        public String toString() {
            return translated == null ? original : translated;
        }

        public int getReturnCode() {
            return returnCode;
        }

        public String getMessage() {
            return message;
        }

        public Locale getLocale() {
            return locale;
        }

        public String getOriginal() {
            return original;
        }

        public String getTranslated() {
            return translated;
        }

        public ArrayNode getArray() {
            return array;
        }

        public Translated getNested() {
            return nested;
        }
    }
}
