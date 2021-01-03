# Kairyō client
For Minecraft 1.16.4

### Progress
* Client: _3/12_
* Modules: _5/121+_
* Fixes: _218/∞_

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

## Thank you
[FabricMC](https://github.com/FabricMC) for [Fabric](https://github.com/FabricMC/fabric-loader), 
the [Fabric-API](https://github.com/FabricMC/fabric), 
[Yarn](https://github.com/FabricMC/yarn) and 
[Loom](https://github.com/FabricMC/fabric-loom)  
[SpongePowered](https://github.com/SpongePowered) for [Mixin](https://github.com/SpongePowered/Mixin)  
[ZeroMemes](https://github.com/ZeroMemes) for [Alpine](https://github.com/ZeroMemes/Alpine)  
[Gradle](https://gradle.org) for [Gradle](https://github.com/gradle/gradle)  
[John Engelman](https://github.com/johnrengelman) for [Shadow](https://github.com/johnrengelman/shadow)  
The [Wikimedia Foundation](https://wikimediafoundation.org) and all the Wikipedia editors for [Wikipedia](https://www.wikipedia.org), 
giving me more information than I could ever process  
The [Meteor Development](https://github.com/MeteorDevelopment) Team for the [Meteor Client](https://github.com/MeteorDevelopment/meteor-client) and 
[Ridan Vandenbergh](https://github.com/zeroeightysix) for [KAMI](https://github.com/zeroeightysix/KAMI), inspiring me to make my own client  
**All of the nice people on the internet providing solutions and suggestions to various problems.**  
