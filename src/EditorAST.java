import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by willl on 15/11/2015.
 */
public class EditorAST extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {

        final Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);
        final Project project = e.getRequiredData(CommonDataKeys.PROJECT);
        //Access document, caret, and selection
        final Document document = editor.getDocument();
        System.out.println();
        try {
            String[] parts = document.toString().split("file://");
            String path = parts[1].split("]")[0];
            ProcessBuilder pb = new ProcessBuilder("C:\\Program Files\\nodejs\\node.exe",
                    "C:\\Users\\willl\\Cours\\OPL\\jsTool\\jsTool\\jsTool.js",
                    path);
            pb.redirectErrorStream(true);
            Process p = pb.start();
            BufferedReader reader=new BufferedReader(
                    new InputStreamReader(p.getInputStream())
            );
            String line;
            while((line = reader.readLine()) != null) {

                    String[] data = line.split(" ");
                    int lineIndex = document.getLineStartOffset(Integer.parseInt(data[0])-1);
                    int end = lineIndex + Integer.parseInt(data[1]);
                    int start = lineIndex + Integer.parseInt(data[2]);
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        document.replaceString( end, start, " === ");
                    }
                };
                WriteCommandAction.runWriteCommandAction(project, runnable);
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    @Override
    public void update(AnActionEvent e) {

        final Project project = e.getData(CommonDataKeys.PROJECT);
        final Editor editor = e.getData(CommonDataKeys.EDITOR);

        e.getPresentation().setVisible((project != null && editor != null && editor.getDocument().toString().contains(".js")));




    }
}
