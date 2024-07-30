package net.ukrounay.elementalsmithing.particles.custom;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleEffect;

@Environment(value=EnvType.CLIENT)
public class EnergyParticle extends SpriteBillboardParticle {

    private final double startX;
    private final double startY;
    private final double startZ;
    private final float startScale;

    EnergyParticle(ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
        super(clientWorld, d, e, f);
        this.velocityX = g;
        this.velocityY = h;
        this.velocityZ = i;
        this.startX = d;
        this.startY = e;
        this.startZ = f;
        this.prevPosX = d + g;
        this.prevPosY = e + h;
        this.prevPosZ = f + i;
        this.x = this.prevPosX;
        this.y = this.prevPosY;
        this.z = this.prevPosZ;
        this.scale = 0.1f * (this.random.nextFloat() * 0.5f + 0.2f);
        this.startScale = this.scale;
        this.collidesWithWorld = false;
        this.maxAge = (int)(Math.random() * 10.0) + 30;
    }

    @Override
    public void move(double dx, double dy, double dz) {
        this.setBoundingBox(this.getBoundingBox().offset(dx, dy, dz));
        this.repositionFromBoundingBox();
    }

    @Override
    public int getBrightness(float tint) {
//        int i = super.getBrightness(tint);
//        float f = (float)this.age / (float)this.maxAge;
//        f *= f;
//        f *= f;
//        int j = i & 0xFF;
//        int k = i >> 16 & 0xFF;
//        if ((k += (int)(f * 15.0f * 16.0f)) > 240) {
//            k = 240;
//        }
//        return j | k << 16;
        return 196;
    }

    @Override
    public void tick() {
        this.prevPosX = this.x;
        this.prevPosY = this.y;
        this.prevPosZ = this.z;
        if (this.age++ >= this.maxAge) {
            this.markDead();
            return;
        }
        float g = (float)this.age / (float)this.maxAge;
        float f = g;
        float m = 1.0f - f;
        g *= g;
        g *= g;
        this.x = this.startX + this.velocityX * (double)m;
        this.y = this.startY + this.velocityY * (double)m - (double)(g * 1.2f);
        this.z = this.startZ + this.velocityZ * (double)m;
        alpha = 1 - g;
        scale = startScale * 2 * (-pow(f - 0.5f, 2) + 0.5f);

    }


    private static float pow(float a, int b) {
        float result = a;
        for (int i = 1; i <= b; i++) result *= a;
        return result;
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Environment(value=EnvType.CLIENT)
    public static class Factory implements ParticleFactory<DefaultParticleType> {

        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        @Override
        public Particle createParticle(DefaultParticleType type, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            EnergyParticle particle = new EnergyParticle(world, x, y, z, velocityX, velocityY, velocityZ);
            particle.setSprite(this.spriteProvider);
            return particle;
        }
    }
}

