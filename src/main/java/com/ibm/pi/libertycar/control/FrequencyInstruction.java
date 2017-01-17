package com.ibm.pi.libertycar.control;

public class FrequencyInstruction {

    private int channel;
    private int on;
    private int off;

    public FrequencyInstruction(int channel, int on, int off) {
        this.channel = channel;
        this.on = on;
        this.off = off;
    }

    public int getChannel() {
        return channel;
    }

    public int getOn() {
        return on;
    }

    public int getOff() {
        return off;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + channel;
        result = prime * result + off;
        result = prime * result + on;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof FrequencyInstruction))
            return false;
        FrequencyInstruction other = (FrequencyInstruction) obj;
        if (channel != other.channel)
            return false;
        if (off != other.off)
            return false;
        if (on != other.on)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "FrequencyInstruction [channel=" + channel + ", on=" + on + ", off=" + off + "]";
    }

}
