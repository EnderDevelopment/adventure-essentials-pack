#!/usr/bin/env python3
"""
Generate placeholder PNG textures for Ender Essentials.

Creates a set of simple, solid-colour placeholder PNGs (with a subtle
border and a diagonal highlight) for every texture referenced by the mod,
so nothing shows the missing-texture (black/purple checkerboard).

Run from the project root:

    python tools/generate_placeholder_textures.py

The script writes into src/main/resources/assets/ender_essentials/textures/.

Colours are per-asset arm, so each item/mob/gui placeholder is visually
distinct. Replace these with your real textures whenever you like — keep
the same file names and paths.
"""

import struct
import zlib
from pathlib import Path

PROJECT_ROOT = Path(__file__).resolve().parent.parent
TEXTURE_ROOT = (
    PROJECT_ROOT
    / "src"
    / "main"
    / "resources"
    / "assets"
    / "ender_essentials"
    / "textures"
)

# name -> (width, height, base_colour_rgb)
ASSETS = {
    # ---- Item icons (16x16) ----
    "item/dragon_greatsword": (16, 16, (0x9C, 0x27, 0xB0)),
    "item/titan_cleaver": (16, 16, (0x42, 0xA5, 0xF5)),
    "item/obsidian_warhammer": (16, 16, (0x37, 0x38, 0x3B)),
    "item/mythril_longsword": (16, 16, (0x66, 0xBB, 0xEE)),
    # ---- Mob skins (64x64 wolf rig) ----
    "entity/warrior_beast": (64, 64, (0xB7, 0x1C, 0x1C)),
    # ---- GUI (176x166) ----
    "gui/waypoint_screen": (176, 166, (0x1E, 0x2A, 0x38)),
}

# A small diagonal stripe pattern we overlay so placeholders are obviously
# placeholders yet still readable at a glance.
STRIPE = [
    (0, 0, 255), (0, 0, 255), (0, 0, 255), (0, 0, 0), (0, 0, 0),
    (0, 0, 255), (0, 0, 255), (0, 0, 255), (0, 0, 0), (0, 0, 0),
]


def lerp(a, b, t):
    return tuple(int(a[i] + (b[i] - a[i]) * t) for i in range(3))


def blend(base, overlay, alpha):
    return tuple(int(base[i] * (1 - alpha) + overlay[i] * alpha) for i in range(3))


def pixel_colour(base, x, y, width, height):
    u = x / max(1, width - 1)
    v = y / max(1, height - 1)

    # Subtle vertical gradient (darker bottom).
    colour = lerp(base, tuple(c * 3 // 4 for c in base), v)

    # Diagonal stripes, drawn with a soft alpha.
    stripe_idx = (x + y) % len(STRIPE)
    stripe = STRIPE[stripe_idx]
    if stripe != (0, 0, 0):
        alpha = 0.10
        if stripe == (0, 0, 255):
            # White highlight stripe
            colour = blend(colour, (255, 255, 255), alpha)

    # Border outline.
    border = 1
    if x < border or y < border or x >= width - border or y >= height - border:
        colour = blend(colour, (0, 0, 0), 0.55)

    return colour


def write_png(path, width, height, base):
    """Write an RGBA PNG manually (no external dependencies)."""
    rows = []
    for y in range(height):
        row = bytearray([0])  # filter type 0
        for x in range(width):
            r, g, b = pixel_colour(base, x, y, width, height)
            row += bytes((r, g, b, 255))
        rows.append(bytes(row))

    raw = b"".join(rows)

    def chunk(tag, data):
        c = struct.pack(">I", len(data)) + tag + data
        c += struct.pack(">I", zlib.crc32(tag + data) & 0xFFFFFFFF)
        return c

    ihdr = struct.pack(">IIBBBBB", width, height, 8, 6, 0, 0, 0)
    png = b"\x89PNG\r\n\x1a\n"
    png += chunk(b"IHDR", ihdr)
    png += chunk(b"IDAT", zlib.compress(raw, 9))
    png += chunk(b"IEND", b"")
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_bytes(png)


def main():
    created = 0
    for rel_name, (width, height, base) in ASSETS.items():
        path = TEXTURE_ROOT / (rel_name + ".png")
        if path.exists():
            print(f"[skip] {rel_name} already exists")
            continue
        write_png(path, width, height, base)
        print(f"[ ok ] wrote {path.relative_to(PROJECT_ROOT)} ({width}x{height})")
        created += 1

    print(f"\nDone. Created {created} placeholder texture(s).")
    if created == 0:
        print("No new files were needed — everything is already in place.")


if __name__ == "__main__":
    main()
