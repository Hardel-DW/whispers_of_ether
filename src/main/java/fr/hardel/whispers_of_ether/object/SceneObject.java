package fr.hardel.whispers_of_ether.object;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.math.Vec3d;

/**
 * World-persistent renderable object definition.
 *
 * @param strength used by some types
 */
public record SceneObject(String id, SceneObjectType type, Vec3d position,
        float radius, float strength) {

    public static final Codec<Vec3d> VEC3D_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.DOUBLE.fieldOf("x").forGetter(v -> v.x),
            Codec.DOUBLE.fieldOf("y").forGetter(v -> v.y),
            Codec.DOUBLE.fieldOf("z").forGetter(v -> v.z)).apply(instance, Vec3d::new));

    public static final Codec<SceneObject> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("id").forGetter(SceneObject::id),
            SceneObjectType.CODEC.fieldOf("type").forGetter(SceneObject::type),
            VEC3D_CODEC.fieldOf("position").forGetter(SceneObject::position),
            Codec.FLOAT.fieldOf("radius").forGetter(SceneObject::radius),
            Codec.FLOAT.optionalFieldOf("strength", 1.0f).forGetter(SceneObject::strength))
            .apply(instance, SceneObject::new));

}
