package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import com.willfp.ecoenchants.enchantments.util.EnchantmentUtils;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.atomic.AtomicInteger;

public class Wound extends EcoEnchant {
    public Wound() {
        super(
                "wound", EnchantmentType.NORMAL
        );
    }

    // START OF LISTENERS


    @Override
    public void onArrowDamage(LivingEntity attacker, LivingEntity victim, Arrow arrow, int level, EntityDamageByEntityEvent event) {
        if (!EnchantmentUtils.passedChance(this, level))
            return;

        double bleedDamage = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "bleed-damage");

        int bleedCount = this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "amount-per-level");
        bleedCount *= level;
        final int finalBleedCount = bleedCount;

        AtomicInteger currentBleedCount = new AtomicInteger(0);

        new BukkitRunnable() {
            @Override
            public void run() {
                currentBleedCount.addAndGet(1);

                victim.damage(bleedDamage);

                if (currentBleedCount.get() >= finalBleedCount) this.cancel();
            }
        }.runTaskTimer(this.plugin, 0, 10);
    }
}
