/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zparkingb.corrupt;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import net.miginfocom.layout.PlatformDefaults;
import net.miginfocom.swing.MigLayout;
import org.apache.log4j.Logger;

/**
 *
 * @author laurent
 */
public class Main extends javax.swing.JFrame {

    private final static Logger logger = Logger.getLogger(Main.class);
    private static final int START = 10;
    private static final String TITLE = "Corrupt !";
    public static final String PROPERTIES_FILENAME = "corrupt.properties";
    public static final String PROP_FILE = "file";
    public static final String PROP_EXPORT_FOLDER = "exportfolder";
    public static final String PROP_EXPECTED = "expected";
    public static final String PROP_MAX = "max";
    public static final String PROP_LEVEL = "level";

    private final Properties properties;
    protected final Path propFile;

    protected Path lastExportFolder = null;

    /** Creates new form Main */
    public Main() {
        super();
        System.setProperty("http.agent", "Chrome"); // necessary of the CrreativeCommon About image to load

        propFile = Paths.get(PROPERTIES_FILENAME);
        properties = new Properties();

        initComponents();

        // init of the properties
        if (Files.exists(propFile)) {
            try {
                properties.load(new FileInputStream(propFile.toFile()));
            } catch (IOException ex) {
                logger.warn("Fail to read property file " + propFile, ex);
            }
        }

        txtFile.setText(properties.getProperty(PROP_FILE, ""));
        txtExportFolder.setText(properties.getProperty(PROP_EXPORT_FOLDER, ""));
        txtExpected.setText(properties.getProperty(PROP_EXPECTED, "6"));
        txtMaxTrials.setText(properties.getProperty(PROP_MAX, "11"));

        try {
            Level l = Level.valueOf(properties.getProperty(PROP_LEVEL, Level.LOW.name()));
            lstLevel.setSelectedItem(l);
        } catch (Exception e) {
            lstLevel.setSelectedItem(Level.LOW);
        }

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                logger.debug("Closed");
            }

            public void windowClosing(java.awt.event.WindowEvent e) {
                Main.this.windowClosing(e);
            }
        });

    }

//    @SuppressWarnings("unchecked")
    private void initComponents() {

        panMain = new javax.swing.JPanel();
        panFields = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtFile = new javax.swing.JTextField();
        btnBrowseFile = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        txtExportFolder = new javax.swing.JTextField();
        btnBrowseFolder = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        txtExpected = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtMaxTrials = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        lstLevel = new javax.swing.JComboBox<>();
        panAction = new javax.swing.JPanel();
        btnOK = new javax.swing.JButton();
        btnOpenFolder = new javax.swing.JButton();
        btnAbout = new javax.swing.JButton();
        btnEsc = new javax.swing.JButton();
        labTrials = new javax.swing.JLabel();
        progTrials = new javax.swing.JProgressBar();
        labTrialsCount = new javax.swing.JLabel();
        labSuccess = new javax.swing.JLabel();
        progSuccess = new javax.swing.JProgressBar();
        labSuccessCount = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle(TITLE);
        setMinimumSize(new java.awt.Dimension(500, 300));
        setLocationRelativeTo(null);

        panMain.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panMain.setLayout(new java.awt.BorderLayout());

        panFields.setLayout(new MigLayout("fill", "[align left]10px[grow, align left]5px[]"));

        jLabel1.setText("File:");
        panFields.add(jLabel1);

        txtFile.setText("");
        panFields.add(txtFile, "growx");

        btnBrowseFile.setText("Browse...");
        btnBrowseFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBrowseFileActionPerformed(evt);
            }
        });
        panFields.add(btnBrowseFile);

        jLabel4.setText("Export folder:");
        panFields.add(jLabel4, "newline");

        txtExportFolder.setText("C:\\TEMP\\corrupt_test");
        panFields.add(txtExportFolder, "growx");

        btnBrowseFolder.setText("Browse...");
        btnBrowseFolder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBrowseFolderActionPerformed(evt);
            }
        });
        panFields.add(btnBrowseFolder);

        jLabel2.setText("Amount of corruptions:");
        panFields.add(jLabel2, "newline");

        txtExpected.setText("5");
        panFields.add(txtExpected, "span 2");

        jLabel3.setText("Max trials:");
        panFields.add(jLabel3, "newline");

        txtMaxTrials.setText("10");
        panFields.add(txtMaxTrials, "span 2");

        jLabel5.setText("Level:");
        panFields.add(jLabel5, "newline");

        lstLevel.setModel(new DefaultComboBoxModel(Level.values()));
        panFields.add(lstLevel, "span 2");

        panMain.add(panFields, java.awt.BorderLayout.CENTER);

        panAction.setLayout(new MigLayout("fill", "[align left]5px[fill, grow 100, align center]5px[align right]", "[]15px[][]"));

        btnOpenFolder.setText("Open folder");
        btnOpenFolder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOpenFolderActionPerformed(evt);
            }
        });
        btnOpenFolder.setEnabled(false);
        panAction.add(btnOpenFolder, "spanx 3, split 5, shrink, align left");

        btnAbout.setText("?");
        btnAbout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAboutActionPerformed(evt);
            }
        });
        panAction.add(btnAbout, "shrink, align left");

        panAction.add(Box.createHorizontalGlue(), "grow");

        btnOK.setText("Corrupt !");
        btnOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOKActionPerformed(evt);
            }
        });
        panAction.add(btnOK, "align right");

        btnEsc.setText("Close");
        btnEsc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEscActionPerformed(evt);
            }
        });
        panAction.add(btnEsc, "gap left 5px, align right");

        labTrials.setText("Trials:");
        labTrials.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 5));
        panAction.add(labTrials, "newline");

        progTrials.setMaximumSize(new java.awt.Dimension(32767, 20));
        progTrials.setMinimumSize(new java.awt.Dimension(50, 20));
        progTrials.setPreferredSize(new Dimension(200, 20));
        panAction.add(progTrials);

        labTrialsCount.setText("0/0");
        labTrialsCount.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 5, 0, 0));
        labTrialsCount.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        labTrialsCount.setMaximumSize(new java.awt.Dimension(50, 20));
        labTrialsCount.setMinimumSize(new java.awt.Dimension(50, 14));
        labTrialsCount.setPreferredSize(new java.awt.Dimension(50, 0));
        panAction.add(labTrialsCount);

        labSuccess.setText("Successes:");
        labSuccess.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 5));
        panAction.add(labSuccess, "newline");

        progSuccess.setMaximumSize(new java.awt.Dimension(32767, 20));
        progSuccess.setMinimumSize(new java.awt.Dimension(50, 20));
        progSuccess.setPreferredSize(new Dimension(200, 20));
        panAction.add(progSuccess);

        labSuccessCount.setText("0/0");
        labSuccessCount.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 5, 0, 0));
        labSuccessCount.setFocusable(false);
        labSuccessCount.setMaximumSize(new java.awt.Dimension(50, 20));
        labSuccessCount.setMinimumSize(new java.awt.Dimension(50, 14));
        labSuccessCount.setPreferredSize(new java.awt.Dimension(50, 0));
        panAction.add(labSuccessCount);

        panMain.add(panAction, java.awt.BorderLayout.PAGE_END);

        getContentPane().add(panMain, java.awt.BorderLayout.CENTER);

        pack();
    }

    private void btnOKActionPerformed(java.awt.event.ActionEvent evt) {

        BufferedImage buffImage = null;

        Path path = Paths.get(txtFile.getText());

        logger.info("Looking for " + path + " 's dimension");

        if (Files.notExists(path)) {
            logger.error(path + " does not exist");
            JOptionPane.showMessageDialog(this, "Invalid source image path:\n" + path, TITLE, JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (imageioSupportedFormats == null) {
            initImagIOSupportedFormats();
        }

        final String filename = path.getFileName().toString();
        String fileextension = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();

        if (imageioSupportedFormats.contains(fileextension)) {
            //via ImageIO
            try {
                buffImage = ImageIO.read(path.toFile());
//            } catch (IOException ex) {
            } catch (Throwable ex) { // en principe IOException, mais je catch aussi les java.lang.OutOfMemoryError 
                logger.error("While reading \"" + path + "\" (with ImaegIO)");
                logger.error(ex.toString());
                JOptionPane.showMessageDialog(this, "Fail to read source image.\n" + ex.toString(), TITLE, JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        else {
            logger.error("Unsupported image format in " + path);
            JOptionPane.showMessageDialog(this, "Unsupported image format.", TITLE, JOptionPane.ERROR_MESSAGE);
            return;
        }
        //TODO: vérifier qu'on a bien JPEG

        int _w = 0;
        int _h = 0;
        try {
            _h = buffImage.getHeight(null);
            _w = buffImage.getWidth(null);
            if ((_h <= 0) || (_w <= 0)) {
                logger.error("Invalid size (" + _w + ", " + _h + ")");
                return;
            }
        } catch (Exception ex) {
            logger.error("Could not retrieve the image size");
            logger.error(ex.getMessage());
            JOptionPane.showMessageDialog(this, "Could not retrieve source image's size.\n" + ex.toString(), TITLE, JOptionPane.ERROR_MESSAGE);
            return;
        }

        final int w = _w;
        final int h = _h;

        logger.info("The dimension of " + path + " is (" + w + ", " + h + ")");

        Path rootFolder = Paths.get(txtExportFolder.getText());
        if (Files.notExists(rootFolder)) {
            logger.error("Invalid export folder path" + rootFolder + " does not exist");
            JOptionPane.showMessageDialog(this, "Invalid export folder path:\n" + rootFolder, TITLE, JOptionPane.ERROR_MESSAGE);
            return;
        }

        String exp = filename.substring(0, filename.lastIndexOf(".")).toLowerCase();
        final Path exportFolder = rootFolder.resolve(exp);
        if (Files.notExists(exportFolder)) {
            try {
                Files.createDirectory(exportFolder);
            } catch (IOException ex) {
                logger.error("Impossible to create the export folder " + exportFolder, ex);
                JOptionPane.showMessageDialog(this, "Fail to create the export folder.\n" + ex.toString(), TITLE, JOptionPane.ERROR_MESSAGE);
            }
        }

        // manipulate the raster
        logger.info("Corrupting the image");

        // init
        // 1.1) A buffer, that we will reuse
        int[] clean;
        int[] work;
        try {
            final long size = Files.size(path);
            logger.debug("Buffer array size: " + size);
            clean = new int[(int) size];
            work = new int[(int) size];
        } catch (IOException ex) {
            logger.error("Error when building the buffer array", ex);
            JOptionPane.showMessageDialog(this, "Fail to retrieve source size.\n" + ex.toString(), TITLE, JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 1.2) read the clean buffer
        int pos = 0;
        try {
            FileInputStream fis = new FileInputStream(path.toFile());
            // read one byte at a time
            try (BufferedInputStream reader = new BufferedInputStream(fis)) {
                // read one byte at a time
                int ch;
                while ((ch = reader.read()) != -1) {
                    clean[pos++] = ch;
                }
            }
        } catch (IOException ex) {
            logger.error("Error reading the image", ex);
            JOptionPane.showMessageDialog(this, "Error while reading the source image.\n" + ex.toString(), TITLE, JOptionPane.ERROR_MESSAGE);
        }

        // 1.3) Misc
        int max = Integer.valueOf(txtMaxTrials.getText());
//        int max = 2;
        int expected = Integer.valueOf(txtExpected.getText());
//        int expected = 1;
        final Level level = (lstLevel.getSelectedIndex() == -1) ? Level.LOW : (Level) lstLevel.getSelectedItem();
        progTrials.setMaximum(max);
        progTrials.setMinimum(0);
        progSuccess.setMaximum(expected);
        progSuccess.setMinimum(0);

        // process
        SwingWorker<Set<Path>, Path> sw = new SwingWorker() {
            int success = 0;
            int trial = 0;

            @Override
            protected Set<Path> doInBackground() throws Exception {
                Set<Path> result = new HashSet<>(expected);
                while ((trial < max) && (success < expected)) {
                    trial++;
                    logger.debug("Starting trial " + trial + "/" + max + " -- successes: " + success + "/" + expected);
                    // update the screen
                    publish();

                    // 2.1) Copy the clean buffer to the work buffer
                    System.arraycopy(clean, 0, work, 0, clean.length);

                    // 2.2) Scramble
                    int start = START, end = clean.length - 1;
                    int nReplacements = rand(level.low, level.up);

                    for (int i = 0; i < nReplacements; i++) {
                        int posA = rand(start, end);
                        int posB = rand(start, end);
                        int tmp = work[posA];
                        work[posA] = work[posB];
                        work[posB] = work[tmp];
                    }

                    // 2.3) Write to temp file
                    //Path temp = Files.createTempFile("", "." + fileextension);
                    String timestamp = ZonedDateTime.now(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("uuuuMMdd_HHmmss_n"));
                    String exportFileName = timestamp + "_" + filename;
                    Path exportFile = exportFolder.resolve(exportFileName);

                    try {
                        FileOutputStream fos = new FileOutputStream(exportFile.toFile());
                        BufferedOutputStream writer = new BufferedOutputStream(fos);

                        for (int i = 0; i < work.length; i++) {
                            int b = work[i];
                            writer.write(b);
                        }

                        // flush remaining bytes
                        writer.flush();

                        // close the writer
                        writer.close();

                    } catch (IOException ex) {
                        logger.debug("While writing temprary file " + exportFile + " (with ImaegIO)");
                        continue;
                    }

                    // 2.4) Verify that this is a correct file
                    boolean check = false;
                    try {
                        BufferedImage checkBuff = ImageIO.read(exportFile.toFile());
                        int hcheck = checkBuff.getHeight(null);
                        int wcheck = checkBuff.getWidth(null);
                        if ((hcheck <= 0) || (wcheck <= 0)) {
                            logger.debug("The temp file has an invalid size (" + wcheck + ", " + hcheck + ")");
                        }
                        else if ((hcheck != h) || (wcheck != w)) {
                            logger.debug("The temp file has an different size (" + wcheck + ", " + hcheck + " / " + w + ", " + h + ")");
                        }
                        else {
                            check = true;
                            logger.info("Corrupted file " + exportFile + " verified with success");
                        }

                    } catch (Throwable ex) { // en principe IOException, mais je catch aussi les java.lang.OutOfMemoryError 
                        logger.error("While re-opening the corrupeted file " + exportFile + " (with ImaegIO)");
                    } finally {
                        if (!check) {
                            logger.debug("Deleting " + exportFile);
                            Files.delete(exportFile);
                            continue;
                        }
                    }

                    // 2.5) Success, we notify the success
                    success++;
                    publish(exportFile);
                    result.add(exportFile);

                }

                return result;
            }

            @Override
            protected void done() {
                Set<Path> result = null;
                try {
                    result = (Set<Path>) get();
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                } catch (ExecutionException ex) {
                    ex.printStackTrace();
                }

                if (result != null && !result.isEmpty()) {
                    btnOpenFolder.setEnabled(true);
                    lastExportFolder = result.iterator().next().getParent();
                }
                else {
                    btnOpenFolder.setEnabled(false);
                }
            }

            @Override
            protected void process(List chunks) {
                updateProgress(trial, max, success, expected);

            }

        };

        sw.execute();

    }

    private static final int rand(int low, int up) {
        return (int) (Math.random() * (up - low) + low);
    }

    private void updateProgress(int trial, int max, int success, int expected) {
        progTrials.setValue(trial);
        progSuccess.setValue(success);
        labSuccessCount.setText(success + "/" + expected);
        labTrialsCount.setText(trial + "/" + max);
    }

    private void btnOpenFolderActionPerformed(java.awt.event.ActionEvent evt) {
        if (lastExportFolder != null) try {
            Desktop.getDesktop().open(lastExportFolder.toFile());
        } catch (IOException ex) {
            logger.error("Failed to open the export folder " + lastExportFolder, ex);
            JOptionPane.showMessageDialog(this, "Failed to open the export folder:\n" + lastExportFolder + "\n" + ex.toString(), TITLE, JOptionPane.ERROR_MESSAGE);
        }
    }

    private void btnAboutActionPerformed(java.awt.event.ActionEvent evt) {
        JPanel panAbout = new JPanel(new MigLayout("fill, gapy 10px, flowy", "[align center]", "[align center, shrink, grow 0]-5px[align center, shrink, grow 0]10px[align top, grow 100]"));
        JLabel labAboutCorrupt = new JLabel("Corrupt !");
        final Font f = labAboutCorrupt.getFont();
        labAboutCorrupt.setFont(f.deriveFont(Font.PLAIN, (float) (f.getSize() * 1.5)));
        panAbout.add(labAboutCorrupt);

        final JLabel labParkingB = new JLabel("<html><a href='www.parkingb.be'>Parkingb B</a></html>");
        labParkingB.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                openWebpage("https://www.parkingb.be/");
            }
        });
        panAbout.add(labParkingB);

        panAbout.add(
                new JLabel("<html>Shared under <a href='https://creativecommons.org/licenses/by-nc-sa/4.0/'>Creative Commons Attribution-NonCommercial-ShareAlike 4.0</a></html>"),
                "align left"
        );

        if (ccImage == null) {
            try {
                URL url = new URL("https://mirrors.creativecommons.org/presskit/buttons/88x31/png/by-nc-sa.png");
                ccImage = ImageIO.read(url.openStream());
                ccImage = ccImage.getScaledInstance(-1, 40, Image.SCALE_SMOOTH);
            } catch (IOException ex) {
                logger.warn("Fail to load Creatice Common logo", ex);
            }
        }
        if (ccImage != null) {
            final JLabel labCC = new JLabel(new ImageIcon(ccImage));
            panAbout.add(labCC, "alignx left");
            labCC.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    openWebpage("https://creativecommons.org/licenses/by-nc-sa/4.0/");
                }
            });
        }
        final JLabel labRecycl = new JLabel("<html>Based on a work at <a href=\"http://www.recyclism.com/corrupt.html\">www.recyclism.com</a></html>");
        panAbout.add(labRecycl, "align left");
        labRecycl.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                openWebpage("http://www.recyclism.com/corrupt.html");
            }
        });

        final JLabel labGithubOrig = new JLabel("<html>Original php code source can be found on <a href=\"https://github.com/recyclism/Corrupt.Processing\">Github</a></html>");
        panAbout.add(labGithubOrig, "align left");
        labGithubOrig.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                openWebpage("https://github.com/recyclism/Corrupt.Processing");
            }
        });

        final JLabel labGithubMe = new JLabel("<html>The source code can be found on <a href=\"https://github.com/lgvr123/corrupt\">Github</a></html>");
        panAbout.add(labGithubMe, "align left");
        labGithubMe.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                openWebpage("https://github.com/lgvr123/corrupt");
            }
        });

        JLabel labFree = new JLabel("Feel free to update, modify and share corrupt with as many people as possible.");
        labFree.setFont(f.deriveFont(Font.PLAIN, (float) (f.getSize() * 1.2)));
        panAbout.add(labFree, "align left, gaptop 20px");

        JOptionPane.showMessageDialog(this, panAbout, TITLE, JOptionPane.INFORMATION_MESSAGE);
    }

    private void btnBrowseFileActionPerformed(java.awt.event.ActionEvent evt) {
        if (fileChooser == null) {
            String p = properties.getProperty(PROP_FILE, null);
            Path orig = null;
            if (p != null) {
                orig = Paths.get(p).getParent(); // parent folder
                if (Files.notExists(orig))
                    orig = null;
            }
            if (orig != null) {
                fileChooser = new JFileChooser(orig.toFile());
            }
            else {
                fileChooser = new JFileChooser();
            }
            fileChooser.setFileFilter(new FileNameExtensionFilter("JPEG files", "jpg", "jpeg"));
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        }

        int returnVal = fileChooser.showOpenDialog(this);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            txtFile.setText(file.toString());
        }
    }

    private void btnBrowseFolderActionPerformed(java.awt.event.ActionEvent evt) {
        if (folderChooser == null) {
            String p = properties.getProperty(PROP_EXPORT_FOLDER, null);
            Path orig = null;
            if (p != null) {
                orig = Paths.get(p);
                if (Files.notExists(orig))
                    orig = null;
            }
            if (orig != null) {
                folderChooser = new JFileChooser(orig.toFile());
            }
            else {
                folderChooser = new JFileChooser();
            }
            folderChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        }

        int returnVal = folderChooser.showOpenDialog(this);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = folderChooser.getSelectedFile();
            txtExportFolder.setText(file.toString());
        }
    }

    private void btnEscActionPerformed(java.awt.event.ActionEvent evt) {
        WindowEvent windowClosing = new WindowEvent(this, WindowEvent.WINDOW_CLOSING);
        dispatchEvent(windowClosing);
    }

    public void windowClosing(java.awt.event.WindowEvent e) {
        logger.debug("Saving properties");
        properties.setProperty(PROP_FILE, txtFile.getText());
        properties.setProperty(PROP_EXPORT_FOLDER, txtExportFolder.getText());
        properties.setProperty(PROP_EXPECTED, txtExpected.getText());
        properties.setProperty(PROP_MAX, txtMaxTrials.getText());
        properties.setProperty(PROP_LEVEL, ((Level) lstLevel.getSelectedItem()).name());

        try {
            properties.store(new FileOutputStream(propFile.toFile()), "");
        } catch (IOException ex) {
            logger.warn("Fail to save properties to " + propFile, ex);
        }

        System.exit(0);
    }

    public static void openWebpage(String urlString) {
        try {
            Desktop.getDesktop().browse(new URL(urlString).toURI());
        } catch (Exception ex) {
            logger.warn("failed to open url in browser", ex);

        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

        //FlatIntelliJLaf.install();
        FlatDarculaLaf.install();


        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Main dialog = new Main();
                dialog.setVisible(true);
            }
        });
    }

    private static void initImagIOSupportedFormats() {
        imageioSupportedFormats = new HashSet<>();
        for (String ext : ImageIO.getReaderFileSuffixes()) {
            imageioSupportedFormats.add(ext.toLowerCase());
        }
    }

    private static Set<String> imageioSupportedFormats
            = null;

    private enum Level {
        LOW(1, 10), MEDIUM(10, 30), HIGH(30, 100);
        public final int low;
        public final int up;

        private Level(int low, int up) {
            this.low = low;
            this.up = up;
        }
    }

    private class Status {

        public int trial = 0;
        public int success = 0;
    }

    // Variables declaration - do not modify                     
    private javax.swing.JButton btnBrowseFile;
    private javax.swing.JButton btnEsc;
    private javax.swing.JButton btnOK;
    private javax.swing.JButton btnBrowseFolder;
    private javax.swing.JButton btnOpenFolder;
    private javax.swing.JButton btnAbout;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel labTrials;
    private javax.swing.JLabel labSuccess;
    private javax.swing.JPanel panMain;
    private javax.swing.JPanel panAction;
    private javax.swing.JLabel labSuccessCount;
    private javax.swing.JLabel labTrialsCount;
    private javax.swing.JComboBox<String> lstLevel;
    private javax.swing.JPanel panFields;
    private javax.swing.JProgressBar progSuccess;
    private javax.swing.JProgressBar progTrials;
    private javax.swing.JTextField txtExportFolder;
    private javax.swing.JTextField txtFile;
    private javax.swing.JTextField txtMaxTrials;
    private javax.swing.JTextField txtExpected;
    private JFileChooser fileChooser = null;
    private JFileChooser folderChooser = null;
    private Image ccImage = null;
}
