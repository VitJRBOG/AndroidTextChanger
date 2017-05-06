package com.example.vitjrbog.textchanger;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    int charChange(String varStringActionType) {
        try {
            int varIntSymbChanged = 0;

            char[] arrayCharCyr = {
                    'е', 'х', 'а', 'р', 'о', 'с'
            };
            char[] arrayCharLat = {
                    'e', 'x', 'a', 'p', 'o', 'c'
            };

            char[] arrayCharFrom;
            char[] arrayCharTo;

            if (varStringActionType.equals("CyrToLat")) {
                arrayCharFrom = arrayCharCyr;
                arrayCharTo = arrayCharLat;
            } else {
                if (varStringActionType.equals("LatToCyr")) {
                    arrayCharFrom = arrayCharLat;
                    arrayCharTo = arrayCharCyr;
                } else {
                    TextView textViewStatusResult =
                            (TextView)findViewById(R.id.status_result);

                    textViewStatusResult.setText(R.string.status_error_unknown_oprtn_type);

                    return 0;
                }
            }

            ClipboardManager objClipboardManager =
                    (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);

            ClipData clipData = objClipboardManager.getPrimaryClip();

            if (clipData == null || clipData.getItemCount() == 0 ||
                    clipData.getItemCount() > 0 && clipData.getItemAt(0).getText() == null) {
                TextView textViewStatusResult =
                        (TextView)findViewById(R.id.status_result);

                textViewStatusResult.setText(R.string.status_error_clipboard);

                return 0;
            }

            String varStringTextFromClipboard = clipData.getItemAt(0).getText().toString();

            char[] arrayCharFromClipboard = varStringTextFromClipboard.toCharArray();

            for (int i = 0; i < arrayCharFromClipboard.length; i++) {
                for (int j = 0; j < arrayCharFrom.length; j++) {
                    if (arrayCharFromClipboard[i] == arrayCharFrom[j]) {
                        arrayCharFromClipboard[i] = arrayCharTo[j];
                        varIntSymbChanged++;
                    }
                }
            }

            String varStringNewText = "";

            for (char varChar : arrayCharFromClipboard) {
                varStringNewText += varChar;
            }

            objClipboardManager.setText(varStringNewText);

            return varIntSymbChanged;
        } catch (Exception varException) {
            TextView textViewStatusResult =
                    (TextView)findViewById(R.id.status_result);

            String varStringError = getResources().getString(R.string.status_error) +
                    " " + varException.getLocalizedMessage();

            textViewStatusResult.setText(varStringError);

            System.out.print(varException.getMessage());
        }

        return 0;
    }

    public void onClickCyrToLat(View view) {
        TextView textViewActionName =
                (TextView)findViewById(R.id.action_name);
        TextView textViewStatusResult =
                (TextView)findViewById(R.id.status_result);

        try {
            textViewActionName.setText(R.string.action_name_cyr_to_lat);

            int varIntSymbChanged = charChange("CyrToLat");

            String varStringStatusResult = getResources().getString(R.string.status_change_symb) +
                    " " + varIntSymbChanged;

            textViewStatusResult.setText(varStringStatusResult);

        } catch (Exception varException) {
            String varStringError = getResources().getString(R.string.status_error) +
                    " " + varException.getLocalizedMessage();

            textViewStatusResult.setText(varStringError);

            System.out.print(varException.getMessage());
        }
    }

    public void onClickLatToCyr(View view) {
        TextView textViewActionName =
                (TextView)findViewById(R.id.action_name);
        TextView textViewStatusResult =
                (TextView)findViewById(R.id.status_result);

        try {

            textViewActionName.setText(R.string.action_name_lat_to_cyr);

            int varIntSymbChanged = charChange("LatToCyr");

            String varStringStatusResult = getResources().getString(R.string.status_change_symb) +
                    " " + varIntSymbChanged;

            textViewStatusResult.setText(varStringStatusResult);

        } catch (Exception varException) {
            String varStringError = getResources().getString(R.string.status_error) +
                    varException.getMessage();

            textViewStatusResult.setText(varStringError);

            System.out.print(varException.getMessage());
        }
    }

    private class TextCensorResult {
        int varIntChanges;
        String varStringText;

        void setVarIntChanges(int varIntChanges) {
            this.varIntChanges = varIntChanges;
        }
        void setVarStringText(String varStringText) {
            this.varStringText = varStringText;
        }

        int getVarIntChanges() {
            return this.varIntChanges;
        }
        String getVarStringText() {
            return this.varStringText;
        }

        TextCensorResult() {
            this.varIntChanges = 0;
            this.varStringText = "";
        }
    }

    TextCensorResult algorithmTextCensor(int varIntWordCensored, char[] arrayCharText,
                                         String varStringText) {
        TextCensorResult objTextCensorResult =
                new TextCensorResult();

        String[] arrayStringSearchWords = {"бля", "сук", "суч", "еба", "хер", "хре", "хуй",
                "хуя", "хуе", "пид", "жоп", "ебл", "уёб", "уеб", "оеб", "аёб", "аеб", "оёб",
                "ебу",
                "ебёт", "ебет", "трах", "ганд", "хрин", "ублю", "муда", "блеа", "пздц",
                "ваги", "шлюх", "член", "мраз",
                "пизд", "говн", "гове", "говё", "гонд", "порн", "дроч",
                "перм", "архангел", "ростов", "брян", "иркут", "рыбин",
                "хабаров", "самар", "уфим", "калмы", "казан", "краснодар", "краснояр" };

        for (String varStringSearchWord : arrayStringSearchWords) {
            int varIntCheckIndx = varStringText.toLowerCase().indexOf(varStringSearchWord);
            if (varIntCheckIndx != -1) {
                arrayCharText[varIntCheckIndx + 1] = '*';
                arrayCharText[varIntCheckIndx + 2] = '*';
                varIntWordCensored++;

                String varStringNewText = "";

                for (char varChar : arrayCharText) {
                    varStringNewText += varChar;
                }

                return algorithmTextCensor(varIntWordCensored, arrayCharText, varStringNewText);
            }
        }

        objTextCensorResult.setVarStringText(varStringText);
        objTextCensorResult.setVarIntChanges(varIntWordCensored);

        return objTextCensorResult;
    }

    int textCensor() {
        try {
            int varIntWordsCensored = 0;

            ClipboardManager objClipboardManager =
                    (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);

            ClipData clipData = objClipboardManager.getPrimaryClip();

            if (clipData == null || clipData.getItemCount() == 0 ||
                    clipData.getItemCount() > 0 && clipData.getItemAt(0).getText() == null) {
                TextView textViewStatusResult =
                        (TextView)findViewById(R.id.status_result);

                textViewStatusResult.setText(R.string.status_error_clipboard);

                return 0;
            }

            String varStringTextFromClipboard = clipData.getItemAt(0).getText().toString();
            char[] arrayCharTextFromClipboard = varStringTextFromClipboard.toCharArray();

            TextCensorResult objTextCensorResult = algorithmTextCensor(varIntWordsCensored,
                    arrayCharTextFromClipboard, varStringTextFromClipboard);

            String varStringNewText = objTextCensorResult.getVarStringText();
            varIntWordsCensored = objTextCensorResult.getVarIntChanges();

            objClipboardManager.setText(varStringNewText);

            return varIntWordsCensored;
        } catch (Exception varException) {
            TextView textViewStatusResult =
                    (TextView)findViewById(R.id.status_result);

            String varStringError = getResources().getString(R.string.status_error) +
                    " " + varException.getLocalizedMessage();

            textViewStatusResult.setText(varStringError);

            System.out.print(varException.getMessage());
        }

        return 0;
    }

    public void onClickCensor(View view) {
        TextView textViewActionName =
                (TextView)findViewById(R.id.action_name);
        TextView textViewStatusResult =
                (TextView)findViewById(R.id.status_result);

        try {

            textViewActionName.setText(R.string.action_name_censor);

            int varIntWordsCensored = textCensor();

            String varStringStatusResult = getResources().getString(R.string.status_censor) +
                    " " + varIntWordsCensored;

            textViewStatusResult.setText(varStringStatusResult);

        } catch (Exception varException) {
            String varStringError = getResources().getString(R.string.status_error) +
                    " " + varException.getLocalizedMessage();

            textViewStatusResult.setText(varStringError);

            System.out.print(varException.getMessage());
        }
    }

    private class TextFormatResult {
        private int[] arrayIntChanges =
                new int[3];
        private char[] arrayCharTextFromClipboard =
                new char[1];

        void setArrayIntChanges(int[] arrayIntChanges) {
            this.arrayIntChanges = arrayIntChanges;
        }
        void setArrayCharText(char[] arrayCharTextFromClipboard) {
            this.arrayCharTextFromClipboard = arrayCharTextFromClipboard;
        }

        int[] getArrayIntChanges() {
            return this.arrayIntChanges;
        }
        char[] getArrayCharText() {
            return this.arrayCharTextFromClipboard;
        }

        TextFormatResult() {
            this.arrayIntChanges[0] = 0;
            this.arrayIntChanges[1] = 0;
            this.arrayIntChanges[2] = 0;
            this.arrayCharTextFromClipboard[0] = ' ';
        }
    }

    TextFormatResult algorithmTextFormatSpaceInsert(char[] arrayCharText, int[] arrayIntChanges) {

        TextFormatResult objTextFormatResult =
                new TextFormatResult();

        char[] arrayCharCheckSymbols = {
                '.', ',', '?', '!', ':', ';' };

        ArrayList<Character> arrayList =
                new ArrayList<>();

        for (char varChar : arrayCharText) {
            arrayList.add(varChar);
        }

        try {

            for (int i = 0; true; i++) {
                for (char varChar : arrayCharCheckSymbols) {
                    if (i < arrayList.size() - 1 &&
                            i >= 1 &&
                            arrayList.get(i + 1) != '\n') {
                        if (arrayList.get(i) == varChar && arrayList.get(i + 1) != ' ') {

                            arrayList.add(i + 1, ' ');
                            arrayIntChanges[0]++;
                        }

                        if (i < arrayList.size() - 2 &&
                                arrayList.get(i) == ')' && arrayList.get(i + 1) == ')' &&
                                arrayList.get(i + 2) != ')' && arrayList.get(i + 2) != ' ') {

                            arrayList.add(i + 2, ' ');
                            arrayIntChanges[0]++;
                        }

                        if (i < arrayList.size() - 2 &&
                                arrayList.get(i) == '(' && arrayList.get(i + 1) == '(' &&
                                arrayList.get(i + 2) != '(' && arrayList.get(i + 2) != ' ') {

                            arrayList.add(i + 2, ' ');
                            arrayIntChanges[0]++;
                        }
                    }
                }
                if (i >= arrayList.size() - 1) {
                    break;
                }
            }
        } catch (Exception varException) {
            TextView textViewStatusResult =
                    (TextView)findViewById(R.id.status_result);

            String varStringError = getResources().getString(R.string.status_error) +
                    " " + varException.getLocalizedMessage();

            textViewStatusResult.setText(varStringError);

            System.out.print(varException.getMessage());
        }

        char[] arrayCharNewText =
                new char[arrayList.size()];

        for (int i = 0; i < arrayCharNewText.length; i++) {
            arrayCharNewText[i] = arrayList.get(i);
        }

        objTextFormatResult.setArrayCharText(arrayCharNewText);
        objTextFormatResult.setArrayIntChanges(arrayIntChanges);

        return objTextFormatResult;
    }

    TextFormatResult algorithmTextFormatSpaceRemove(char[] arrayCharText, int[] arrayIntChanges) {

        TextFormatResult objTextFormatResult =
                new TextFormatResult();

        char[] arrayCharCheckSymbols = {
                '.', ',', '?', '!', ':', ';' };

        ArrayList<Character> arrayList =
                new ArrayList<>();

        for (char varChar : arrayCharText) {
            arrayList.add(varChar);
        }

        try {

            for (int i = 0; true; i++) {

                if (i >= 1) {
                    if (arrayList.get(i) == ' ' && arrayList.get(i - 1) == '\n') {
                        arrayList.remove(i);
                        arrayIntChanges[1]++;
                    }

                    if (i >= 2 &&
                            arrayList.get(i) == ')' && arrayList.get(i - 1) == ')' &&
                            arrayList.get(i - 2) == ' ') {

                        arrayList.remove(i - 2);
                        arrayIntChanges[1]++;
                    }

                    if (i >= 2 &&
                            arrayList.get(i) == '(' && arrayList.get(i - 1) == '(' &&
                            arrayList.get(i - 2) == ' ') {

                        arrayList.remove(i - 2);
                        arrayIntChanges[1]++;
                    }

                    for (char varChar : arrayCharCheckSymbols) {
                        if (arrayList.get(i) == varChar && arrayList.get(i - 1) == ' ') {

                            arrayList.remove(i - 1);
                            arrayIntChanges[1]++;
                        }
                    }
                }

                if (i >= arrayList.size() - 1) {
                    break;
                }
            }

        } catch (Exception varException) {
            TextView textViewStatusResult =
                    (TextView)findViewById(R.id.status_result);

            String varStringError = getResources().getString(R.string.status_error) +
                    " " + varException.getLocalizedMessage();

            textViewStatusResult.setText(varStringError);

            System.out.print(varException.getMessage());
        }

        char[] arrayCharNewText =
                new char[arrayList.size()];

        for (int i = 0; i < arrayCharNewText.length; i++) {
            arrayCharNewText[i] = arrayList.get(i);
        }

        objTextFormatResult.setArrayCharText(arrayCharNewText);
        objTextFormatResult.setArrayIntChanges(arrayIntChanges);

        return objTextFormatResult;
    }

    TextFormatResult algorithmTextFormatUppercase(char[] arrayCharText, int[] arrayIntChanges) {
        TextFormatResult objTextFormatResult =
                new TextFormatResult();

        char[] arrayCharCheckSymbols = {
                '.', '?', '!'};

        try {

            String varStringSymbol = arrayCharText[0] + "";
            char[] arrayCharSymbol = varStringSymbol.toUpperCase().toCharArray();
            arrayCharText[0] = arrayCharSymbol[0];
            arrayIntChanges[2]++;

            for (int i = 0; i < arrayCharText.length; i++) {
                if (i >= 1) {
                    if (arrayCharText[i - 1] == '\n') {
                        varStringSymbol = arrayCharText[i] + "";
                        arrayCharSymbol = varStringSymbol.toUpperCase().toCharArray();
                        arrayCharText[i] = arrayCharSymbol[0];

                        arrayIntChanges[2]++;
                    }
                    if (i < arrayCharText.length - 2) {
                        for (char varChar : arrayCharCheckSymbols) {
                            if (arrayCharText[i] == varChar && arrayCharText[i + 1] == ' ') {
                                varStringSymbol = arrayCharText[i + 2] + "";
                                arrayCharSymbol = varStringSymbol.toUpperCase().toCharArray();
                                arrayCharText[i + 2] = arrayCharSymbol[0];

                                arrayIntChanges[2]++;
                            }
                        }
                        if (arrayCharText[i] == ')' && arrayCharText[i - 1] == ')' &&
                                arrayCharText[i + 1] == ' ') {
                            varStringSymbol = arrayCharText[i + 2] + "";
                            arrayCharSymbol = varStringSymbol.toUpperCase().toCharArray();
                            arrayCharText[i + 2] = arrayCharSymbol[0];

                            arrayIntChanges[2]++;
                        }

                        if (arrayCharText[i] == '(' && arrayCharText[i - 1] == '(' &&
                                arrayCharText[i + 1] == ' ') {
                            varStringSymbol = arrayCharText[i + 2] + "";
                            arrayCharSymbol = varStringSymbol.toUpperCase().toCharArray();
                            arrayCharText[i + 2] = arrayCharSymbol[0];

                            arrayIntChanges[2]++;
                        }
                    }
                }
            }

        } catch (Exception varException) {
            TextView textViewStatusResult =
                    (TextView)findViewById(R.id.status_result);

            String varStringError = getResources().getString(R.string.status_error) +
                    " " + varException.getLocalizedMessage();

            textViewStatusResult.setText(varStringError);

            System.out.print(varException.getMessage());
        }

        objTextFormatResult.setArrayCharText(arrayCharText);
        objTextFormatResult.setArrayIntChanges(arrayIntChanges);

        return objTextFormatResult;
    }

    int[] textFormat() {
        try {
            int[] arrayIntChanges = {0, 0, 0};

            ClipboardManager objClipboardManager =
                    (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);

            ClipData clipData = objClipboardManager.getPrimaryClip();

            if (clipData == null || clipData.getItemCount() == 0 ||
                    clipData.getItemCount() > 0 && clipData.getItemAt(0).getText() == null) {
                TextView textViewStatusResult =
                        (TextView)findViewById(R.id.status_result);

                textViewStatusResult.setText(R.string.status_error_clipboard);

                int[] arrayIntIfError = {0, 0, 0};

                return arrayIntIfError;
            }

            String varStringTextFromClipboard = clipData.getItemAt(0).getText().toString();
            char[] arrayCharTextFromClipboard =
                    varStringTextFromClipboard.toLowerCase().toCharArray();

            TextFormatResult objTextFormatResult = algorithmTextFormatSpaceInsert(
                    arrayCharTextFromClipboard, arrayIntChanges);

            objTextFormatResult = algorithmTextFormatSpaceRemove(
                    objTextFormatResult.getArrayCharText(),
                    objTextFormatResult.getArrayIntChanges());

            objTextFormatResult = algorithmTextFormatUppercase(
                    objTextFormatResult.getArrayCharText(),
                    objTextFormatResult.getArrayIntChanges());

            char[] arrayCharNewText = objTextFormatResult.getArrayCharText();
            arrayIntChanges = objTextFormatResult.getArrayIntChanges();

            String varStringNewText = "";

            for (char varChar : arrayCharNewText) {
                varStringNewText += varChar;
            }

            objClipboardManager.setText(varStringNewText);

            return arrayIntChanges;
        } catch (Exception varException) {
            TextView textViewStatusResult =
                    (TextView)findViewById(R.id.status_result);

            String varStringError = getResources().getString(R.string.status_error) +
                    " " + varException.getLocalizedMessage();

            textViewStatusResult.setText(varStringError);

            System.out.print(varException.getMessage());
        }

        int[] arrayIntIfError = {0, 0, 0};

        return arrayIntIfError;
    }

    public void onClickFormat(View view) {
        TextView textViewActionName =
                (TextView)findViewById(R.id.action_name);
        TextView textViewStatusResult =
                (TextView)findViewById(R.id.status_result);
        try {

            textViewActionName.setText(R.string.action_name_format);

            int[] arrayIntChanges = textFormat();

            String varStringStatusResult =
                    getResources().getString(R.string.status_format_spc_ins) +
                    " " + arrayIntChanges[0] + "\n" +
                            getResources().getString(R.string.status_format_spc_rem) +
                            " " + arrayIntChanges[1] + "\n" +
                            getResources().getString(R.string.status_format_sym_upcs) +
                            " " + arrayIntChanges[2];

            textViewStatusResult.setText(varStringStatusResult);

        } catch (Exception varException) {
            String varStringError = getResources().getString(R.string.status_error) +
                    " " + varException.getLocalizedMessage();

            textViewStatusResult.setText(varStringError);

            System.out.print(varException.getMessage());
        }
    }
}
