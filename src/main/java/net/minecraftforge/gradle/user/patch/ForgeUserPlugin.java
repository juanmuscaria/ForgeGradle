package net.minecraftforge.gradle.user.patch;

import static net.minecraftforge.gradle.user.patch.UserPatchConstants.FML_AT;
import static net.minecraftforge.gradle.user.patch.UserPatchConstants.FML_PATCHES_ZIP;
import static net.minecraftforge.gradle.user.patch.UserPatchConstants.FORGE_AT;
import static net.minecraftforge.gradle.user.patch.UserPatchConstants.FORGE_PATCHES_ZIP;
import static net.minecraftforge.gradle.user.patch.UserPatchConstants.RES_DIR;
import static net.minecraftforge.gradle.user.patch.UserPatchConstants.SRC_DIR;

import net.minecraftforge.gradle.common.Constants;
import net.minecraftforge.gradle.tasks.ProcessJarTask;
import net.minecraftforge.gradle.tasks.ProcessSrcJarTask;

public class ForgeUserPlugin extends UserPatchBasePlugin {

    @Override
    public String getApiName() {
        return "forge";
    }

    @Override
    protected String getApiGroup() {
        return "net.minecraftforge";
    }

    @Override
    protected void configureDeobfuscation(ProcessJarTask task) {
        task.addTransformerClean(this.delayedFile(FML_AT));
        task.addTransformerClean(this.delayedFile(FORGE_AT));
    }

    @Override
    protected void configurePatching(ProcessSrcJarTask patch) {
        patch.addStage("fml", this.delayedFile(FML_PATCHES_ZIP), this.delayedFile(SRC_DIR), this.delayedFile(RES_DIR));
        patch.addStage("forge", this.delayedFile(FORGE_PATCHES_ZIP));
    }

    @Override
    protected void doVersionChecks(String version, int buildNumber) {
        if (version.startsWith("10.")) {
            if (buildNumber < 1048)
                throw new IllegalArgumentException("ForgeGradle 1.2 only supports Forge 1.7 versions newer than 10.12.0.1048. Found: " + version);
        } else if (version.startsWith("11.")) {
            if (buildNumber > 1502)
                throw new IllegalArgumentException("ForgeGradle 1.2 only supports Forge 1.8 before 11.14.3.1503. Found: " + version);
        } else
            throw new IllegalArgumentException("ForgeGradle 1.2 does not support forge " + version);
    }

    @Override
    protected String getVersionsJsonUrl() {
        // TODO Auto-generated method stub
        return Constants.FORGE_MAVEN + "/net/minecraftforge/forge/json";
    }
}
