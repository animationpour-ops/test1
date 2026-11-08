#!/usr/bin/env python3
"""Génère textures (pixel art), modèles JSON, blockstate, lang, recettes et loot table du mod Items Divins."""
import json, os, random
from PIL import Image, ImageDraw

ROOT = os.path.join(os.path.dirname(os.path.abspath(__file__)), "..", "src", "main", "resources")
ASSETS = os.path.join(ROOT, "assets", "divinitems")
DATA = os.path.join(ROOT, "data", "divinitems")
TEX_ITEM = os.path.join(ASSETS, "textures", "item")
TEX_BLOCK = os.path.join(ASSETS, "textures", "block")
TEX_ARMOR = os.path.join(ASSETS, "textures", "models", "armor")
for d in (TEX_ITEM, TEX_BLOCK, TEX_ARMOR):
    os.makedirs(d, exist_ok=True)

# ---------- palette ----------
W   = (246, 248, 252, 255)   # blanc
L   = (206, 212, 224, 255)   # gris clair
M   = (150, 158, 176, 255)   # gris moyen
D   = (84, 90, 108, 255)     # gris foncé
K   = (40, 44, 58, 255)      # presque noir
Y   = (252, 207, 72, 255)    # or
YD  = (190, 140, 30, 255)    # or foncé
C   = (130, 235, 255, 255)   # cyan lumineux
CD  = (60, 170, 210, 255)    # cyan foncé
P   = (200, 120, 255, 255)   # violet
T   = (0, 0, 0, 0)

KEY = {'.': T, 'W': W, 'L': L, 'M': M, 'D': D, 'K': K, 'Y': Y, 'y': YD, 'C': C, 'c': CD, 'P': P}

def from_art(rows):
    assert len(rows) == 16, len(rows)
    img = Image.new("RGBA", (16, 16), T)
    px = img.load()
    for y, row in enumerate(rows):
        assert len(row) == 16, (y, row)
        for x, ch in enumerate(row):
            px[x, y] = KEY[ch]
    return img

# ---------- items ----------
SWORD = [
"..............WL",
".............WWL",
"............WWLM",
"...........WWLM.",
"..........WWLM..",
".........WWLM...",
"........WWLM....",
".y.....WWLM.....",
".yy...WWLM......",
"..yy.WWLM.......",
"...yYYLM........",
"....YYYy........",
"...yYYYY........",
"..yyy.yY........",
".yyy............",
".yy.............",
]

PICKAXE = [
"......LLWWWWL...",
"....LWWWMMMMWL..",
"...LWMM....MWWL.",
"..LWM.......MWL.",
"..LM...yy....ML.",
"......yYy.....L.",
".....yYy........",
"....yYy.........",
"...yYy..........",
"..yYy...........",
".yYy............",
".yy.............",
"yy..............",
"................",
"................",
"................",
]

AXE = [
"................",
".......LWWL.....",
"......LWWWWL....",
".....LWWWWWWL...",
".....LWWWWMWL...",
".....LWWWMyyL...",
"......LWMyYy....",
".......LMyYy....",
"........yYy.....",
".......yYy......",
"......yYy.......",
".....yYy........",
"....yYy.........",
"...yYy..........",
"..yYy...........",
"..yy............",
]

def bow_art(pull):
    img = Image.new("RGBA", (16, 16), T)
    d = ImageDraw.Draw(img)
    px = img.load()
    # arc (courbe sur la gauche), en pixels
    arc = [(10,1),(9,1),(8,2),(7,3),(6,4),(5,5),(4,6),(4,7),(3,8),(3,9),(4,10),(4,11),(5,12),(6,13),(7,14),(8,14),(9,15)]
    for (x, y) in arc:
        px[x, y] = W
        if x + 1 < 16 and px[x+1, y][3] == 0:
            px[x+1, y] = M
    # poignée dorée
    for y in (7, 8, 9):
        px[3, y] = Y
        px[4, y] = YD
    # pointes cyan
    px[10, 1] = C; px[9, 15] = C
    # corde (elle recule quand on tend l'arc)
    if pull == 0:
        for y in range(2, 15):
            px[11, y] = L if y % 2 else W
        px[10, 1] = C
    else:
        mid_x = 8 - pull  # la corde passe plus à gauche
        pts = [(10,1),(10,2),(9,3),(9,4),(8,5),(mid_x+1,6),(mid_x,7),(mid_x,8),(mid_x,9),(mid_x+1,10),(8,11),(9,12),(9,13),(10,14),(9,15)]
        for (x, y) in pts:
            px[x, y] = L
        # flèche
        for x in range(mid_x - 0, 15):
            if 0 <= x < 16:
                px[x, 8] = M if x % 2 else D
        px[14, 8] = C; px[15, 8] = C
    return img

HELMET = [
"................",
"................",
"....WWWWWWWW....",
"...WWLLLLLLWW...",
"..WWLWWWWWWLWW..",
"..WLWWWWWWWWLW..",
"..WLWYWWWWYWLW..",
"..WLWWWWWWWWLW..",
"..WLcCCWWCCcLW..",
"..WLWWWWWWWWLW..",
"..WL.WW..WW.LW..",
"..WM........MW..",
"..LM........ML..",
"................",
"................",
"................",
]

CHESTPLATE = [
"................",
"..WW........WW..",
".WWLW......WLWW.",
".WLLLWWWWWWLLLW.",
".WLYLWLLLLWLYLW.",
".WLLLLWWWWLLLLW.",
".LWWLLWYYWLLWWL.",
"..LWLLWYCWLLWL..",
"...WLLWYYWLLW...",
"...WWLWWWWLWW...",
"...WLLLLLLLLW...",
"...WYWWWWWWYW...",
"....LLLLLLLL....",
"....MMMMMMMM....",
"................",
"................",
]

LEGGINGS = [
"................",
"...YWWWWWWWWY...",
"...WLLLLLLLLW...",
"...WLWWWWWWLW...",
"...WLWW..WWLW...",
"...WLWW..WWLW...",
"...WLW....WLW...",
"...WLW....WLW...",
"...WLW....WLW...",
"...WLW....WLW...",
"...WYW....WYW...",
"...WLW....WLW...",
"...LLL....LLL...",
"...MMM....MMM...",
"................",
"................",
]

BOOTS = [
"................",
"................",
"................",
"................",
"...WWW....WWW...",
"...WLW....WLW...",
"...WLW....WLW...",
"...WYW....WYW...",
"...WLW....WLW...",
"..WWLW....WLWW..",
".WLLLLW..WLLLLW.",
".WWWWWWW.WWWWWWW",
".LLLLLLL.LLLLLLL",
".MMMMMMM.MMMMMMM",
"................",
"................",
]

STAR = [
"................",
".......WW.......",
".......WW.......",
"......WYYW......",
"......WYYW......",
"..WWWWYYCYWWWW..",
"...WYYYCCYYYW...",
"....WYYCCYYW....",
".....WYCCYW.....",
".....WYYYYW.....",
"....WYYWWYYW....",
"....WYW..WYW....",
"...WYW....WYW...",
"...WW......WW...",
"................",
"................",
]

def save(img, folder, name):
    img.save(os.path.join(folder, name + ".png"))

for name, art in [("divine_sword", SWORD), ("divine_pickaxe", PICKAXE), ("divine_axe", AXE),
                  ("divine_helmet", HELMET), ("divine_chestplate", CHESTPLATE),
                  ("divine_leggings", LEGGINGS), ("divine_boots", BOOTS), ("divine_star", STAR)]:
    save(from_art(art), TEX_ITEM, name)

save(bow_art(0), TEX_ITEM, "divine_bow")
for i in range(3):
    save(bow_art(i + 1), TEX_ITEM, f"divine_bow_pulling_{i}")

# ---------- textures du bloc (forge céleste) ----------
random.seed(7)
def marble(base=W, vein=L, size=16):
    img = Image.new("RGBA", (size, size), base)
    px = img.load()
    for y in range(size):
        for x in range(size):
            r = random.random()
            if r < 0.18: px[x, y] = vein
            elif r < 0.22: px[x, y] = M
    return img

white = marble()
save(white, TEX_BLOCK, "forge_white")

gold = Image.new("RGBA", (16, 16), Y)
gp = gold.load()
for y in range(16):
    for x in range(16):
        r = random.random()
        if r < 0.2: gp[x, y] = YD
        elif r > 0.9: gp[x, y] = (255, 235, 150, 255)
save(gold, TEX_BLOCK, "forge_gold")

# plaque de runes : fond sombre, glyphes cyan
rune = Image.new("RGBA", (16, 16), K)
rp = rune.load()
for i in range(16):
    for j in (0, 15):
        rp[i, j] = D; rp[j, i] = D
random.seed(11)
for gy in range(2, 14, 3):
    for gx in range(2, 14, 3):
        # petit glyphe 2x2/3x2 aléatoire
        for dy in range(2):
            for dx in range(2):
                if random.random() < 0.7:
                    rp[gx + dx, gy + dy] = C if random.random() < 0.7 else CD
save(rune, TEX_BLOCK, "forge_rune")

# ---------- textures d'armure portée (64x32) ----------
def armor_layer(layer):
    random.seed(100 + layer)
    img = Image.new("RGBA", (64, 32), T)
    px = img.load()

    def fill(x0, y0, x1, y1):
        for y in range(y0, y1):
            for x in range(x0, x1):
                r = random.random()
                px[x, y] = W if r > 0.2 else (L if r > 0.06 else M)

    def outline(x0, y0, x1, y1, col):
        for x in range(x0, x1):
            px[x, y0] = col; px[x, y1 - 1] = col
        for y in range(y0, y1):
            px[x0, y] = col; px[x1 - 1, y] = col

    if layer == 1:
        regions = [(0, 0, 32, 16),    # tête
                   (16, 16, 40, 32),  # torse
                   (40, 16, 56, 32),  # bras
                   (0, 16, 16, 32)]   # jambes / bottes
    else:
        regions = [(16, 16, 40, 32),  # taille
                   (0, 16, 16, 32)]   # jambes
    for r in regions:
        fill(*r)

    faces = []
    if layer == 1:
        faces += [(8, 0, 16, 8), (16, 0, 24, 8), (0, 8, 8, 16), (8, 8, 16, 16), (16, 8, 24, 16), (24, 8, 32, 16),
                  (20, 20, 28, 32), (32, 20, 40, 32), (16, 20, 20, 32), (28, 20, 32, 32),
                  (44, 20, 48, 32), (40, 20, 44, 32), (48, 20, 52, 32), (52, 20, 56, 32)]
    faces += [(4, 20, 8, 32), (12, 20, 16, 32), (0, 20, 4, 32), (8, 20, 12, 32),
              (20, 20, 28, 24) if layer == 2 else (20, 20, 28, 21)]
    for f in faces:
        outline(*f, Y)

    if layer == 1:
        # visière lumineuse (face avant du casque)
        for x in range(9, 15):
            px[x, 11] = C; px[x, 12] = CD
        # gemme sur le plastron
        for (x, y) in [(23, 23), (24, 23), (23, 24), (24, 24)]:
            px[x, y] = C
        # bottes dorées (bas des jambes)
        for y in range(28, 32):
            for x in range(0, 16):
                if px[x, y][3]:
                    px[x, y] = YD if (x + y) % 2 else Y
    else:
        for x in range(16, 40):
            for y in (16, 17):
                if px[x, y][3]: px[x, y] = Y
    return img

save(armor_layer(1), TEX_ARMOR, "divine_layer_1")
save(armor_layer(2), TEX_ARMOR, "divine_layer_2")

# ---------- modèles ----------
def wjson(path, obj):
    os.makedirs(os.path.dirname(path), exist_ok=True)
    with open(path, "w", encoding="utf-8") as f:
        json.dump(obj, f, indent=2, ensure_ascii=False)
        f.write("\n")

MI = os.path.join(ASSETS, "models", "item")
for name in ["divine_pickaxe", "divine_axe", "divine_helmet", "divine_chestplate", "divine_leggings", "divine_boots", "divine_star"]:
    wjson(os.path.join(MI, name + ".json"), {"parent": "minecraft:item/generated", "textures": {"layer0": f"divinitems:item/{name}"}})
wjson(os.path.join(MI, "divine_sword.json"), {"parent": "minecraft:item/handheld", "textures": {"layer0": "divinitems:item/divine_sword"}})
wjson(os.path.join(MI, "divine_pickaxe.json"), {"parent": "minecraft:item/handheld", "textures": {"layer0": "divinitems:item/divine_pickaxe"}})
wjson(os.path.join(MI, "divine_axe.json"), {"parent": "minecraft:item/handheld", "textures": {"layer0": "divinitems:item/divine_axe"}})

wjson(os.path.join(MI, "divine_bow.json"), {
    "parent": "minecraft:item/bow",
    "textures": {"layer0": "divinitems:item/divine_bow"},
    "overrides": [
        {"predicate": {"pulling": 1}, "model": "divinitems:item/divine_bow_pulling_0"},
        {"predicate": {"pulling": 1, "pull": 0.65}, "model": "divinitems:item/divine_bow_pulling_1"},
        {"predicate": {"pulling": 1, "pull": 0.9}, "model": "divinitems:item/divine_bow_pulling_2"},
    ],
})
for i in range(3):
    wjson(os.path.join(MI, f"divine_bow_pulling_{i}.json"), {
        "parent": f"minecraft:item/bow_pulling_{i}",
        "textures": {"layer0": f"divinitems:item/divine_bow_pulling_{i}"},
    })

# forge : modèle construit dans Blockbench (15 cubes), UV au mappage Minecraft standard
def vanilla_uv(f, t):
    return {
        "north": [16 - t[0], 16 - t[1], 16 - f[0], 16 - f[1]],
        "south": [f[0], 16 - t[1], t[0], 16 - f[1]],
        "east":  [16 - t[2], 16 - t[1], 16 - f[2], 16 - f[1]],
        "west":  [f[2], 16 - t[1], t[2], 16 - f[1]],
        "up":    [f[0], f[2], t[0], t[2]],
        "down":  [f[0], 16 - t[2], t[0], 16 - f[2]],
    }

def bcube(name, frm, to, tex, rot=None):
    e = {"name": name, "from": frm, "to": to}
    if rot:
        e["rotation"] = rot
    e["faces"] = {k: {"uv": uv, "texture": f"#{tex}"} for k, uv in vanilla_uv(frm, to).items()}
    return e

elements = [
    bcube("base_lower", [0, 0, 0], [16, 2, 16], "white"),
    bcube("base_upper", [1, 2, 1], [15, 4, 15], "white"),
    bcube("trim_gold", [0.5, 2, 0.5], [15.5, 2.6, 15.5], "gold"),
    bcube("rune_frame", [3, 4, 3], [13, 4.4, 13], "gold"),
    bcube("rune_plate", [4, 4.4, 4], [12, 4.8, 12], "rune"),
    bcube("pillar_nw", [1.5, 4, 1.5], [4.5, 11, 4.5], "white"),
    bcube("pillar_ne", [11.5, 4, 1.5], [14.5, 11, 4.5], "white"),
    bcube("pillar_sw", [1.5, 4, 11.5], [4.5, 11, 14.5], "white"),
    bcube("pillar_se", [11.5, 4, 11.5], [14.5, 11, 14.5], "white"),
    bcube("cap_nw", [1, 11, 1], [5, 12.5, 5], "gold"),
    bcube("cap_ne", [11, 11, 1], [15, 12.5, 5], "gold"),
    bcube("cap_sw", [1, 11, 11], [5, 12.5, 15], "gold"),
    bcube("cap_se", [11, 11, 11], [15, 12.5, 15], "gold"),
    bcube("star_a", [6.5, 7, 6.5], [9.5, 10, 9.5], "gold", {"angle": 45, "axis": "x", "origin": [8, 8.5, 8]}),
    bcube("star_b", [6.5, 7, 6.5], [9.5, 10, 9.5], "gold", {"angle": 45, "axis": "z", "origin": [8, 8.5, 8]}),
]
wjson(os.path.join(ASSETS, "models", "block", "celestial_forge.json"), {
    "textures": {"white": "divinitems:block/forge_white", "gold": "divinitems:block/forge_gold",
                 "rune": "divinitems:block/forge_rune", "particle": "divinitems:block/forge_white"},
    "elements": elements,
    "display": {
        "gui": {"rotation": [30, 225, 0], "scale": [0.62, 0.62, 0.62]},
        "ground": {"scale": [0.25, 0.25, 0.25], "translation": [0, 3, 0]},
        "fixed": {"scale": [0.5, 0.5, 0.5]},
        "thirdperson_righthand": {"rotation": [75, 45, 0], "scale": [0.375, 0.375, 0.375], "translation": [0, 2.5, 0]},
        "firstperson_righthand": {"rotation": [0, 45, 0], "scale": [0.4, 0.4, 0.4]},
    },
})
wjson(os.path.join(MI, "celestial_forge.json"), {"parent": "divinitems:block/celestial_forge"})
wjson(os.path.join(ASSETS, "blockstates", "celestial_forge.json"), {"variants": {"": {"model": "divinitems:block/celestial_forge"}}})

# ---------- lang ----------
fr = {
    "itemGroup.divinitems": "Items Divins",
    "item.divinitems.divine_star": "Étoile divine",
    "item.divinitems.divine_sword": "Épée divine",
    "item.divinitems.divine_pickaxe": "Pioche divine",
    "item.divinitems.divine_axe": "Hache divine",
    "item.divinitems.divine_bow": "Arc divin",
    "item.divinitems.divine_helmet": "Casque divin",
    "item.divinitems.divine_chestplate": "Plastron divin",
    "item.divinitems.divine_leggings": "Jambières divines",
    "item.divinitems.divine_boots": "Bottes divines",
    "block.divinitems.celestial_forge": "Forge céleste",
    "tooltip.divinitems.level": "Niveau Divin",
    "tooltip.divinitems.star": "Cœur de tout équipement divin. Se dépense à la Forge céleste.",
    "tooltip.divinitems.forge": "Clic droit avec un équipement en diamant (ou un arc) + une Étoile divine.",
    "message.divinitems.marked": "✦ Cible marquée : %s ✦",
    "message.divinitems.need_star": "Il vous faut une Étoile divine dans l'inventaire.",
    "message.divinitems.forged": "✦ La forge céleste s'embrase ! ✦",
    "message.divinitems.forge_hint": "Tenez un équipement en diamant (ou un arc) et ayez une Étoile divine.",
    "ability.divinitems.celestial_edge": "Tranchant céleste",
    "ability.divinitems.celestial_edge.desc": "Chaque coup libère un croissant lumineux qui blesse les ennemis proches.",
    "ability.divinitems.divine_execution": "Exécution divine",
    "ability.divinitems.divine_execution.desc": "Les monstres sous 25 % de vie sont exécutés. Vous gagnez de l'absorption.",
    "ability.divinitems.last_judgment": "Jugement dernier",
    "ability.divinitems.last_judgment.desc": "Clic droit : marque la cible visée, puis la foudre céleste s'abat sur elle.",
    "ability.divinitems.deadly_constellation": "Constellation meurtrière",
    "ability.divinitems.deadly_constellation.desc": "Tir à pleine charge : des lignes d'étoiles relient et frappent les monstres proches.",
    "ability.divinitems.star_rain": "Pluie d'étoiles",
    "ability.divinitems.star_rain.desc": "Tir à pleine charge en étant accroupi : une pluie d'étoiles s'abat sur la zone visée.",
    "ability.divinitems.divine_vein": "Veine divine",
    "ability.divinitems.divine_vein.desc": "Mine tout le filon de minerai d'un coup (accroupi = minage normal).",
    "ability.divinitems.celestial_felling": "Abattage céleste",
    "ability.divinitems.celestial_felling.desc": "Abat l'arbre entier d'un coup (accroupi = coupe normale).",
    "ability.divinitems.night_vision": "Vision nocturne",
    "ability.divinitems.night_vision.desc": "Vous voyez comme en plein jour.",
    "ability.divinitems.authoritative_gaze": "Regard autoritaire",
    "ability.divinitems.authoritative_gaze.desc": "Le monstre que vous fixez est paralysé et révélé.",
    "ability.divinitems.celestial_presence": "Présence céleste",
    "ability.divinitems.celestial_presence.desc": "Régénération, résistance et bouclier doré permanents.",
    "ability.divinitems.celestial_stride": "Course céleste",
    "ability.divinitems.celestial_stride.desc": "Vitesse accrue en permanence.",
    "ability.divinitems.divine_step": "Pas divin",
    "ability.divinitems.divine_step.desc": "Sauts surpuissants et aucune dégât de chute.",
}
en = {
    "itemGroup.divinitems": "Divine Items",
    "item.divinitems.divine_star": "Divine Star",
    "item.divinitems.divine_sword": "Divine Sword",
    "item.divinitems.divine_pickaxe": "Divine Pickaxe",
    "item.divinitems.divine_axe": "Divine Axe",
    "item.divinitems.divine_bow": "Divine Bow",
    "item.divinitems.divine_helmet": "Divine Helmet",
    "item.divinitems.divine_chestplate": "Divine Chestplate",
    "item.divinitems.divine_leggings": "Divine Leggings",
    "item.divinitems.divine_boots": "Divine Boots",
    "block.divinitems.celestial_forge": "Celestial Forge",
    "tooltip.divinitems.level": "Divine Level",
    "tooltip.divinitems.star": "Heart of all divine gear. Spent at the Celestial Forge.",
    "tooltip.divinitems.forge": "Right-click with diamond gear (or a bow) + a Divine Star.",
    "message.divinitems.marked": "✦ Target marked: %s ✦",
    "message.divinitems.need_star": "You need a Divine Star in your inventory.",
    "message.divinitems.forged": "✦ The Celestial Forge ignites! ✦",
    "message.divinitems.forge_hint": "Hold diamond gear (or a bow) and carry a Divine Star.",
    "ability.divinitems.celestial_edge": "Celestial Edge",
    "ability.divinitems.celestial_edge.desc": "Every hit releases a luminous crescent that hurts nearby enemies.",
    "ability.divinitems.divine_execution": "Divine Execution",
    "ability.divinitems.divine_execution.desc": "Monsters below 25% health are executed. You gain absorption.",
    "ability.divinitems.last_judgment": "Last Judgment",
    "ability.divinitems.last_judgment.desc": "Right-click: marks your target, then celestial lightning strikes it.",
    "ability.divinitems.deadly_constellation": "Deadly Constellation",
    "ability.divinitems.deadly_constellation.desc": "Full-charge shot: star lines link and strike nearby monsters.",
    "ability.divinitems.star_rain": "Star Rain",
    "ability.divinitems.star_rain.desc": "Full-charge shot while sneaking: a rain of stars falls on the target area.",
    "ability.divinitems.divine_vein": "Divine Vein",
    "ability.divinitems.divine_vein.desc": "Mines the whole ore vein at once (sneak = normal mining).",
    "ability.divinitems.celestial_felling": "Celestial Felling",
    "ability.divinitems.celestial_felling.desc": "Fells the whole tree at once (sneak = normal chopping).",
    "ability.divinitems.night_vision": "Night Vision",
    "ability.divinitems.night_vision.desc": "See as if in broad daylight.",
    "ability.divinitems.authoritative_gaze": "Authoritative Gaze",
    "ability.divinitems.authoritative_gaze.desc": "The monster you stare at is paralyzed and revealed.",
    "ability.divinitems.celestial_presence": "Celestial Presence",
    "ability.divinitems.celestial_presence.desc": "Permanent regeneration, resistance and golden shield.",
    "ability.divinitems.celestial_stride": "Celestial Stride",
    "ability.divinitems.celestial_stride.desc": "Permanent speed boost.",
    "ability.divinitems.divine_step": "Divine Step",
    "ability.divinitems.divine_step.desc": "Mighty jumps and no fall damage.",
}
wjson(os.path.join(ASSETS, "lang", "fr_fr.json"), fr)
wjson(os.path.join(ASSETS, "lang", "en_us.json"), en)

# ---------- données : recettes, loot table, tags ----------
def item(i): return {"item": i}
wjson(os.path.join(DATA, "recipe", "celestial_forge.json"), {
    "type": "minecraft:crafting_shaped", "category": "misc",
    "pattern": ["DDD", "III"],
    "key": {"D": item("minecraft:diamond"), "I": item("minecraft:iron_ingot")},   # comme dans la vidéo : 3 diamants / 3 fer
    "result": {"id": "divinitems:celestial_forge", "count": 1},
})
wjson(os.path.join(DATA, "recipe", "divine_star.json"), {
    "type": "minecraft:crafting_shaped", "category": "misc",
    "pattern": ["GDG", "DED", "GDG"],
    "key": {"G": item("minecraft:gold_ingot"), "D": item("minecraft:diamond"), "E": item("minecraft:emerald")},
    "result": {"id": "divinitems:divine_star", "count": 1},
})
wjson(os.path.join(DATA, "loot_table", "blocks", "celestial_forge.json"), {
    "type": "minecraft:block",
    "pools": [{"rolls": 1.0, "bonus_rolls": 0.0,
               "entries": [{"type": "minecraft:item", "name": "divinitems:celestial_forge"}],
               "conditions": [{"condition": "minecraft:survives_explosion"}]}],
    "random_sequence": "divinitems:blocks/celestial_forge",
})
wjson(os.path.join(ROOT, "data", "minecraft", "tags", "block", "mineable", "pickaxe.json"),
      {"replace": False, "values": ["divinitems:celestial_forge"]})
print("assets OK")
