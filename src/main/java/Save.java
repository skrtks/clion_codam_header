import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileEvent;
import com.intellij.openapi.vfs.VirtualFileListener;
import com.intellij.openapi.vfs.VirtualFileManager;
import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class Save implements ApplicationComponent {
    public Save() {
    }

    @Override
    public void initComponent() {
        VirtualFileManager.getInstance().addVirtualFileListener(new VirtualFileListener() {
            public void contentsChanged(@NotNull VirtualFileEvent event) {
                VirtualFile file = event.getFile();
                String filename = file.getName();
                String extension = file.getExtension();
                String start = Objects.requireNonNull(FileDocumentManager.getInstance().getDocument(file)).getText(new TextRange(0, 5));
                if (!start.equals("/* **") && !start.equals("# ***"))
                    return;
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date date = new Date();
                while (filename.length() < 51)
                    filename += ' ';
                String user = "by " + System.getenv("USER");
                while (user.length() < 17)
                    user += ' ';
                String header;
                if (filename.contains("Makefile"))
                    header = "#    Updated: " + dateFormat.format(date) + " " + user + "########   odam.nl          #";
                else
                    header = "/*   Updated: " + dateFormat.format(date) + " " + user + "########   odam.nl         */";

                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        Objects.requireNonNull(FileDocumentManager.getInstance().getDocument(file)).replaceString(648, 648 + header.length(), header);
                    }
                };
                WriteCommandAction.runWriteCommandAction(ProjectManager.getInstance().getOpenProjects()[0], runnable);
            }
        });
    }

    @Override
    public void disposeComponent() {
    }

    @Override
    @NotNull
    public String getComponentName() {
        return "Save";
    }
}