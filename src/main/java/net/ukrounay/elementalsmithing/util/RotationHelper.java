package net.ukrounay.elementalsmithing.util;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;
import org.joml.Vector3f;

public class RotationHelper {
    public static void applyFacingRotation(MatrixStack matrices, Direction facing) {
        switch (facing) {
            case DOWN  -> matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180));
            case NORTH -> matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90));
            case SOUTH -> matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-90));
            case WEST  -> matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(-90));
            case EAST  -> matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(90));
            case UP    -> {}
        }
    }

    public static void applyFacingRotation(Vector3f offset, Direction facing) {
        switch (facing) {
            case DOWN  -> offset.rotate(RotationAxis.POSITIVE_X.rotationDegrees(180));
            case NORTH -> offset.rotate(RotationAxis.POSITIVE_X.rotationDegrees(90));
            case SOUTH -> offset.rotate(RotationAxis.POSITIVE_X.rotationDegrees(-90));
            case WEST  -> offset.rotate(RotationAxis.POSITIVE_Z.rotationDegrees(-90));
            case EAST  -> offset.rotate(RotationAxis.POSITIVE_Z.rotationDegrees(90));
            case UP    -> {}
        }
    }
}
