<!--suppress HtmlDeprecatedTag, XmlDeprecatedElement -->

<center>
<img alt="a regular screenshot of a minecraft forest - what did you expect?" src="https://cdn.modrinth.com/data/XH8cnjHW/images/8d675995eb6af57465e360a1720063d004389b79.png"/>
Ambient sounds to fit vanilla.<br/>
A backfork of <a href="https://modrinth.com/mod/charmonium">Charmonium</a>.<br/>
Requires <a href="https://modrinth.com/mod/connector">Connector</a> and <a href="https://modrinth.com/mod/forgified-fabric-api">FFAPI</a> on (neo)forge.<br/>
</center>

## Features

Charmonium (and therefore Euphonium) adds biome, structure, and environment-specific ambient sound to the game.

By default, volume can be tweaked through the "Ambient Sounds" slider.

### Configuration

Euphonium can be configured via `config/euphonium.toml`, including:
- Sound channel
- Volume multiplier for world ambience
- Toggles for each type of ambience
- Tweaks for cave darkness ambience

## Design

This is a fork and backport of Charmonium 6.0 - an excellent rewrite of the mod by svenhjol!

Charmonium 6.0 can't be reliably built from source. It's API, Charmony, is also not source-visible.<br/>
To remedy this, Euphonium has all new buildscripts and forgoes Charmony, using [Kaleido](//github.com/sisby-folk/kaleido-config) for a toml config.

## Afterword

All mods are built on the work of many others.

This mod is included in [Tinkerer's Quilt](https://modrinth.com/modpack/tinkerers-quilt) - our modpack about rediscovering vanilla.

We're open to better ways to implement our mods. If you see something odd and have an idea, let us know! 
