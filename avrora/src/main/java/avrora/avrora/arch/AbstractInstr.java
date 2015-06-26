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
 */

package avrora.avrora.arch;

/**
 * The <code>AbstractInstr</code> interface represents an interface implemented
 * by any instructions generated by jIntGen. This interface has methods to get
 * the name and size of an instruction, as well as a reference to an
 * <code>AbstractArchitecture</code> object that contains information about the
 * architecture that contains this instruction.
 *
 * @author jIntGen
 */
public interface AbstractInstr
{

    /**
     * The <code>getSize()</code> method returns the size of this instruction in
     * bytes. Each instance of an instruction has a fixed, known size in bytes,
     * and all architecture implementations that implement this abstract
     * interface can return their size in bytes.
     *
     * @return the size of this instruction in bytes
     */
    public int getSize();


    /**
     * The <code>getName()</code> method returns the name of this instruction as
     * a string. Each instance of an instruction has a known name, and all
     * architecture implementations that implement this abstract interface can
     * return their name as a string.
     *
     * @return the name of this instruction as a string
     */
    public String getName();


    /**
     * The <code>getArchitecture()</code> method returns a reference to the
     * <code>Architecture</code> class that this instruction is a member of. The
     * architecture instance allows the user to get information about the
     * architecture, including its name, instruction set, and other features.
     *
     * @return a reference to a <code>AbstractArchitecture</code> object that
     *         contains information about the architecture that this instruction
     *         is a member of
     */
    public AbstractArchitecture getArchitecture();
}
