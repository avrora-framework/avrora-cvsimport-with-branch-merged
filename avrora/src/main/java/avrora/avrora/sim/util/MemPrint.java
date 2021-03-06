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

package avrora.avrora.sim.util;

import java.io.BufferedWriter;
import java.io.FileWriter;

import avrora.avrora.sim.AtmelInterpreter;
import avrora.avrora.sim.Simulator;
import avrora.avrora.sim.State;
import avrora.cck.text.StringUtil;
import avrora.cck.text.Terminal;

/**
 * <code>MemPrint</code> is a simple utility that allows the simulated program
 * to send output to the screen or log it into a file
 *
 * @author John Regehr
 * @author Rodolfo de Paz
 */
public class MemPrint extends Simulator.Watch.Empty
{

    int base;
    int max;
    String log;


    public MemPrint(int b, int m, String l)
    {
        base = b;
        max = m;
        log = l;
        // Open file first time without append mode
        if (!log.equals(""))
        {
            try
            {
               FileWriter foo = new FileWriter(log);
               // TODO: foo.close(() is a hoftix
               foo.close();
            }
            catch (Exception e)
            {// Catch exception if any
                System.err.println("Error: " + e.getMessage());
            }
        }
    }


    static private int getInt16(AtmelInterpreter a, int offset)
    {
        final int l = a.getDataByte(offset);
        final int h = a.getDataByte(offset + 1);
        return ((h & 0xff) << 8) + (l & 0xff);
    }


    @Override
    public void fireBeforeWrite(State state, int data_addr, byte value)
    {
        if (data_addr != base)
        {
            Terminal.printRed("Unexpected interception by printer!");
        }

        Simulator sim = state.getSimulator();
        AtmelInterpreter a = (AtmelInterpreter) sim.getInterpreter();
        StringBuffer buf = new StringBuffer();
        StringBuffer fil = new StringBuffer();
        SimUtil.getIDTimeString(buf, sim);
        boolean ret = false;// indicates that it is a return line
        switch (value)
        {
        case 0x1:// for 2byte hexadecimals
        case 0x3:// for 2byte integers
            final int v = getInt16(a, base + 1);
            if (value == 0x1)
            {
                fil.append(StringUtil.toHex(v, 4));
                buf.append(StringUtil.toHex(v, 4));
                break;
            }
            if (value == 0x3)
            {
                fil.append(v);
                buf.append(v);
                break;
            }
            break;
        case 0x6:// for string pointers
            final int strAddr = getInt16(a, base + 1);
            for (int i = 0; i <= max; i++)
            {
                byte b = a.getDataByte(strAddr + i);
                if (b == 0)
                    break;// break if end of string
                fil.append(String.valueOf((char) b));
                if (b != 10)
                    buf.append(String.valueOf((char) b));// not return char
                else if (i == 0)
                    ret = true;// return line
            }
            break;
        case 0x7:// for binary hex dumps
            final int bufAddr = getInt16(a, base + 1);
            final int bufLen = getInt16(a, base + 3);
            for (int i = 0; i < bufLen; i++)
            {
                final byte b = a.getDataByte(bufAddr + i);
                if (i > 0)
                {
                    fil.append(" ");
                    buf.append(" ");
                }
                fil.append(StringUtil.toHex(b, 2));
                buf.append(StringUtil.toHex(b, 2));
            }
            break;
        case 0x4:// for 4byte hexadecimals
        case 0x5:// for 4byte integers
            long r = ((a.getDataByte(base + 4) & 0xff) << 24)
                    + ((a.getDataByte(base + 3) & 0xff) << 16)
                    + ((a.getDataByte(base + 2) & 0xff) << 8)
                    + (a.getDataByte(base + 1) & 0xff) & 0xffffffff;
            if (value == 0x4)
            {
                fil.append(StringUtil.toHex(r, 8));
                buf.append(StringUtil.toHex(r, 8));
                break;
            }
            if (value == 0x5)
            {
                fil.append(r);
                buf.append(r);
                break;
            }
            break;
        case 0x2:// for strings
        default:// for already formatted variables (i.e. TinyOS printf)
            for (int i = 0; i <= max; i++)
            {
                byte b = a.getDataByte(base + i + 1);
                if (b == 0)
                    break;// break if end of string
                fil.append(String.valueOf((char) b));
                if (b != 10)
                    buf.append(String.valueOf((char) b));// not return char
                else if (i == 0)
                    ret = true;// return line
            }
            break;
        }

        synchronized (Terminal.class)
        {
            if (!ret && fil.length() != 0)
                Terminal.println(buf.toString());// print in screen if not
                                                 // return line and something to
                                                 // print
            if (!log.equals(""))
                printToFile(fil.toString());// print in file
        }
    }


    /**
     * The <code>PrintToFile</code> method prints to a file the character sent
     * to it
     * 
     * @param str
     *            string to print
     */
    public void printToFile(String str)
    {
        try
        {
            BufferedWriter out = new BufferedWriter(new FileWriter(log, true));
            // Str = ControlChars(Str);
            out.write(str);
            out.close();
        }
        catch (Exception e)
        {// Catch exception if any
            System.err.println("Error: " + e.getMessage());
        }
    }
}
