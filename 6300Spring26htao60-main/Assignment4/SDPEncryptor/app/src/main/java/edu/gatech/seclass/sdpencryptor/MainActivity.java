package edu.gatech.seclass.sdpencryptor;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private EditText plainTextInput;
    private EditText key1Input;
    private EditText key2Input;
    private Button generateCipherTextButton;
    private EditText cipherTextOutput;

    private static final String CIPHER_ALPHABET =
            "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Bind views to layout IDs
        plainTextInput = findViewById(R.id.plainTextInputID);
        key1Input = findViewById(R.id.key1InputID);
        key2Input = findViewById(R.id.key2InputID);
        generateCipherTextButton = findViewById(R.id.generateCipherTextButtonID);
        cipherTextOutput = findViewById(R.id.cipherTextOutputID);

        generateCipherTextButton.setOnClickListener(v -> onEncodeMessage());
    }

    private int gcd(int a, int b) {
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return Math.abs(a);
    }

    /**
     * Checks if plainTextInput is non-empty and contains at least one letter or number.
     * Uses setError("Invalid Input Tex") if invalid.
     *
     * @return true if valid, false otherwise
     */
    private boolean checkPlainTextInput() {
        String text = plainTextInput.getText().toString().trim();
        if (text.isEmpty()) {
            plainTextInput.setError("Invalid Message");
            return false;
        }
        boolean hasLetterOrNumber = false;
        for (int i = 0; i < text.length(); i++) {
            if (Character.isLetterOrDigit(text.charAt(i))) {
                hasLetterOrNumber = true;
                break;
            }
        }
        if (!hasLetterOrNumber) {
            plainTextInput.setError("Invalid Message");
            return false;
        }
        plainTextInput.setError(null);
        return true;
    }

    /**
     * Checks if Key1 is an integer coprime to 62 between 1 and 61 (inclusive).
     * Displays error icon and "Invalid Key1" message if invalid.
     */
    private boolean checkKey1() {
        String key1Str = key1Input.getText().toString();
        if (key1Str.isEmpty()) {
            key1Input.setError("Invalid Key1");
            return false;
        }
        try {
            int key1 = Integer.parseInt(key1Str);
            boolean isValid = key1 > 0 && key1 < 62 && gcd(key1, 62) == 1;
            if (!isValid) {
                key1Input.setError("Invalid Key1");
            }
            return isValid;
        } catch (NumberFormatException e) {
            key1Input.setError("Invalid Key1");
            return false;
        }
    }

    /**
     * Checks if Key2 is a valid number between 1 and 61 (inclusive).
     * Displays error icon and "Invalid Key2" message if invalid.
     */
    private boolean checkKey2() {
        String key2Str = key2Input.getText().toString();
        if (key2Str.isEmpty()) {
            key2Input.setError("Invalid Key2");
            return false;
        }
        try {
            int key2 = Integer.parseInt(key2Str);
            boolean isValid = key2 > 0 && key2 < 62;
            if (!isValid) {
                key2Input.setError("Invalid Key2");
            }
            return isValid;
        } catch (NumberFormatException e) {
            key2Input.setError("Invalid Key2");
            return false;
        }
    }


    private void onEncodeMessage() {
        boolean checkTextResult = checkPlainTextInput();
        boolean checkKey1Result =checkKey1();
        boolean checkKey2Result =checkKey2();

        if (!checkTextResult || !checkKey1Result || !checkKey2Result) {
            return;
        }

        String message = plainTextInput.getText().toString();
        int key1, key2;
        try {
            key1 = Integer.parseInt(key1Input.getText().toString());
            key2 = Integer.parseInt(key2Input.getText().toString());
        } catch (NumberFormatException e){
            throw new IllegalArgumentException("Invalid key format", e);
        }

        StringBuilder encrypted = new StringBuilder();

        for (char c : message.toCharArray()) {
            int index = CIPHER_ALPHABET.indexOf(c);

            if (index >= 0) {
                int encryptedIndex = (key1 * index + key2) % 62;
                encrypted.append(CIPHER_ALPHABET.charAt(encryptedIndex));
            } else {
                encrypted.append(c);
            }
        }

        cipherTextOutput.setText(encrypted.toString());
    }
}