package com.example.fx;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import utils.Crypto;
import utils.Cryptomethods;

import java.io.File;

import java.io.IOException;

public class HelloController {
    @FXML
    public TextField userKey;
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
    private String Language;



    @FXML
    protected void ChoosePathEncryp() {

        FileChooser fileChooser = new FileChooser();
        this.pathFiles = fileChooser.showOpenDialog(null);

    }

    @FXML
    protected void selectionLangRU() {
        this.Language = "ru";
    }

    @FXML
    protected void selectionLangEN() {
        this.Language = "en";
    }

    @FXML
    public void makeEncryption() throws IOException {
        int couter = 3;
        if (pathFiles == null) {
            errorPathEncryp.setText("Ошибка выберете файл еще раз!");
            notStartEncryp.setText("Невозможно запустить шифровку!");
            couter -= 1;
        } else {
            errorPathEncryp.setText("");
            notStartEncryp.setText("");
        }
        if (userKey.getText() == null) {
            isEmtyKeyEncryp.setText("Введите ключ!");
            notStartEncryp.setText("Невозможно запустить шифровку!");
            couter -= 1;
        } else {
            isEmtyKeyEncryp.setText("");
            notStartEncryp.setText("");
        }
        if (Language == null) {
            notLangEncryp.setText("Выберете язык!");
            notStartEncryp.setText("Невозможно запустить шифровку!");
            couter -= 1;
        } else {
            notLangEncryp.setText("");
            notStartEncryp.setText("");
        }
        if (couter == 3) {
            int userKey = Integer.parseInt(this.userKey.getText());
            Crypto crypto = new Crypto(pathFiles, userKey, Language);
            Crypto.encrypt(crypto);
            pathFiles=null;
            this.userKey = null;
            Language = null;
        }
    }


    @FXML
    public void makeDecryption() throws IOException {
        int couter = 3;
        if (pathFiles == null) {
            errorPathDecryp.setText("Ошибка выберете файл еще раз!");
            notStartDecryp.setText("Невозможно запустить разшифровку!");
            couter -= 1;
        } else {
            errorPathDecryp.setText("");
            notStartDecryp.setText("");
        }
        if (userKeyDecryp.getText() == null) {
            isEmtyKeyDecryp.setText("Введите ключ!");
            notStartDecryp.setText("Невозможно запустить разшифровку!");
            couter -= 1;
        } else {
            isEmtyKeyDecryp.setText("");
            notStartDecryp.setText("");
        }
        if (Language == null) {
            notLangDecryp.setText("Выберете язык!");
            notStartDecryp.setText("Невозможно запустить разшифровку!");
            couter -= 1;
        } else {
            notLangDecryp.setText("");
            notStartDecryp.setText("");
        }
        if (couter == 3) {
            int userKey1 = Integer.parseInt(this.userKeyDecryp.getText());

            Crypto crypto = new Crypto(pathFiles, userKey1, Language);
            Crypto.decrypt(crypto);
            pathFiles=null;
            this.userKey = null;
            Language = null;

        }
    }

    @FXML
    public void makeBrutfors() throws IOException {
        int couter = 2;
        if (pathFiles == null) {
            errorPathBrutfors.setText("Ошибка выберете файл еще раз!");
            notStartBrutfors.setText("Невозможно запустить Brut fors!");
            couter -= 1;
        } else {
            errorPathBrutfors.setText("");
            notStartBrutfors.setText("");
        }
        if (Language == null) {
            notLangBrutfors.setText("Выберете язык!");
            notStartBrutfors.setText("Невозможно запустить Brut fors!");
            couter -= 1;
        } else {
            notLangBrutfors.setText("");
            notStartBrutfors.setText("");
        }
        if (couter == 2) {
            Crypto crypto = new Crypto(pathFiles, Language);
            Crypto.DecryptorBrutforse(crypto);
            brutforsKey.setText(String.valueOf(Cryptomethods.brutforsKey));
            pathFiles=null;
            Language = null;
        }

    }

}