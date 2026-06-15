package com.stopgrav.screen;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class StopwatchScreen extends Screen {
    private static final int GUI_WIDTH = 200;
    private static final int GUI_HEIGHT = 210;
    private String currentMode = "Normal";

    public StopwatchScreen() {
        super(Text.literal("Stopwatch"));
    }

    @Override
    protected void init() {
        int x = (this.width - GUI_WIDTH) / 2;
        int y = (this.height - GUI_HEIGHT) / 2;
        int btnW = 160;
        int btnH = 24;
        int startX = x + (GUI_WIDTH - btnW) / 2;

        this.addDrawableChild(ButtonWidget.builder(
            Text.literal("Stop Time"),
            btn -> {
                sendCommand("gamerule doDaylightCycle false");
                sendCommand("gamerule doWeatherCycle false");
                sendCommand("gamerule randomTickSpeed 0");
                currentMode = "Stopped";
            })
            .position(startX, y + 50).size(btnW, btnH).build());

        this.addDrawableChild(ButtonWidget.builder(
            Text.literal("Normal Time"),
            btn -> {
                sendCommand("gamerule doDaylightCycle true");
                sendCommand("gamerule doWeatherCycle true");
                sendCommand("gamerule randomTickSpeed 3");
                currentMode = "Normal";
            })
            .position(startX, y + 82).size(btnW, btnH).build());

        this.addDrawableChild(ButtonWidget.builder(
            Text.literal("Reverse Time"),
            btn -> {
                sendCommand("gamerule doDaylightCycle false");
                sendCommand("time add -200");
                currentMode = "Reversing";
            })
            .position(startX, y + 114).size(btnW, btnH).build());

        this.addDrawableChild(ButtonWidget.builder(
            Text.literal("Speed Up Time"),
            btn -> {
                sendCommand("gamerule randomTickSpeed 100");
                sendCommand("time add 1000");
                currentMode = "Fast";
            })
            .position(startX, y + 146).size(btnW, btnH).build());

        this.addDrawableChild(ButtonWidget.builder(
            Text.literal("Close"),
            btn -> this.close())
            .position(startX, y + 182).size(btnW, 18).build());
    }

    private void sendCommand(String command) {
        if (this.client != null && this.client.player != null) {
            this.client.player.networkHandler.sendChatCommand(command);
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context);
        int x = (this.width - GUI_WIDTH) / 2;
        int y = (this.height - GUI_HEIGHT) / 2;

        context.fill(x, y, x + GUI_WIDTH, y + GUI_HEIGHT, 0xEE000000);
        context.fill(x + 1, y + 1, x + GUI_WIDTH - 1, y + GUI_HEIGHT - 1, 0xFF1A1A2E);

        context.fill(x, y, x + GUI_WIDTH, y + 2, 0xFFFFD700);
        context.fill(x, y + GUI_HEIGHT - 2, x + GUI_WIDTH, y + GUI_HEIGHT, 0xFFFFD700);
        context.fill(x, y, x + 2, y + GUI_HEIGHT, 0xFFFFD700);
        context.fill(x + GUI_WIDTH - 2, y, x + GUI_WIDTH, y + GUI_HEIGHT, 0xFFFFD700);

        context.drawCenteredTextWithShadow(this.textRenderer, "Stopwatch", x + GUI_WIDTH / 2, y + 10, 0xFFFFD700);
        context.fill(x + 10, y + 22, x + GUI_WIDTH - 10, y + 23, 0xFF444466);
        context.drawCenteredTextWithShadow(this.textRenderer, "Mode: " + currentMode, x + GUI_WIDTH / 2, y + 34, 0xFFFFFFFF);

        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean shouldPause() { return false; }
}
