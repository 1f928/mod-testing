package com.example.examplemod;

import com.example.examplemod.items.BatItem;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("examplemod")
public class ExampleMod {
    public static final String MOD_ID = "examplemod";

    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, ExampleMod.MOD_ID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ExampleMod.MOD_ID);

    private static final RegistryObject<Block> TEST_BLOCK = BLOCKS.register(
        "test_block",
        () -> new Block(AbstractBlock.Properties
            .of(Material.STONE)
            .strength(5)
            .harvestLevel(2)
            .sound(SoundType.ANVIL))
    );

    private static final RegistryObject<BatItem> WOOD_BAT = ITEMS.register(
        "wood_bat",
        () -> new BatItem(ItemTier.WOOD, -2.4F, 0.5F, (new Item.Properties()).tab(ItemGroup.TAB_COMBAT))
    );

    private static final RegistryObject<BatItem> IRON_BAT = ITEMS.register(
        "iron_bat",
        () -> new BatItem(ItemTier.IRON,-2.4F, 1.0F, (new Item.Properties()).tab(ItemGroup.TAB_COMBAT))
    );

    private static final RegistryObject<BatItem> GOLD_BAT = ITEMS.register(
        "gold_bat",
        () -> new BatItem(ItemTier.GOLD, -2.4F, 1.5F, (new Item.Properties()).tab(ItemGroup.TAB_COMBAT))
    );

    private static final RegistryObject<Item> TEST_BLOCK_ITEM = ITEMS.register(
        "test_block",
        () -> new BlockItem(TEST_BLOCK.get(), new Item.Properties().tab(ItemGroup.TAB_MISC))
    );

    private static final RegistryObject<Item> TEST_ITEM = ITEMS.register(
        "test_item",
        () -> new Item(new Item.Properties().tab(ItemGroup.TAB_MISC))
    );

    public ExampleMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);

        // Register methods for mod loading
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::clientSetup);

        // Register for game events
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void batKnockback(LivingAttackEvent event) {
        if (
               event.getSource().getEntity() == null
            || !(event.getSource().getEntity() instanceof LivingEntity)
            || event.getSource().getEntity().level.isClientSide
            || !(((LivingEntity) event.getSource().getEntity()).getMainHandItem().getItem() instanceof BatItem)
        ) { return; }

        LivingEntity targetEntity = event.getEntityLiving();
        LivingEntity sourceEntity = (LivingEntity) event.getSource().getEntity();
        BatItem bat = (BatItem) sourceEntity.getMainHandItem().getItem();

        float knockbackAmount = bat.getKnockbackAmount();
        double knockbackX = -sourceEntity.getLookAngle().x();
        double knockbackZ = -sourceEntity.getLookAngle().z();

        // Apply knockback, apply item damage, and then cancel event to avoid entity damage
        targetEntity.knockback(knockbackAmount, knockbackX, knockbackZ);
        sourceEntity.getMainHandItem().hurtAndBreak(1, sourceEntity, entity -> entity.broadcastBreakEvent(EquipmentSlotType.MAINHAND));
        event.setCanceled(true);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        // some preinit code
        LOGGER.info("HELLO FROM PREINIT");
        LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        // do something that can only be done on the client
        LOGGER.info("Got game settings {}", event.getMinecraftSupplier().get().options);
    }


}
