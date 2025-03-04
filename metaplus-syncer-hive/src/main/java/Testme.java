import org.apache.commons.io.input.Tailer;
import org.apache.commons.io.input.TailerListenerAdapter;

import java.io.IOException;
import java.nio.file.*;

public class Testme {

    public static void main(String[] args) throws IOException {
        System.out.println("testme");

        Tailer.builder().setFile("hahah").setTailerListener(new TailerListenerAdapter() {
            public void handle(String line) {
                System.out.println("line: " + line);
            }
        }).get();

        try (WatchService watchService = FileSystems.getDefault().newWatchService()) {
            Path path = Paths.get("/tmp/testtail");
            path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.OVERFLOW);
            while (true) {
                WatchKey key = watchService.take();
                for (WatchEvent<?> watchEvent : key.pollEvents()) {
                    if (watchEvent.kind() == StandardWatchEventKinds.ENTRY_CREATE) {
                        System.out.println("create..." + System.currentTimeMillis());
                    } else if (watchEvent.kind() == StandardWatchEventKinds.ENTRY_MODIFY) {
                        System.out.println("modify..." + System.currentTimeMillis());
                    } else if (watchEvent.kind() == StandardWatchEventKinds.ENTRY_DELETE) {
                        System.out.println("delete..." + System.currentTimeMillis());
                    } else if (watchEvent.kind() == StandardWatchEventKinds.OVERFLOW) {
                        System.out.println("overflow..." + System.currentTimeMillis());
                    }
                }
                if (!key.reset()) {
                    System.out.println("reset false");
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
