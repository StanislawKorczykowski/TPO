package zad1;


import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class Futil {
     public static void processDir(String dirName, String resultFileName) {
         Path dirPath = Paths.get(dirName);
         Path resultPath = Paths.get(resultFileName);
         Charset cp1250 = Charset.forName("Cp1250");
         try (FileChannel outChannel = FileChannel.open(resultPath, StandardOpenOption.CREATE, StandardOpenOption.WRITE,
                 StandardOpenOption.TRUNCATE_EXISTING)) {

             Files.walkFileTree(dirPath, new SimpleFileVisitor<Path>() {
                 @Override
                 public FileVisitResult visitFile( Path filePath, BasicFileAttributes attrs) throws IOException {
                     if (Files.isRegularFile(filePath)) {
                         try (FileChannel inChannel =  FileChannel.open(filePath)) {
                             ByteBuffer  buffer = ByteBuffer.allocate(1024);
                             while (inChannel.read(buffer) > 0) {
                                 buffer.flip();
                                 CharBuffer charBuffer = cp1250.decode(buffer);
                                 ByteBuffer outBuffer = StandardCharsets.UTF_8.encode(charBuffer);
                                 outChannel.write(outBuffer);
                                 buffer.clear();
                             }
                         }
                     }
                     return FileVisitResult.CONTINUE;
                 }
             });

        } catch (IOException e)  {
            e.printStackTrace();
        }
       
    }
     
}

