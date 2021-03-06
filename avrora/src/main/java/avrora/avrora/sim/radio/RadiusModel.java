/**
 * Copyright (c) 2007, Ben L. Titzer
 * See the file "license.txt" for details.
 *
 * Created Nov 14, 2007
 */
package avrora.avrora.sim.radio;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import avrora.avrora.sim.radio.Medium.TXRX;
import avrora.avrora.sim.radio.Topology.Position;

/**
 * The <code>RadiusModel</code> definition.
 *
 * @author Ben L. Titzer
 */
public class RadiusModel implements Medium.Arbitrator
{

    protected final double minimumDistance;
    protected final double minimumDistanceSq;
    protected final double maximumDistance;
    protected final double maximumDistanceSq;
    protected final Map<TXRX, Position> positions;


    public RadiusModel(double minDist, double maxDist)
    {
        maximumDistance = maxDist;
        maximumDistanceSq = maxDist * maxDist;
        minimumDistance = minDist;
        minimumDistanceSq = minDist * minDist;
        positions = new HashMap<TXRX, Position>();
    }


    @Override
    public int getNoise(int index)
    {
        return (-90);

    }


    @Override
    public double computeReceivedPower(Medium.Transmission t,
            Medium.Receiver receiver, int Milliseconds)
    {

        return (0);
    }


    @Override
    public boolean lockTransmission(Medium.Receiver receiver,
            Medium.Transmission trans, int Milliseconds)
    {
        return distanceSq(trans.origin, receiver) <= maximumDistanceSq;
    }


    @Override
    public char mergeTransmissions(Medium.Receiver receiver,
            List<Medium.Transmission> it, long bit, int Milliseconds)
    {
        assert it.size() > 0;
        boolean one = false;
        int value = 0;
        for (Medium.Transmission next : it)
        {
            if (lockTransmission(receiver, next, Milliseconds))
            {
                if (one)
                {
                    int nval = 0xff & next.getByteAtTime(bit);
                    value |= (nval << 8) ^ (value << 8); // compute corrupted
                                                         // bits
                    value |= nval;
                } else
                {
                    one = true;
                    value = 0xff & next.getByteAtTime(bit);
                }

            }
        }
        assert one;
        return (char) value;
    }


    public void setPosition(Radio radio, Topology.Position pos)
    {
        positions.put(radio.getTransmitter(), pos);
        positions.put(radio.getReceiver(), pos);
    }


    protected double distanceSq(Medium.Transmitter t, Medium.Receiver r)
    {
        double distSq = 0;
        Topology.Position a = positions.get(t);
        Topology.Position b = positions.get(r);
        if (a != null && b != null)
        {
            double dx = a.x - b.x;
            double dy = a.y - b.y;
            double dz = a.z - b.z;
            // no need to take the square root if we are just checking a
            // threshold.
            distSq = dx * dx + dy * dy + dz * dz;
        }
        if (distSq < minimumDistance)
            return minimumDistanceSq;
        else
            return distSq;
    }
}
