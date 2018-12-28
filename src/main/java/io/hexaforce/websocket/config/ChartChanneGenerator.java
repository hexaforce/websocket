package io.hexaforce.websocket.config;

import java.util.Set;

import org.springframework.stereotype.Component;

import com.google.common.collect.Sets;

import io.hexaforce.websocket.config.AbstractTextWebSocketHandler.DataChanneGenerator;
import io.hexaforce.websocket.model.DataChannel1;
import io.hexaforce.websocket.model.SessionGroup;

@Component
public class ChartChanneGenerator implements DataChanneGenerator<SessionGroup, DataChannel1> {

    @Override
    public SessionGroup getSessionGroup() {
        return SessionGroup.BUY;
    }

    @Override
    public Set<DataChannel1> channeList() {
        return Sets.newHashSet(DataChannel1.values());
    }
    
}
