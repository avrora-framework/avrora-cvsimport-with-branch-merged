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
 * Creation date: Dec 6, 2005
 */

package avrora.avrora.test.sim.msp430;

import avrora.avrora.Defaults;
import avrora.avrora.arch.AbstractArchitecture;
import avrora.avrora.arch.msp430.MSP430Architecture;
import avrora.avrora.core.Program;
import avrora.avrora.sim.Simulator;
import avrora.avrora.sim.platform.Platform;
import avrora.avrora.sim.types.SingleSimulation;
import avrora.avrora.test.sim.StateAccessor;
import avrora.avrora.test.sim.Tester;

/**
 * @author Ben L. Titzer
 */
public class MSP430Tester extends Tester
{
    @Override
    public Simulator newSimulator(Program p)
    {
        Platform platform = Defaults.getPlatform("telos").newPlatform(0,
                new SingleSimulation(), p);
        return platform.getMicrocontroller().getSimulator();
    }


    @Override
    public AbstractArchitecture getArchitecture()
    {
        return MSP430Architecture.INSTANCE;
    }


    @Override
    public StateAccessor getAccessor(Simulator sim)
    {
        return new MSP430StateAccessor(sim.getProgram(), sim);
    }
}
