package kim.biryeong.manager.gametest.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import net.minecraft.gametest.framework.JUnitLikeTestReporter;
import org.jetbrains.annotations.NotNull;

public class SavingXmlReportingTestCompletionListener extends JUnitLikeTestReporter {
    public SavingXmlReportingTestCompletionListener(File destination) throws ParserConfigurationException {
        super(destination);
    }

    @Override
    public void save(@NotNull File destination) throws TransformerException {
        try {
            Files.createDirectories(destination.toPath().getParent());
        } catch (IOException e) {
            throw new TransformerConfigurationException("Failed to create parent directory", e);
        }

        super.save(destination);
    }
}
