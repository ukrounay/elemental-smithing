package net.ukrounay.elementalsmithing.particles.custom;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.Vec3d;
import net.ukrounay.elementalsmithing.util.FastMath;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class TracingEnergyParticle extends SpriteBillboardParticle {

    private final Vec3d center;
    private final float radius;
    private final float angleSpeed;
    private final int speed;
    private final float startScale;
    private double currentAngle;
    private final Quaternionf orbitalPlaneNormal;

    private List<Vec3d> trail = new ArrayList<>();

    protected TracingEnergyParticle(ClientWorld world, double x, double y, double z) {
        super(world, x, y, z);
        this.center = new Vec3d(x, y, z);
        this.radius = 0.4f;
        this.angleSpeed = 0.4f;
        this.speed = 4;
        this.currentAngle = 0;
        this.maxAge = random.nextInt(10) + 10;
        this.scale = 0.06f;
        this.startScale = scale;
        this.orbitalPlaneNormal = new Quaternionf(random.nextFloat(), random.nextFloat(), random.nextFloat(), 1.0);
    }

    /**
     * Updating position along a circular trajectory
     */
    private void transformPosition() {
        Vector3f offset = new Vector3f(
            (float)Math.cos(this.currentAngle),
            0,
            (float)Math.sin(this.currentAngle)
        );
        offset.mul(this.radius);
        offset.rotate(this.orbitalPlaneNormal);
        offset.add(this.center.toVector3f());
        offset.y -= this.radius;
        this.setPos(offset.x, offset.y, offset.z);
    }

    /**
     * Updating sprite size
     * @param progress relation of current age of particle to its max age
     */
    private void calcScale(float progress) {
        this.scale = this.startScale * 2 * (-FastMath.sq(progress - 0.5f) + 0.5f);
    }

    /**
     * Updating sprite transparency
     * @param progress relation of current age of particle to its max age
     */
    private void calcAlpha(float progress) {
        this.alpha = 1 - FastMath.pow(progress, 4);
    }

    @Override
    public void tick() {
        super.tick();
        float progress = (float)this.age / this.maxAge;
        for (int i = 0; i <= speed; i++) {
            currentAngle += angleSpeed / speed;
            transformPosition();
            trail.add(new Vec3d(x, y, z));
            if (trail.size() > speed * maxAge / 2) {
                trail.remove(0);
            }
        }
        calcScale(progress);
        calcAlpha(progress);
    }

    public Vector3f calculateMidpoint(Vector3f point1, Vector3f point2) {
        float x = (point1.x() + point2.x()) / 2.0f;
        float y = (point1.y() + point2.y()) / 2.0f;
        float z = (point1.z() + point2.z()) / 2.0f;
        return new Vector3f(x, y, z);
    }


    // gpt
    @Override
    public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
        if (trail.size() < 2) return; // Need at least 2 points to draw a line

        Vec3d cameraPos = camera.getPos();
        Vec3d prevPos = trail.get(0);

        Vector3f[] prevVertices = new Vector3f[] {prevPos.subtract(cameraPos).toVector3f(), prevPos.subtract(cameraPos).toVector3f()};

        for (int i = 1; i < trail.size(); i++) {

            Vec3d currentPos = trail.get(i);

            Vector3f startPoint = prevPos.subtract(cameraPos).toVector3f();
            Vector3f endPoint = currentPos.subtract(cameraPos).toVector3f();

            Quaternionf normal = new Quaternionf();
            Vector3f surfaceCenterPoint = calculateMidpoint(startPoint, endPoint);
            surfaceCenterPoint.rotationTo(center.toVector3f(), normal);

            float distance = (float) prevPos.distanceTo(currentPos);

            Vector3f[] vector3fs = new Vector3f[] {
                    new Vector3f(distance, 1, 0),
                    new Vector3f(distance, -1, 0)
            };

            float currentSize = FastMath.cubexp((float) i / trail.size()) * scale * 0.5f;

            for (Vector3f vector3f : vector3fs) {
                vector3f.rotate(normal);
                vector3f.mul(currentSize);
                vector3f.add(endPoint);
            }

            float k = this.getMinU();
            float l = this.getMaxU();
            float m = this.getMinV();
            float n = this.getMaxV();

            vertexConsumer.vertex(prevVertices[0].x(), prevVertices[0].y(), prevVertices[0].z()).texture(k, n).color(red, green, blue, alpha).light(182).next();
            vertexConsumer.vertex(prevVertices[1].x(), prevVertices[1].y(), prevVertices[1].z()).texture(k, m).color(red, green, blue, alpha).light(182).next();
            vertexConsumer.vertex(vector3fs[1].x(), vector3fs[1].y(), vector3fs[1].z()).texture(l, m).color(red, green, blue, alpha).light(182).next();
            vertexConsumer.vertex(vector3fs[0].x(), vector3fs[0].y(), vector3fs[0].z()).texture(l, n).color(red, green, blue, alpha).light(182).next();

            vertexConsumer.vertex(vector3fs[0].x(), vector3fs[0].y(), vector3fs[0].z()).texture(k, n).color(red, green, blue, alpha).light(182).next();
            vertexConsumer.vertex(vector3fs[1].x(), vector3fs[1].y(), vector3fs[1].z()).texture(k, m).color(red, green, blue, alpha).light(182).next();
            vertexConsumer.vertex(prevVertices[1].x(), prevVertices[1].y(), prevVertices[1].z()).texture(l, m).color(red, green, blue, alpha).light(182).next();
            vertexConsumer.vertex(prevVertices[0].x(), prevVertices[0].y(), prevVertices[0].z()).texture(l, n).color(red, green, blue, alpha).light(182).next();

            prevPos = currentPos;
            prevVertices = vector3fs;
        }
    }


    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Environment(value = EnvType.CLIENT)
    public static class Factory implements ParticleFactory<DefaultParticleType> {

        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        @Override
        public Particle createParticle(DefaultParticleType parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            TracingEnergyParticle particle = new TracingEnergyParticle(world, x, y, z);
            particle.setSprite(this.spriteProvider);
            return particle;
        }
    }

}


