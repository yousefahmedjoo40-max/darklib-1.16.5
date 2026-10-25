# Darklib (Forge 1.16.5)

Library mod that makes the **night terrifyingly dark** (heavy black fog, short view distance) while the **day stays vanilla**.
Open sky matters: caves/houses aren't affected, Night Vision reduces the effect, water/lava fog untouched.

## Config
`config/darklib-client.toml`
- `enabled`
- `nightFogDensity` (default 0.09)
- `nightVisionMultiplier` (default 0.15)

## Use as a dependency (in your mod)
`mods.toml`:
```toml
[[dependencies.yourmodid]]
    modId="darklib"
    mandatory=true
    versionRange="[1.0.0,)"
    ordering="AFTER"
    side="BOTH"
```
Code (1.1.0 adds a hook for lamps: `DarklibAPI.registerDarknessReducer(Supplier<Float>)`):
```java
float night = DarklibAPI.getNightFactor(world); // 0 day -> 1 night
```
Gradle (CurseMaven): `compileOnly fg.deobf("curse.maven:darklib-PROJECTID:FILEID")`

## Build
```
gradle build        # or ./gradlew build after: gradle wrapper --gradle-version 7.6.4
```
Jar: `build/libs/darklib-1.16.5-1.0.0.jar`  (JDK 8 required)

## Publish
1. Create the project on CurseForge, copy its Project ID into `.github/workflows/release.yml`
2. Add repo secret `CURSEFORGE_TOKEN` (CurseForge API token)
3. `git tag v1.0.0 && git push origin v1.0.0`
