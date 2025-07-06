import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.nio.file.*;
import java.util.Base64;
import java.util.UUID;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;

class SecureNoteXGUI extends JFrame {

    private static final String NOTES_DIR = "notes";

    public SecureNoteXGUI() {
        setTitle("🔐 SecureNoteX – Encrypted Note Sharing");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 300);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLabel title = new JLabel("🔐 SecureNoteX", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        add(title, BorderLayout.NORTH);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 1, 10, 10));

        JButton createBtn = new JButton("📝 Create and Encrypt Note");
        JButton decryptBtn = new JButton("🔓 Decrypt Received Note");

        createBtn.addActionListener(e -> showEncryptScreen());
        decryptBtn.addActionListener(e -> showDecryptScreen());

        panel.add(createBtn);
        panel.add(decryptBtn);

        add(panel, BorderLayout.CENTER);
    }

    private void showEncryptScreen() {
        JTextArea noteArea = new JTextArea(5, 30);
        JScrollPane scroll = new JScrollPane(noteArea);
        int result = JOptionPane.showConfirmDialog(
                this,
                scroll,
                "Enter your note to encrypt",
                JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                String note = noteArea.getText();

                KeyGenerator keyGen = KeyGenerator.getInstance("AES");
                keyGen.init(256);
                SecretKey secretKey = keyGen.generateKey();
                byte[] keyBytes = secretKey.getEncoded();
                String base64Key = Base64.getEncoder().encodeToString(keyBytes);

                Cipher cipher = Cipher.getInstance("AES");
                cipher.init(Cipher.ENCRYPT_MODE, secretKey);
                byte[] encryptedBytes = cipher.doFinal(note.getBytes());
                String encryptedNote = Base64.getEncoder().encodeToString(encryptedBytes);

                String uuid = UUID.randomUUID().toString();
                Files.createDirectories(Paths.get(NOTES_DIR));
                Files.write(Paths.get(NOTES_DIR, uuid + ".txt"), encryptedNote.getBytes());

                JOptionPane.showMessageDialog(this,
                        "✅ Note encrypted successfully!\n\nNote ID: " + uuid + "\nDecryption Key:\n" + base64Key,
                        "Encryption Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                showError(ex.getMessage());
            }
        }
    }

    private void showDecryptScreen() {
        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));

        JTextField noteIdField = new JTextField();
        JTextField keyField = new JTextField();

        panel.add(new JLabel("Enter Note ID:"));
        panel.add(noteIdField);
        panel.add(new JLabel("Enter Decryption Key:"));
        panel.add(keyField);

        int result = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Decrypt Note",
                JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                String noteId = noteIdField.getText().trim();
                String base64Key = keyField.getText().trim();

                Path notePath = Paths.get(NOTES_DIR, noteId + ".txt");
                if (!Files.exists(notePath)) {
                    showError("❌ Note not found.");
                    return;
                }

                String encryptedNote = new String(Files.readAllBytes(notePath));
                byte[] keyBytes = Base64.getDecoder().decode(base64Key);
                SecretKeySpec secretKey = new SecretKeySpec(keyBytes, "AES");

                Cipher cipher = Cipher.getInstance("AES");
                cipher.init(Cipher.DECRYPT_MODE, secretKey);
                byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedNote));
                String decryptedNote = new String(decryptedBytes);

                JTextArea decryptedArea = new JTextArea(decryptedNote);
                decryptedArea.setLineWrap(true);
                decryptedArea.setWrapStyleWord(true);
                decryptedArea.setEditable(false);
                JOptionPane.showMessageDialog(this, new JScrollPane(decryptedArea), "✅ Decrypted Note",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                showError("❌ Decryption failed: " + ex.getMessage());
            }
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SecureNoteXGUI().setVisible(true));
    }
}
