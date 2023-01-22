package org.replydev.mcping.model;

import com.dslplatform.json.CompiledJson;
import lombok.Value;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@CompiledJson
@Value
public class Description {
    String text;
    List<ChatObject> extra;
    private static final Pattern descriptionPattern = Pattern.compile("§[0-9a-r]");

    public String getText() {
        StringBuilder stringBuilder = new StringBuilder(text);
        if (extra != null) {
            extra.forEach((chatObject -> stringBuilder.append(chatObject.getText())));
        }
        Matcher stylesMatcher = descriptionPattern.matcher(stringBuilder);
        return stylesMatcher.replaceAll("");
    }
}
