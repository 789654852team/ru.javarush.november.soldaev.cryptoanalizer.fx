package view;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import utils.Criptoanalizer;

import java.io.File;

import java.io.IOException;
import java.security.KeyException;
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
    @FXML
    public File pathFiles;
    private final int MAXUSERKEY = 60;
    private final int MINUSERKEY = 0;
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
        Criptoanalizer.createAlphabet(language);

    }

    @FXML
    protected void selectionLangEN() throws IOException {
        this.language = "en";
        Criptoanalizer.createAlphabet(language);
    }

    public int getUserKeyEncryp() {
        Scanner scanner = new Scanner(this.userKeyEncryp.getText());
        if (scanner.hasNextInt()) {
            int userKeyInt = Integer.parseInt(this.userKeyEncryp.getText());
            return userKeyInt;
        } else {
            return invalidKey;
        }
    }

    public int getUserKeyDecryp() {
        Scanner scanner = new Scanner(this.userKeyDecryp.getText());
        if (scanner.hasNextInt()) {
            int userKeyInt = Integer.parseInt(this.userKeyDecryp.getText());
            return userKeyInt;
        } else {
            return invalidKey;
        }
    }

    @FXML
    public void makeEncryption() throws IOException, KeyException {
        int conditionCounter = 3;
        if (pathFiles == null) {
            errorPathEncryp.setText("Ошибка выберете!");
            notStartEncryp.setText("Невозможно запустить шифровку!");
            conditionCounter -= 1;
        } else {
            if (pathFiles.length() < 1) {
                errorPathEncryp.setText("Файл пуст!");
                notStartEncryp.setText("Невозможно запустить Brut fors!");
                conditionCounter -= 1;
            } else {
                errorPathEncryp.setText("");
                notStartEncryp.setText("");
            }
        }
        if (getUserKeyEncryp() == invalidKey) {
            isEmtyKeyEncryp.setText("Неверный ключ!");
            notStartEncryp.setText("Невозможно запустить шифровку!");
            conditionCounter -= 1;
        } else if (getUserKeyEncryp() > MAXUSERKEY || getUserKeyEncryp() < MINUSERKEY) {
            isEmtyKeyEncryp.setText("Неверный ключ! Введите ключ в диапазоне от 1 до 60!");
            notStartEncryp.setText("Невозможно запустить шифровку!");
            conditionCounter -= 1;
        } else {
            isEmtyKeyEncryp.setText("");
            notStartEncryp.setText("");
        }
        if (language == null) {
            notLangEncryp.setText("Выберете язык!");
            notStartEncryp.setText("Невозможно запустить шифровку!");
            conditionCounter -= 1;
        } else {
            notLangEncryp.setText("");
            notStartEncryp.setText("");
        }
        if (conditionCounter == 3) {
            Criptoanalizer.encrypt(pathFiles, getUserKeyEncryp());
        }
    }

    @FXML
    public void makeDecryption() throws IOException, KeyException {
        int conditionCounter = 3;
        if (pathFiles == null) {
            errorPathDecryp.setText("Ошибка выберете файл!");
            notStartDecryp.setText("Невозможно запустить разшифровку!");
            conditionCounter -= 1;
        } else {
            if (pathFiles.length() < 1) {
                isEmtyKeyDecryp.setText("Файл пуст!");
                notStartDecryp.setText("Невозможно запустить Brutfors!");
                conditionCounter -= 1;
            } else {
                isEmtyKeyDecryp.setText("");
                notStartDecryp.setText("");
            }
        }
        if (getUserKeyDecryp() == invalidKey) {
            isEmtyKeyDecryp.setText("Неверный ключ!");
            notStartDecryp.setText("Невозможно запустить шифровку!");
            conditionCounter -= 1;
        } else if (getUserKeyDecryp() > MAXUSERKEY || getUserKeyDecryp() < MINUSERKEY) {
            isEmtyKeyDecryp.setText("Неверный ключ! Введите ключ в диапазоне от 1 до 60!");
            notStartDecryp.setText("Невозможно запустить шифровку!");
            conditionCounter -= 1;
        } else {
            isEmtyKeyDecryp.setText("");
            notStartDecryp.setText("");
        }
        if (language == null) {
            notLangDecryp.setText("Выберете язык!");
            notStartDecryp.setText("Невозможно запустить разшифровку!");
            conditionCounter -= 1;
        } else {
            notLangDecryp.setText("");
            notStartDecryp.setText("");
        }
        if (conditionCounter == 3) {
            Criptoanalizer decryption = new Criptoanalizer();
            decryption.decrypt(pathFiles, getUserKeyDecryp());
        }
    }

    @FXML
    public void makeBrutfors() throws IOException, KeyException {
        int conditionCounter = 2;
        if (pathFiles == null) {
            errorPathBrutfors.setText("Ошибка выберете файл!");
            notStartBrutfors.setText("Невозможно запустить Brutfors!");
            conditionCounter -= 1;
        } else {
            if (pathFiles.length() < 1) {
                errorPathBrutfors.setText("Файл пуст!");
                notStartBrutfors.setText("Невозможно запустить Brutfors!");
                conditionCounter -= 1;
            } else {
                errorPathBrutfors.setText("");
                notStartBrutfors.setText("");
            }
        }
        if (language == null) {
            notLangBrutfors.setText("Выберете язык!");
            notStartBrutfors.setText("Невозможно запустить Brutfors!");
            conditionCounter -= 1;
        } else {
            notLangBrutfors.setText("");
            notStartBrutfors.setText("");
        }
        if (conditionCounter == 2) {
            Criptoanalizer.decryptorBrutforse(pathFiles);
            brutforsKey.setText(String.valueOf(Criptoanalizer.brutforsKey));
        }
    }

}