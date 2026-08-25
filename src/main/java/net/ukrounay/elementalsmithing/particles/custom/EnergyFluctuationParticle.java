package net.ukrounay.elementalsmithing.particles.custom;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.render.*;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.MathHelper;
import org.joml.Quaternionf;
import org.joml.Vector3f;

@Environment(EnvType.CLIENT)
public class EnergyFluctuationParticle extends Particle {

    private static final int STEP_COUNT = 120;
    private final Vector3f center;
    private final float radius;

    private static final float MAX_WIDTH = 0.07f;

    private float headPosition = 0f;
    private final float rotationSpeed;
    private final float trailLength;

    private static class Point {
        Vector3f position = new Vector3f();
        float width;
        float alpha;
    }
    private final Point[] points = new Point[STEP_COUNT];

    private final Vector3f[] offsets = new Vector3f[STEP_COUNT];
    private final Vector3f tangent = new Vector3f();

    private final Vector3f p0 = new Vector3f();
    private final Vector3f p1 = new Vector3f();

    float a0;
    float a1;

    private final Vector3f cam = new Vector3f();

    private final Vector3f toCamera = new Vector3f();

    private final Vector3f A = new Vector3f();
    private final Vector3f B = new Vector3f();
    private final Vector3f C = new Vector3f();
    private final Vector3f D = new Vector3f();

    float u0;
    float u1;
    float v0;
    float v1;

    protected EnergyFluctuationParticle(ClientWorld world, double x, double y, double z) {
        super(world, x, y, z);
        maxAge = STEP_COUNT * this.random.nextInt(3);
        this.center = new Vector3f((float) x, (float) y, (float) z);
        this.radius = 0.4f;
        this.maxAge = random.nextInt(10) + 10;
        for (int i = 0; i < STEP_COUNT; i++) {
            points[i] = new Point();
            offsets[i] = new Vector3f();
        }
        generatePath();

        float laps = 1.5f + random.nextFloat();
        this.rotationSpeed = STEP_COUNT * laps / maxAge;
        this.trailLength = STEP_COUNT * 0.55f;
    }


    private void generatePath() {

        Quaternionf rotation =
                new Quaternionf()
                        .rotateXYZ(
                                random.nextFloat() * MathHelper.TAU,
                                random.nextFloat() * MathHelper.TAU,
                                random.nextFloat() * MathHelper.TAU
                        );

        for (int i = 0; i < STEP_COUNT; i++) {

            float t = i / (float)(STEP_COUNT - 1);

            float angle = t * MathHelper.TAU;

            Vector3f p = new Vector3f(
                    MathHelper.cos(angle) * radius,
                    0,
                    MathHelper.sin(angle) * radius
            );

            p.rotate(rotation);

            points[i].position.set(center).add(p);
        }
    }

    private void updateWidths() {
        float head = headPosition % STEP_COUNT;

        for (int i = 0; i < STEP_COUNT; i++) {
            float dist = head - i;
            if (dist < 0) dist += STEP_COUNT;

            if (dist > trailLength) {
                points[i].width = 0f;
                points[i].alpha = 0f;
                continue;
            }

            float frac = dist / trailLength;
            float shape = MathHelper.sin(frac * (float) Math.PI);
            points[i].width = MAX_WIDTH * shape;
            points[i].alpha = shape;
        }
    }

    private void updateOffsets(Vector3f cam) {
        for (int i = 0; i < STEP_COUNT - 1; i++) {
            int prevIdx = (i - 1 + STEP_COUNT) % STEP_COUNT;
            int nextIdx = i + 1;

            tangent.set(points[nextIdx].position).sub(points[prevIdx].position);
            if (tangent.lengthSquared() < 1e-8f) {
                tangent.set(1, 0, 0);
            } else {
                tangent.normalize();
            }

            toCamera.set(cam).sub(points[i].position).normalize();
            tangent.cross(toCamera, offsets[i]);

            if (offsets[i].lengthSquared() < 1e-8f) {
                offsets[i].set(0, 1, 0);
            } else {
                offsets[i].normalize();
            }
            offsets[i].mul(points[i].width);
        }

        for (int i = 1; i < STEP_COUNT - 1; i++) {
            if (offsets[i].dot(offsets[i - 1]) < 0) {
                offsets[i].negate();
            }
        }

        offsets[STEP_COUNT - 1].set(offsets[0]);
    }

    @Override
    public void tick() {
        prevPosX = x;
        prevPosY = y;
        prevPosZ = z;

        age++;

        if (age >= maxAge) {
            markDead();
            return;
        }

        float progress = (float) age / maxAge;
        alpha = 1.0f - progress;

        headPosition += rotationSpeed;
        updateWidths();
    }


    @Override
    public void buildGeometry(VertexConsumer vc, Camera camera, float tickDelta) {

        u0 = sprite.getMinU();
        u1 = sprite.getMaxU();
        v0 = sprite.getMinV();
        v1 = sprite.getMaxV();
        float u = (u0 + u1) * 0.5f;

        cam.set(
                camera.getPos().x,
                camera.getPos().y,
                camera.getPos().z
        );

        updateOffsets(cam);

        for (int i = 0; i < STEP_COUNT - 1; i++) {
            if (points[i].width <= 0f && points[i + 1].width <= 0f) continue;

            p0.set(points[i].position);
            p1.set(points[i + 1].position);

            A.set(p0).sub(offsets[i]).sub(cam);
            B.set(p0).add(offsets[i]).sub(cam);
            C.set(p1).add(offsets[i + 1]).sub(cam);
            D.set(p1).sub(offsets[i + 1]).sub(cam);

            a0 = points[i].alpha * alpha;
            a1 = points[i + 1].alpha * alpha;



            vc.vertex(A.x, A.y, A.z).texture(u, v0).color(red, green, blue, a0).light(15728880).next();
            vc.vertex(B.x, B.y, B.z).texture(u, v1).color(red, green, blue, a0).light(15728880).next();
            vc.vertex(C.x, C.y, C.z).texture(u, v1).color(red, green, blue, a1).light(15728880).next();
            vc.vertex(D.x, D.y, D.z).texture(u, v0).color(red, green, blue, a1).light(15728880).next();        }
    }


    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class Factory implements ParticleFactory<DefaultParticleType> {

        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }


        @Override
        public Particle createParticle(DefaultParticleType type, ClientWorld world, double x, double y, double z, double vx, double vy, double vz) {
            EnergyFluctuationParticle particle = new EnergyFluctuationParticle(world, x, y, z);
            particle.setVelocity(vx, vy, vz);
            particle.setSprite(spriteProvider);
            return particle;
        }
    }

    private Sprite sprite = null;

    public void setSprite(SpriteProvider provider) {
        this.sprite = provider.getSprite(this.random);
    }


}