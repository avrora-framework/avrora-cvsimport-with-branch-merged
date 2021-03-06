/**
 * Copyright (c) 2005, Regents of the University of California
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
 *
 * Creation date: Oct 5, 2005
 */

package avrora.jintgen.types;

import avrora.cck.parser.AbstractToken;
import avrora.cck.parser.ErrorReporter;
import avrora.cck.parser.SourcePoint;

/**
 * The <code>TypeErrorReporter</code> class implements an
 * <code>ErrorReporter</code> subclass that has methods to report typing errors
 * that occur in type checking a program. For example, when a type mismatch
 * occurs, the typechecker can call <code>.TypeMismatch()</code> with the type
 * of syntactic construct in question <code>Typeable</code> instance, and the
 * type expected.
 *
 * An instance of this error reporter is needed with each <code>TypeEnv</code>
 * instance so that an unresolved <code>TypeRef</code> can be reported to the
 * user.
 *
 * @author Ben L. Titzer
 */
public class TypeErrorReporter extends ErrorReporter
{

    public void UnresolvedType(AbstractToken t)
    {
        unresolved("Type", "type", t);
    }


    public void TypeMismatch(String what, Typeable expr, Type expect)
    {
        String report = "Type mismatch in " + what + ": expected " + expect
                + ", found " + expr.getType();
        error("TypeMismatch", expr.getSourcePoint(), report);
    }


    public void TypesCannotBeCompared(Typeable t1, Typeable t2)
    {
        String report = "Types cannot be compared: " + t1.getType() + " and "
                + t2.getType();
        SourcePoint sp = new SourcePoint(t1.getSourcePoint(),
                t2.getSourcePoint());
        error("TypesCannotBeCompared", sp, report);
    }


    public void ExpectedTypeClass(Typeable t, String tc)
    {
        String report = "Expect " + tc + " type, found: " + t.getType();
        error("ExpectedTypeClass", t.getSourcePoint(), report, tc);

    }
}
