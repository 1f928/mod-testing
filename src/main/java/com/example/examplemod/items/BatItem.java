package com.example.examplemod.items;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.ImmutableMultimap.Builder;
import net.minecraft.enchantment.IVanishable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TieredItem;
import java.util.UUID;

public class BatItem extends TieredItem implements IVanishable {

    private static final UUID BASE_KNOCKBACK_UUID = UUID.fromString("8ead9559-842a-4333-834d-94c01bf26833");

    private final float attackDamage;
    private final float knockbackAmount;
    private final Multimap<Attribute, AttributeModifier> defaultModifiers;

    public BatItem(IItemTier itemTier, float attackSpeed, float knockback, Properties props) {
        super(itemTier, props);
        this.attackDamage = 1;
        this.knockbackAmount = knockback;
        Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", (double)this.attackDamage, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", (double)attackSpeed, AttributeModifier.Operation.ADDITION));
        this.defaultModifiers = builder.build();
    }

    public float getDamage() { return this.attackDamage; }

    public float getKnockbackAmount() { return this.knockbackAmount; }

    public boolean hurtEnemy(ItemStack bat, LivingEntity player, LivingEntity target) {
        bat.hurtAndBreak(1, player, entity -> entity.broadcastBreakEvent(EquipmentSlotType.MAINHAND));
        return true;
    }

    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlotType slot) {
        return slot == EquipmentSlotType.MAINHAND ? this.defaultModifiers : super.getDefaultAttributeModifiers(slot);
    }
}
