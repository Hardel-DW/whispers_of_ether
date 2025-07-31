package fr.hardel.whispers_of_ether.spell;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Identifier;

public record Spell(Identifier icon, String name, int manaCost) {
    public static final Codec<Spell> CODEC = RecordCodecBuilder.create(instance -> 
        instance.group(
            Identifier.CODEC.fieldOf("icon").forGetter(Spell::icon),
            Codec.STRING.fieldOf("name").forGetter(Spell::name),
            Codec.INT.fieldOf("mana_cost").forGetter(Spell::manaCost)
        ).apply(instance, Spell::new)
    );

    public Identifier getTextureId() {
        return Identifier.of(icon.getNamespace(), "textures/spell/" + icon.getPath() + ".png");
    }

    public void cast(Object... args) {
        // Logique de cast à implémenter
    }
}