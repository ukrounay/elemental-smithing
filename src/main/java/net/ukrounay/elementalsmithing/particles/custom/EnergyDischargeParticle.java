package net.ukrounay.elementalsmithing.particles.custom;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.render.*;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.LightType;
import org.joml.Vector3f;

/**
 * A jagged, lightning-like arc that fractally displaces a straight line
 * between a start point (x, y, z) and a destination point. The destination
 * is NOT a velocity delta — it is an absolute world position packed into
 * the particle's vx/vy/vz spawn parameters (see Factory below).
 *
 * Rendering reuses the same "ribbon of billboarded quads facing the camera"
 * approach as EnergyFluctuationParticle, but instead of a looping trail
 * chasing its own tail around a circle, the bolt grows outward from the
 * start point and then fades away as a whole.
 */
@Environment(EnvType.CLIENT)
public class EnergyDischargeParticle extends Particle {

    // Must be (power of 2) + 1 so the midpoint-displacement subdivision is exact.
    private static final int STEP_COUNT = 33;

    private static final float MAX_WIDTH = 0.06f;

    // How jagged the bolt is: fraction of segment length used as max perpendicular
    // displacement at the first subdivision. Halves (roughly) each subdivision.
    private static final float ROUGHNESS = 0.55f;

    private final Vector3f start;
    private final Vector3f end;

    private int cachedLight = 15728880;

    private static class Point {
        final Vector3f position = new Vector3f();
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

    // How many of the STEP_COUNT segments are "grown in" yet, as a float so
    // the leading edge can interpolate smoothly instead of popping in.
    private float growth = 0f;
    private final int growTicks;

    // Small per-tick brightness jitter for a flicker feel.
    private float flicker = 1f;

    protected EnergyDischargeParticle(ClientWorld world, double x, double y, double z,
                                      double destX, double destY, double destZ) {
        super(world, x, y, z);

        this.start = new Vector3f((float) x, (float) y, (float) z);
        this.end = new Vector3f((float) destX, (float) destY, (float) destZ);

        this.maxAge = random.nextInt(6) + 10;
        this.growTicks = Math.max(1, Math.round(maxAge * 0.35f));

        for (int i = 0; i < STEP_COUNT; i++) {
            points[i] = new Point();
            offsets[i] = new Vector3f();
        }

        generatePath();

        BlockPos pos = BlockPos.ofFloored(x, y, z);
        int blockLight = world.getLightLevel(LightType.BLOCK, pos);
        int skyLight = world.getLightLevel(LightType.SKY, pos);

        int boostedBlock = Math.min(blockLight + 4, 15);
        int boostedSky = Math.min(skyLight + 4, 15);

        cachedLight = boostedBlock << 4 | boostedSky << 20;
    }

    /**
     * Fractal midpoint-displacement: start with a straight line from start to
     * end, then repeatedly bisect each segment, nudging the new midpoint
     * sideways by a shrinking random amount. Produces a natural jagged bolt.
     */
    private void generatePath() {
        points[0].position.set(start);
        points[STEP_COUNT - 1].position.set(end);

        // Seed all intermediate points with a plain linear interpolation first;
        // displace() below will perturb them.
        for (int i = 1; i < STEP_COUNT - 1; i++) {
            float t = i / (float) (STEP_COUNT - 1);
            points[i].position.set(start).lerp(end, t);
        }

        float boltLength = start.distance(end);
        float initialAmplitude = boltLength * 0.18f;

        displace(0, STEP_COUNT - 1, initialAmplitude);
    }

    private void displace(int lo, int hi, float amplitude) {
        if (hi - lo <= 1) return;

        int mid = (lo + hi) / 2;

        // Direction of this sub-segment, used to build a perpendicular offset.
        Vector3f dir = new Vector3f(points[hi].position).sub(points[lo].position);
        if (dir.lengthSquared() < 1e-8f) {
            dir.set(0, 1, 0);
        } else {
            dir.normalize();
        }

        // Any vector not parallel to dir, then cross twice to get a vector
        // perpendicular to dir with a random spin around dir.
        Vector3f arbitrary = Math.abs(dir.y) < 0.9f ? new Vector3f(0, 1, 0) : new Vector3f(1, 0, 0);
        Vector3f perp = new Vector3f(dir).cross(arbitrary).normalize();
        Vector3f perp2 = new Vector3f(dir).cross(perp).normalize();

        float angle = random.nextFloat() * MathHelper.TAU;
        float mag = (random.nextFloat() * 2f - 1f) * amplitude;

        Vector3f jitter = new Vector3f(perp).mul(MathHelper.cos(angle))
                .add(new Vector3f(perp2).mul(MathHelper.sin(angle)))
                .mul(mag);

        // Recompute the true midpoint (not the stale linear seed) and displace it.
        Vector3f midpoint = new Vector3f(points[lo].position).lerp(points[hi].position, 0.5f);
        points[mid].position.set(midpoint).add(jitter);

        float nextAmplitude = amplitude * ROUGHNESS;
        displace(lo, mid, nextAmplitude);
        displace(mid, hi, nextAmplitude);
    }

    private void updateWidthsAndAlpha() {
        for (int i = 0; i < STEP_COUNT; i++) {
            // Base taper: thicker near the origin point, thinning toward the destination.
            float t = i / (float) (STEP_COUNT - 1);
            float taper = 1f - t * 0.7f;

            // Growth mask: segments ahead of the growing tip are invisible;
            // the tip itself fades in over a couple of indices for smoothness.
            float reveal = MathHelper.clamp(growth - i, 0f, 1f);

            float shape = taper * reveal;
            points[i].width = MAX_WIDTH * shape;
            points[i].alpha = shape;
        }
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

        // Grow the bolt in quickly, then hold, then let the overall fade (below) take over.
        growth = STEP_COUNT * MathHelper.clamp((float) age / growTicks, 0f, 1f);

        float progress = (float) age / maxAge;
        // Ease-out fade so the bolt lingers near full brightness before dying.
        alpha = 1.0f - progress * progress;

        flicker = 0.75f + random.nextFloat() * 0.25f;

        updateWidthsAndAlpha();
    }

    private void updateOffsets(Vector3f cam) {
        for (int i = 0; i < STEP_COUNT; i++) {
            int prevIdx = Math.max(i - 1, 0);
            int nextIdx = Math.min(i + 1, STEP_COUNT - 1);

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

        for (int i = 1; i < STEP_COUNT; i++) {
            if (offsets[i].dot(offsets[i - 1]) < 0) {
                offsets[i].negate();
            }
        }
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

            a0 = points[i].alpha * alpha * flicker;
            a1 = points[i + 1].alpha * alpha * flicker;

            vc.vertex(A.x, A.y, A.z).texture(u, v0).color(red, green, blue, a0).light(cachedLight).next();
            vc.vertex(B.x, B.y, B.z).texture(u, v1).color(red, green, blue, a0).light(cachedLight).next();
            vc.vertex(C.x, C.y, C.z).texture(u, v1).color(red, green, blue, a1).light(cachedLight).next();
            vc.vertex(D.x, D.y, D.z).texture(u, v0).color(red, green, blue, a1).light(cachedLight).next();
        }
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
            // vx/vy/vz are repurposed here as the absolute destination point,
            // NOT a velocity delta. Spawn like:
            //   world.addParticle(YOUR_TYPE, x, y, z, destX, destY, destZ);
            EnergyDischargeParticle particle = new EnergyDischargeParticle(world, x, y, z, vx, vy, vz);
            particle.setSprite(spriteProvider);
            return particle;
        }
    }

    private Sprite sprite = null;

    public void setSprite(SpriteProvider provider) {
        this.sprite = provider.getSprite(this.random);
    }
}