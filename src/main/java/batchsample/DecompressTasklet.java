package batchsample;

import lombok.Data;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.core.io.Resource;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.zip.ZipInputStream;

/**
 * DecompressTasklet class decompresses a ZIP archive into its source flat file
 */
@Data
public class DecompressTasklet  implements Tasklet {
    // Tasklet parameters
    private Resource inputResource;
    private String targetDirectory;
    private String targetFile;

    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        // Opens archive
        ZipInputStream zis = new ZipInputStream(new BufferedInputStream(inputResource.getInputStream()));

        // Creates target directory if absent
        File targetDirectoryAsFile = new File(targetDirectory);
        if(!targetDirectoryAsFile.exists()) {
            FileUtils.forceMkdir(targetDirectoryAsFile);
        }
        File target = new File(targetDirectory,targetFile);
        BufferedOutputStream dest;
        // Decompresses archive
        while(zis.getNextEntry() != null) {
            if(!target.exists()) {
                target.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(target);
            dest = new BufferedOutputStream(fos);
            IOUtils.copy(zis,dest);
            dest.flush();
            dest.close();
        }
        zis.close();
        if(!target.exists()) {
            throw new IllegalStateException("Could not decompress anything from the archive!");
        }
        // Notify Spring Batch that the tasklet finished
        return RepeatStatus.FINISHED;
    }
}
