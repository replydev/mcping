package org.replydev.mcping.model;

import com.dslplatform.json.CompiledJson;
import lombok.Value;

import java.util.List;

/**
 * Players information
 */
@Value
@CompiledJson
class Players {
    int max;
    int online;
    List<Player> sample;
}
