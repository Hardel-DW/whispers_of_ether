package fr.hardel.whispers_of_ether.object;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.Vec3d;

/**
 * World-persistent renderable object definition.
 *
 * @param strength used by some types (e.g. black hole lensing)
 */
public record SceneObject(String id, fr.hardel.whispers_of_ether.object.SceneObject.Type type, Vec3d position,
                          float radius, float strength) {

    public enum Type implements StringIdentifiable {
        GALAXY_SPHERE("galaxy_sphere"),
        BLACK_HOLE("black_hole");

        public static final Codec<Type> CODEC = StringIdentifiable.createCodec(Type::values);
        private final String name;

        Type(String name) {
            this.name = name;
        }

        @Override
        public String asString() {
            return this.name;
        }
    }

    public static final Codec<Vec3d> VEC3D_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.DOUBLE.fieldOf("x").forGetter(v -> v.x),
            Codec.DOUBLE.fieldOf("y").forGetter(v -> v.y),
            Codec.DOUBLE.fieldOf("z").forGetter(v -> v.z)).apply(instance, Vec3d::new));

    public static final Codec<SceneObject> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    Codec.STRING.fieldOf("id").forGetter(SceneObject::id),
                    Type.CODEC.fieldOf("type").forGetter(SceneObject::type),
                    VEC3D_CODEC.fieldOf("position").forGetter(SceneObject::position),
                    Codec.FLOAT.fieldOf("radius").forGetter(SceneObject::radius),
                    Codec.FLOAT.optionalFieldOf("strength", 1.0f).forGetter(SceneObject::strength))
            .apply(instance, SceneObject::new));

}
