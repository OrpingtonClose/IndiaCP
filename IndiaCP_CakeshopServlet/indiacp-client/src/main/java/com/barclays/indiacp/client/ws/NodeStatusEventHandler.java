package main.java.com.barclays.indiacp.client.ws;

import com.fasterxml.jackson.databind.JavaType;
import com.google.common.reflect.TypeToken;
import main.java.com.barclays.indiacp.client.model.Node;
import main.java.com.barclays.indiacp.client.model.res.APIData;
import main.java.com.barclays.indiacp.client.model.res.APIResponse;

public abstract class NodeStatusEventHandler extends EventHandler<Node> {

    @SuppressWarnings("serial")
    private static final TypeToken<APIResponse<APIData<Node>, Node>> typeToken =
            new TypeToken<APIResponse<APIData<Node>, Node>>() {};

    private static final JavaType valType = objectMapper.constructType(typeToken.getType());

    public static final String TOPIC = "/topic/node/status";

    @Override
    public String getTopic() {
        return TOPIC;
    }

    @Override
    public JavaType getValType() {
        return valType;
    }

}
