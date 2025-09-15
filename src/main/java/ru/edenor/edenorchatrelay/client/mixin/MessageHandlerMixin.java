package ru.edenor.edenorchatrelay.client.mixin;

import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.edenor.edenorchatrelay.MinecraftMessageHandler;


@Mixin(ClientPlayNetworkHandler.class)
public class MessageHandlerMixin {

    @Inject(method = "onGameMessage", at = @At(value = "TAIL"))
    private void addChatMessage(GameMessageS2CPacket packet, CallbackInfo ci) {
        var rawMessage = packet.content().getString();
        MinecraftMessageHandler.onMinecraftMessage(rawMessage);
    }
}
