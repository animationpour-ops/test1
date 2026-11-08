# Items Divins — mod Forge (Minecraft 1.21.1)

Recréation perso, pour le fun, du mod « items divins » vu dans la vidéo : Forge céleste, Étoile divine,
armes/outils/armure de **Niveau Divin** (tooltip arc-en-ciel), capacités et effets visuels à particules.

> ⚠️ Le code n'a **pas pu être compilé** là où il a été écrit (pas d'accès aux serveurs Maven de Forge/Minecraft).
> Il a été relu à la main contre l'API Forge 1.21.1 (mappings Mojang). Si Gradle signale une erreur, c'est probablement
> un nom de méthode ou de classe : copie l'erreur et on la corrige.

## Compiler avec GitHub (sans rien installer)

1. Crée un dépôt GitHub (privé si tu veux) et envoie-y **tout le contenu** de ce dossier (y compris `.github/`).
2. Onglet **Actions** → le workflow « Build mod » se lance tout seul à chaque envoi (ou bouton *Run workflow*).
3. Quand il est vert, ouvre l'exécution → section **Artifacts** → télécharge `divine-items-jar`.
4. S'il est rouge, ouvre l'étape « Build » et copie les lignes d'erreur : on les corrige.

## Compiler

1. Installer **JDK 21**.
2. **Méthode la plus sûre** : télécharger le **MDK Forge 1.21.1** sur https://files.minecraftforge.net, le dézipper,
   puis remplacer son dossier `src/` par celui de ce projet (et mettre `mod_id=divinitems`, `mod_group_id=com.divinitems`
   dans son `gradle.properties`). Ouvrir dans **IntelliJ IDEA** (import Gradle automatique).
   Les fichiers `build.gradle` / `gradle.properties` fournis ici sont une version de secours basée sur le MDK.
3. Construire : `gradlew build` → le .jar est dans `build/libs/`. Tester : `gradlew runClient`.
4. Mettre le .jar dans `.minecraft/mods/` avec **Forge 1.21.1**.

> Les mappings sont les mappings officiels Mojang (`official`). La version de Forge dans `gradle.properties`
> (`forge_version`) est à régler sur la dernière 1.21.1 si Gradle ne la trouve pas.

## Contenu

| Item | Capacités |
|---|---|
| **Forge céleste** (bloc, modèle 3D fait dans Blockbench) | Clic droit avec un équipement en **diamant** (ou un **arc**) + 1 **Étoile divine** dans l'inventaire → version divine, avec foudre + pilier de lumière |
| **Étoile divine** | Composant de forge |
| **Épée divine** | *Tranchant céleste* (croissant lumineux + dégâts de zone), *Exécution divine* (monstre < 25 % de vie exécuté, +absorption), *Jugement dernier* (clic droit : cible marquée → foudre après 1,5 s) |
| **Arc divin** | *Constellation meurtrière* (tir à pleine charge : lignes d'étoiles entre les monstres), *Pluie d'étoiles* (idem accroupi) |
| **Pioche divine** | *Veine divine* : minage de tout le filon (accroupi = normal) |
| **Hache divine** | *Abattage céleste* : tout l'arbre d'un coup (accroupi = normal) |
| **Casque divin** | *Vision nocturne*, *Regard autoritaire* (le monstre fixé est paralysé et révélé), Protection X |
| **Plastron divin** | *Présence céleste* : régénération, résistance, bouclier doré |
| **Jambières divines** | *Course céleste* : vitesse |
| **Bottes divines** | *Pas divin* : saut boosté, aucune dégât de chute, traînée d'étoiles |

Recettes (table de craft) :
- **Forge céleste** : 3 diamants (rangée du haut) + 3 lingots de fer (rangée du milieu) — comme dans la vidéo.
- **Étoile divine** : 4 lingots d'or + 4 diamants + 1 émeraude au centre (`data/divinitems/recipe/divine_star.json`, à modifier à ta guise).

## Ce qui vient de la vidéo, et ce qui est interprété

La vidéo est en 512×288 : tout ne se lit pas.

**Vu clairement** : le nom « Forge céleste », le tooltip « Niveau Divin » arc-en-ciel, la recette de la forge,
les noms des capacités *Tranchant céleste, Exécution divine, Jugement dernier, Constellation meurtrière,
Pluie d'étoiles, Regard autoritaire, Pas divin*, « Cible marquée : <mob> », Vision nocturne, le casque
(+3 armure, +3 robustesse, +10 % recul, Protection X), l'allure blanc/or des items et de la forge.

**Interprété par moi** (illisible ou non visible) : ce que fait exactement chaque capacité, les dégâts, les
durées de recharge, l'attribution des capacités aux pièces d'armure, le nom *Présence céleste* (le mot
suivant « Présence » est illisible), la recette de l'Étoile divine, les enchantements donnés à la forge
(Tranchant V, Efficacité V…), la forme exacte de la forge. Les valeurs sont dans le code
(`items/*.java`, `ArmorEffects.java`, `ForgeUpgrades.java`) — facile à ajuster.

**Pas identique à 100 %** : les textures sont du pixel art refait de zéro (aucun fichier de la vidéo n'a été
copié) et les effets utilisent les particules vanilla (END_ROD, éclairs, flash…).

## Régénérer les textures / modèles

`python3 tools/gen_assets.py` (Pillow requis) réécrit textures, modèles, lang, recettes et loot table.
