package cx.rain.mc.nbtedit.forge.networking;

import cx.rain.mc.nbtedit.NBTEdit;
import cx.rain.mc.nbtedit.api.netowrking.INBTEditNetworking;
import cx.rain.mc.nbtedit.forge.networking.packet.*;
import cx.rain.mc.nbtedit.utility.Constants;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

/**
 * Created by Jay113355 on 6/28/2016.
 * Edited by qyl27 on 2022.9.19.
 */
public class NBTEditNetworkingImpl implements INBTEditNetworking {
	private static SimpleChannel CHANNEL;

	private static final ResourceLocation CHANNEL_RL = new ResourceLocation(NBTEdit.MODID, "editing");

	private static int ID = 0;

	public NBTEditNetworkingImpl() {
		CHANNEL = NetworkRegistry.newSimpleChannel(CHANNEL_RL,
				() -> NBTEdit.VERSION,
				(version) -> version.equals(NBTEdit.VERSION),
				(version) -> version.equals(NBTEdit.VERSION)
		);

		registerMessages();
	}

	private static synchronized int nextId() {
		return ID++;
	}

	private void registerMessages() {
		NBTEdit.getInstance().getLogger().info("Register networking.");

		CHANNEL.messageBuilder(S2CRayTracePacket.class, nextId())
				.encoder(S2CRayTracePacket::toBytes)
				.decoder(S2CRayTracePacket::new)
				.consumer(S2CRayTracePacket::clientHandle)
				.add();


		CHANNEL.messageBuilder(C2SEntityEditingRequestPacket.class, nextId())
				.encoder(C2SEntityEditingRequestPacket::toBytes)
				.decoder(C2SEntityEditingRequestPacket::new)
				.consumer(C2SEntityEditingRequestPacket::serverHandle)
				.add();

		CHANNEL.messageBuilder(C2SBlockEntityEditingRequestPacket.class, nextId())
				.encoder(C2SBlockEntityEditingRequestPacket::toBytes)
				.decoder(C2SBlockEntityEditingRequestPacket::new)
				.consumer(C2SBlockEntityEditingRequestPacket::serverHandle)
				.add();

		CHANNEL.messageBuilder(C2SItemStackEditingRequestPacket.class, nextId())
				.encoder(C2SItemStackEditingRequestPacket::toBytes)
				.decoder(C2SItemStackEditingRequestPacket::new)
				.consumer(C2SItemStackEditingRequestPacket::serverHandle)
				.add();


		CHANNEL.messageBuilder(S2COpenEntityEditingGuiPacket.class, nextId())
				.encoder(S2COpenEntityEditingGuiPacket::toBytes)
				.decoder(S2COpenEntityEditingGuiPacket::new)
				.consumer(S2COpenEntityEditingGuiPacket::clientHandle)
				.add();

		CHANNEL.messageBuilder(S2COpenBlockEntityEditingGuiPacket.class, nextId())
				.encoder(S2COpenBlockEntityEditingGuiPacket::toBytes)
				.decoder(S2COpenBlockEntityEditingGuiPacket::new)
				.consumer(S2COpenBlockEntityEditingGuiPacket::clientHandle)
				.add();

		CHANNEL.messageBuilder(S2COpenItemStackEditingGuiPacket.class, nextId())
				.encoder(S2COpenItemStackEditingGuiPacket::toBytes)
				.decoder(S2COpenItemStackEditingGuiPacket::new)
				.consumer(S2COpenItemStackEditingGuiPacket::clientHandle)
				.add();


		CHANNEL.messageBuilder(C2SEntitySavingPacket.class, nextId())
				.encoder(C2SEntitySavingPacket::toBytes)
				.decoder(C2SEntitySavingPacket::new)
				.consumer(C2SEntitySavingPacket::serverHandle)
				.add();

		CHANNEL.messageBuilder(C2SBlockEntitySavingPacket.class, nextId())
				.encoder(C2SBlockEntitySavingPacket::toBytes)
				.decoder(C2SBlockEntitySavingPacket::new)
				.consumer(C2SBlockEntitySavingPacket::serverHandle)
				.add();

		CHANNEL.messageBuilder(C2SItemStackSavingPacket.class, nextId())
				.encoder(C2SItemStackSavingPacket::toBytes)
				.decoder(C2SItemStackSavingPacket::new)
				.consumer(C2SItemStackSavingPacket::serverHandle)
				.add();
	}

	@Override
	public void serverRayTraceRequest(ServerPlayer player) {
		CHANNEL.sendTo(new S2CRayTracePacket(), player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
	}

	@Override
	public void clientOpenGuiRequest(Entity entity, boolean self) {
		CHANNEL.sendToServer(new C2SEntityEditingRequestPacket(entity.getUUID(), entity.getId(), self));
	}

	@Override
	public void clientOpenGuiRequest(BlockPos pos) {
		CHANNEL.sendToServer(new C2SBlockEntityEditingRequestPacket(pos));
	}

	@Override
	public void clientOpenGuiRequest(ItemStack stack) {
		CHANNEL.sendToServer(new C2SItemStackEditingRequestPacket(stack));
	}

	@Override
	public void serverOpenClientGui(ServerPlayer player, Entity entity) {
		if (NBTEdit.getInstance().getPermission().hasPermission(player)) {
			if (entity instanceof Player && !NBTEdit.getInstance().getConfig().canEditOthers()) {
				NBTEdit.getInstance().getLogger().info("Player " + player.getName().getString() +
						" tried to use /nbtedit on a player. But config is not allow that.");
				player.createCommandSourceStack().sendFailure(new TranslatableComponent(Constants.MESSAGE_CANNOT_EDIT_OTHER_PLAYER)
						.withStyle(ChatFormatting.RED));
				return;
			}

			NBTEdit.getInstance().getLogger().info("Player " + player.getName().getString() +
					" is editing entity " + entity.getUUID() + ".");
			player.getServer().execute(() -> {
				var tag = entity.serializeNBT();
				CHANNEL.sendTo(new S2COpenEntityEditingGuiPacket(entity.getUUID(), entity.getId(), tag, false),
						player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
			});
		} else {
			player.createCommandSourceStack().sendFailure(new TranslatableComponent(Constants.MESSAGE_NO_PERMISSION)
					.withStyle(ChatFormatting.RED));
		}
	}

	@Override
	public void serverOpenClientGui(ServerPlayer player, BlockPos pos) {
		if (NBTEdit.getInstance().getPermission().hasPermission(player)) {
			NBTEdit.getInstance().getLogger().info("Player " + player.getName().getString() +
					" is editing block at XYZ " + pos.getX() + " " +	pos.getY() + " " + pos.getZ() + ".");
			var blockEntity = player.getLevel().getBlockEntity(pos);
			if (blockEntity != null) {
				var tag = blockEntity.serializeNBT();
				CHANNEL.sendTo(new S2COpenBlockEntityEditingGuiPacket(pos, tag),
						player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
			} else {
				player.createCommandSourceStack().sendFailure(new TranslatableComponent(Constants.MESSAGE_TARGET_IS_NOT_BLOCK_ENTITY)
						.withStyle(ChatFormatting.RED));
			}
		} else {
			player.createCommandSourceStack().sendFailure(new TranslatableComponent(Constants.MESSAGE_NO_PERMISSION)
					.withStyle(ChatFormatting.RED));
		}
	}

	@Override
	public void serverOpenClientGui(ServerPlayer player) {
		if (NBTEdit.getInstance().getPermission().hasPermission(player)) {
			NBTEdit.getInstance().getLogger().info("Player " + player.getName().getString() + " is editing itself.");
			player.getServer().execute(() -> {
				var tag = player.serializeNBT();
				CHANNEL.sendTo(new S2COpenEntityEditingGuiPacket(player.getUUID(), player.getId(), tag, true),
						player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
			});
		} else {
			player.createCommandSourceStack().sendFailure(new TranslatableComponent(Constants.MESSAGE_NO_PERMISSION)
					.withStyle(ChatFormatting.RED));
		}
	}

	@Override
	public void serverOpenClientGui(ServerPlayer player, ItemStack stack) {
		if (NBTEdit.getInstance().getPermission().hasPermission(player)) {
			NBTEdit.getInstance().getLogger().info("Player " + player.getName().getString() +
					" is editing ItemStack named " + stack.getDisplayName().getString() + ".");
			player.getServer().execute(() -> {
				var tag = stack.serializeNBT();
				CHANNEL.sendTo(new S2COpenItemStackEditingGuiPacket(stack, tag),
						player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
			});
		} else {
			player.createCommandSourceStack().sendFailure(new TranslatableComponent(Constants.MESSAGE_NO_PERMISSION)
					.withStyle(ChatFormatting.RED));
		}
	}

	@Override
	public void saveEditing(Entity entity, CompoundTag tag, boolean self) {
		CHANNEL.sendToServer(new C2SEntitySavingPacket(entity.getUUID(), entity.getId(), tag, self));
	}

	@Override
	public void saveEditing(BlockPos pos, CompoundTag tag) {
		CHANNEL.sendToServer(new C2SBlockEntitySavingPacket(pos, tag));
	}

	@Override
	public void saveEditing(ItemStack stack, CompoundTag tag) {
		CHANNEL.sendToServer(new C2SItemStackSavingPacket(stack, tag));
	}
}
