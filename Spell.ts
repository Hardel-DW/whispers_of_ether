/**
 * DO NOT TOUCH THIS FILE. IT'S THE DOCUMENTATION OF THE SPELLS.
 */

/**
 * Interface racine définissant un sort complet.
 */
interface Spell {
    /** Icône du sort (identifiant d'item ou chemin de ressource) */
    icon: string;
    /** Nom affiché du sort */
    name: string;
    /** Temps de recharge en ticks (20 ticks = 1 seconde). Si non défini, pas de cooldown. */
    cooldown?: number;
    /** Conditions pour pouvoir lancer le sort */
    condition?: Predicate;
    /** Tableau des timelines à exécuter lorsque le sort est activé. Toutes les timelines de ce tableau sont lancées en parallèle. */
    timelines: TimelineAction[];
    /** Organisation/orchestration des timelines */
    organization: OrganizationTimeline;
    /** Actions passives, vérifiées en permanence et déclenchées si leurs conditions sont remplies */
    passive?: SpellAction[];
}

/** Représente les prédicat de datapack. */
type Predicate = any;

/** Représente une position dans le monde */
interface Position {
    x: number;
    y: number;
    z: number;
}

/** Représente une rotation d'entité */
interface Rotation {
    /** Rotation verticale (pitch) */
    x: number;
    /** Rotation horizontale (yaw) */
    y: number;
}

//================================================================================
// Actions Spécifiques
//================================================================================

interface PlayParticleAction {
    type: 'play_particle';
    particle: string;
    position?: Position;
    offset?: Position;
    count?: number;
    speed?: number;
}

interface PlaySoundAction {
    type: 'play_sound';
    sound: string;
    position?: Position;
    volume?: number;
    pitch?: number;
}

interface DamageAction {
    type: 'damage';
    /** La quantité de dégâts. Une valeur négative soigne l'entité. */
    amount: number;
}

interface KnockbackAction {
    type: 'knockback';
    /** La force du recul */
    strength: number;
    /** Angle vertical du recul */
    verticalAngle?: number;
    /** Angle horizontal du recul */
    horizontalAngle?: number;
}

interface DropEquipmentAction {
    type: 'drop_equipment';
    slot: 'mainhand' | 'offhand' | 'head' | 'chest' | 'legs' | 'feet' | 'saddle' | "body" | "hand" | "armor" | "all";
}

interface UseAmmoAction {
    type: 'ammo_use';
    /** L'identifiant de l'item à consommer */
    item: string;
    count: number;
}

interface ItemAction {
    type: 'item';
    operation: 'add' | 'remove' | 'spawn';
    /** L'identifiant de l'item */
    item: string;
    count?: number;
    component: string; // Component of the item.
}

interface LootAction {
    type: 'loot';
    operation: 'add' | 'remove' | 'spawn';
    /** L'identifiant de la table de butin */
    lootTable: string;
}

interface DamageItemAction {
    type: 'damage_item';
    amount: number;
    /** L'emplacement de l'item à endommager */
    slot: 'mainhand' | 'offhand' | 'head' | 'chest' | 'legs' | 'feet';
}

interface AddProjectileAction {
    type: 'add_projectile';
    /** Nombre de projectiles additionnels à lancer */
    count: number;
}

interface StatusEffectAction {
    type: 'status_effect';
    /** L'identifiant de l'effet de statut (ex: "minecraft:speed") */
    effect: string;
    /** Durée en ticks */
    duration: number;
    amplifier?: number;
    showParticles?: boolean;
}

interface AttributeModifierAction {
    type: 'attribute';
    /** L'identifiant de l'attribut (ex: "minecraft:generic.movement_speed") */
    attribute: string;
    /** La valeur du modificateur */
    value: number;
    operation: 'add' | 'multiply';
    /** Durée en ticks. Si non défini, le modificateur est permanent. */
    duration?: number;
}

interface ExperienceAction {
    type: 'experience';
    /** Quantité de points ou de niveaux */
    amount: number;
    unit: 'points' | 'levels';
}

interface TeleportAction {
    type: 'teleport';
    position: Position;
}

interface SetRotationAction {
    type: 'rotation';
    rotation: Rotation;
}

interface AttackAction {
    type: 'attack';
    /** Si vrai, simule une attaque avec l'item tenu en main principale */
    useItem: boolean;
}

interface SpawnEntityAction {
    type: 'spawn_entity';
    /** L'identifiant de l'entité (ex: "minecraft:zombie") */
    entity: string;
    position?: Position;
    rotation?: Rotation;
}

interface SetBlockAction {
    type: 'set_block';
    /** L'identifiant du bloc (ex: "minecraft:stone") */
    block: string;
    position: Position;
    /** Les états du bloc (ex: { "powered": "true" }) */
    state?: Record<string, string | number | boolean>;
}

interface FillBlockAction {
    type: 'fill_block';
    block: string;
    from: Position;
    to: Position;
    state?: Record<string, string | number | boolean>;
}

interface FreezeAction {
    type: 'freeze';
    /** Durée du gel en ticks */
    duration: number;
}

interface ExplodeAction {
    type: 'explode';
    /** La puissance de l'explosion */
    power: number;
    /** Le type de destruction des blocs */
    destructionType: 'none' | 'break' | 'destroy';
}

interface DamageImmunityAction {
    type: 'damage_immunity';
    /** Durée de l'immunité en ticks */
    duration: number;
}

interface PreventDropAction {
    type: 'prevent_drop';
    /** Durée de l'effet en ticks */
    duration: number;
    slots: 'equipment' | 'armor' | 'all';
}

interface KeepItemOnDeathAction {
    type: 'keep_item_on_death';
    /** Si défini, l'effet ne dure que ce nombre de ticks */
    duration?: number;
}

interface IncrementInternalValueAction {
    type: 'increment_internal_value';
    id: string;
    amount: number;
}

interface DecrementInternalValueAction {
    type: 'decrement_internal_value';
    id: string;
    amount: number;
}

interface SetInternalValueAction {
    type: 'set_internal_value';
    id: string;
    value: number;
}

interface ToggleInternalValueAction {
    type: 'toggle_internal_value';
    id: string;
}


/**
 * Union de toutes les actions possibles pour une utilisation polymorphique.
 * La propriété 'type' sert de discriminateur.
 */
type Action =
    | IncrementInternalValueAction
    | DecrementInternalValueAction
    | SetInternalValueAction
    | ToggleInternalValueAction
    | PlayParticleAction
    | PlaySoundAction
    | DamageAction
    | KnockbackAction
    | DropEquipmentAction
    | UseAmmoAction
    | ItemAction
    | LootAction
    | DamageItemAction
    | AddProjectileAction
    | StatusEffectAction
    | AttributeModifierAction
    | ExperienceAction
    | TeleportAction
    | SetRotationAction
    | AttackAction
    | SpawnEntityAction
    | SetBlockAction
    | FillBlockAction
    | FreezeAction
    | ExplodeAction
    | DamageImmunityAction
    | PreventDropAction
    | KeepItemOnDeathAction;

//================================================================================
// Structure Principale du Sort
//================================================================================

/**
 * Définit une action unique au sein d'une séquence de sort.
 */
interface SpellAction {
    /** L'action concrète à exécuter */
    action: Action;
    /** Conditions pour que cette action spécifique puisse s'exécuter */
    condition?: Predicate;
    /** Target of the action. Par défaut nous même. */
    target?: Target;
}

interface TimelineAction {
    id: string;
    actions: SpellAction[];
    /** Duration of the timeline in ticks. */
    duration: number;
    /** Conditions pour que cette timeline puisse s'exécuter */
    condition?: Predicate;
    /** Wait some seconds before the action. */
    wait?: number;
}

/** Exécute les timelines l'une après l'autre */
interface SequentialOrganization {
    type: 'sequential';
    /** IDs des timelines à exécuter dans l'ordre */
    timelineIds: string[];
}

/** Exécute toutes les timelines en parallèle */
interface ParallelOrganization {
    type: 'parallel';
    /** IDs des timelines à exécuter simultanément */
    timelineIds: string[];
}

/** Exécute une timeline selon une condition */
interface ConditionalOrganization {
    type: 'conditional';
    /** Condition pour déclencher cette timeline */
    condition: Predicate;
    /** ID de la timeline à exécuter si la condition est vraie */
    timelineId: string;
}

/** Exécute une timeline aléatoire parmi plusieurs */
interface RandomOrganization {
    type: 'random';
    /** Liste des timelines avec leurs poids de probabilité */
    choices: {
        timelineId: string;
        weight: number;
    }[];
}

/** Répète une timeline un certain nombre de fois */
interface LoopOrganization {
    type: 'loop';
    /** ID de la timeline à répéter */
    timelineId: string;
    /** Nombre de répétitions */
    iterations: number;
    /** Délai entre chaque répétition en ticks */
    delay?: number;
}

/** Organisation imbriquée permettant de combiner plusieurs types */
interface GroupOrganization {
    type: 'group';
    /** Organisation des éléments du groupe */
    organization: OrganizationTimeline;
}

/**
 * Union de tous les types d'organisation possibles
 */
type OrganizationTimeline =
    | SequentialOrganization
    | ParallelOrganization
    | ConditionalOrganization
    | RandomOrganization
    | LoopOrganization
    | GroupOrganization;

/** Cible une ou plusieurs entités dans une zone définie. */
interface AreaTarget {
    type: 'area';
    /** La forme de la zone d'effet */
    shape: Shape;
    /** Le nombre maximum d'entités à affecter. Si non défini, toutes les entités sont ciblées. */
    limit?: number;
    /** Coordonnée de la zone. */
    /** Prédicats pour filtrer les entités dans la zone */
    condition?: Predicate;
}

type Shape = {
    area: (ShapeShere | ShapeCube | ShapeBox)[]
    position: PositionTargetRelative | PositionTargetAbsolute;
}

type ShapeShere = {
    type: 'sphere';
    radius: number;
}

type ShapeCube = {
    type: 'cube';
    size: number;
}

type ShapeBox = {
    type: 'rectangle';
    x: number;
    y: number;
    z: number;
}

interface PositionTargetAbsolute {
    type: 'absolute';
    position: Position;
}

interface PositionTargetRelative {
    type: 'relative' | 'local';
    // Sois equivalent de ~ ~ ~ sois de ^ ^ ^
    offset: Position;
}

interface AimedTarget {
    type: 'aimed';
    min_distance: number;
    max_distance: number;
    /** Prédicats pour filtrer les entités dans la zone */
    condition?: Predicate;
}

/**
 * Union de toutes les stratégies de ciblage possibles.
 * Par défaut, si `target` n'est pas spécifié dans une SpellAction, la cible est 'self'.
 */
type Target =
    | 'self'          // Le lanceur du sort
    | AimedTarget     // L'entité lanceur regarde
    | AreaTarget      // Une zone autour du lanceur
J