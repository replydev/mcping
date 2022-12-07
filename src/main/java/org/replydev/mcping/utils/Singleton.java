package org.replydev.mcping.utils;

import com.dslplatform.json.DslJson;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Singleton {
    public final DslJson<Object> dslJson = new DslJson<>();
}
