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

package avrora.avrora.stack.isea;

import java.util.Arrays;

import avrora.cck.text.StringUtil;
import avrora.cck.text.Terminal;
import avrora.cck.util.Util;

/**
 * @author Ben L. Titzer
 */
public class ISEAbstractState
{

    public void merge(ISEAbstractState s)
    {
        if (depth != s.depth)
            throw Util.failure("stack height mismatch");
        // merge elements (registers)
        for (int cntr = 0; cntr < elements.length; cntr++)
        {
            elements[cntr].read = elements[cntr].read || s.elements[cntr].read;
            elements[cntr].value = ISEValue.merge(elements[cntr].value,
                    s.elements[cntr].value);
        }
        // merge stack contents
        for (int cntr = 0; cntr < depth; cntr++)
        {
            stack[cntr] = ISEValue.merge(stack[cntr], s.stack[cntr]);
        }
    }


    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + depth;
        result = prime * result + Arrays.hashCode(elements);
        result = prime * result + Arrays.hashCode(stack);
        return result;
    }


    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ISEAbstractState other = (ISEAbstractState) obj;
        if (depth != other.depth)
            return false;
        if (!Arrays.equals(elements, other.elements))
            return false;
        if (!Arrays.equals(stack, other.stack))
            return false;
        return true;
    }


    public ISEAbstractState copy()
    {
        return new ISEAbstractState(elements, stack, depth);
    }


    public void push(byte val)
    {
        stack[depth++] = val;
    }


    public byte pop()
    {
        return stack[--depth];
    }


    public void print(int pc)
    {
        Terminal.print(StringUtil.addrToString(pc) + ": ");
        for (int cntr = 0; cntr < elements.length; cntr++)
        {
            Element e = elements[cntr];
            String star = e.read ? "*" : "";
            String str = star + ISEValue.toString(e.value);
            Terminal.print(StringUtil.rightJustify(str, 4));
            if (cntr % 16 == 15)
                nextln();
        }
        nextln();
        Terminal.print("(");
        for (int cntr = depth; cntr > 0; cntr--)
        {
            Terminal.print(ISEValue.toString(stack[cntr - 1]));
            if (cntr > 1)
                Terminal.print(", ");
        }
        Terminal.print(")");

        Terminal.nextln();
    }


    protected void nextln()
    {
        Terminal.print("\n        ");
    }

    public static class Element
    {
        public final String name;
        boolean read;
        byte value;


        public Element(String n, byte val, boolean r)
        {
            name = n;
            value = val;
            read = r;
        }


        public Element copy()
        {
            return new Element(name, value, read);
        }


        @Override
        public int hashCode()
        {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((name == null) ? 0 : name.hashCode());
            result = prime * result + (read ? 1231 : 1237);
            result = prime * result + value;
            return result;
        }


        @Override
        public boolean equals(Object obj)
        {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Element other = (Element) obj;
            if (name == null)
            {
                if (other.name != null)
                    return false;
            } else if (!name.equals(other.name))
                return false;
            if (read != other.read)
                return false;
            if (value != other.value)
                return false;
            return true;
        }
    }


    public byte readElement(int element)
    {
        elements[element].read = true;
        return elements[element].value;
    }


    public byte getElement(int element)
    {
        return elements[element].value;
    }


    public void writeElement(int element, byte val)
    {
        elements[element].value = val;
    }


    public boolean isRead(int element)
    {
        return elements[element].read;
    }

    final Element[] elements;
    final byte[] stack;
    int depth;


    public ISEAbstractState(Element[] e, byte[] nstack, int ndepth)
    {
        elements = new Element[e.length];
        for (int cntr = 0; cntr < e.length; cntr++)
        {
            Element et = e[cntr];
            elements[cntr] = new Element(et.name, et.value, et.read);
        }
        stack = new byte[nstack.length];
        System.arraycopy(nstack, 0, stack, 0, ndepth);
        depth = ndepth;
    }
}
