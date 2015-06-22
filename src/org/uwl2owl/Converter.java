package org.uwl2owl;

import java.awt.BorderLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;
import java.net.URLConnection;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileFilter;

import org.argouml.configuration.Configuration;
import org.argouml.i18n.Translator;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.persistence.AbstractFilePersister;
import org.argouml.persistence.PersistenceManager;
import org.argouml.persistence.ProjectFileView;
import org.argouml.ui.ProjectBrowser;
import org.argouml.util.ArgoFrame;

@SuppressWarnings("javadoc")
public class Converter {
    private final String CrLf = "\r\n";

    public void httpConn(File tmpfile) {
        URLConnection conn = null;
        OutputStream os = null;
        InputStream is = null;

        try {
            URL url = new URL("http://www.baptisteautin.com/projets-personnels/php_uml/convertisseur-de-fichier-xmi/lang/en/");
            System.out.println("url:" + url);
            conn = url.openConnection();
            conn.setDoOutput(true);
            
            System.out.println("###!@#!@#!@#!@#!@#!@#!@#!@#!@#");
            System.out.println(tmpfile.getName()+"*************");
//            InputStream umlFile = getClass().getResourceAsStream("/"+tmpfile);
            
            InputStream umlFile = new FileInputStream(new File(tmpfile.getAbsolutePath()));
            
//            InputStream umlFile = getClass().getResourceAsStream("/org.xmi");
            
            
            byte[] imgData = new byte[umlFile.available()];
            umlFile.read(imgData);

            String message1 = "";
            message1 += "-----------------------------4664151417711" + CrLf;
            message1 += "Content-Disposition: form-data; name=\"xmi_file\"; filename=\"org.xmi\""
                    + CrLf;
            message1 += "Content-Type: file/xmi" + CrLf;
            message1 += CrLf;

            // the image is sent between the messages in the multipart message.

            String message2 = "";
            message2 += CrLf + "-----------------------------4664151417711--"
                    + CrLf;

            conn.setRequestProperty("Content-Type",
                    "multipart/form-data; boundary=---------------------------4664151417711");
            // might not need to specify the content-length when sending chunked
            // data.
            conn.setRequestProperty("Content-Length", String.valueOf((message1
                    .length() + message2.length() + imgData.length)));

            System.out.println("open os");
            os = conn.getOutputStream();

            System.out.println(message1);
            os.write(message1.getBytes());

            // SEND THE UMLFILE
            int index = 0;
            int size = 1024;
            do {
                System.out.println("write:" + index);
                if ((index + size) > imgData.length) {
                    size = imgData.length - index;
                }
                os.write(imgData, index, size);
                index += size;
            } while (index < imgData.length);
            System.out.println("written:" + index);

            System.out.println(message2);
            os.write(message2.getBytes());
            os.flush();

            System.out.println("open is");
            is = conn.getInputStream();

            File file = new File("result2.xmi");
            if(!file.exists()) {
            	file.createNewFile();
            }
            
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            
            String temp = "";
            char buff = 1024;
            int len;
            byte[] data = new byte[buff];
            do {
//                System.out.println("READ");
                len = is.read(data);

                if (len > 0) {
                    System.out.println(new String(data, 0, len));
                    temp += new String(data, 0, len);
                }
            } while (len > 0);
            
            bw.write(temp);
            bw.flush();
            bw.close();
            System.out.println("**********************************************************");
            System.out.println("DONE");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("Close connection");
            try {
                os.close();
            } catch (Exception e) {
            }
            try {
                is.close();
            } catch (Exception e) {
            }
            try {

            } catch (Exception e) {
            }
        }
        
        try
		{
			String path = "/Users/alex/Documents/ArgoUMLv2.0/ArgoUMLwithU2Otool/src/";
			@SuppressWarnings("unused")
                        Process proc = Runtime.getRuntime().exec("java -jar "+path+"uml2owl.jar verbose=true IRI=http://www.tuwien.ac.at/out.owl converter=VisualParadigmConverterXMI2  input="+path+"result2.xmi output="+path+"result2.owl" 
			);
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public File createAndShowFileChoose() {
        
        File theFile = null;
        
        // Most of this code originates from ActionOpenProject.
        ProjectBrowser pb = ProjectBrowser.getInstance();
        Project p = ProjectManager.getManager().getCurrentProject();
//        PersistenceManager pm = PersistenceManager.getInstance();

        if (!ProjectBrowser.getInstance().askConfirmationAndSave()) {
            return null;
        }

        JFileChooser chooser = null;
        if (p != null && p.getURI() != null) {
            File file = new File(p.getURI());
            if (file.getParentFile() != null) {
                chooser = new JFileChooser(file.getParent());
            }
        } else {
            chooser = new JFileChooser();
        }

        if (chooser == null) {
            chooser = new JFileChooser();
        }

        chooser.setDialogTitle(
                Translator.localize("filechooser.import-xmi"));

        chooser.setFileView(ProjectFileView.getInstance());

        chooser.setAcceptAllFileFilterUsed(false);

//        pm.setXmiFileChooserFilter(chooser);

        String fn =
            Configuration.getString(
                PersistenceManager.KEY_IMPORT_XMI_PATH);
        if (fn.length() > 0) {
            chooser.setSelectedFile(new File(fn));
        }

        int retval = chooser.showOpenDialog(pb);
        if (retval == JFileChooser.APPROVE_OPTION) {
            theFile = chooser.getSelectedFile();

//            if (!theFile.canRead()) {
//                /* Try adding the extension from the chosen filter. */
//                FileFilter ffilter = chooser.getFileFilter();
//                if (ffilter instanceof AbstractFilePersister) {
//                    AbstractFilePersister afp =
//                        (AbstractFilePersister) ffilter;
//                    File m =
//                        new File(theFile.getPath() + "."
//                                + afp.getExtension());
//                    if (m.canRead()) {
//                        theFile = m;
//                    }
//                }
//            }
//            Configuration.setString(
//                    PersistenceManager.KEY_IMPORT_XMI_PATH,
//                    theFile.getPath());
//
//            ProjectBrowser.getInstance().loadProjectWithProgressMonitor(
//                    theFile, true);
        }
        return theFile;
    }
    
    JButton openButton, saveButton;
    JTextArea log;
    JFileChooser fc;
    JPanel fileChooserDemo = new JPanel();
    
    public void fileChooser() {
        log = new JTextArea(5,20);
        log.setMargin(new Insets(5,5,5,5));
        log.setEditable(false);
        JScrollPane logScrollPane = new JScrollPane(log);
        
        fc = new JFileChooser();
        openButton = new JButton("Open a File...");
        openButton.addActionListener(new ActionListener() {
            
            public void actionPerformed(ActionEvent e) {
                // TODO: Auto-generated method stub
                
            }
        });
        
//        saveButton = new JButton("Save a File...");
//        saveButton.addActionListener(this);
        
        JPanel buttonPanel = new JPanel(); //use FlowLayout
        buttonPanel.add(openButton);
        buttonPanel.add(saveButton);
        
        fileChooserDemo.add(buttonPanel, BorderLayout.PAGE_START);
        fileChooserDemo.add(logScrollPane, BorderLayout.CENTER);
    }
    
}