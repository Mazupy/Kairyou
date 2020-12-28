# Kairyō client
For Minecraft 1.16.4

### Progress
* Client: _2/10_
* Modules: _4/121+_
* Fixes: _96/∞_

Expected the client to reach a mature state till September 2021

## Setup

### Prerequisites
* [Git](https://git-scm.com/downloads)
* [Gradle](https://gradle.org/releases/)
* [Fabric Loader](https://fabricmc.net/use/)
* [Fabric API](https://www.curseforge.com/minecraft/mc-mods/fabric-api)

### Building
Start Git bash where you want to try it:
```bash
# Clone this repository (creates a kairyou directory containing everything)
git clone https://github.com/Mazupy/kairyou.git
# Go into the repository
cd kairyou
# Build the jar (Windows)
gradle build
# Build the jar (Linux)
./gradle build
```
### Installing
In the directory `build/libs/` you will find `kairyou-#.#.#-alpha.jar`, _ignore any `-all.jar`, `-sources.jar` or `-dev.jar`_.  
Install the fabric loader and put the `fabric-api-[...].jar` and the `kairyou-#.#.#.jar` into the mods folder.

## **Plea**
> When using this mod on a server, make sure any modules you use are compliant with the rules.
