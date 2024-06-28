package net.ukrounay.elementalsmithing.item.custom;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.ukrounay.elementalsmithing.ElementalSmithing;

import java.util.*;


public class ElementalEnergyMethods {

    private static final Random random = new Random();

    // Terraforming state
    public static boolean isTerraforming = false;
    public static int maxTerraformingTicks;
    public static int remainingTerraformingTicks;
    public static BlockPos startPos;
    public static List<CurveState> curves; // List to track each curve's state
    public static double maxTerraformingDistance = 0;
    public static Block centerBlock;
    public static Block outerBlock;

    // Restoring state
    public static boolean isRestoring = false;
    public static int maxRestoringTicks;
    public static int remainingRestoringTicks;
    public static Map<BlockPos, BlockState> restorationMap = new HashMap<>(); // List to track each curve's state
    public static int terraformingStage;

    public static final List<Spike> spikes = new ArrayList<>(); // List to hold all spikes

    // spikes state
    public static boolean isSpikesGenerating = false;
    public static int currentSpikeIndex = 0;
    public static int stepIndex = 0;

    private static class Spike {
        List<BlockPos> positions;
        Block block;
        int halfStepIndex;

        Spike(List<BlockPos> positions, Block block, int halfStepIndex) {
            this.positions = positions;
            this.block = block;
            this.halfStepIndex = halfStepIndex;
        }
    }

    // Inner class to represent the state of each curve
    private static class CurveState {
        BlockPos currentPos;
        double currentAngle;
        int initialThickness; // Initial thickness of the curve

        CurveState(BlockPos startPos, double initialAngle, int initialThickness) {
            this.currentPos = startPos;
            this.currentAngle = initialAngle;
            this.initialThickness = initialThickness;
        }
    }

    /**
     * Starts the terraforming process with multiple curves.
     *
     * @param world The game world.
     * @param pos The starting position for terraforming.
     * @param initialBlock The block type for the initial point of the curve.
     * @param curveBlock The block type to use for the rest of the curve.
     * @param ticks The total number of ticks over which to perform terraforming.
     */
    public static void startTerraforming(World world, BlockPos pos, Block initialBlock, Block curveBlock, int ticks, int stage) {

        isTerraforming = true;
        startPos = pos;
        centerBlock = initialBlock;
        outerBlock = curveBlock;
        maxTerraformingTicks = ticks;
        remainingTerraformingTicks = maxTerraformingTicks;
        terraformingStage = stage;

        // Replace the starting block with the initial block type
        if(world.getBlockState(pos).getBlock() != Blocks.BEDROCK)
            world.setBlockState(startPos, initialBlock.getDefaultState());

        switch (terraformingStage) {
            case 0:
                // Determine a random number of curves between 3 and 8
                int numCurves = random.nextInt(5) + 4; // Random number between 4 and 8

                // Initialize multiple curves with random directions and thicknesses
                curves = new ArrayList<>();

                double initialAngle = random.nextDouble() * 2 * Math.PI; // Random initial direction
                int initialThickness = random.nextInt(2) + 1; // Random thickness between 1 and 2
                curves.add(new CurveState(pos, initialAngle, initialThickness));


                for (int i = 1; i < numCurves; i++) {
                    initialAngle += ((random.nextDouble() / 2) + 1) * Math.PI; // Random initial direction
                    initialThickness = random.nextInt(2) + 2; // Random thickness between 2 and 3
                    curves.add(new CurveState(pos, initialAngle, initialThickness));
                }
                break;
            default:
                ElementalSmithing.LOGGER.info("terraformingStage: " + terraformingStage);
                break;
        }


    }

    /**
     * Continues the terraforming process.
     *
     * @param world The game world.
     */
    public static void continueTerraforming(World world) {
        if (isTerraforming) {
            switch (terraformingStage) {
                case 0:
                    for (CurveState curve : curves) {
                        // Random step length between 1 and 3 blocks
                        int stepLength = random.nextInt(3) + 1;

                        for (int i = 0; i < stepLength; i++) {
                            // Calculate the next position in the curve's direction
                            curve.currentPos = getNextPosition(curve.currentPos, curve.currentAngle);

                            // Calculate current thickness decreasing linearly to 1
                            int currentThickness = calculateThickness(curve.initialThickness, maxTerraformingTicks, remainingTerraformingTicks);

                            // Set the blocks at the current position with thickness
                            placeThickenedBlocksLowerHalfSphere(world, curve.currentPos, currentThickness, centerBlock, outerBlock);

                            // Slightly adjust the angle to keep the curve smooth and curvy
                            curve.currentAngle += (random.nextDouble() - 0.5) * Math.PI / 4; // Adjust angle by up to ±45 degrees
                        }
                    }
                    break;
                case 1:
                    restorationMap.clear();
                    var radius = Math.max(1, (int)(maxTerraformingDistance / 2 * ((maxTerraformingTicks - remainingTerraformingTicks) / (double)maxTerraformingTicks)));
                    var terraformAbleBlocks = getBlocksInSphere(world, startPos, List.of(outerBlock, Blocks.BEDROCK), radius, Math.max(1, radius/2), radius);
                    for (BlockPos blockpos : terraformAbleBlocks)
                        if(blockpos.getY() <= startPos.getY()) {
                            if(restorationMap.putIfAbsent(blockpos, world.getBlockState(blockpos)) == null)
                                world.setBlockState(blockpos, outerBlock.getDefaultState());
                        }
                    break;
                case 2:
                    var innerRadius = Math.max(1, (int)((maxTerraformingDistance/2 - 2)  * ((maxTerraformingTicks - remainingTerraformingTicks) / (double)maxTerraformingTicks)));
                    var innerTerraformAbleBlocks = getBlocksInSphere(world, startPos, List.of(centerBlock, Blocks.BEDROCK), innerRadius, Math.max(1, innerRadius/2), innerRadius);
                    for (BlockPos blockpos : innerTerraformAbleBlocks)
                        if(blockpos.getY() <= startPos.getY()) {
                            if(restorationMap.putIfAbsent(blockpos, world.getBlockState(blockpos)) == null)
                                world.setBlockState(blockpos, centerBlock.getDefaultState());
                        }
                    break;
                default:
                    ElementalSmithing.LOGGER.info("terraformingStage: " + terraformingStage);
                    break;
            }


            // Decrement the remaining ticks
            remainingTerraformingTicks--;

            // Stop terraforming if out of ticks
            if (remainingTerraformingTicks <= 0) {
                isTerraforming = false;
            }
        }
    }





    /**
     * Precomputes the curly spikes around a central point.
     *
     * @param initialBlock The block type for the initial part of the spike.
     * @param curveBlock The block type to use for the rest of the spike.
     */
    public static void prepareCurlySpikes(Block initialBlock, Block curveBlock) {
        spikes.clear(); // Clear previous spikes
        int numSpikes = 8; // Number of spikes
        double angleIncrement = 2 * Math.PI / numSpikes; // Angle between each spike
        int radius =  (int)(maxTerraformingDistance/2) - 3;
        for (int i = 0; i < numSpikes; i++) {
            double angle = i * angleIncrement; // Calculate the angle for this spike
            int spikeHeight = random.nextInt(6, (int)(maxTerraformingDistance / 1.5)) + 1;

            // Precompute the positions for the spike
            List<BlockPos> positions = new ArrayList<>();
            precomputeCurlySpike(startPos, radius, spikeHeight, angle, positions);

            // Store the spike positions and the block type
            spikes.add(new Spike(positions, random.nextDouble() > 0.5 ? curveBlock : initialBlock, positions.size() / 2));
        }

        // Start the generation process
        isSpikesGenerating = true;
        currentSpikeIndex = 0;
        stepIndex = 0;
    }

    /**
     * Precomputes the positions for a single curly spike.
     *
     * @param centerPos The central position.
     * @param radius The radius from the center to the base of the spike.
     * @param height The height of the spike.
     * @param angle The angle at which the spike is oriented.
     * @param positions The list to store the precomputed positions.
     */
    private static void precomputeCurlySpike(BlockPos centerPos, int radius, int height, double angle, List<BlockPos> positions) {
        // Generate the spike positions
        for (int y = 0; y < height; y++) {
            double t = (double) y / height; // Normalized height
            double curvatureAngle = angle - (t * Math.PI / 2); // Bend the spike towards the center over its height

            // Compute the x and z offsets based on the curvature
            int dx = (int) (radius * Math.sin(curvatureAngle));
            int dz = (int) (radius * Math.cos(curvatureAngle));

            BlockPos spikePos = centerPos.add(dx, y, dz);

            // Calculate the thickness, which decreases linearly towards the end of the spike
            double thickness = 2.5 - (1.5 * t);
            positions.addAll(getThicknessPositions(spikePos, thickness));
        }
    }

    /**
     * Adds positions around the center position to simulate thickness.
     *
     * @param centerPos The center position.
     * @param thickness The thickness to add.
     */
    private static ArrayList<BlockPos> getThicknessPositions(BlockPos centerPos, double thickness) {
        var thicknessPositions = new ArrayList<BlockPos>();
        int intThickness = (int)Math.ceil(thickness);
        for (int dx = -intThickness; dx <= intThickness; dx++) {
            for (int dy = -intThickness; dy <= intThickness; dy++) {
                for (int dz = -intThickness; dz <= intThickness; dz++) {
                    // Calculate the distance from the center position
                    double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);
                    // Check if the position is within the spherical radius
                    if (distance <= thickness)
                        thicknessPositions.add(centerPos.add(dx, dy, dz));
                }
            }
        }
        return thicknessPositions;
    }

    /**
     * Continues the generation of the spikes step by step.
     *
     * @param world The game world.
     */
    public static void continueGeneratingSpikes(World world) {
        if (isSpikesGenerating && currentSpikeIndex < spikes.size()) {
            Spike currentSpike = spikes.get(currentSpikeIndex);

            // Determine how many positions to place in this step
            int stepLength = currentSpike.positions.size() / 4; // We place 2 positions per tick (you can adjust this)
            int endIndex = Math.min(stepIndex + stepLength, currentSpike.positions.size());

            // Place blocks for the current step
            for (int i = stepIndex; i < endIndex; i++) {
                BlockPos pos = currentSpike.positions.get(i);
                world.setBlockState(pos, currentSpike.block.getDefaultState());
            }

            // Move to the next step
            stepIndex += stepLength;

            // If halfway through, prepare the next spike
            if (stepIndex >= currentSpike.positions.size()) {
                currentSpikeIndex++;
                stepIndex = 0;
            }

            // Stop if all spikes have been generated
            if (currentSpikeIndex >= spikes.size()) {
                isSpikesGenerating = false;
            }
        }
    }



//    /**
//     * Generates curly spikes around a central point.
//     *
//     * @param world The game world.
//     * @param centerPos The central position around which to generate the spikes.
//     * @param radius The radius of the circle around the center.
//     * @param maxHeight The maximum height for the spikes.
//     * @param block The block type to use for the spikes.
//     */
//    public static void generateCurlySpikes(World world, BlockPos centerPos, int radius, int maxHeight, Block block) {
//        int numSpikes = 8; // We want 8 spikes
//        double angleIncrement = 2 * Math.PI / numSpikes; // Angle between each spike
//
//        for (int i = 0; i < numSpikes; i++) {
//            double angle = i * angleIncrement; // Calculate the angle for this spike
//            createCurlySpike(world, centerPos, radius, maxHeight, angle, block);
//        }
//    }
//
//    /**
//     * Creates a single curly spike.
//     *
//     * @param world The game world.
//     * @param centerPos The central position.
//     * @param radius The radius from the center to the base of the spike.
//     * @param maxHeight The maximum height for the spike.
//     * @param angle The angle at which the spike is oriented.
//     * @param block The block type to use for the spike.
//     */
//    private static void createCurlySpike(World world, BlockPos centerPos, int radius, int maxHeight, double angle, Block block) {
//        // Determine the base position of the spike
//        int baseX = centerPos.getX() + (int) (radius * Math.cos(angle));
//        int baseZ = centerPos.getZ() + (int) (radius * Math.sin(angle));
//        BlockPos basePos = new BlockPos(baseX, centerPos.getY(), baseZ);
//
//        // Randomly determine the height and curvature of the spike
//        int spikeHeight = random.nextInt(maxHeight) + 1; // Height between 1 and maxHeight
//        double curvature = (random.nextDouble() * 0.5) + 0.5; // Curvature between 0.5 and 1.0
//
//        // Generate the spike
//        for (int y = 0; y < spikeHeight; y++) {
//            double t = (double) y / spikeHeight; // Normalized height
//            int dx = (int) (t * radius * curvature * Math.cos(angle + Math.PI * t)); // Curly effect
//            int dz = (int) (t * radius * curvature * Math.sin(angle + Math.PI * t)); // Curly effect
//
//            BlockPos spikePos = basePos.add(dx, y, dz);
//            placeBlockWithThickness(world, spikePos, block, 2 - (y / (spikeHeight / 2))); // Decrease thickness as it goes up
//        }
//    }
//
//    /**
//     * Places a block with a given thickness around a central point.
//     *
//     * @param world The game world.
//     * @param centerPos The center position.
//     * @param block The block type to place.
//     * @param thickness The thickness of the placement (1 for a single block, 2 for 3x3 area).
//     */
//    private static void placeBlockWithThickness(World world, BlockPos centerPos, Block block, int thickness) {
//        for (int dx = -thickness; dx <= thickness; dx++) {
//            for (int dy = -thickness; dy <= thickness; dy++) {
//                for (int dz = -thickness; dz <= thickness; dz++) {
//                    BlockPos pos = centerPos.add(dx, dy, dz);
//                    double distance = MathHelper.sqrt(dx * dx + dy * dy + dz * dz);
//                    if (distance <= thickness) {
//                        world.setBlockState(pos, block.getDefaultState());
//                    }
//                }
//            }
//        }
//    }



    /**
     * Starts the restoration process.
     *
     * @param restoringTicks Number of ticks needed for restoration.
     */
    public static void startRestoration(int restoringTicks) {
        isRestoring = true;
        maxRestoringTicks = restoringTicks;
        remainingRestoringTicks = maxRestoringTicks;
    }

    /**
     * Continues the restoration process step by step each tick.
     *
     * @param world The game world.
     */
    public static void continueRestoration(World world) {
        if (isRestoring) {
            int radius = (int) (Math.ceil(maxTerraformingDistance + 5) * ((double) (maxRestoringTicks - remainingRestoringTicks) / maxRestoringTicks));
            var remainingMap = new HashMap<BlockPos, BlockState>();
            for (Iterator<BlockPos> iterator = restorationMap.keySet().iterator(); iterator.hasNext(); ) {
                BlockPos pos = iterator.next();
                BlockState state = restorationMap.get(pos);
                if (state != null) {
                    if (Math.sqrt(pos.getSquaredDistance(startPos)) <= radius)
                        world.setBlockState(pos, state);
                    else remainingMap.put(pos, state);
                }
            }
            restorationMap = remainingMap;
            remainingRestoringTicks--;

            // If we've restored all positions, stop the restoration process
            if (remainingRestoringTicks <= 0) {
                isRestoring = false;
            }
        }
    }

    /**
     * Calculates the next position in the path based on the current angle.
     *
     * @param currentPos The current block position.
     * @param angle The current direction angle in radians.
     * @return The next block position.
     */
    private static BlockPos getNextPosition(BlockPos currentPos, double angle) {
        int dx = (int) Math.round(Math.cos(angle));
        int dz = (int) Math.round(Math.sin(angle));

        // Return the new position (keeping y the same for simplicity)
        return currentPos.add(dx, 0, dz);
    }

    /**
     * Calculates the current thickness of the curve based on the initial thickness and remaining ticks.
     *
     * @param initialThickness The initial thickness of the curve.
     * @param maxTicks The maximum number of ticks for terraforming.
     * @param remainingTicks The number of ticks remaining.
     * @return The current thickness of the curve.
     */
    private static int calculateThickness(int initialThickness, int maxTicks, int remainingTicks) {
        double factor = (double) remainingTicks / maxTicks;
        return (int)(initialThickness * factor);
    }

    /**
     * Places blocks in a thickened line around the given position in the lower half of a sphere.
     *
     * @param world The game world.
     * @param centerPos The center position of the curve at this step.
     * @param thickness The thickness of the curve.
     * @param centerBlock The block type to place in the center of curve.
     * @param outerBlock The block type to place in the other parts of curve.
     */
    private static void placeThickenedBlocksLowerHalfSphere(World world, BlockPos centerPos, int thickness, Block centerBlock, Block outerBlock) {
        // Iterate through a cube of side length (2 * thickness + 1) centered at centerPos
        for (int dx = -thickness; dx <= thickness; dx++) {
            for (int dy = -thickness; dy <= 0; dy++) { // Only consider blocks below or at the center Y
                for (int dz = -thickness; dz <= thickness; dz++) {
                    // Calculate the distance from the center position
                    double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);

                    // Check if the position is within the spherical radius
                    if (distance <= thickness) {
                        BlockPos pos = centerPos.add(dx, dy, dz);
                        if(restorationMap.putIfAbsent(pos, world.getBlockState(pos)) == null) {
                            double distanceFromCenter = Math.sqrt(pos.getSquaredDistance(startPos));
                            if (distanceFromCenter > maxTerraformingDistance) maxTerraformingDistance = distanceFromCenter;
                            world.setBlockState(pos, outerBlock.getDefaultState());
                        }
                    }
                }
            }
        }
        restorationMap.putIfAbsent(centerPos, world.getBlockState(centerPos));
        double distanceFromCenter = Math.sqrt(centerPos.getSquaredDistance(startPos));
        if (distanceFromCenter > maxTerraformingDistance) maxTerraformingDistance = distanceFromCenter;
        world.setBlockState(centerPos, centerBlock.getDefaultState());
    }


    public static ArrayList<BlockPos> getBlocksInSphere(World world, BlockPos pos, List<Block> blacklist, int radiusX, int radiusY, int radiusZ) {
        ArrayList<BlockPos> blockPositions = new ArrayList<>();

        int cx = pos.getX();
        int cy = pos.getY();
        int cz = pos.getZ();

        // Iterate through blocks in a cube bounding the spheroid
        for (int x = -radiusX; x <= radiusX; x++) {
            for (int y = -radiusY; y <= radiusY; y++) {
                for (int z = -radiusZ; z <= radiusZ; z++) {
                    // Calculate the absolute block position
                    BlockPos blockPos = pos.add(x, y, z);

                    // Check if the block is within the ellipsoid using the ellipsoid equation
                    double normalizedX = (double)x / (double)radiusX;
                    double normalizedY = (double)y / (double)radiusY;
                    double normalizedZ = (double)z / (double)radiusZ;

                    double distanceSquared = normalizedX * normalizedX + normalizedY * normalizedY + normalizedZ * normalizedZ;

                    // If within the spheroid, add the block position
                    if (distanceSquared < 1.0) {
                        var currentBlockState = world.getBlockState(blockPos);
                        if (!blacklist.contains(currentBlockState.getBlock()))
                            blockPositions.add(blockPos);
                    }
                }
            }
        }

        return blockPositions;
    }


}
