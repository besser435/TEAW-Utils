# TEAW-Utils
NeoForge mod for the [ToEndAllWars](https://toendallwars.org/) Minecraft server.

This mod does the following:
* Limits what blocks can be assembled on contraptions.
  * Can limit the max number of blocks individually
  * Ban blocks altogether
  * Ban entire namespaces
  

* Adds the following crafting ingredients for use in a datapack (items with no recipe or standalone use) 
  * Nether Star Frame
  * Uncompressed Nether Star


# TODO:
* We have recipe for NS Frame in the mod, but no NS recipe. Should we put all recipes into the server side datapack, or put the
  NS recipe in the mod? If we need to change it later, we can just override it with a datapack on the server without
  updating the mod.


* Fix recipe for the Nether Star Frame. Its messy and doesn't use tags for the glass. e
Can we make it so we don't need to manually specify "item" like in 26.1? If we use datagen it won't matter though.
Should probably just use datagen.