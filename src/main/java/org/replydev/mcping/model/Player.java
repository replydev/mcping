package org.replydev.mcping.model;

import com.dslplatform.json.CompiledJson;
import lombok.Value;


/**
 * Player connected to a server
 */
@Value
@CompiledJson
public class Player {
    String id;
    String name;
}
