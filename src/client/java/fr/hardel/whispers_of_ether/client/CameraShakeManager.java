package fr.hardel.whispers_of_ether.client;

import fr.hardel.whispers_of_ether.attributes.ModAttribute;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;

/**
 * Camera shake manager that applies screen shake effects based on player's
 * attributes.
 * Uses CAMERA_SHAKING_STRENGTH and CAMERA_SHAKING_FREQUENCY attributes to
 * modify the effect.
 * Usage examples:
 * - CameraShakeManager.shake(1.0f, 0.5f): Shake with intensity 1.0 for 0.5
 * seconds
 * - CameraShakeManager.constantShake(0.5f): Constant shake with intensity 0.5
 * - CameraShakeManager.stopShake(): Stop all shaking effects
 */
public class CameraShakeManager {
    private static CameraShakeManager instance;

    private float shakeIntensity = 0.0f;
    private float shakeDuration = 0.0f;
    private float maxShakeDuration = 0.0f;
    private long lastUpdateTime = 0;

    private float currentShakeX = 0.0f;
    private float currentShakeY = 0.0f;
    private float currentShakeZ = 0.0f;

    private CameraShakeManager() {
    }

    public static CameraShakeManager getInstance() {
        if (instance == null) {
            instance = new CameraShakeManager();
        }
        return instance;
    }

    public void update() {
        Minecraft client = Minecraft.getInstance();
        if (client.player == null) {
            resetShake();
            return;
        }

        long currentTime = System.currentTimeMillis();
        if (lastUpdateTime == 0) {
            lastUpdateTime = currentTime;
            return;
        }

        float deltaTime = (currentTime - lastUpdateTime) / 1000.0f;
        lastUpdateTime = currentTime;

        if (shakeDuration > 0) {
            shakeDuration -= deltaTime;
            if (shakeDuration <= 0) {
                resetShake();
                return;
            }
        }

        updateShakeValues(client.player);
    }

    private void updateShakeValues(Player player) {
        double strengthAttribute = player.getAttributeValue(ModAttribute.CAMERA_SHAKING_STRENTH);
        double frequencyAttribute = player.getAttributeValue(ModAttribute.CAMERA_SHAKING_FREQUENCY);

        float baseIntensity = shakeIntensity;
        if (baseIntensity == 0 && strengthAttribute > 0.0 && frequencyAttribute > 0.0) {
            baseIntensity = (float) strengthAttribute;
            this.shakeIntensity = baseIntensity;
        } else if (strengthAttribute == 0.0 || frequencyAttribute == 0.0) {
            this.shakeIntensity = 0.0f;
            baseIntensity = 0.0f;
        }

        float effectiveIntensity = baseIntensity;

        if (shakeDuration > 0 && maxShakeDuration > 0) {
            float fadeMultiplier = shakeDuration / maxShakeDuration;
            effectiveIntensity *= fadeMultiplier;
        }

        if (effectiveIntensity > 0) {
            float time = (System.currentTimeMillis() % 10000) / 1000.0f;
            float frequency = (float) frequencyAttribute;

            currentShakeX = (float) (Math.sin(time * frequency * 2.0) * effectiveIntensity * 0.1);
            currentShakeY = (float) (Math.cos(time * frequency * 1.7) * effectiveIntensity * 0.08);
            currentShakeZ = (float) (Math.sin(time * frequency * 2.3) * effectiveIntensity * 0.05);
        } else {
            currentShakeX = 0.0f;
            currentShakeY = 0.0f;
            currentShakeZ = 0.0f;
        }
    }

    public void addShake(float intensity, float duration) {
        this.shakeIntensity = Math.max(this.shakeIntensity, intensity);
        if (duration > 0) {
            this.shakeDuration = Math.max(this.shakeDuration, duration);
            this.maxShakeDuration = Math.max(this.maxShakeDuration, duration);
        }
    }

    public void addShake(float intensity) {
        addShake(intensity, 0);
    }

    public void setConstantShake(float intensity) {
        this.shakeIntensity = intensity;
        this.shakeDuration = 0;
        this.maxShakeDuration = 0;
    }

    public void resetShake() {
        this.shakeIntensity = 0.0f;
        this.shakeDuration = 0.0f;
        this.maxShakeDuration = 0.0f;
        this.currentShakeX = 0.0f;
        this.currentShakeY = 0.0f;
        this.currentShakeZ = 0.0f;
    }

    public float getShakeX() {
        return currentShakeX;
    }

    public float getShakeY() {
        return currentShakeY;
    }

    public float getShakeZ() {
        return currentShakeZ;
    }

    public boolean isShaking() {
        return shakeIntensity > 0.001f;
    }

    public float getCurrentIntensity() {
        return shakeIntensity;
    }

    // Static convenience methods

    /**
     * Apply a temporary shake effect with specified intensity and duration
     */
    public static void shake(float intensity, float duration) {
        getInstance().addShake(intensity, duration);
    }

    /**
     * Apply a temporary shake effect with default duration (0.5 seconds)
     */
    public static void shake(float intensity) {
        shake(intensity, 0.5f);
    }

    /**
     * Set a constant shake effect that persists until manually stopped
     */
    public static void constantShake(float intensity) {
        getInstance().setConstantShake(intensity);
    }

    /**
     * Stop all shake effects immediately
     */
    public static void stopShake() {
        getInstance().resetShake();
    }
}