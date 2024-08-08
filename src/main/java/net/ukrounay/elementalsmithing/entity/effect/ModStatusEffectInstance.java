package net.ukrounay.elementalsmithing.entity.effect;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.DynamicOps;
import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.Optional;

public class ModStatusEffectInstance extends StatusEffectInstance {

    public static final Logger LOGGER = LogUtils.getLogger();
    public static final int INFINITE = -1;
    public final StatusEffect type;
    public int duration;
    public int amplifier;
    public boolean ambient;
    public boolean showParticles;
    public boolean showIcon;
    @Nullable
    public ModStatusEffectInstance hiddenEffect;
    public final Optional<FactorCalculationData> factorCalculationData;

    public ModStatusEffectInstance(StatusEffect type) {
        this(type, 0, 0);
    }

    public ModStatusEffectInstance(StatusEffect type, int duration) {
        this(type, duration, 0);
    }

    public ModStatusEffectInstance(StatusEffect type, int duration, int amplifier) {
        this(type, duration, amplifier, false, true);
    }

    public ModStatusEffectInstance(StatusEffect type, int duration, int amplifier, boolean ambient, boolean visible) {
        this(type, duration, amplifier, ambient, visible, visible);
    }

    public ModStatusEffectInstance(StatusEffect type, int duration, int amplifier, boolean ambient, boolean showParticles, boolean showIcon) {
        this(type, duration, amplifier, ambient, showParticles, showIcon, null, type.getFactorCalculationDataSupplier());
    }

    public ModStatusEffectInstance(StatusEffect type, int duration, int amplifier, boolean ambient, boolean showParticles, boolean showIcon, @Nullable ModStatusEffectInstance hiddenEffect, Optional<FactorCalculationData> factorCalculationData) {
        super(type, duration, amplifier, ambient, showParticles, showIcon, hiddenEffect, factorCalculationData);
        this.type = type;
        this.setDuration(duration);
        this.setAmplifier(amplifier);
        this.setAmbient(ambient);
        this.setShowParticles(showParticles);
        this.setShowIcon(showIcon);
        this.setHiddenEffect(hiddenEffect);
        this.factorCalculationData = factorCalculationData;
    }

    public ModStatusEffectInstance(StatusEffectInstance instance) {
        super(instance);
        this.type = instance.getEffectType();
        this.factorCalculationData = this.getEffectType().getFactorCalculationDataSupplier();
        this.copyFrom(instance);
    }

    public Optional<FactorCalculationData> getFactorCalculationData() {
        return this.factorCalculationData;
    }

    void copyFrom(StatusEffectInstance that) {
        this.setDuration(that.getDuration());
        this.setAmplifier(that.getAmplifier());
        this.setAmbient(that.isAmbient());
        this.setShowParticles(that.shouldShowParticles());
        this.setShowIcon(that.shouldShowIcon());
    }

    @Override
    public boolean upgrade(StatusEffectInstance that) {
        return that instanceof ModStatusEffectInstance && this.upgrade((ModStatusEffectInstance)that);
    }

    public boolean upgrade(ModStatusEffectInstance that) {
        if (this.getEffectType() != that.getEffectType()) {
            getLogger().warn("This method should only be called for matching effects!");
        }
        int i = this.getDuration();
        boolean bl = false;
        if (that.getAmplifier() > this.getAmplifier()) {
            if (that.lastsShorterThan(this)) {
                ModStatusEffectInstance statusEffectInstance = this.getHiddenEffect();
                this.setHiddenEffect(new ModStatusEffectInstance(this));
                this.getHiddenEffect().setHiddenEffect(statusEffectInstance);
            }
            this.setAmplifier(that.getAmplifier());
            this.setDuration(that.getDuration());
            bl = true;
        } else if (this.lastsShorterThan(that)) {
            if (that.getAmplifier() == this.getAmplifier()) {
                this.setDuration(that.getDuration());
                bl = true;
            } else if (this.getHiddenEffect() == null) {
                this.setHiddenEffect(new ModStatusEffectInstance(that));
            } else {
                this.getHiddenEffect().upgrade(that);
            }
        }
        if (!that.isAmbient() && this.isAmbient() || bl) {
            this.setAmbient(that.isAmbient());
            bl = true;
        }
        if (that.shouldShowParticles() != this.shouldShowParticles()) {
            this.setShowParticles(that.shouldShowParticles());
            bl = true;
        }
        if (that.shouldShowIcon() != this.shouldShowIcon()) {
            this.setShowIcon(that.shouldShowIcon());
            bl = true;
        }
        return bl;
    }

    public boolean lastsShorterThan(StatusEffectInstance effect) {
        return !this.isInfinite() && (this.getDuration() < effect.getDuration() || effect.isInfinite());
    }

    public boolean isInfinite() {
        return this.getDuration() == -1;
    }

    public boolean isDurationBelow(int duration) {
        return !this.isInfinite() && this.getDuration() <= duration;
    }

    public int mapDuration(Int2IntFunction mapper) {
        if (this.isInfinite() || this.getDuration() == 0) {
            return this.getDuration();
        }
        return mapper.applyAsInt(this.getDuration());
    }

    public static Logger getLogger() {
        return LOGGER;
    }

    public StatusEffect getEffectType() {
        return type;
    }


    public int getDuration() {
        return this.duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getAmplifier() {
        return this.amplifier;
    }

    public void setAmplifier(int amplifier) {
        this.amplifier = amplifier;
    }

    public boolean isAmbient() {
        return this.ambient;
    }

    public void setAmbient(boolean ambient) {
        this.ambient = ambient;
    }

    public boolean shouldShowParticles() {
        return this.showParticles;
    }

    public void setShowParticles(boolean showParticles) {
        this.showParticles = showParticles;
    }

    public boolean shouldShowIcon() {
        return this.showIcon;
    }

    public void setShowIcon(boolean showIcon) {
        this.showIcon = showIcon;
    }

    public boolean update(LivingEntity entity, Runnable overwriteCallback) {
        if (this.isActive()) {
            int i;
            int n = i = this.isInfinite() ? entity.age : this.getDuration();
            if (this.getEffectType().canApplyUpdateEffect(i, this.getAmplifier())) {
                this.applyUpdateEffect(entity);
            }
            this.updateDuration();
            if (this.getDuration() == 0 && this.getHiddenEffect() != null) {
                this.copyFrom(this.getHiddenEffect());
                this.setHiddenEffect(this.getHiddenEffect().getHiddenEffect());
                overwriteCallback.run();
            }
        }
        return this.isActive();
    }

    public boolean isActive() {
        return this.isInfinite() || this.getDuration() > 0;
    }

    public int updateDuration() {
        if (this.getHiddenEffect() != null) {
            this.getHiddenEffect().updateDuration();
        }
        this.setDuration(this.mapDuration(duration -> duration - 1));
        return this.getDuration();
    }

    public int updateDuration(int amount) {
        this.setDuration(this.getDuration() + amount);
        return this.getDuration();
    }

    public void applyUpdateEffect(LivingEntity entity) {
        if (this.isActive()) {
            this.getEffectType().applyUpdateEffect(entity, this.getAmplifier());
        }
    }

    public String getTranslationKey() {
        return this.getEffectType().getTranslationKey();
    }

    public String toString() {
        String string = this.getAmplifier() > 0 ? this.getTranslationKey() + " x " + (this.getAmplifier() + 1) + ", Duration: " + this.getDurationString() : this.getTranslationKey() + ", Duration: " + this.getDurationString();
        if (!this.shouldShowParticles()) {
            string = string + ", Particles: false";
        }
        if (!this.shouldShowIcon()) {
            string = string + ", Show Icon: false";
        }
        return string;
    }

    public String getDurationString() {
        if (this.isInfinite()) {
            return "infinite";
        }
        return Integer.toString(this.getDuration());
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof StatusEffectInstance) {
            StatusEffectInstance statusEffectInstance = (StatusEffectInstance)o;
            return this.getDuration() == statusEffectInstance.getDuration() && this.getAmplifier() == statusEffectInstance.getAmplifier() && this.isAmbient() == statusEffectInstance.isAmbient() && this.getEffectType().equals(statusEffectInstance.getEffectType());
        }
        return false;
    }

    public int hashCode() {
        int i = this.getEffectType().hashCode();
        i = 31 * i + this.getDuration();
        i = 31 * i + this.getAmplifier();
        i = 31 * i + (this.isAmbient() ? 1 : 0);
        return i;
    }

    public NbtCompound writeNbt(NbtCompound nbt) {
        nbt.putInt("Id", StatusEffect.getRawId(this.getEffectType()));
        this.writeTypelessNbt(nbt);
        return nbt;
    }

    public void writeTypelessNbt(NbtCompound nbt) {
        nbt.putByte("Amplifier", (byte)this.getAmplifier());
        nbt.putInt("Duration", this.getDuration());
        nbt.putBoolean("Ambient", this.isAmbient());
        nbt.putBoolean("ShowParticles", this.shouldShowParticles());
        nbt.putBoolean("ShowIcon", this.shouldShowIcon());
        if (this.getHiddenEffect() != null) {
            NbtCompound nbtCompound = new NbtCompound();
            this.getHiddenEffect().writeNbt(nbtCompound);
            nbt.put("HiddenEffect", nbtCompound);
        }
    }

    @Nullable
    public static ModStatusEffectInstance fromNbt(NbtCompound nbt) {
        int i = nbt.getInt("Id");
        StatusEffect statusEffect = StatusEffect.byRawId(i);
        if (statusEffect == null) {
            return null;
        }
        return ModStatusEffectInstance.fromNbt(statusEffect, nbt);
    }

    public static ModStatusEffectInstance fromNbt(StatusEffect type, NbtCompound nbt) {
        byte i = nbt.getByte("Amplifier");
        int j = nbt.getInt("Duration");
        boolean bl = nbt.getBoolean("Ambient");
        boolean bl2 = true;
        if (nbt.contains("ShowParticles", NbtElement.BYTE_TYPE)) {
            bl2 = nbt.getBoolean("ShowParticles");
        }
        boolean bl3 = bl2;
        if (nbt.contains("ShowIcon", NbtElement.BYTE_TYPE)) {
            bl3 = nbt.getBoolean("ShowIcon");
        }
        ModStatusEffectInstance statusEffectInstance = null;
        if (nbt.contains("HiddenEffect", NbtElement.COMPOUND_TYPE)) {
            statusEffectInstance = ModStatusEffectInstance.fromNbt(type, nbt.getCompound("HiddenEffect"));
        }
        return new ModStatusEffectInstance(type, j, Math.max(i, 0), bl, bl2, bl3, statusEffectInstance, Optional.empty());
    }




    /**
     * The effect hidden when upgrading effects. Duration decreases with this
     * effect.
     *
     * <p>This exists so that long-duration low-amplifier effects reappears
     * after short-duration high-amplifier effects run out.
     */
    public @Nullable ModStatusEffectInstance getHiddenEffect() {
        return hiddenEffect;
    }

    public void setHiddenEffect(@Nullable ModStatusEffectInstance hiddenEffect) {
        this.hiddenEffect = hiddenEffect;
    }
}
