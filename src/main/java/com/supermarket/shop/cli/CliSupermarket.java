package com.supermarket.shop.cli;

import org.springframework.shell.jline.PromptProvider;

import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;
import org.springframework.stereotype.Component;

@Component
public class CliSupermarket implements PromptProvider {

    @Override
    public AttributedString getPrompt() {
        return new AttributedString("Supermarket:>",
                AttributedStyle.DEFAULT.foreground(AttributedStyle.BLUE)
        );
    }
}
