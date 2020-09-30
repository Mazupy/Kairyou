# Kairyō client

## Setup

### Prerequisites
* [Fabric Loader](https://fabricmc.net/use/)
* [Fabric API](https://www.curseforge.com/minecraft/mc-mods/fabric-api)
* [Git](https://git-scm.com/downloads)
* [Gradle](https://gradle.org/releases/)

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
In the directory `build/libs/` you will find `kairyou-#.#.#.jar`, _ignore any `-sources.jar` or `-dev.jar`_.  
Install the fabric loader and put the `fabric-api-[...].jar` and the `kairyou-#.#.#.jar` into the mods folder.

## **Plea**
> When using this mod on a server, make sure any modules you use are compliant with the rules.
