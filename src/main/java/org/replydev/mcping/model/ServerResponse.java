package org.replydev.mcping.model;

import com.dslplatform.json.CompiledJson;
import lombok.Value;

@Value
@CompiledJson
public class ServerResponse {
    Version version;
    Players players;
    Description description;
    String favicon;
    boolean previewChat;
    boolean enforcesSecureChat;
}
