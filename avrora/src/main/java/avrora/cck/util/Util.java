/**
 * Copyright (c) 2004-2005, Regents of the University of California
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *
 * Neither the name of the University of California, Los Angeles nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package avrora.cck.util;

import avrora.cck.text.StringUtil;
import avrora.cck.text.Terminal;

import java.io.File;

/**
 * The <code>Util</code> class contains several utilities relating to exceptions
 * and errors that are useful.
 *
 * @author Ben L. Titzer
 */
public class Util
{
    /**
     * The <code>Error</code> class is the base class of errors that contains
     * some extra helper methods to generate a report. It provides a few extra
     * utility functions and is useful for distinguishing exceptions generated
     * by the application itself and the Java runtime.
     */
    public static class Error extends java.lang.Error
    {

        private static final long serialVersionUID = 1L;
        protected final String message, param;
        public static boolean STACKTRACES;


        public Error(String p)
        {
            super(p);
            message = "Error";
            param = p;
        }


        public Error(String n, String p)
        {
            super(n);
            message = n;
            param = p;
        }


        public String getParam()
        {
            return param;
        }


        /**
         * The <code>report()</code> method generates a textual report that is
         * printed on the terminal for the user.
         */
        public void report()
        {
            Terminal.print(Terminal.ERROR_COLOR, message);
            Terminal.print(": " + param + '\n');
            if (STACKTRACES)
            {
                printStackTrace();
            }
        }
    }

    /**
     * The <code>InternalError</code> class is a class of errors corresponding
     * to exceptional conditions within the application.
     */
    public static class InternalError extends Error
    {

        private static final long serialVersionUID = 1L;
        private String category;


        public InternalError(String param)
        {
            super(param);
            category = "Internal Error";
        }


        public InternalError(String c, String param)
        {
            super(param);
            category = c;
        }


        public void report()
        {
            Terminal.print(Terminal.ERROR_COLOR, category);
            Terminal.print(": " + param + '\n');
            printStackTrace();
        }
    }

    /**
     * The <code>Unexpected</code> class wraps an unexpected exception that may
     * happen during execution. This is useful for a "catch all" type of clause
     * to handle all the possible exceptions that could happen during execution
     * without having to write explicit handlers for each.
     */
    public static class Unexpected extends Error
    {

        private static final long serialVersionUID = 1L;

        public final Throwable thrown;


        public Unexpected(Throwable t)
        {
            super(StringUtil.quote(t.getClass()));
            thrown = t;
        }


        public void report()
        {
            Terminal.print(Terminal.ERROR_COLOR, "Unexpected exception");
            Terminal.print(": " + param + '\n');
            thrown.printStackTrace();
        }


        public void rethrow() throws Throwable
        {
            throw thrown;
        }
    }


    /**
     * The <code>unimplemented()</code> method is a utility that constructs a
     * <code>InternalError</code> instance. This is called from methods or
     * classes with unimplemented functionality for documentation and fail-fast
     * purposes.
     *
     * @return an instance of the <code>Util.InternalError</code> class that
     *         specifies that this functionality is not yet implemented
     */
    public static InternalError unimplemented()
    {
        return new InternalError("unimplemented");
    }


    /**
     * The <code>unreachable()</code> method is a utility that constructs a
     * <code>InternalError</code> instance. This is called from states that are
     * expected to be unreachable.
     *
     * @return an instance of the <code>Util.InternalError</code> class that
     *         specifies that this case is unreachable
     */
    public static InternalError unreachable()
    {
        return new InternalError("unreachable");
    }


    /**
     * The <code>failure()</code> method is a utility that constructs a
     * <code>InternalError</code> instance with the specified message. It is
     * useful for internal error conditions and defensive programming.
     *
     * @param s
     *            the error message for the failure
     * @return an instance of the <code>Util.InternalError</code> class with the
     *         specified error message
     */
    public static InternalError failure(String s)
    {
        return new InternalError(s);
    }


    /**
     * The <code>enforce()</code> method is a utility similar to
     * <code>assert</code> that checks a boolean predicate at runtime, throwing
     * the specified internal error upon failure. It is useful for internal
     * error conditions and defensive programming.
     *
     * @param b
     *            the boolean condition to enforce
     * @param s
     *            the string error message
     */
    public static void enforce(boolean b, String s)
    {
        if (!b)
            throw new InternalError(s);
    }


    /**
     * The <code>failure()</code> method is a utility that constructs a
     * <code>InternalError</code> instance with the specified message. It is
     * useful for internal error conditions and defensive programming.
     *
     * @param c
     *            the class of the error
     * @param s
     *            the error message
     * @return an instance of the <code>Util.InternalError</code> class with the
     *         specified error message
     */
    public static InternalError failure(String c, String s)
    {
        return new InternalError(c, s);
    }


    /**
     * The <code>warning()</code> method writes a warning to the terminal. It
     * does not produce an exception or a stack trace.
     *
     * @param s
     *            the warning to report
     */
    public static void warning(String s)
    {
        Terminal.print(Terminal.WARN_COLOR, "Internal Warning");
        Terminal.print(": " + s + '\n');
    }


    /**
     * The <code>unexpected()</code> method is a utility method that wraps an
     * unexpected exception so that it can be throw again and reported later.
     * This is useful for code that does IO but does not want to handle IO
     * exceptions, for example.
     *
     * @param t
     *            the throwable that was encountered
     * @return a new instance of the <code>Unexpected</code> class that wraps up
     *         the thrown exception
     */
    public static Unexpected unexpected(Throwable t)
    {
        return new Unexpected(t);
    }


    /**
     * The <code>userError()</code> method constructs and throws an error in
     * situations that are likely due to user error. This is useful for files
     * that are not found, an incorrect option value, etc.
     *
     * @param s
     *            the message for the user
     */
    public static void userError(String s)
    {
        throw new Error(s);
    }


    /**
     * The <code>userError()</code> method constructs and throws an error in
     * situations that are likely due to user error. This is useful for files
     * that are not found, an incorrect option value, etc.
     *
     * @param s
     *            the message for the user
     * @param p
     *            the parameter to the message, automatically put in quotes
     */
    public static void userError(String s, String p)
    {
        throw new Error(s, p);
    }


    /**
     * The <code>verifyFilesExist()</code> method verifies that each of the
     * specified files exists and is readable. If one of the files does not
     * exist, it will report an error to the user.
     *
     * @param files
     *            the files to verify the existence of
     * @return true if all of the files exist and are readable
     */
    public static boolean verifyFilesExist(String[] files)
    {
        boolean success = true;
        for (int cntr = 0; cntr < files.length; cntr++)
        {
            String file = files[cntr];
            if (!verifyFileExists(file))
                success = false;
        }
        return success;
    }


    /**
     * The <code>verifyFileExists()</code> method verifies that the specified
     * file exists and is readable. If the file does not exist, it will report
     * an error to the user and return <code>false</code>.
     *
     * @param file
     *            the name of the file
     * @return true if the file exists and is readable; false otherwise
     */
    public static boolean verifyFileExists(String file)
    {
        File f = new File(file);
        if (!f.exists())
        {
            userError("File not found", file);
            return false;
        }
        return true;
    }

}
