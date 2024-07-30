package net.ukrounay.elementalsmithing.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.BookEditScreen;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.recipebook.ClientRecipeBook;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.stat.StatHandler;
import net.minecraft.util.Hand;
import net.ukrounay.elementalsmithing.item.ModItems;
import net.ukrounay.elementalsmithing.util.ModTags;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//@Mixin(ClientPlayerEntity.class)
//public abstract class ClientPlayerEntityMixin extends AbstractClientPlayerEntity {
//
//	@Final
//	@Shadow
//    protected MinecraftClient client;
//
//	public ClientPlayerEntityMixin(ClientWorld world, GameProfile profile) {
//		super(world, profile);
//	}
//
//	@Inject(at = @At("RETURN"), method = "useBook")
//	protected void onUseBook(ItemStack book, Hand hand, CallbackInfo info) {
//		if (book.isIn(ModTags.Items.READABLE_BOOKS)) {
//			client.setScreen(new BookEditScreen(client.player, book, hand));
//		}
//	}
//}