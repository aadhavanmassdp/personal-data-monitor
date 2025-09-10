import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.zip.*;
import java.util.Base64;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class SteganographyApplication extends JFrame {
    private JMenuBar menuBar;
    private JMenu fileMenu, encryptMenu, compressMenu, embedMenu, toolsMenu;
    private JMenuItem openItem, saveItem, exitItem;
    private JMenuItem encryptItem, decryptItem;
    private JMenuItem compressItem, decompressItem;
    private JMenuItem embedItem, extractItem;
    private JMenuItem crcItem;
    
    private JPanel mainPanel;
    private JTextArea logArea;
    private JScrollPane logScrollPane;
    
    // Core modules
    private InputModule inputModule;
    private EncryptionModule encryptionModule;
    private CRCModule crcModule;
    private CompressionModule compressionModule;
    private SteganographyModule steganographyModule;
    
    public SteganographyApplication() {
        initializeModules();
        initializeGUI();
    }
    
    private void initializeModules() {
        inputModule = new InputModule();
        encryptionModule = new EncryptionModule();
        crcModule = new CRCModule();
        compressionModule = new CompressionModule();
        steganographyModule = new SteganographyModule();
    }
    
    private void initializeGUI() {
        setTitle("Advanced Steganography System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        
        // Create menu bar
        createMenuBar();
        
        // Create main panel
        mainPanel = new JPanel(new BorderLayout());
        
        // Create log area
        logArea = new JTextArea(15, 60);
        logArea.setEditable(false);
        logArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        logScrollPane = new JScrollPane(logArea);
        logScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        mainPanel.add(logScrollPane, BorderLayout.CENTER);
        
        // Add welcome panel
        JPanel welcomePanel = createWelcomePanel();
        mainPanel.add(welcomePanel, BorderLayout.NORTH);
        
        add(mainPanel);
        
        log("Steganography System Initialized Successfully");
        log("All modules loaded and ready for operation");
    }
    
    private void createMenuBar() {
        menuBar = new JMenuBar();
        
        // File Menu
        fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);
        
        openItem = new JMenuItem("Open", KeyEvent.VK_O);
        openItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        openItem.addActionListener(e -> openFile());
        
        saveItem = new JMenuItem("Save", KeyEvent.VK_S);
        saveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        saveItem.addActionListener(e -> saveFile());
        
        exitItem = new JMenuItem("Exit", KeyEvent.VK_X);
        exitItem.addActionListener(e -> System.exit(0));
        
        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);
        
        // Encryption Menu
        encryptMenu = new JMenu("Encryption");
        encryptMenu.setMnemonic(KeyEvent.VK_E);
        
        encryptItem = new JMenuItem("Encrypt Data");
        encryptItem.addActionListener(e -> showEncryptionDialog());
        
        decryptItem = new JMenuItem("Decrypt Data");
        decryptItem.addActionListener(e -> showDecryptionDialog());
        
        encryptMenu.add(encryptItem);
        encryptMenu.add(decryptItem);
        
        // Compression Menu
        compressMenu = new JMenu("Compression");
        compressMenu.setMnemonic(KeyEvent.VK_C);
        
        compressItem = new JMenuItem("Compress File");
        compressItem.addActionListener(e -> showCompressionDialog());
        
        decompressItem = new JMenuItem("Decompress File");
        decompressItem.addActionListener(e -> showDecompressionDialog());
        
        compressMenu.add(compressItem);
        compressMenu.add(decompressItem);
        
        // Steganography Menu
        embedMenu = new JMenu("Steganography");
        embedMenu.setMnemonic(KeyEvent.VK_S);
        
        embedItem = new JMenuItem("Embed Data");
        embedItem.addActionListener(e -> showEmbedDialog());
        
        extractItem = new JMenuItem("Extract Data");
        extractItem.addActionListener(e -> showExtractDialog());
        
        embedMenu.add(embedItem);
        embedMenu.add(extractItem);
        
        // Tools Menu
        toolsMenu = new JMenu("Tools");
        toolsMenu.setMnemonic(KeyEvent.VK_T);
        
        crcItem = new JMenuItem("Generate CRC");
        crcItem.addActionListener(e -> showCRCDialog());
        
        toolsMenu.add(crcItem);
        
        menuBar.add(fileMenu);
        menuBar.add(encryptMenu);
        menuBar.add(compressMenu);
        menuBar.add(embedMenu);
        menuBar.add(toolsMenu);
        
        setJMenuBar(menuBar);
    }
    
    private JPanel createWelcomePanel() {
        JPanel panel = new JPanel(new FlowLayout());
        panel.setBorder(BorderFactory.createTitledBorder("System Status"));
        
        JLabel statusLabel = new JLabel("System Ready - All Modules Operational");
        statusLabel.setForeground(Color.GREEN);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        panel.add(statusLabel);
        return panel;
    }
    
    private void log(String message) {
        logArea.append("[" + java.time.LocalTime.now().toString() + "] " + message + "\n");
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }
    
    // Menu Action Methods
    private void openFile() {
        File file = inputModule.selectFile(this, "Open File");
        if (file != null) {
            log("File opened: " + file.getName());
        }
    }
    
    private void saveFile() {
        File file = inputModule.saveFile(this, "Save File");
        if (file != null) {
            log("File saved: " + file.getName());
        }
    }
    
    private void showEncryptionDialog() {
        new EncryptionDialog(this, encryptionModule).setVisible(true);
    }
    
    private void showDecryptionDialog() {
        new DecryptionDialog(this, encryptionModule).setVisible(true);
    }
    
    private void showCompressionDialog() {
        new CompressionDialog(this, compressionModule).setVisible(true);
    }
    
    private void showDecompressionDialog() {
        new DecompressionDialog(this, compressionModule).setVisible(true);
    }
    
    private void showEmbedDialog() {
        new EmbedDialog(this, steganographyModule, inputModule).setVisible(true);
    }
    
    private void showExtractDialog() {
        new ExtractDialog(this, steganographyModule, inputModule).setVisible(true);
    }
    
    private void showCRCDialog() {
        new CRCDialog(this, crcModule, inputModule).setVisible(true);
    }
    
    public void logMessage(String message) {
        log(message);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeel());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new SteganographyApplication().setVisible(true);
        });
    }
}

// Input Module Class
class InputModule {
    
    public File selectFile(Component parent, String title) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle(title);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        
        int result = fileChooser.showOpenDialog(parent);
        if (result == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        }
        return null;
    }
    
    public File saveFile(Component parent, String title) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle(title);
        
        int result = fileChooser.showSaveDialog(parent);
        if (result == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        }
        return null;
    }
    
    public File selectImageFile(Component parent, String title) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle(title);
        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(File f) {
                if (f.isDirectory()) return true;
                String name = f.getName().toLowerCase();
                return name.endsWith(".bmp") || name.endsWith(".jpg") || 
                       name.endsWith(".jpeg") || name.endsWith(".png");
            }
            
            @Override
            public String getDescription() {
                return "Image Files (*.bmp, *.jpg, *.jpeg, *.png)";
            }
        });
        
        int result = fileChooser.showOpenDialog(parent);
        if (result == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        }
        return null;
    }
    
    public boolean isValidFileFormat(File file, String[] supportedFormats) {
        if (file == null) return false;
        String fileName = file.getName().toLowerCase();
        for (String format : supportedFormats) {
            if (fileName.endsWith(format.toLowerCase())) {
                return true;
            }
        }
        return false;
    }
    
    public byte[] readFileToBytes(File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesRead);
            }
            return baos.toByteArray();
        }
    }
    
    public void writeBytesToFile(byte[] data, File file) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(data);
        }
    }
}

// Encryption Module Class
class EncryptionModule {
    
    public String encryptText(String plainText, String key) {
        try {
            // Simple Base64 encoding with key mixing for demonstration
            String mixedText = mixWithKey(plainText, key);
            return Base64.getEncoder().encodeToString(mixedText.getBytes("UTF-8"));
        } catch (Exception e) {
            throw new RuntimeException("Encryption failed", e);
        }
    }
    
    public String decryptText(String encryptedText, String key) {
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(encryptedText);
            String mixedText = new String(decodedBytes, "UTF-8");
            return unmixWithKey(mixedText, key);
        } catch (Exception e) {
            throw new RuntimeException("Decryption failed", e);
        }
    }
    
    public byte[] encryptBytes(byte[] data, String key) {
        try {
            // XOR encryption with key
            byte[] keyBytes = key.getBytes("UTF-8");
            byte[] encrypted = new byte[data.length];
            
            for (int i = 0; i < data.length; i++) {
                encrypted[i] = (byte) (data[i] ^ keyBytes[i % keyBytes.length]);
            }
            
            return encrypted;
        } catch (Exception e) {
            throw new RuntimeException("Byte encryption failed", e);
        }
    }
    
    public byte[] decryptBytes(byte[] encryptedData, String key) {
        // XOR decryption (XOR is its own inverse)
        return encryptBytes(encryptedData, key);
    }
    
    private String mixWithKey(String text, String key) {
        StringBuilder mixed = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char textChar = text.charAt(i);
            char keyChar = key.charAt(i % key.length());
            mixed.append((char) (textChar + keyChar));
        }
        return mixed.toString();
    }
    
    private String unmixWithKey(String mixedText, String key) {
        StringBuilder original = new StringBuilder();
        for (int i = 0; i < mixedText.length(); i++) {
            char mixedChar = mixedText.charAt(i);
            char keyChar = key.charAt(i % key.length());
            original.append((char) (mixedChar - keyChar));
        }
        return original.toString();
    }
}

// CRC Module Class
class CRCModule {
    private CRC32 crc32;
    
    public CRCModule() {
        crc32 = new CRC32();
    }
    
    public long generateCRC(String data) {
        crc32.reset();
        crc32.update(data.getBytes());
        return crc32.getValue();
    }
    
    public long generateCRC(byte[] data) {
        crc32.reset();
        crc32.update(data);
        return crc32.getValue();
    }
    
    public long generateCRC(File file) throws IOException {
        crc32.reset();
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                crc32.update(buffer, 0, bytesRead);
            }
        }
        return crc32.getValue();
    }
    
    public boolean verifyCRC(String data, long expectedCRC) {
        return generateCRC(data) == expectedCRC;
    }
    
    public boolean verifyCRC(byte[] data, long expectedCRC) {
        return generateCRC(data) == expectedCRC;
    }
    
    public boolean verifyCRC(File file, long expectedCRC) throws IOException {
        return generateCRC(file) == expectedCRC;
    }
}

// Compression Module Class
class CompressionModule {
    
    public CompressionResult compressFile(File inputFile, File outputFile, ProgressCallback callback) throws IOException {
        long originalSize = inputFile.length();
        long startTime = System.currentTimeMillis();
        
        try (FileInputStream fis = new FileInputStream(inputFile);
             FileOutputStream fos = new FileOutputStream(outputFile);
             GZIPOutputStream gzos = new GZIPOutputStream(fos)) {
            
            byte[] buffer = new byte[1024];
            int bytesRead;
            long totalBytesRead = 0;
            
            while ((bytesRead = fis.read(buffer)) != -1) {
                gzos.write(buffer, 0, bytesRead);
                totalBytesRead += bytesRead;
                
                if (callback != null) {
                    int progress = (int) ((totalBytesRead * 100) / originalSize);
                    callback.updateProgress(progress);
                }
            }
        }
        
        long compressedSize = outputFile.length();
        long endTime = System.currentTimeMillis();
        double compressionRatio = ((double) (originalSize - compressedSize) / originalSize) * 100;
        
        return new CompressionResult(
            inputFile.getName(),
            outputFile.getName(),
            originalSize,
            compressedSize,
            compressionRatio,
            endTime - startTime
        );
    }
    
    public void decompressFile(File inputFile, File outputFile, ProgressCallback callback) throws IOException {
        long inputSize = inputFile.length();
        
        try (FileInputStream fis = new FileInputStream(inputFile);
             GZIPInputStream gzis = new GZIPInputStream(fis);
             FileOutputStream fos = new FileOutputStream(outputFile)) {
            
            byte[] buffer = new byte[1024];
            int bytesRead;
            long totalBytesRead = 0;
            
            while ((bytesRead = gzis.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
                totalBytesRead += bytesRead;
                
                if (callback != null) {
                    // Approximate progress for decompression
                    int progress = Math.min(99, (int) ((totalBytesRead * 50) / inputSize));
                    callback.updateProgress(progress);
                }
            }
            
            if (callback != null) {
                callback.updateProgress(100);
            }
        }
    }
    
    public byte[] compressData(byte[] data) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             GZIPOutputStream gzos = new GZIPOutputStream(baos)) {
            gzos.write(data);
            gzos.finish();
            return baos.toByteArray();
        }
    }
    
    public byte[] decompressData(byte[] compressedData) throws IOException {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(compressedData);
             GZIPInputStream gzis = new GZIPInputStream(bais);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = gzis.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesRead);
            }
            return baos.toByteArray();
        }
    }
    
    public interface ProgressCallback {
        void updateProgress(int progress);
    }
    
    public static class CompressionResult {
        public final String sourceFileName;
        public final String destinationFileName;
        public final long originalSize;
        public final long compressedSize;
        public final double compressionRatio;
        public final long processingTime;
        
        public CompressionResult(String sourceFileName, String destinationFileName,
                               long originalSize, long compressedSize,
                               double compressionRatio, long processingTime) {
            this.sourceFileName = sourceFileName;
            this.destinationFileName = destinationFileName;
            this.originalSize = originalSize;
            this.compressedSize = compressedSize;
            this.compressionRatio = compressionRatio;
            this.processingTime = processingTime;
        }
        
        @Override
        public String toString() {
            return String.format(
                "Compression Report:\n" +
                "Source File: %s\n" +
                "Destination File: %s\n" +
                "Original Size: %d bytes\n" +
                "Compressed Size: %d bytes\n" +
                "Compression Ratio: %.2f%%\n" +
                "Processing Time: %d ms",
                sourceFileName, destinationFileName, originalSize, 
                compressedSize, compressionRatio, processingTime
            );
        }
    }
}

// Steganography Module Class
class SteganographyModule {
    
    public void embedDataInImage(File imageFile, byte[] data, File outputFile) throws IOException {
        BufferedImage image = ImageIO.read(imageFile);
        BufferedImage outputImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        
        // Copy original image
        Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();
        
        // Convert data to binary string
        StringBuilder binaryData = new StringBuilder();
        for (byte b : data) {
            binaryData.append(String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0'));
        }
        
        // Add end marker
        binaryData.append("1111111111111110"); // End marker
        
        String binaryString = binaryData.toString();
        int dataIndex = 0;
        
        // Embed data using LSB technique
        outerLoop:
        for (int y = 0; y < outputImage.getHeight() && dataIndex < binaryString.length(); y++) {
            for (int x = 0; x < outputImage.getWidth() && dataIndex < binaryString.length(); x++) {
                int rgb = outputImage.getRGB(x, y);
                
                int red = (rgb >> 16) & 0xFF;
                int green = (rgb >> 8) & 0xFF;
                int blue = rgb & 0xFF;
                
                // Modify LSB of red channel
                if (dataIndex < binaryString.length()) {
                    int bit = Character.getNumericValue(binaryString.charAt(dataIndex));
                    red = (red & 0xFE) | bit;
                    dataIndex++;
                }
                
                int newRgb = (red << 16) | (green << 8) | blue;
                outputImage.setRGB(x, y, newRgb);
            }
        }
        
        // Save the output image
        String formatName = getImageFormat(outputFile.getName());
        ImageIO.write(outputImage, formatName, outputFile);
    }
    
    public byte[] extractDataFromImage(File imageFile) throws IOException {
        BufferedImage image = ImageIO.read(imageFile);
        StringBuilder binaryData = new StringBuilder();
        
        // Extract LSB from red channel
        outerLoop:
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int rgb = image.getRGB(x, y);
                int red = (rgb >> 16) & 0xFF;
                
                // Extract LSB
                int lsb = red & 1;
                binaryData.append(lsb);
                
                // Check for end marker
                if (binaryData.length() >= 16) {
                    String lastBits = binaryData.substring(binaryData.length() - 16);
                    if ("1111111111111110".equals(lastBits)) {
                        binaryData.setLength(binaryData.length() - 16); // Remove end marker
                        break outerLoop;
                    }
                }
            }
        }
        
        // Convert binary string to bytes
        String binaryString = binaryData.toString();
        byte[] extractedData = new byte[binaryString.length() / 8];
        
        for (int i = 0; i < extractedData.length; i++) {
            String byteString = binaryString.substring(i * 8, (i + 1) * 8);
            extractedData[i] = (byte) Integer.parseInt(byteString, 2);
        }
        
        return extractedData;
    }
    
    private String getImageFormat(String fileName) {
        String extension = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
        switch (extension) {
            case "jpg":
            case "jpeg":
                return "jpg";
            case "png":
                return "png";
            case "bmp":
                return "bmp";
            default:
                return "png"; // Default to PNG
        }
    }
    
    public boolean canEmbedData(File imageFile, int dataSize) throws IOException {
        BufferedImage image = ImageIO.read(imageFile);
        int availableBits = image.getWidth() * image.getHeight();
        int requiredBits = (dataSize * 8) + 16; // Data bits + end marker
        return availableBits >= requiredBits;
    }
}

// Dialog Classes

// Encryption Dialog
class EncryptionDialog extends JDialog {
    private JTextArea inputArea, outputArea;
    private JTextField keyField;
    private JButton encryptButton;
    private EncryptionModule encryptionModule;
    
    public EncryptionDialog(Frame parent, EncryptionModule encryptionModule) {
        super(parent, "Encrypt Data", true);
        this.encryptionModule = encryptionModule;
        initializeComponents();
    }
    
    private void initializeComponents() {
        setLayout(new BorderLayout());
        setSize(500, 400);
        setLocationRelativeTo(getParent());
        
        // Input panel
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("Input Text"));
        inputArea = new JTextArea(8, 40);
        inputArea.setLineWrap(true);
        inputArea.setWrapStyleWord(true);
        inputPanel.add(new JScrollPane(inputArea), BorderLayout.CENTER);
        
        // Key panel
        JPanel keyPanel = new JPanel(new FlowLayout());
        keyPanel.add(new JLabel("Encryption Key:"));
        keyField = new JTextField(20);
        keyPanel.add(keyField);
        
        encryptButton = new JButton("Encrypt");
        encryptButton.addActionListener(e -> performEncryption());
        keyPanel.add(encryptButton);
        
        // Output panel
        JPanel outputPanel = new JPanel(new BorderLayout());
        outputPanel.setBorder(BorderFactory.createTitledBorder("Encrypted Output"));
        outputArea = new JTextArea(8, 40);
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);
        outputArea.setEditable(false);
        outputPanel.add(new JScrollPane(outputArea), BorderLayout.CENTER);
        
        add(inputPanel, BorderLayout.NORTH);
        add(keyPanel, BorderLayout.CENTER);
        add(outputPanel, BorderLayout.SOUTH);
    }
    
    private void performEncryption() {
        String text = inputArea.getText().trim();
        String key = keyField.getText().trim();
        
        if (text.isEmpty() || key.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both text and key", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            String encrypted = encryptionModule.encryptText(text, key);
            outputArea.setText(encrypted);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Encryption failed: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

// Decryption Dialog
class DecryptionDialog extends JDialog {
    private JTextArea inputArea, outputArea;
    private JTextField keyField;
    private JButton decryptButton;
    private EncryptionModule encryptionModule;
    
    public DecryptionDialog(Frame parent, EncryptionModule encryptionModule) {
        super(parent, "Decrypt Data", true);
        this.encryptionModule = encryptionModule;
        initializeComponents();
    }
    
    private void initializeComponents() {
        setLayout(new BorderLayout());
        setSize(500, 400);
        setLocationRelativeTo(getParent());
        
        // Input panel
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("Encrypted Text"));
        inputArea = new JTextArea(8, 40);
        inputArea.setLineWrap(true);
        inputArea.setWrapStyleWord(true);
        inputPanel.add(new JScrollPane(inputArea), BorderLayout.CENTER);
        
        // Key panel
        JPanel keyPanel = new JPanel(new FlowLayout());
        keyPanel.add(new JLabel("Decryption Key:"));
        keyField = new JTextField(20);
        keyPanel.add(keyField);
        
        decryptButton = new JButton("Decrypt");
        decryptButton.addActionListener(e -> performDecryption());
        keyPanel.add(decryptButton);
        
        // Output panel
        JPanel outputPanel = new JPanel(new BorderLayout());
        outputPanel.setBorder(BorderFactory.createTitledBorder("Decrypted Output"));
        outputArea = new JTextArea(8, 40);
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);
        outputArea.setEditable(false);
        outputPanel.add(new JScrollPane(outputArea), BorderLayout.CENTER);
        
        add(inputPanel, BorderLayout.NORTH);
        add(keyPanel, BorderLayout.CENTER);
        add(outputPanel, BorderLayout.SOUTH);
    }
    
    private void performDecryption() {
        String text = inputArea.getText().trim();
        String key = keyField.getText().trim();
        
        if (text.isEmpty() || key.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both encrypted text and key", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            String decrypted = encryptionModule.decryptText(text, key);
            outputArea.setText(decrypted);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Decryption failed: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

// Compression Dialog
class CompressionDialog extends JDialog {
    private JTextField inputFileField, outputFileField;
    private JButton browseInputButton, browseOutputButton, compressButton;
    private JProgressBar progressBar;
    private JTextArea resultArea;
    private CompressionModule compressionModule;
    private InputModule inputModule;
    
    public CompressionDialog(Frame parent, CompressionModule compressionModule) {
        super(parent, "Compress File", true);
        this.compressionModule = compressionModule;
        this.inputModule = new InputModule();
        initializeComponents();
    }
    
    private void initializeComponents() {
        setLayout(new BorderLayout());
        setSize(500, 350);
        setLocationRelativeTo(getParent());
        
        // File selection panel
        JPanel filePanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        filePanel.add(new JLabel("Input File:"), gbc);
        
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        inputFileField = new JTextField(30);
        inputFileField.setEditable(false);
        filePanel.add(inputFileField, gbc);
        
        gbc.gridx = 2; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        browseInputButton = new JButton("Browse");
        browseInputButton.addActionListener(e -> selectInputFile());
        filePanel.add(browseInputButton, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.WEST;
        filePanel.add(new JLabel("Output File:"), gbc);
        
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        outputFileField = new JTextField(30);
        filePanel.add(outputFileField, gbc);
        
        gbc.gridx = 2; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        browseOutputButton = new JButton("Browse");
        browseOutputButton.addActionListener(e -> selectOutputFile());
        filePanel.add(browseOutputButton, gbc);
        
        // Progress panel
        JPanel progressPanel = new JPanel(new BorderLayout());
        progressPanel.setBorder(BorderFactory.createTitledBorder("Progress"));
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressPanel.add(progressBar, BorderLayout.CENTER);
        
        compressButton = new JButton("Compress");
        compressButton.addActionListener(e -> performCompression());
        progressPanel.add(compressButton, BorderLayout.SOUTH);
        
        // Result panel
        JPanel resultPanel = new JPanel(new BorderLayout());
        resultPanel.setBorder(BorderFactory.createTitledBorder("Compression Report"));
        resultArea = new JTextArea(6, 40);
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        resultPanel.add(new JScrollPane(resultArea), BorderLayout.CENTER);
        
        add(filePanel, BorderLayout.NORTH);
        add(progressPanel, BorderLayout.CENTER);
        add(resultPanel, BorderLayout.SOUTH);
    }
    
    private void selectInputFile() {
        File file = inputModule.selectFile(this, "Select File to Compress");
        if (file != null) {
            inputFileField.setText(file.getAbsolutePath());
            // Auto-suggest output file name
            String outputPath = file.getAbsolutePath() + ".gz";
            outputFileField.setText(outputPath);
        }
    }
    
    private void selectOutputFile() {
        File file = inputModule.saveFile(this, "Save Compressed File");
        if (file != null) {
            outputFileField.setText(file.getAbsolutePath());
        }
    }
    
    private void performCompression() {
        String inputPath = inputFileField.getText().trim();
        String outputPath = outputFileField.getText().trim();
        
        if (inputPath.isEmpty() || outputPath.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select both input and output files", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        File inputFile = new File(inputPath);
        File outputFile = new File(outputPath);
        
        if (!inputFile.exists()) {
            JOptionPane.showMessageDialog(this, "Input file does not exist", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        compressButton.setEnabled(false);
        progressBar.setValue(0);
        
        SwingWorker<CompressionModule.CompressionResult, Integer> worker = new SwingWorker<CompressionModule.CompressionResult, Integer>() {
            @Override
            protected CompressionModule.CompressionResult doInBackground() throws Exception {
                return compressionModule.compressFile(inputFile, outputFile, progress -> {
                    publish(progress);
                });
            }
            
            @Override
            protected void process(java.util.List<Integer> chunks) {
                int latestProgress = chunks.get(chunks.size() - 1);
                progressBar.setValue(latestProgress);
            }
            
            @Override
            protected void done() {
                try {
                    CompressionModule.CompressionResult result = get();
                    resultArea.setText(result.toString());
                    progressBar.setValue(100);
                    JOptionPane.showMessageDialog(CompressionDialog.this, "Compression completed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception e) {
                    resultArea.setText("Compression failed: " + e.getMessage());
                    JOptionPane.showMessageDialog(CompressionDialog.this, "Compression failed: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                } finally {
                    compressButton.setEnabled(true);
                }
            }
        };
        
        worker.execute();
    }
}

// Decompression Dialog
class DecompressionDialog extends JDialog {
    private JTextField inputFileField, outputFileField;
    private JButton browseInputButton, browseOutputButton, decompressButton;
    private JProgressBar progressBar;
    private CompressionModule compressionModule;
    private InputModule inputModule;
    
    public DecompressionDialog(Frame parent, CompressionModule compressionModule) {
        super(parent, "Decompress File", true);
        this.compressionModule = compressionModule;
        this.inputModule = new InputModule();
        initializeComponents();
    }
    
    private void initializeComponents() {
        setLayout(new BorderLayout());
        setSize(500, 200);
        setLocationRelativeTo(getParent());
        
        // File selection panel
        JPanel filePanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        filePanel.add(new JLabel("Compressed File:"), gbc);
        
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        inputFileField = new JTextField(30);
        inputFileField.setEditable(false);
        filePanel.add(inputFileField, gbc);
        
        gbc.gridx = 2; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        browseInputButton = new JButton("Browse");
        browseInputButton.addActionListener(e -> selectInputFile());
        filePanel.add(browseInputButton, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.WEST;
        filePanel.add(new JLabel("Output File:"), gbc);
        
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        outputFileField = new JTextField(30);
        filePanel.add(outputFileField, gbc);
        
        gbc.gridx = 2; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        browseOutputButton = new JButton("Browse");
        browseOutputButton.addActionListener(e -> selectOutputFile());
        filePanel.add(browseOutputButton, gbc);
        
        // Progress panel
        JPanel progressPanel = new JPanel(new BorderLayout());
        progressPanel.setBorder(BorderFactory.createTitledBorder("Progress"));
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressPanel.add(progressBar, BorderLayout.CENTER);
        
        decompressButton = new JButton("Decompress");
        decompressButton.addActionListener(e -> performDecompression());
        progressPanel.add(decompressButton, BorderLayout.SOUTH);
        
        add(filePanel, BorderLayout.NORTH);
        add(progressPanel, BorderLayout.CENTER);
    }
    
    private void selectInputFile() {
        File file = inputModule.selectFile(this, "Select Compressed File");
        if (file != null) {
            inputFileField.setText(file.getAbsolutePath());
            // Auto-suggest output file name
            String outputPath = file.getAbsolutePath();
            if (outputPath.endsWith(".gz")) {
                outputPath = outputPath.substring(0, outputPath.length() - 3);
            } else {
                outputPath += ".decompressed";
            }
            outputFileField.setText(outputPath);
        }
    }
    
    private void selectOutputFile() {
        File file = inputModule.saveFile(this, "Save Decompressed File");
        if (file != null) {
            outputFileField.setText(file.getAbsolutePath());
        }
    }
    
    private void performDecompression() {
        String inputPath = inputFileField.getText().trim();
        String outputPath = outputFileField.getText().trim();
        
        if (inputPath.isEmpty() || outputPath.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select both input and output files", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        File inputFile = new File(inputPath);
        File outputFile = new File(outputPath);
        
        if (!inputFile.exists()) {
            JOptionPane.showMessageDialog(this, "Input file does not exist", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        decompressButton.setEnabled(false);
        progressBar.setValue(0);
        
        SwingWorker<Void, Integer> worker = new SwingWorker<Void, Integer>() {
            @Override
            protected Void doInBackground() throws Exception {
                compressionModule.decompressFile(inputFile, outputFile, progress -> {
                    publish(progress);
                });
                return null;
            }
            
            @Override
            protected void process(java.util.List<Integer> chunks) {
                int latestProgress = chunks.get(chunks.size() - 1);
                progressBar.setValue(latestProgress);
            }
            
            @Override
            protected void done() {
                try {
                    get();
                    progressBar.setValue(100);
                    JOptionPane.showMessageDialog(DecompressionDialog.this, "Decompression completed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(DecompressionDialog.this, "Decompression failed: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                } finally {
                    decompressButton.setEnabled(true);
                }
            }
        };
        
        worker.execute();
    }
}

// Embed Dialog
class EmbedDialog extends JDialog {
    private JTextField dataFileField, imageFileField, outputFileField;
    private JButton browseDataButton, browseImageButton, browseOutputButton, embedButton;
    private JCheckBox encryptCheckBox, compressCheckBox;
    private JTextField keyField;
    private JLabel imagePreviewLabel;
    private SteganographyModule steganographyModule;
    private InputModule inputModule;
    
    public EmbedDialog(Frame parent, SteganographyModule steganographyModule, InputModule inputModule) {
        super(parent, "Embed Data in Image", true);
        this.steganographyModule = steganographyModule;
        this.inputModule = inputModule;
        initializeComponents();
    }
    
    private void initializeComponents() {
        setLayout(new BorderLayout());
        setSize(600, 500);
        setLocationRelativeTo(getParent());
        
        // File selection panel
        JPanel filePanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        filePanel.add(new JLabel("Data File:"), gbc);
        
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        dataFileField = new JTextField(25);
        dataFileField.setEditable(false);
        filePanel.add(dataFileField, gbc);
        
        gbc.gridx = 2; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        browseDataButton = new JButton("Browse");
        browseDataButton.addActionListener(e -> selectDataFile());
        filePanel.add(browseDataButton, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.WEST;
        filePanel.add(new JLabel("Cover Image:"), gbc);
        
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        imageFileField = new JTextField(25);
        imageFileField.setEditable(false);
        filePanel.add(imageFileField, gbc);
        
        gbc.gridx = 2; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        browseImageButton = new JButton("Browse");
        browseImageButton.addActionListener(e -> selectImageFile());
        filePanel.add(browseImageButton, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2; gbc.anchor = GridBagConstraints.WEST;
        filePanel.add(new JLabel("Output Image:"), gbc);
        
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        outputFileField = new JTextField(25);
        filePanel.add(outputFileField, gbc);
        
        gbc.gridx = 2; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        browseOutputButton = new JButton("Browse");
        browseOutputButton.addActionListener(e -> selectOutputFile());
        filePanel.add(browseOutputButton, gbc);
        
        // Options panel
        JPanel optionsPanel = new JPanel(new FlowLayout());
        optionsPanel.setBorder(BorderFactory.createTitledBorder("Options"));
        
        encryptCheckBox = new JCheckBox("Encrypt Data");
        compressCheckBox = new JCheckBox("Compress Data");
        optionsPanel.add(encryptCheckBox);
        optionsPanel.add(compressCheckBox);
        
        optionsPanel.add(new JLabel("Key:"));
        keyField = new JTextField(15);
        optionsPanel.add(keyField);
        
        // Image preview panel
        JPanel previewPanel = new JPanel(new BorderLayout());
        previewPanel.setBorder(BorderFactory.createTitledBorder("Image Preview"));
        imagePreviewLabel = new JLabel("No image selected", JLabel.CENTER);
        imagePreviewLabel.setPreferredSize(new Dimension(200, 150));
        previewPanel.add(imagePreviewLabel, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        embedButton = new JButton("Embed Data");
        embedButton.addActionListener(e -> performEmbed());
        buttonPanel.add(embedButton);
        
        add(filePanel, BorderLayout.NORTH);
        add(optionsPanel, BorderLayout.CENTER);
        add(previewPanel, BorderLayout.EAST);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void selectDataFile() {
        File file = inputModule.selectFile(this, "Select Data File");
        if (file != null) {
            dataFileField.setText(file.getAbsolutePath());
        }
    }
    
    private void selectImageFile() {
        File file = inputModule.selectImageFile(this, "Select Cover Image");
        if (file != null) {
            imageFileField.setText(file.getAbsolutePath());
            updateImagePreview(file);
            // Auto-suggest output file name
            String outputPath = file.getParent() + File.separator + "stego_" + file.getName();
            outputFileField.setText(outputPath);
        }
    }
    
    private void selectOutputFile() {
        File file = inputModule.saveFile(this, "Save Stego Image");
        if (file != null) {
            outputFileField.setText(file.getAbsolutePath());
        }
    }
    
    private void updateImagePreview(File imageFile) {
        try {
            BufferedImage image = ImageIO.read(imageFile);
            // Scale image for preview
            int maxWidth = 180;
            int maxHeight = 130;
            int width = image.getWidth();
            int height = image.getHeight();
            
            double scale = Math.min((double) maxWidth / width, (double) maxHeight / height);
            int scaledWidth = (int) (width * scale);
            int scaledHeight = (int) (height * scale);
            
            Image scaledImage = image.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
            imagePreviewLabel.setIcon(new ImageIcon(scaledImage));
            imagePreviewLabel.setText("");
        } catch (IOException e) {
            imagePreviewLabel.setIcon(null);
            imagePreviewLabel.setText("Preview not available");
        }
    }
    
    private void performEmbed() {
        String dataPath = dataFileField.getText().trim();
        String imagePath = imageFileField.getText().trim();
        String outputPath = outputFileField.getText().trim();
        
        if (dataPath.isEmpty() || imagePath.isEmpty() || outputPath.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select all required files", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        File dataFile = new File(dataPath);
        File imageFile = new File(imagePath);
        File outputFile = new File(outputPath);
        
        if (!dataFile.exists() || !imageFile.exists()) {
            JOptionPane.showMessageDialog(this, "Selected files do not exist", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        embedButton.setEnabled(false);
        
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                // Read data file
                byte[] data = inputModule.readFileToBytes(dataFile);
                
                // Apply compression if selected
                if (compressCheckBox.isSelected()) {
                    CompressionModule compressionModule = new CompressionModule();
                    data = compressionModule.compressData(data);
                }
                
                // Apply encryption if selected
                if (encryptCheckBox.isSelected()) {
                    String key = keyField.getText().trim();
                    if (key.isEmpty()) {
                        throw new IllegalArgumentException("Encryption key is required when encryption is enabled");
                    }
                    EncryptionModule encryptionModule = new EncryptionModule();
                    data = encryptionModule.encryptBytes(data, key);
                }
                
                // Check if image can hold the data
                if (!steganographyModule.canEmbedData(imageFile, data.length)) {
                    throw new IllegalArgumentException("Image is too small to hold the data. Please select a larger image.");
                }
                
                // Embed data in image
                steganographyModule.embedDataInImage(imageFile, data, outputFile);
                
                return null;
            }
            
            @Override
            protected void done() {
                try {
                    get();
                    JOptionPane.showMessageDialog(EmbedDialog.this, "Data embedded successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(EmbedDialog.this, "Embedding failed: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                } finally {
                    embedButton.setEnabled(true);
                }
            }
        };
        
        worker.execute();
    }
}

// Extract Dialog
class ExtractDialog extends JDialog {
    private JTextField imageFileField, outputFileField;
    private JButton browseImageButton, browseOutputButton, extractButton;
    private JCheckBox decryptCheckBox, decompressCheckBox;
    private JTextField keyField;
    private JLabel imagePreviewLabel;
    private SteganographyModule steganographyModule;
    private InputModule inputModule;
    
    public ExtractDialog(Frame parent, SteganographyModule steganographyModule, InputModule inputModule) {
        super(parent, "Extract Data from Image", true);
        this.steganographyModule = steganographyModule;
        this.inputModule = inputModule;
        initializeComponents();
    }
    
    private void initializeComponents() {
        setLayout(new BorderLayout());
        setSize(550, 400);
        setLocationRelativeTo(getParent());
        
        // File selection panel
        JPanel filePanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        filePanel.add(new JLabel("Stego Image:"), gbc);
        
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        imageFileField = new JTextField(25);
        imageFileField.setEditable(false);
        filePanel.add(imageFileField, gbc);
        
        gbc.gridx = 2; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        browseImageButton = new JButton("Browse");
        browseImageButton.addActionListener(e -> selectImageFile());
        filePanel.add(browseImageButton, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.WEST;
        filePanel.add(new JLabel("Output File:"), gbc);
        
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        outputFileField = new JTextField(25);
        filePanel.add(outputFileField, gbc);
        
        gbc.gridx = 2; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        browseOutputButton = new JButton("Browse");
        browseOutputButton.addActionListener(e -> selectOutputFile());
        filePanel.add(browseOutputButton, gbc);
        
        // Options panel
        JPanel optionsPanel = new JPanel(new FlowLayout());
        optionsPanel.setBorder(BorderFactory.createTitledBorder("Options"));
        
        decryptCheckBox = new JCheckBox("Decrypt Data");
        decompressCheckBox = new JCheckBox("Decompress Data");
        optionsPanel.add(decryptCheckBox);
        optionsPanel.add(decompressCheckBox);
        
        optionsPanel.add(new JLabel("Key:"));
        keyField = new JTextField(15);
        optionsPanel.add(keyField);
        
        // Image preview panel
        JPanel previewPanel = new JPanel(new BorderLayout());
        previewPanel.setBorder(BorderFactory.createTitledBorder("Image Preview"));
        imagePreviewLabel = new JLabel("No image selected", JLabel.CENTER);
        imagePreviewLabel.setPreferredSize(new Dimension(200, 150));
        previewPanel.add(imagePreviewLabel, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        extractButton = new JButton("Extract Data");
        extractButton.addActionListener(e -> performExtract());
        buttonPanel.add(extractButton);
        
        add(filePanel, BorderLayout.NORTH);
        add(optionsPanel, BorderLayout.CENTER);
        add(previewPanel, BorderLayout.EAST);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void selectImageFile() {
        File file = inputModule.selectImageFile(this, "Select Stego Image");
        if (file != null) {
            imageFileField.setText(file.getAbsolutePath());
            updateImagePreview(file);
            // Auto-suggest output file name
            String outputPath = file.getParent() + File.separator + "extracted_data";
            outputFileField.setText(outputPath);
        }
    }
    
    private void selectOutputFile() {
        File file = inputModule.saveFile(this, "Save Extracted Data");
        if (file != null) {
            outputFileField.setText(file.getAbsolutePath());
        }
    }
    
    private void updateImagePreview(File imageFile) {
        try {
            BufferedImage image = ImageIO.read(imageFile);
            // Scale image for preview
            int maxWidth = 180;
            int maxHeight = 130;
            int width = image.getWidth();
            int height = image.getHeight();
            
            double scale = Math.min((double) maxWidth / width, (double) maxHeight / height);
            int scaledWidth = (int) (width * scale);
            int scaledHeight = (int) (height * scale);
            
            Image scaledImage = image.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
            imagePreviewLabel.setIcon(new ImageIcon(scaledImage));
            imagePreviewLabel.setText("");
        } catch (IOException e) {
            imagePreviewLabel.setIcon(null);
            imagePreviewLabel.setText("Preview not available");
        }
    }
    
    private void performExtract() {
        String imagePath = imageFileField.getText().trim();
        String outputPath = outputFileField.getText().trim();
        
        if (imagePath.isEmpty() || outputPath.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select both image and output files", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        File imageFile = new File(imagePath);
        File outputFile = new File(outputPath);
        
        if (!imageFile.exists()) {
            JOptionPane.showMessageDialog(this, "Image file does not exist", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        extractButton.setEnabled(false);
        
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                // Extract data from image
                byte[] data = steganographyModule.extractDataFromImage(imageFile);
                
                // Apply decryption if selected
                if (decryptCheckBox.isSelected()) {
                    String key = keyField.getText().trim();
                    if (key.isEmpty()) {
                        throw new IllegalArgumentException("Decryption key is required when decryption is enabled");
                    }
                    EncryptionModule encryptionModule = new EncryptionModule();
                    data = encryptionModule.decryptBytes(data, key);
                }
                
                // Apply decompression if selected
                if (decompressCheckBox.isSelected()) {
                    CompressionModule compressionModule = new CompressionModule();
                    data = compressionModule.decompressData(data);
                }
                
                // Save extracted data
                inputModule.writeBytesToFile(data, outputFile);
                
                return null;
            }
            
            @Override
            protected void done() {
                try {
                    get();
                    JOptionPane.showMessageDialog(ExtractDialog.this, "Data extracted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(ExtractDialog.this, "Extraction failed: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                } finally {
                    extractButton.setEnabled(true);
                }
            }
        };
        
        worker.execute();
    }
}

// CRC Dialog
class CRCDialog extends JDialog {
    private JTextField fileField;
    private JButton browseButton, generateButton;
    private JTextArea inputArea, resultArea;
    private JTabbedPane tabbedPane;
    private CRCModule crcModule;
    private InputModule inputModule;
    
    public CRCDialog(Frame parent, CRCModule crcModule, InputModule inputModule) {
        super(parent, "CRC Generator and Verifier", true);
        this.crcModule = crcModule;
        this.inputModule = inputModule;
        initializeComponents();
    }
    
    private void initializeComponents() {
        setLayout(new BorderLayout());
        setSize(500, 400);
        setLocationRelativeTo(getParent());
        
        tabbedPane = new JTabbedPane();
        
        // File CRC tab
        JPanel filePanel = new JPanel(new BorderLayout());
        
        JPanel fileSelectPanel = new JPanel(new FlowLayout());
        fileSelectPanel.add(new JLabel("File:"));
        fileField = new JTextField(30);
        fileField.setEditable(false);
        fileSelectPanel.add(fileField);
        
        browseButton = new JButton("Browse");
        browseButton.addActionListener(e -> selectFile());
        fileSelectPanel.add(browseButton);
        
        generateButton = new JButton("Generate CRC");
        generateButton.addActionListener(e -> generateFileCRC());
        fileSelectPanel.add(generateButton);
        
        JPanel fileResultPanel = new JPanel(new BorderLayout());
        fileResultPanel.setBorder(BorderFactory.createTitledBorder("CRC Result"));
        JTextArea fileResultArea = new JTextArea(8, 40);
        fileResultArea.setEditable(false);
        fileResultArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        fileResultPanel.add(new JScrollPane(fileResultArea), BorderLayout.CENTER);
        
        filePanel.add(fileSelectPanel, BorderLayout.NORTH);
        filePanel.add(fileResultPanel, BorderLayout.CENTER);
        
        // Text CRC tab
        JPanel textPanel = new JPanel(new BorderLayout());
        
        JPanel textInputPanel = new JPanel(new BorderLayout());
        textInputPanel.setBorder(BorderFactory.createTitledBorder("Input Text"));
        inputArea = new JTextArea(6, 40);
        inputArea.setLineWrap(true);
        inputArea.setWrapStyleWord(true);
        textInputPanel.add(new JScrollPane(inputArea), BorderLayout.CENTER);
        
        JButton textGenerateButton = new JButton("Generate CRC");
        textGenerateButton.addActionListener(e -> generateTextCRC());
        textInputPanel.add(textGenerateButton, BorderLayout.SOUTH);
        
        JPanel textResultPanel = new JPanel(new BorderLayout());
        textResultPanel.setBorder(BorderFactory.createTitledBorder("CRC Result"));
        resultArea = new JTextArea(6, 40);
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        textResultPanel.add(new JScrollPane(resultArea), BorderLayout.CENTER);
        
        textPanel.add(textInputPanel, BorderLayout.NORTH);
        textPanel.add(textResultPanel, BorderLayout.CENTER);
        
        tabbedPane.addTab("File CRC", filePanel);
        tabbedPane.addTab("Text CRC", textPanel);
        
        add(tabbedPane, BorderLayout.CENTER);
        
        // Store reference to file result area
        this.resultArea = fileResultArea; // This will be used for file results
    }
    
    private void selectFile() {
        File file = inputModule.selectFile(this, "Select File for CRC");
        if (file != null) {
            fileField.setText(file.getAbsolutePath());
        }
    }
    
    private void generateFileCRC() {
        String filePath = fileField.getText().trim();
        if (filePath.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a file", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        File file = new File(filePath);
        if (!file.exists()) {
            JOptionPane.showMessageDialog(this, "File does not exist", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            long crc = crcModule.generateCRC(file);
            String result = String.format(
                "File: %s\n" +
                "Size: %d bytes\n" +
                "CRC32: %08X (%d)\n" +
                "Generated at: %s",
                file.getName(),
                file.length(),
                crc,
                crc,
                java.time.LocalDateTime.now().toString()
            );
            resultArea.setText(result);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void generateTextCRC() {
        String text = inputArea.getText();
        if (text.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter some text", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        long crc = crcModule.generateCRC(text);
        String result = String.format(
            "Text: \"%s\"\n" +
            "Length: %d characters\n" +
            "CRC32: %08X (%d)\n" +
            "Generated at: %s",
            text.length() > 50 ? text.substring(0, 47) + "..." : text,
            text.length(),
            crc,
            crc,
            java.time.LocalDateTime.now().toString()
        );
        
        // Get the result area from the current tab
        JPanel currentPanel = (JPanel) tabbedPane.getSelectedComponent();
        JPanel resultPanel = (JPanel) currentPanel.getComponent(1);
        JScrollPane scrollPane = (JScrollPane) resultPanel.getComponent(0);
        JTextArea currentResultArea = (JTextArea) scrollPane.getViewport().getView();
        currentResultArea.setText(result);
    }
}
