#!/usr/bin/env python3
"""BMM Extras 1.2.0 tiers: recipes, models, lang, tags and textures for 22 new tiers.
Textures come from ~/mc-modding/BMM-Weapon_Textures/bmm_ramp_tiers.py (run that first into ramp_tiers/).
Crafted tiers clone Stibnite's 37 recipe shapes (D = base unit, B = storage block, or the base unit when the
material has no block)."""
import json, os, shutil
ROOT = os.path.abspath(os.path.join(os.path.dirname(__file__), '..', 'src', 'main', 'resources'))
REC = f'{ROOT}/data/bmm_extras/recipe'
TEX_SRC = os.path.expanduser('~/mc-modding/BMM-Weapon_Textures/ramp_tiers')

def I(s): return {'tag': s[1:]} if s.startswith('#') else {'item': s}
ES, GAIA, MA = 'eternal_starlight', 'gaiadimension', 'mysticalagriculture'
# name: (display, condition modid or None, recipe)
#   ('craft', D, B or None) | ('smith', template, base tier (bmm_extras) or 'bmm:<mat>', addition) | ('ma', previous base)
TIERS = {
 'amaramber': ('Amaramber', ES, ('craft', '#c:ingots/amaramber', None)),
 'deepsilver': ('Deepsilver', ES, ('craft', '#c:ingots/deepsilver', f'{ES}:deepsilver_block')),
 'malarite': ('Malarite', ES, ('craft', '#c:gems/malarite', f'{ES}:malarite_block')),
 'thermal_springstone': ('Thermal Springstone', ES, ('craft', '#c:ingots/thermal_springstone', None)),
 'glacite': ('Glacite', ES, ('craft', '#c:gems/glacite', f'{ES}:glacite_block')),
 'flowglaze': ('Flowglaze', ES, ('smith', f'{ES}:flowglaze_upgrade_smithing_template', 'glacite', f'{ES}:flowglaze')),
 'starfire': ('Starfire', ES, ('smith', f'{ES}:starfire_upgrade_smithing_template', 'thermal_springstone', f'{ES}:starfire')),
 'unrealium': ('Unrealium', ES, ('craft', '#c:ingots/unrealium', f'{ES}:unrealium_block')),
 'starlit_diamond': ('Starlit Diamond', ES, ('craft', '#c:gems/starlit_diamond', f'{ES}:starlit_diamond_block')),
 'benitoite': ('Benitoite', GAIA, ('craft', f'{GAIA}:benitoite', f'{GAIA}:benitoite_block')),
 'sugilite': ('Sugilite', GAIA, ('craft', f'{GAIA}:sugilite', f'{GAIA}:sugilite_block')),
 'albite': ('Albite', GAIA, ('craft', f'{GAIA}:albite', f'{GAIA}:albite_block')),
 'diopside': ('Diopside', GAIA, ('craft', f'{GAIA}:diopside', f'{GAIA}:diopside_block')),
 'proustite': ('Proustite', GAIA, ('craft', f'{GAIA}:proustite', f'{GAIA}:proustite_block')),
 'emerald': ('Emerald', None, ('craft', 'minecraft:emerald', 'minecraft:emerald_block')),
 'lapis': ('Lapis', None, ('craft', 'minecraft:lapis_lazuli', 'minecraft:lapis_block')),
 'inferium': ('Inferium', MA, ('ma', 'bmm:diamond')),
 'prudentium': ('Prudentium', MA, ('ma', 'inferium')),
 'tertium': ('Tertium', MA, ('ma', 'prudentium')),
 'imperium': ('Imperium', MA, ('ma', 'tertium')),
 'supremium': ('Supremium', MA, ('ma', 'imperium')),
 'awakened_supremium': ('Awakened Supremium', MA, ('ma', 'supremium')),
}

def base_item(base, w):
    return f'bromax_melees:{base[4:]}_{w}' if base.startswith('bmm:') else f'bmm_extras:{base}_{w}'

def main():
    weapons = sorted(f[len('stibnite_'):-5] for f in os.listdir(REC) if f.startswith('stibnite_'))
    assert len(weapons) == 37, weapons
    lang_path = f'{ROOT}/assets/bmm_extras/lang/en_us.json'
    lang = json.load(open(lang_path))
    wname = {w: lang[f'item.bmm_extras.stibnite_{w}'][len('Stibnite '):] for w in weapons}
    tags = [f'{ROOT}/data/bmm_extras/tags/item/weapons.json'] + [f'{ROOT}/data/minecraft/tags/item/enchantable/{t}.json' for t in ('sword', 'weapon', 'sharp_weapon')]
    tagdata = {p: json.load(open(p)) for p in tags}
    for name, (disp, mod, r) in TIERS.items():
        conds = [{'type': 'neoforge:mod_loaded', 'modid': mod}] if mod else []
        if r[0] == 'ma': conds.append({'type': 'neoforge:mod_loaded', 'modid': 'cucumber'})
        for w in weapons:
            out = f'bmm_extras:{name}_{w}'
            if r[0] == 'craft':
                rec = json.load(open(f'{REC}/stibnite_{w}.json'))
                rec.pop('neoforge:conditions', None)
                key = {}
                for k, v in rec['key'].items():
                    it = v.get('item')
                    key[k] = I(r[1]) if it == f'{GAIA}:stibnite' else I(r[2] or r[1]) if it == f'{GAIA}:stibnite_block' else v
                rec['key'] = key
                rec['result'] = {'id': out, 'count': 1}
            elif r[0] == 'smith':
                rec = {'type': 'minecraft:smithing_transform', 'template': I(r[1]), 'base': I(base_item(r[2], w)),
                       'addition': I(r[3]), 'result': {'id': out, 'count': 1}}
            else:
                rec = {'type': 'cucumber:shaped_transfer_damage', 'transfer_components': True,
                       'pattern': [' G ', 'BWB', ' G '],
                       'key': {'G': I(f'{MA}:{name}_gemstone_block'), 'B': I(f'{MA}:{name}_ingot_block'),
                               'W': I(base_item(r[1], w))}, 'result': {'id': out, 'count': 1}}
            rec = ({'neoforge:conditions': conds} if conds else {}) | rec
            with open(f'{REC}/{name}_{w}.json', 'w') as f: json.dump(rec, f, indent=2); f.write('\n')
            m = {'parent': 'bromax_melees:item/bmm_handheld', 'textures': {'layer0': f'bmm_extras:item/{name}_{w}'}}
            with open(f'{ROOT}/assets/bmm_extras/models/item/{name}_{w}.json', 'w') as f: json.dump(m, f, indent=2); f.write('\n')
            shutil.copy(f'{TEX_SRC}/{name}_{w}.png', f'{ROOT}/assets/bmm_extras/textures/item/{name}_{w}.png')
            lang[f'item.bmm_extras.{name}_{w}'] = f'{disp} {wname[w]}'
            for p, d in tagdata.items():
                if any(v.get('id', '').startswith('bmm_extras:stibnite_') for v in d['values'] if isinstance(v, dict)):
                    if not any(isinstance(v, dict) and v.get('id') == out for v in d['values']):
                        d['values'].append({'id': out, 'required': False})
    for p, d in tagdata.items():
        with open(p, 'w') as f: json.dump(d, f, indent=2); f.write('\n')
    with open(lang_path, 'w') as f: json.dump(lang, f, indent=2, ensure_ascii=False); f.write('\n')
    print(len(TIERS), 'tiers x', len(weapons))

if __name__ == '__main__': main()
