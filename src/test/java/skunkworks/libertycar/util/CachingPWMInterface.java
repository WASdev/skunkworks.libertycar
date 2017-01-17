package skunkworks.libertycar.util;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;

import com.ibm.pi.libertycar.control.FrequencyInstruction;
import com.ibm.pi.libertycar.driver.PWMInterface;

public class CachingPWMInterface implements PWMInterface {
    
    Deque<FrequencyInstruction> cache = new ArrayDeque<FrequencyInstruction>();

    @Override
    public void setPWM(int channel, int on, int off) throws IOException {
        FrequencyInstruction instruction = new FrequencyInstruction(channel, on, off);
        cache.add(instruction);

    }

    public Deque<FrequencyInstruction> getInstructionCache() {
        return new ArrayDeque<FrequencyInstruction>(cache);
    }

}
