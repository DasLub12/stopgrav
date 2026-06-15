package com.stopgrav;

import com.stopgrav.item.ModItems;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StopGrav implements ModInitializer {
    public static final String MOD_ID = "stopgrav";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        ModItems.register();
        LOGGER.info("StopGrav mod loaded!");
    }
}
