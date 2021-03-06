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

package avrora.avrora.sim.platform;

import java.util.HashMap;

import avrora.avrora.sim.mcu.Microcontroller;

/**
 * The <code>Platform</code> interface represents both a microcontroller
 * instance and the devices connected to it. These two things together describe
 * a node and its capabilities.
 *
 * @author Ben L. Titzer
 */
public abstract class Platform
{

    protected final Microcontroller mcu;
    protected final HashMap<String, Object> devices;


    protected Platform(Microcontroller m)
    {
        mcu = m;
        devices = new HashMap<String, Object>();
        mcu.setPlatform(this);
    }


    /**
     * The <code>getMicrocontroller()</code> method returns a reference to the
     * microcontroller that is driving this platform.
     * 
     * @return a reference to the microcontroller in this platform
     */
    public Microcontroller getMicrocontroller()
    {
        return mcu;
    }


    /**
     * The <code>addDevice()</code> method is used by subclasses of
     * <code>Platform</code> to add external devices that are connected to the
     * microcontroller.
     * 
     * @param name
     *            the name of the device as a string
     * @param o
     *            the object representing the device
     */
    protected void addDevice(String name, Object o)
    {
        devices.put(name, o);
    }


    /**
     * The <code>getDevice()</code> method looks up a device attached to this
     * platform. This device might be a radio, a sensor, etc.
     * 
     * @param name
     *            the name of the device as a string
     * @return an object representing the device if it exists; null otherwise
     */
    public Object getDevice(String name)
    {
        return devices.get(name);
    }
}
