package view;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import utils.Criptoanalizer;
import utils.TranscriptionalException;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Controller {
    @FXML
    public TextField userKeyEncryp;
    @FXML
    public Text errorPathEncryp;
    @FXML
    public Text notStartEncryp;
    @FXML
    public Text notLangEncryp;
    @FXML
    public Text isEmtyKeyEncryp;
    @FXML
    public TextField userKeyDecryp;
    @FXML
    public Text notStartDecryp;
    @FXML
    public Text isEmtyKeyDecryp;
    @FXML
    public Text errorPathDecryp;
    @FXML
    public Text notLangDecryp;
    @FXML
    public Text brutforsKey;
    @FXML
    public Text errorPathBrutfors;
    @FXML
    public Text notLangBrutfors;
    @FXML
    public Text notStartBrutfors;
    public File pathFiles;
    private String language;
    private int invalidKey = -1;

    @FXML
    protected void сhoosePath() {
        FileChooser fileChooser = new FileChooser();
        this.pathFiles = fileChooser.showOpenDialog(null);

    }

    @FXML
    protected void selectionLangRU() throws IOException {
        this.language = "ru";
    }

    @FXML
    protected void selectionLangEN() throws IOException {
        this.language = "en";
    }

    public int getUserKeyEncryp() {
        Scanner scanner = new Scanner(this.userKeyEncryp.getText());
        if (scanner.hasNextInt()) {
            return Integer.parseInt(this.userKeyEncryp.getText());
        } else {
            return invalidKey;
        }
    }

    public int getUserKeyDecryp() {
        Scanner scanner = new Scanner(this.userKeyDecryp.getText());
        if (scanner.hasNextInt()) {
            return Integer.parseInt(this.userKeyDecryp.getText());
        } else {
            return invalidKey;
        }
    }

    @FXML
    public void makeEncryption() throws IOException {
        int conditionCounter = 2;
        if (pathFiles == null) {
            errorPathEncryp.setText("Ошибка выберете!");
            notStartEncryp.setText("Невозможно запустить шифровку!");
            conditionCounter--;
        } else {
            if (pathFiles.length() < 1) {
                errorPathEncryp.setText("Файл пуст!");
                notStartEncryp.setText("Невозможно запустить Brut fors!");
                conditionCounter--;
            } else {
                errorPathEncryp.setText("");
                notStartEncryp.setText("");
            }
        }

        if (language == null) {
            notLangEncryp.setText("Выберете язык!");
            notStartEncryp.setText("Невозможно запустить шифровку!");
            conditionCounter--;
        } else {
            notLangEncryp.setText("");
            notStartEncryp.setText("");
        }
        if (conditionCounter == 2) {
            try {
                Criptoanalizer.createAlphabet(language);
                Criptoanalizer.encrypt(pathFiles, getUserKeyEncryp());
                language = null;
            } catch (TranscriptionalException e) {
                notStartEncryp.setText(e.getMessage());
            }
        }
    }

    @FXML
    public void makeDecryption() throws IOException {
        int conditionCounter = 2;
        if (pathFiles == null) {
            errorPathDecryp.setText("Ошибка выберете файл!");
            notStartDecryp.setText("Невозможно запустить разшифровку!");
            conditionCounter--;
        } else {
            if (pathFiles.length() < 1) {
                errorPathDecryp.setText("Файл пуст!");
                notStartDecryp.setText("Невозможно запустить Brutfors!");
                conditionCounter--;
            } else {
                isEmtyKeyDecryp.setText("");
                notStartDecryp.setText("");
            }
        }
        if (language == null) {
            notLangDecryp.setText("Выберете язык!");
            notStartDecryp.setText("Невозможно запустить разшифровку!");
            conditionCounter--;
        } else {
            notLangDecryp.setText("");
            notStartDecryp.setText("");
        }
        if (conditionCounter == 2) {
            try {
                Criptoanalizer.createAlphabet(language);
                Criptoanalizer.decrypt(pathFiles, getUserKeyDecryp());
                language = null;
            } catch (TranscriptionalException e) {
                notStartDecryp.setText(e.getMessage());
            }
        }
    }

    @FXML
    public void makeBrutfors() throws IOException {
        int conditionCounter = 2;
        if (pathFiles == null) {
            errorPathBrutfors.setText("Ошибка выберете файл!");
            notStartBrutfors.setText("Невозможно запустить Brutfors!");
            conditionCounter--;
        } else {
            if (pathFiles.length() < 1) {
                errorPathBrutfors.setText("Файл пуст!");
                notStartBrutfors.setText("Невозможно запустить Brutfors!");
                conditionCounter--;
            } else {
                errorPathBrutfors.setText("");
                notStartBrutfors.setText("");
            }
        }
        if (language == null) {
            notLangBrutfors.setText("Выберете язык!");
            notStartBrutfors.setText("Невозможно запустить Brutfors!");
            conditionCounter--;
        } else {
            notLangBrutfors.setText("");
            notStartBrutfors.setText("");
        }
        if (conditionCounter == 2) {
            try {
                Criptoanalizer.createAlphabet(language);
                Criptoanalizer.decryptorBrutforse(pathFiles);
                brutforsKey.setText(String.valueOf(Criptoanalizer.brutforsKey));
                language = null;
            } catch (TranscriptionalException e) {
                notStartBrutfors.setText(e.getMessage());
            }
        }
    }

}