package org.replydev.mcping.model;

import com.dslplatform.json.CompiledJson;
import lombok.Value;

@Value
@CompiledJson
public class ChatObject {
    String text;
    String color;
    boolean bold;
}
