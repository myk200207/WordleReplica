package edu.virginia.cs.gui;

import edu.virginia.cs.wordle.IllegalWordException;
import edu.virginia.cs.wordle.LetterResult;
import edu.virginia.cs.wordle.WordleImplementation;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class WordleController {
    @FXML
    private Label welcomeText;
    @FXML
    private TextField guessInput;

    @FXML
    private Label message;

    @FXML
    private Label label1, label2, label3, label4, label5, label6, label7, label8, label9, label10, label11, label12, label13, label14, label15,
            label16, label17, label18, label19, label20, label21, label22, label23, label24, label25, label26, label27, label28, label29, label30;

    private Button button = new Button();
    private List<Label> labelList = new ArrayList<>();

    private WordleImplementation wordleImplementation;


    private int TotalLetterCount = 0;
    private int localLetterCount = 0;

    private int initialiseCount = 0;
    int rownumber = 0;

    private ArrayList<Character> enteredLetters;

    public void initialize(){
        wordleImplementation = new WordleImplementation();
        //System.out.println(wordleImplementation.getAnswer());

        TotalLetterCount = 0;
        localLetterCount = 0;
        enteredLetters = new ArrayList<>();

        message.setVisible(false);

        for(Label label : labelList){
            label.setText("");
        }
        labelList = Arrays.asList(label1, label2, label3, label4, label5, label6, label7, label8, label9, label10, label11, label12, label13, label14, label15,
                label16, label17, label18, label19, label20, label21, label22, label23, label24, label25, label26, label27, label28, label29, label30);

        for(int i = 0; i <labelList.size();i++){
            labelList.get(i).setStyle("-fx-background-color: white");
            labelList.get(i).setStyle("-fx-border-color: black");
        }
        rownumber=0;
    }

    @FXML
    protected void onBackSpace(KeyEvent event){
        if(event.getCode() == KeyCode.BACK_SPACE){
            message.setVisible(false);
            if(TotalLetterCount > 0){
                if(localLetterCount > 0){
                    enteredLetters.remove(enteredLetters.size()-1);

                    TotalLetterCount--;
                    localLetterCount--;
                    labelList.get(TotalLetterCount).setText("");
                }
            }
        }
        if(event.getCode() == KeyCode.ENTER){
            message.setVisible(false);
            if(TotalLetterCount > 0 && TotalLetterCount %5 == 0){

                String guessWord = getGuessWord();

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                try{
                    LetterResult[] result = wordleImplementation.submitGuess(guessWord);
                    rownumber = (int) TotalLetterCount / 5;

                    setColor(rownumber, result);

                    if(wordleImplementation.isWin()){
                        alert.setTitle("Congratulations you won!");
                        restart(alert);
                    } else if (wordleImplementation.isLoss()) {
                        alert.setTitle("Sorry. Better luck next time. Answer: " + wordleImplementation.getAnswer());
                        restart(alert);
                    }
                    localLetterCount = 0;
                }catch (IllegalWordException e){
                    message.setText("Entered an invalid word.");
                    message.setVisible(true);
                }
            }else{
                message.setText("Not enough letters");
                message.setVisible(true);
            }
        }
    }

    private String getGuessWord() {
        Character char1 = enteredLetters.get(enteredLetters.size()-1);
        Character char2 = enteredLetters.get(enteredLetters.size()-2);
        Character char3 = enteredLetters.get(enteredLetters.size()-3);
        Character char4 = enteredLetters.get(enteredLetters.size()-4);
        Character char5 = enteredLetters.get(enteredLetters.size()-5);
        String guessWord = "" + char5 + char4 + char3 + char2 + char1;
        return guessWord;
    }

    private void restart(Alert alert) {
        alert.setHeaderText("Would you like to play again?");
        Optional<ButtonType> results = alert.showAndWait();
        if(results.isPresent() && results.get() == ButtonType.OK) {
            initialize();
        }else{
            System.exit(0);
        }
    }

    private void setColor(int rollNum, LetterResult[] result){
        int labelNum = rollNum*5 - 5;
        for (int i = 0; i < result.length; i++) {
            if(result[i] == LetterResult.GREEN){
                labelList.get(labelNum+i).setStyle("-fx-background-color: #6aaa64; -fx-text-fill: white;");
            }else if (result[i] == LetterResult.YELLOW){
                labelList.get(labelNum+i).setStyle("-fx-background-color: #c9b458; -fx-text-fill: white;");
            }else
                labelList.get(labelNum+i).setStyle("-fx-background-color: #787c7e; -fx-text-fill: white;");
        }
    }
    @FXML
    protected void onTextEntry(KeyEvent event){
        try {
            String str = event.getCharacter();
            str = str.toUpperCase();
            char letter = str.charAt(0);
            if (letter >= 'A' && letter <= 'Z') {
                message.setVisible(false);
                if (localLetterCount < 5) {
                    enteredLetters.add(letter);
                    labelList.get(TotalLetterCount).setText(str);
                    TotalLetterCount++;
                    localLetterCount++;
                }
            }
        }catch(StringIndexOutOfBoundsException e){
            ;
        }
    }

}