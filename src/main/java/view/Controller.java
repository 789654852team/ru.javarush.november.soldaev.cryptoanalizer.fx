package view;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import utils.Criptoanalizer;

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
    @FXML
    private File pathFiles;
    @FXML
    private String language;


    @FXML
    protected void ChoosePathEncryp() {
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

    public int getUserKey() {
        Scanner scanner = new Scanner(this.userKeyDecryp.getText());
        if (scanner.hasNextInt()) {
            int userKeyInt = Integer.parseInt(String.valueOf(scanner));
            return userKeyInt;
        } else {
            return 2000;
        }
    }

    @FXML
    public void makeEncryption() throws IOException {
        int couter = 3;
        if (pathFiles == null) {
            errorPathEncryp.setText("Ошибка выберете!");
            notStartEncryp.setText("Невозможно запустить шифровку!");
            couter -= 1;
        } else {
            errorPathEncryp.setText("");
            notStartEncryp.setText("");
        }
        if (getUserKey() > 1000 || getUserKey() < 0) {
            isEmtyKeyEncryp.setText("Неверный ключ! Введите ключ в диапазоне от 1 до 999!");
            notStartEncryp.setText("Невозможно запустить шифровку!");
            couter -= 1;
        } else if (getUserKey() == 2000) {
            isEmtyKeyEncryp.setText("Неверный ключ!");
            notStartEncryp.setText("Невозможно запустить шифровку!");
        } else {
            isEmtyKeyEncryp.setText("");
            notStartEncryp.setText("");
        }
        if (language == null) {
            notLangEncryp.setText("Выберете язык!");
            notStartEncryp.setText("Невозможно запустить шифровку!");
            couter -= 1;
        } else {
            notLangEncryp.setText("");
            notStartEncryp.setText("");
        }
        if (couter == 3) {
            Criptoanalizer.encrypt(pathFiles, getUserKey());
            pathFiles = null;
            this.userKeyEncryp = null;
            language = null;
        }
    }

    @FXML
    public void makeDecryption() throws IOException {
        int couter = 3;
        if (pathFiles == null) {
            errorPathDecryp.setText("Ошибка выберете файл!");
            notStartDecryp.setText("Невозможно запустить разшифровку!");
            couter -= 1;
        } else {
            errorPathDecryp.setText("");
            notStartDecryp.setText("");
        }
        if (getUserKey() > 1000 || getUserKey() < 0) {
            isEmtyKeyDecryp.setText("Неверный ключ! Введите ключ в диапазоне от 1 до 999!");
            notStartDecryp.setText("Невозможно запустить разшифровку!");
            couter -= 1;
        } else if (getUserKey() == 2000) {
            isEmtyKeyEncryp.setText("Неверный ключ!");
            notStartEncryp.setText("Невозможно запустить шифровку!");
        } else {
            isEmtyKeyEncryp.setText("");
            notStartEncryp.setText("");
        }
        if (language == null) {
            notLangDecryp.setText("Выберете язык!");
            notStartDecryp.setText("Невозможно запустить разшифровку!");
            couter -= 1;
        } else {
            notLangDecryp.setText("");
            notStartDecryp.setText("");
        }
        if (couter == 3) {

            Criptoanalizer.decrypt(pathFiles, getUserKey());
            pathFiles = null;
            this.userKeyDecryp = null;
            language = null;

        }
    }

    @FXML
    public void makeBrutfors() throws IOException {
        int couter = 2;
        if (pathFiles == null) {
            errorPathBrutfors.setText("Ошибка выберете файл!");
            notStartBrutfors.setText("Невозможно запустить Brut fors!");
            couter -= 1;
        } else {
            errorPathBrutfors.setText("");
            notStartBrutfors.setText("");
        }
        if (language == null) {
            notLangBrutfors.setText("Выберете язык!");
            notStartBrutfors.setText("Невозможно запустить Brut fors!");
            couter -= 1;
        } else {
            notLangBrutfors.setText("");
            notStartBrutfors.setText("");
        }
        if (couter == 2) {
            Criptoanalizer.decryptorBrutforse(pathFiles);
            brutforsKey.setText(String.valueOf(Criptoanalizer.brutforsKey));
            pathFiles = null;
            language = null;
        }
    }

}