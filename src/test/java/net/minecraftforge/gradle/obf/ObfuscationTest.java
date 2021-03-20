package net.minecraftforge.gradle.obf;

import net.minecraftforge.gradle.common.BasePlugin;
import net.minecraftforge.gradle.common.Constants;
import net.minecraftforge.gradle.delayed.DelayedFile;
import net.minecraftforge.gradle.tasks.user.SingleDeobfTask;
import org.apache.commons.io.FileUtils;
import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class ObfuscationTest {
    private static final File EXECUTION_ROOT = new File("build/tmp/testing/obf");
    private static final File TEST_ROOT = new File("src/test/resources/net/minecraftforge/gradle/obf");
    private static final File ACTUAL_CLEAN = new File(TEST_ROOT, "ActualClean.jar");
    private static final File ACTUAL_OBF = new File(TEST_ROOT, "ActualObf.jar");
    private static final File DEP_CLEAN = new File(TEST_ROOT, "DepClean.jar");
    private static final File SRG = new File(TEST_ROOT, "obfuscate.srg");

    private Project makeProject() throws IOException {
        FileUtils.copyDirectory(new File("src/test/resources/"), new File("build/tmp/testing/obf/src/test/resources"));
        return ProjectBuilder.builder().withProjectDir(EXECUTION_ROOT).build();
    }

    @Test
    public void singleArtifactTest() throws IOException {
        String expectedHash = Constants.hash(ACTUAL_OBF);
        File output = new File(EXECUTION_ROOT, "output.jar");

        Project proj = makeProject();
        SingleDeobfTask task = BasePlugin.makeTask(proj, "deobfOne", SingleDeobfTask.class);
        task.setInJar(new DelayedFile(ACTUAL_CLEAN));
        task.setSrg(new DelayedFile(SRG));
        task.addClasspath(DEP_CLEAN);
        task.setOutJar(new DelayedFile(output));
        task.setDoesCache(false);

        task.doTask();

        String actualHash = Constants.hash(output);
        Assert.assertEquals(expectedHash, actualHash);
    }
}
