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
 * Creation date: Nov 29, 2005
 */

package avrora.avrora.test.sim.msp430;

import avrora.avrora.arch.msp430.MSP430Interpreter;
import avrora.avrora.arch.msp430.MSP430Symbol;
import avrora.avrora.core.Program;
import avrora.avrora.sim.Simulator;
import avrora.avrora.test.sim.StateAccessor;
import avrora.cck.util.Arithmetic;
import avrora.cck.util.Util;

/**
 * The <code>MSP430StateAccessor</code> class provides access to the state of
 * the MSP430 simulator for the purpose of the automated simulation tester. It
 * provides a number of names such "r1" and "pc" that correspond to values in
 * the simulation. Test cases can specify an initial state, a sequence of
 * instructions to execute, and the expected final state.
 *
 * @author Ben L. Titzer
 */
public class MSP430StateAccessor extends StateAccessor
{
    final MSP430Interpreter interpreter;


    public MSP430StateAccessor(Program p, Simulator s)
    {
        super(p, s);
        interpreter = (MSP430Interpreter) simulator.getInterpreter();

        // install the general purpose registers
        for (int cntr = 0; cntr < 16; cntr++)
        {
            accessors.put("r" + cntr, newRegister(cntr));
        }

        // install accessors for PC, SRAM, SP, and FLASH
        accessors.put("data", new DATA());
        accessors.put("regs", new REGS());
        accessors.put("pc", new PC());
        accessors.put("sp", new SP());
        accessors.put("cycles", new Cycles());
        accessors.put("C", new Flag(0));
        accessors.put("Z", new Flag(1));
        accessors.put("N", new Flag(2));
        accessors.put("V", new Flag(8));
    }


    Register newRegister(int cntr)
    {
        Register r = new Register();
        r.reg = MSP430Symbol.get_GPR("r" + cntr);
        return r;
    }

    class Register extends Accessor
    {
        MSP430Symbol.GPR reg;


        @Override
        protected int get()
        {
            return interpreter.getRegister(reg);
        }


        @Override
        protected void set(int val)
        {
            interpreter.setRegister(reg, (char) val);
        }
    }

    class PC extends Accessor
    {
        @Override
        protected int get()
        {
            return interpreter.getState().getPC();
        }


        @Override
        protected void set(int val)
        {
            throw Util.unimplemented();
        }
    }

    class SP extends Accessor
    {
        @Override
        protected int get()
        {
            return interpreter.getState().getSP();
        }


        @Override
        protected void set(int val)
        {
            interpreter.setRegister(MSP430Symbol.GPR.SP, (char) val);
        }
    }

    class DATA extends Accessor
    {
        @Override
        protected int get()
        {
            return 0;
        }


        @Override
        protected void set(int val)
        {
            // do nothing.
        }


        @Override
        protected int getIndex(int ind)
        {
            return interpreter.getSRAM(ind);
        }


        @Override
        protected void setIndex(int ind, int val)
        {
            interpreter.setData(ind, (char) val);
        }
    }

    class REGS extends Accessor
    {
        @Override
        protected int get()
        {
            return 0;
        }


        @Override
        protected void set(int val)
        {
            // do nothing.
        }


        @Override
        protected int getIndex(int ind)
        {
            return interpreter.getRegister(ind);
        }


        @Override
        protected void setIndex(int ind, int val)
        {
            throw Util.unimplemented();
        }
    }

    class Flag extends Accessor
    {
        final int bit;


        Flag(int b)
        {
            bit = b;
        }


        @Override
        protected int get()
        {
            return (interpreter.getSREG() >> bit) & 1;
        }


        @Override
        protected void set(int val)
        {
            int reg = interpreter.getSREG();
            reg = Arithmetic.setBit(reg, bit, val != 0);
            interpreter.setRegister(MSP430Symbol.GPR.SR, (char) reg);
        }
    }

    class Cycles extends Accessor
    {
        @Override
        protected int get()
        {
            return (int) interpreter.getState().getCycles();
        }


        @Override
        protected void set(int val)
        {
            interpreter.getMainClock().advance(val);
        }
    }
}
