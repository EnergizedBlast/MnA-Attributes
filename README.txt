An addon mod for Mana and Artifice that implements attributes for multiple aspects of the magic system.

This does NOT add any items that enable these properties on their own, modpack makers and developers must add that on their own.

There is a config that allows players to set their starting values for each when they first join a world (e.g. you can set your starting mana multiplier to 0 and force players to get mana from other equipment or mods). (Config changes do not apply retroactively to already existing players and require using the /attribute command to set them instead)

Currently implemented attributes:

    Max Mana: Flat additive bonus mana
    Max Mana Multiplier: Multiplies the player's BASE mana
    Mana Regen: Shortens the time mana takes to fill up (values +2 instantly fill mana)
    Cast Speed: Multiplies the cooldown time between spell casts
    Spell Efficiency: Divides the mana costs of spell casts (higher value = lower cost)
    Spell Damage: Flat additive bonus damage on all damage components
    Spell Damage Multiplier: Multiplies the player's TOTAL damage from damage components
    An attribute representing each affinity; all affinities are now updated and controlled by their respective attribute (use /attribute to set affinity values instead, asthe MnA set-affinity command no longer works as a result)
