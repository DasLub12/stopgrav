package com.stopgrav.screen;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.math.Direction;
import org.anti_ad.mc.gravityapi.api.GravityApiClient;

public class AnchorScreen extends Screen {
    private static final int GUI_WIDTH = 240;
    private static final int GUI_HEIGHT = 320;
    private static final int TAB_GRAVITY = 0;
    private static final int TAB_CREATIVE = 1;
    private int activeTab = TAB_GRAVITY;
    private String currentGravity = "Normal";

    private ButtonWidget btnNormal, btnUp, btnNorth, btnSouth, btnWest, btnEast;
    private ButtonWidget btnLow, btnHigh, btnZero, btnCreative;

    public AnchorScreen() {
        super(Text.literal("Anchor"));
    }

    @Override
    protected void init() {
        int x = (this.width - GUI_WIDTH) / 2;
        int y = (this.height - GUI_HEIGHT) / 2;

        this.addDrawableChild(ButtonWidget.builder(
            Text.literal("Gravity"),
            btn -> { activeTab = TAB_GRAVITY; refreshButtons(); })
            .position(x + 5, y + 28).size(110, 20).build());

        this.addDrawableChild(ButtonWidget.builder(
            Text.literal("Creative"),
            btn -> { activeTab = TAB_CREATIVE; refreshButtons(); })
            .position(x + 125, y + 28).size(110, 20).build());

        int bx = x + 10;
        int bw = 100;
        int bh = 22;

        btnNormal = ButtonWidget.builder(Text.literal("Normal (Down)"),
            btn -> setGravity(Direction.DOWN, "Normal"))
            .position(bx, y + 60).size(bw, bh).build();
        this.addDrawableChild(btnNormal);

        btnUp = ButtonWidget.builder(Text.literal("Up"),
            btn -> setGravity(Direction.UP, "Up"))
            .position(bx + bw + 10, y + 60).size(bw, bh).build();
        this.addDrawableChild(btnUp);

        btnNorth = ButtonWidget.builder(Text.literal("North"),
            btn -> setGravity(Direction.NORTH, "North"))
            .position(bx, y + 90).size(bw, bh).build();
        this.addDrawableChild(btnNorth);

        btnSouth = ButtonWidget.builder(Text.literal("South"),
            btn -> setGravity(Direction.SOUTH, "South"))
            .position(bx + bw + 10, y + 90).size(bw, bh).build();
        this.addDrawableChild(btnSouth);

        btnWest = ButtonWidget.builder(Text.literal("West"),
            btn -> setGravity(Direction.WEST, "West"))
            .position(bx, y + 120).size(bw, bh).build();
        this.addDrawableChild(btnWest);

        btnEast = ButtonWidget.builder(Text.literal("East"),
            btn -> setGravity(Direction.EAST, "East"))
            .position(bx + bw + 10, y + 120).size(bw, bh).build();
        this.addDrawableChild(btnEast);

        btnLow = ButtonWidget.builder(Text.literal("Low Gravity"),
            btn -> {
                setGravity(Direction.DOWN, "Low");
                sendCommand("effect give @s slow_falling 99999 0 true");
                sendCommand("effect give @s jump_boost 99999 3 true");
            })
            .position(bx, y + 150).size(bw, bh).build();
        this.addDrawableChild(btnLow);

        btnHigh = ButtonWidget.builder(Text.literal("High Gravity"),
            btn -> {
                setGravity(Direction.DOWN, "High");
                sendCommand("effect give @s slowness 99999 2 true");
                sendCommand("effect clear @s slow_falling");
            })
            .position(bx + bw + 10, y + 150).size(bw, bh).build();
        this.addDrawableChild(btnHigh);

        btnZero = ButtonWidget.builder(Text.literal("Zero Gravity"),
            btn -> {
                currentGravity = "Zero";
                sendCommand("effect give @s levitation 99999 0 true");
            })
            .position(bx, y + 180).size(GUI_WIDTH - 20, bh).build();
        this.addDrawableChild(btnZero);

        btnCreative = ButtonWidget.builder(Text.literal("Open Creative Inventory"),
            btn -> { sendCommand("gamemode creative"); this.close(); })
            .position(x + 10, y + 70).size(GUI_WIDTH - 20, 28).build();
        this.addDrawableChild(btnCreative);

        this.addDrawableChild(ButtonWidget.builder(
            Text.literal("Close"),
            btn -> this.close())
            .position(x + 10, y + GUI_HEIGHT - 28).size(GUI_WIDTH - 20, 20).build());

        refreshButtons();
    }

    private void setGravity(Direction direction, String name) {
        currentGravity = name;
        if (this.client != null && this.client.player != null) {
            GravityApiClient.setGravityDirection(this.client.player, direction);
        }
    }

    private void sendCommand(String command) {
        if (this.client != null && this.client.player != null) {
            this.client.player.networkHandler.sendChatCommand(command);
        }
    }

    private void refreshButtons() {
        boolean g = activeTab == TAB_GRAVITY;
        btnNormal.visible = g;
        btnUp.visible = g;
        btnNorth.visible = g;
        btnSouth.visible = g;
        btnWest.visible = g;
        btnEast.visible = g;
        btnLow.visible = g;
        btnHigh.visible = g;
        btnZero.visible = g;
        btnCreative.visible = !g;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context);
        int x = (this.width - GUI_WIDTH) / 2;
        int y = (this.height - GUI_HEIGHT) / 2;

        context.fill(x, y, x + GUI_WIDTH, y + GUI_HEIGHT, 0xEE000000);
        context.fill(x + 1, y + 1, x + GUI_WIDTH - 1, y + GUI_HEIGHT - 1, 0xFF0D1B2A);

        context.fill(x, y, x + GUI_WIDTH, y + 2, 0xFF00BFFF);
        context.fill(x, y + GUI_HEIGHT - 2, x + GUI_WIDTH, y + GUI_HEIGHT, 0xFF00BFFF);
        context.fill(x, y, x + 2, y + GUI_HEIGHT, 0xFF00BFFF);
        context.fill(x + GUI_WIDTH - 2, y, x + GUI_WIDTH, y + GUI_HEIGHT, 0xFF00BFFF);

        context.drawCenteredTextWithShadow(this.textRenderer, "Anchor", x + GUI_WIDTH / 2, y + 8, 0xFF00BFFF);
        context.fill(x + 5, y + 50, x + GUI_WIDTH - 5, y + 51, 0xFF00BFFF);

        if (activeTab == TAB_GRAVITY) {
            context.drawCenteredTextWithShadow(this.textRenderer,
                "Gravity: " + currentGravity, x + GUI_WIDTH / 2, y + 210, 0xFFFFFFFF);
        } else {
            context.drawCenteredTextWithShadow(this.textRenderer,
                "Get any item even in survival!", x + GUI_WIDTH / 2, y + 110, 0xFFFFFFFF);
            context.drawCenteredTextWithShadow(this.textRenderer,
                "Requires OP permissions.", x + GUI_WIDTH / 2, y + 125, 0xFFFF5555);
        }

        context.fill(x + 5, y + GUI_HEIGHT - 32, x + GUI_WIDTH - 5, y + GUI_HEIGHT - 31, 0xFF00BFFF);

        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean shouldPause() { return false; }
}
