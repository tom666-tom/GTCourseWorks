package edu.gatech.seclass.sdpencryptor;

import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

/**
 * This is a Georgia Tech provided code example for use in assigned private GT
 * repositories. Students and other users of this template code are advised not
 * to share it with other students or to make it available on publicly viewable
 * websites including repositories such as github and gitlab.  Such sharing may
 * be investigated as a GT honor code violation. Created for CS6300.
 */

@RunWith(RobolectricTestRunner.class)
public class SmallTestExamples {

    private MainActivity activity;
    private RobolectricViewAssertions rva = new RobolectricViewAssertions();

    @Before
    public void setUp() {
        activity = Robolectric.buildActivity(MainActivity.class).create().get();
        rva.setActivity(activity);
    }

    @Test(timeout = 500)
    public void testBasicEncryption1() {
        rva.replaceText(R.id.plainTextInputID, "Cat & Dog");
        rva.replaceText(R.id.key1InputID, "5");
        rva.replaceText(R.id.key2InputID, "3");
        rva.clickWithTimeoutAndDefaultMessage(R.id.generateCipherTextButtonID);

        rva.assertTextViewText(R.id.cipherTextOutputID, "tdK & ylH");
    }

    @Test(timeout = 500)
    public void testBasicEncryption2() {
        rva.replaceText(R.id.plainTextInputID, "Up with the White And Gold!");
        rva.replaceText(R.id.key1InputID, "1");
        rva.replaceText(R.id.key2InputID, "1");
        rva.clickWithTimeoutAndDefaultMessage(R.id.generateCipherTextButtonID);

        rva.assertTextViewText(R.id.cipherTextOutputID, "Vq xjui uif Xijuf Boe Hpme!");
    }

    @Test(timeout = 500)
    public void testBasicEncryption3() {
        rva.replaceText(R.id.plainTextInputID, "abcdefg");
        rva.replaceText(R.id.key1InputID, "5");
        rva.replaceText(R.id.key2InputID, "1");
        rva.clickWithTimeoutAndDefaultMessage(R.id.generateCipherTextButtonID);

        rva.assertTextViewText(R.id.cipherTextOutputID, "bglqvAF");
    }

    @Test(timeout = 500)
    public void testBasicEncryption4() {
        rva.replaceText(R.id.plainTextInputID, "Panda Cat");
        rva.replaceText(R.id.key1InputID, "23");
        rva.replaceText(R.id.key2InputID, "1");
        rva.clickWithTimeoutAndDefaultMessage(R.id.generateCipherTextButtonID);
        rva.assertTextViewText(R.id.cipherTextOutputID, "ob0ib zbe");
    }

    @Test(timeout = 500)
    public void testEncryptionWithSpecialCharacterPrefixSuffix() {
        rva.replaceText(R.id.plainTextInputID, "__trigger__");
        rva.replaceText(R.id.key1InputID, "5");
        rva.replaceText(R.id.key2InputID, "1");
        rva.clickWithTimeoutAndDefaultMessage(R.id.generateCipherTextButtonID);

        rva.assertTextViewText(R.id.cipherTextOutputID, "__IyPFFvy__");
    }

    @Test(timeout = 500)
    public void errorTest1() {
        rva.replaceText(R.id.plainTextInputID, "");
        rva.replaceText(R.id.key1InputID, "0");
        rva.replaceText(R.id.key2InputID, "0");
        rva.clickWithTimeoutAndDefaultMessage(R.id.generateCipherTextButtonID);

        rva.assertTextViewError(R.id.plainTextInputID, "Invalid Message");
        rva.assertTextViewError(R.id.key1InputID, "Invalid Key1");
        rva.assertTextViewError(R.id.key2InputID, "Invalid Key2");
        rva.assertTextViewText(R.id.cipherTextOutputID, "");
    }
}
