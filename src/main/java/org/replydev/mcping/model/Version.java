package org.replydev.mcping.model;

import com.dslplatform.json.CompiledJson;
import lombok.Value;

/**
 * Version of the MC Server
 */
@Value
@CompiledJson
public class Version {
    String name;
    int protocol;
}
