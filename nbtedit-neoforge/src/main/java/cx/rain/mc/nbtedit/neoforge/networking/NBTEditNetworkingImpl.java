package cx.rain.mc.nbtedit.neoforge.networking;

import cx.rain.mc.nbtedit.NBTEdit;
import cx.rain.mc.nbtedit.api.netowrking.INBTEditNetworking;
import cx.rain.mc.nbtedit.neoforge.networking.packet.*;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlerEvent;

@Mod.EventBusSubscriber(modid = NBTEdit.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class NBTEditNetworkingImpl implements INBTEditNetworking {

	public static final ResourceLocation C2S_BLOCK_ENTITY_EDITING_PACKET_ID = new ResourceLocation(NBTEdit.MODID, "c2s_block_entity_editing_request");
	public static final ResourceLocation C2S_BLOCK_ENTITY_SAVING_PACKET_ID = new ResourceLocation(NBTEdit.MODID, "c2s_block_entity_saving_request");
	public static final ResourceLocation C2S_ENTITY_EDITING_PACKET_ID = new ResourceLocation(NBTEdit.MODID, "c2s_entity_editing_request");
	public static final ResourceLocation C2S_ENTITY_SAVING_PACKET_ID = new ResourceLocation(NBTEdit.MODID, "c2s_entity_saving_request");
	public static final ResourceLocation C2S_ITEM_STACK_EDITING_PACKET_ID = new ResourceLocation(NBTEdit.MODID, "c2s_item_stack_editing_request");
	public static final ResourceLocation C2S_ITEM_STACK_SAVING_PACKET_ID = new ResourceLocation(NBTEdit.MODID, "c2s_item_stack_saving_request");

	public static final ResourceLocation S2C_OPEN_BLOCK_ENTITY_EDITING_PACKET_ID = new ResourceLocation(NBTEdit.MODID, "s2c_open_block_entity_editing");
	public static final ResourceLocation S2C_OPEN_ENTITY_EDITING_PACKET_ID = new ResourceLocation(NBTEdit.MODID, "s2c_open_entity_editing");
	public static final ResourceLocation S2C_OPEN_ITEM_STACK_EDITING_PACKET_ID = new ResourceLocation(NBTEdit.MODID, "s2c_open_item_stack_editing");
	public static final ResourceLocation S2C_RAY_TRACE_REQUEST_PACKET_ID = new ResourceLocation(NBTEdit.MODID, "s2c_ray_trace_request");

	@SubscribeEvent
	public static void register(RegisterPayloadHandlerEvent event) {
		var registrar = event.registrar(NBTEdit.MODID);

		registrar.play(C2S_BLOCK_ENTITY_EDITING_PACKET_ID, C2SBlockEntityEditingRequestPacket::new, handler -> handler.server(C2SBlockEntityEditingRequestPacket::handle));
		registrar.play(C2S_BLOCK_ENTITY_SAVING_PACKET_ID, C2SBlockEntitySavingPacket::new, handler -> handler.server(C2SBlockEntitySavingPacket::handle));
		registrar.play(C2S_ENTITY_EDITING_PACKET_ID, C2SEntityEditingRequestPacket::new, handler -> handler.server(C2SEntityEditingRequestPacket::handle));
		registrar.play(C2S_ENTITY_SAVING_PACKET_ID, C2SEntitySavingPacket::new, handler -> handler.server(C2SEntitySavingPacket::handle));
		registrar.play(C2S_ITEM_STACK_EDITING_PACKET_ID, C2SItemStackEditingRequestPacket::new, handler -> handler.server(C2SItemStackEditingRequestPacket::handle));
		registrar.play(C2S_ITEM_STACK_SAVING_PACKET_ID, C2SItemStackSavingPacket::new, handler -> handler.server(C2SItemStackSavingPacket::handle));

		registrar.play(S2C_OPEN_BLOCK_ENTITY_EDITING_PACKET_ID, S2COpenBlockEntityEditingGuiPacket::new, handler -> handler.client(S2COpenBlockEntityEditingGuiPacket::handle));
		registrar.play(S2C_OPEN_ENTITY_EDITING_PACKET_ID, S2COpenEntityEditingGuiPacket::new, handler -> handler.client(S2COpenEntityEditingGuiPacket::handle));
		registrar.play(S2C_OPEN_ITEM_STACK_EDITING_PACKET_ID, S2COpenItemStackEditingGuiPacket::new, handler -> handler.client(S2COpenItemStackEditingGuiPacket::handle));
		registrar.play(S2C_RAY_TRACE_REQUEST_PACKET_ID, S2CRayTracePacket::new, handler -> handler.client(S2CRayTracePacket::handle));
	}

	public NBTEditNetworkingImpl() {
	}

	@Override
	public void serverRayTraceRequest(ServerPlayer player) {
		player.connection.send(new S2CRayTracePacket());
	}

	@Override
	public void clientOpenGuiRequest(Entity entity, boolean self) {
		var connection = Minecraft.getInstance().getConnection();
		if (connection != null) {
			connection.send(new C2SEntityEditingRequestPacket(entity.getUUID(), entity.getId(), self));
		}
	}

	@Override
	public void clientOpenGuiRequest(BlockPos pos) {
		var connection = Minecraft.getInstance().getConnection();
		if (connection != null) {
			connection.send(new C2SBlockEntityEditingRequestPacket(pos));
		}
	}

	@Override
	public void clientOpenGuiRequest(ItemStack stack) {
		var connection = Minecraft.getInstance().getConnection();
		if (connection != null) {
			connection.send(new C2SItemStackEditingRequestPacket(stack));
		}
	}

	@Override
	public void serverOpenClientGui(ServerPlayer player, Entity entity) {
		var tag = entity.serializeNBT();
		player.connection.send(new S2COpenEntityEditingGuiPacket(entity.getUUID(), entity.getId(), tag, false));
	}

	@Override
	public void serverOpenClientGui(ServerPlayer player, BlockPos pos, BlockEntity blockEntity) {
		var tag = blockEntity.serializeNBT();
		player.connection.send(new S2COpenBlockEntityEditingGuiPacket(pos, tag));
	}

	@Override
	public void serverOpenClientGui(ServerPlayer player) {
		var tag = player.serializeNBT();
		player.connection.send(new S2COpenEntityEditingGuiPacket(player.getUUID(), player.getId(), tag, true));
	}

	@Override
	public void serverOpenClientGui(ServerPlayer player, ItemStack stack) {
		var tag = stack.save(new CompoundTag());
		player.connection.send(new S2COpenItemStackEditingGuiPacket(stack, tag));
	}

	@Override
	public void saveEditing(Entity entity, CompoundTag tag, boolean self) {
		var connection = Minecraft.getInstance().getConnection();
		if (connection != null) {
			connection.send(new C2SEntitySavingPacket(entity.getUUID(), entity.getId(), tag, self));
		}
	}

	@Override
	public void saveEditing(BlockPos pos, CompoundTag tag) {
		var connection = Minecraft.getInstance().getConnection();
		if (connection != null) {
			connection.send(new C2SBlockEntitySavingPacket(pos, tag));
		}
	}

	@Override
	public void saveEditing(ItemStack stack, CompoundTag tag) {
		var connection = Minecraft.getInstance().getConnection();
		if (connection != null) {
			connection.send(new C2SItemStackSavingPacket(stack, tag));
		}
	}
}